package com.example.sangria

import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

import scala.concurrent.Future

object SchemaDefinition {

  val characters = Fetcher.caching { (ctx: CharacterRepository, ids: Seq[Int]) =>
    Future.successful(ids.flatMap(id => ctx.getHuman(id) orElse ctx.getDroid(id)))
  }(HasId(_.id))

  val episodeEnum = EnumType(
    name = "Episode",
    description = Some("One of the films in the Star Wars Trilogy"),
    values = List(
      EnumValue(name = "NewHope", description = Some("Released in 1977."), value = Episode.NewHope),
      EnumValue(name = "Empire",  description = Some("Released in 1980."), value = Episode.Empire),
      EnumValue(name = "Jedi",    description = Some("Released in 1983."), value = Episode.Jedi)
    )
  )

  val starWarsCharacterInterface: InterfaceType[CharacterRepository, StarWarsCharacter] = InterfaceType(
    name = "Character",
    fieldsFn =  () => fields[CharacterRepository, StarWarsCharacter](
      Field(name = "id",        fieldType = IntType,                                      resolve = _.value.id),
      Field(name = "name",      fieldType = OptionType(StringType),                       resolve = _.value.name),
      Field(name = "friends",   fieldType = ListType(starWarsCharacterInterface),         resolve = ctx => characters.deferSeqOpt(ctx.value.friendIds)),
      Field(name = "appearsIn", fieldType = OptionType(ListType(OptionType(episodeEnum))), resolve = _.value.appearsIn.map(e => Some(e)))
    )
  )

  val humanObject = ObjectType(
    name = "Human",
    description = "A humanoid creature in the Star Wars universe.",
    interfaces = interfaces[CharacterRepository, Human](starWarsCharacterInterface),
    fields = fields[CharacterRepository, Human](
      Field(name = "id",         fieldType = IntType,                                       resolve = _.value.id),
      Field(name = "name",       fieldType = OptionType(StringType),                        resolve = _.value.name),
      Field(name = "friends",    fieldType = ListType(starWarsCharacterInterface),          resolve = ctx => characters.deferSeqOpt(ctx.value.friendIds)),
      Field(name = "appearsIn",  fieldType = ListType(OptionType(episodeEnum)),             resolve = _.value.appearsIn.map(e => Some(e))),
      Field(name = "homePlanet", fieldType = OptionType(StringType),                        resolve = _.value.homePlanet)
    ))

  val droidObject = ObjectType(
    name = "Droid",
    description = "A mechanical creature in the Star Wars universe.",
    interfaces = interfaces[CharacterRepository, Droid](starWarsCharacterInterface),
    fields = fields[CharacterRepository, Droid](
      Field(name = "id",              fieldType = IntType,                                       resolve = _.value.id),
      Field(name = "name",            fieldType = OptionType(StringType),                        resolve = _.value.name),
      Field(name = "friends",         fieldType = ListType(starWarsCharacterInterface),          resolve = ctx => characters.deferSeqOpt(ctx.value.friendIds)),
      Field(name = "appearsIn",       fieldType = OptionType(ListType(OptionType(episodeEnum))), resolve = _.value.appearsIn.map(e => Some(e))),
      Field(name = "primaryFunction", fieldType = OptionType(StringType),                        resolve = _.value.primaryFunction)
    )
  )

  val idArgument = Argument("id", IntType)
  val episodeArgument = Argument("episode", OptionInputType(episodeEnum))
  val limitArgument = Argument("limit", OptionInputType(IntType), defaultValue = 20)
  val offsetArgument = Argument("offset", OptionInputType(IntType), defaultValue = 0)

  val query = ObjectType(
    name = "query",
    fields = fields[CharacterRepository, Unit](
      Field(name = "hero",   fieldType = starWarsCharacterInterface, arguments = episodeArgument :: Nil,                 resolve = ctx => ctx.ctx.getHero(ctx.arg(episodeArgument)), deprecationReason = Some("Use `human` or `droid` fields instead")),
      Field(name = "human",  fieldType = OptionType(humanObject),    arguments = idArgument :: Nil,                      resolve = ctx => ctx.ctx.getHuman(ctx arg idArgument)),
      Field(name = "droid",  fieldType = droidObject,                arguments = idArgument :: Nil,                      resolve = ctx => ctx.ctx.getDroid(ctx arg idArgument).get),
      Field(name = "humans", fieldType = ListType(humanObject),      arguments = limitArgument :: offsetArgument :: Nil, resolve = ctx => ctx.ctx.getHumans(ctx arg limitArgument, ctx arg offsetArgument)),
      Field(name = "droids", fieldType = ListType(droidObject),      arguments = limitArgument :: offsetArgument :: Nil, resolve = ctx ⇒ ctx.ctx.getDroids(ctx arg limitArgument, ctx arg offsetArgument))
    )
  )

  val query2 = ObjectType(
    name = "query",
    fields = fields[Unit, Model](
      Field(name = "human",  fieldType = OptionType(humanObject),    arguments = idArgument :: Nil, resolve = _.value.value)
    )
  )

  val starWarsSchema = Schema(query)
}



case class Model(value: String)
