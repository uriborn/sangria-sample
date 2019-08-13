package com.example.sangria

import sangria.ast
import sangria.ast.Document
import sangria.execution.Executor
import sangria.execution.deferred.DeferredResolver
import sangria.parser.QueryParser
import sangria.macros._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

object SangriaSample {

  def main(args: Array[String]): Unit = {
//    queryParserAndRenderer()
    executeGraphQL()(ExecutionContext.Implicits.global)
  }

  def queryParserAndRenderer(): Unit = {
    val query1 =
      """
        |query FetchLukeAndLeiaAliased(
        |  $someVar: Int = 1.23
        |  $anotherVar: Int = 123
        |) @include(if: true) {
        |  luke: human(id: "1000") @include(if: true) {
        |    friends(sort: NAME)
        |  }
        |
        |  leia: human(id: "10103\n \u00F6 ö") {
        |    name
        |  }
        |
        |  ... on User {
        |    birth{
        |      day
        |    }
        |  }
        |
        |  ...Foo
        |}
        |fragment Foo on User @foo(bar: 1) {
        |  baz
        |
      """.stripMargin

    QueryParser.parse(query1) match {
      case Success(document) =>
        println(s"document: $document")
        println(s"document.renderPretty: ${document.renderPretty}")
      case Failure(error) =>
        println(s"Syntax error: ${error.getMessage}")
    }

    // syntax error の場合はコンパイルが通らなくなる
    val query2: Document =
      graphql"""
        {
          name
          friends {
            id
            name
          }
        }
      """
    println(s"query2.renderPretty: ${query2.renderPretty}")

    // syntax error の場合はコンパイルが通らなくなる
    val query3: ast.Value =
      graphqlInput"""
        {
          id: "123456"
          version: 2 # changed 2 times
          deliveries: [
            {
              id: 123,
              received: false,
              note: null,
              state: OPEN
            }
          ]
        }
      """
    println(s"query3.renderPretty: ${query3.renderPretty}")
  }

  private def executeGraphQL()(implicit ec: ExecutionContext): Unit = {
    val query1 =
      """
        |query {
        |  human(id: 1000) {
        |    id
        |    name
        |  }
        |}
      """.stripMargin

    val query2 =
      """
        |query {
        |  human(id: 1000) {
        |    id
        |    name
        |    friends {
        |      name
        |    }
        |  }
        |}
      """.stripMargin

    val query3 =
      """
        |query {
        |  droid(id: 2000) {
        |    id
        |    name
        |  }
        |}
      """.stripMargin

    val query4 =
      """
        |query {
        |  humans(limit: 2, offset: 0) {
        |    id
        |    name
        |  }
        |}
      """.stripMargin

    val query5 =
      """
        |query {
        |  droids(limit: 2, offset: 0) {
        |    id
        |    name
        |  }
        |}
      """.stripMargin

    executeGraphQL("query1", query1)
    executeGraphQL("query2", query2)
    executeGraphQL("query3", query3)
    executeGraphQL("query4", query4)
    executeGraphQL("query5", query5)
  }

  private def executeGraphQL(name: String, queryStr: String)(implicit ec: ExecutionContext): Unit = {
    QueryParser.parse(queryStr) match {
      case Success(document) => executeGraphQL(name, document)
      case Failure(error)    => println(s"Syntax error: ${error.getMessage}")
    }
  }

  private def executeGraphQL(name: String, query: Document)(implicit ec: ExecutionContext): Unit = {
    val executed = Executor.execute(SchemaDefinition.starWarsSchema, query, new CharacterRepository,
      deferredResolver = DeferredResolver.fetchers(SchemaDefinition.characters))

    val result = Await.result(executed, Duration.Inf)

    println(s"$name executed: $result")
  }

}
