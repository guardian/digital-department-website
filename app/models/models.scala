package models

import org.joda.time.DateTime

case class Talk(title: String,
                 url: String,
                 authors: Seq[Author],
                 location: String,
                 date: DateTime,
                 thumbnail: String)

case class Author(name: String,
                  twitterHandle: String,
                  avatar: Option[String])

case class Project(title: String,
                   description: String,
                   url: String,
                   status: Status)

sealed trait Status
case object ActiveProject extends Status
case object IncubatedProject extends Status

case class Events(title: String,
                  description: String,
                  thumbnail: String,
                  url: String,
                  date: Option[DateTime])
