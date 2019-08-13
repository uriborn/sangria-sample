package com.example.sangria

class CharacterRepository {
  def getHero(episode: Option[Episode]): StarWarsCharacter = {
    episode.flatMap(_ => getHuman(1000)) getOrElse Data.droids.last
  }

  def getHumans(limit: Int, offset: Int): List[Human] = Data.humans.slice(offset, limit + offset)
  def getHuman(id: Int): Option[Human] = Data.humans.find(h => h.id == id)

  def getDroids(limit: Int, offset: Int): List[Droid] = Data.droids.slice(offset, limit + offset)
  def getDroid(id: Int): Option[Droid] = Data.droids.find(h => h.id == id)
}
