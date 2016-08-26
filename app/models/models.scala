package models

import org.joda.time.DateTime

case class Author(
  name: String,
  url: Option[String],
  avatar: Option[String])

case class Talk(
  title: String,
  url: String,
  authors: Seq[Author],
  location: String,
  date: DateTime,
  thumbnail: String)

case class Project(
  title: String,
  description: String,
  url: String,
  status: String)

object Project {
  val Active = "Active"
  val Incubated = "Incubated"
}

case class Events(
  title: String,
  description: String,
  thumbnail: String,
  url: String,
  date: Option[DateTime])
