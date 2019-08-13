package com.example.sangria

sealed abstract class Episode(val name: String) extends Product with Serializable

object Episode {
  case object NewHope extends Episode("NewHope")
  case object Empire  extends Episode("Empire")
  case object Jedi    extends Episode("Jedi")
}

trait StarWarsCharacter {
  def id: Int
  def name: Option[String]
  def friendIds: List[Int]
  def appearsIn: List[Episode]
}

case class Human(
  id: Int,
  name: Option[String],
  friendIds: List[Int],
  appearsIn: List[Episode],
  homePlanet: Option[String]
) extends StarWarsCharacter

case class Droid(
  id: Int,
  name: Option[String],
  friendIds: List[Int],
  appearsIn: List[Episode],
  primaryFunction: Option[String]
) extends StarWarsCharacter


object Data {
  val human1 = Human(
    id = 1000,
    name = Some("Luke Skywalker"),
    friendIds = List(1002, 1003, 2000, 2001),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    homePlanet = Some("Tatooine")
  )
  val human2 = Human(
    id = 1001,
    name = Some("Darth Vader"),
    friendIds = List(1004),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    homePlanet = Some("Tatooine")
  )
  val human3 = Human(
    id = 1002,
    name = Some("Han Solo"),
    friendIds = List(1000, 1003, 2001),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    homePlanet = None
  )
  val human4 = Human(
    id = 1003,
    name = Some("Leia Organa"),
    friendIds = List(1000, 1002, 2000, 2001),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    homePlanet = Some("Alderaan")
  )
  val human5 = Human(
    id = 1004,
    name = Some("Wilhuff Tarkin"),
    friendIds = List(1001),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    homePlanet = None
  )

  val humans = List(human1, human2, human3, human4, human5)

  val droid1 = Droid(
    id = 2000,
    name = Some("C-3PO"),
    friendIds = List(1000, 1002, 1003, 2001),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    primaryFunction = Some("Protocol")
  )
  val droid2 = Droid(
    id = 2001,
    name = Some("R2-D2"),
    friendIds = List(1000, 1002, 1003),
    appearsIn = List(Episode.NewHope, Episode.Empire, Episode.Jedi),
    primaryFunction = Some("Astromech")
  )

  val droids = List(droid1, droid2)
}
