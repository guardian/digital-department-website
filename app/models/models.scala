package models

import controllers.Application.CreateTalkFormData
import org.joda.time._
import com.gu.scanamo._

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

object Talk {
  implicit val jodaStringFormat =
    DynamoFormat.coercedXmap[DateTime, String, IllegalArgumentException](DateTime.parse(_).withZone(DateTimeZone.UTC))(_.toString)

  def apply(talkData: CreateTalkFormData): Talk = {
    Talk(title = talkData.title,
      url = talkData.url,
      authors = Seq(Author(name = "some author name", url = None, avatar = None)),
      location = talkData.location,
      date = talkData.date,
      thumbnail = talkData.thumbnail)
  }
}

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
  date: DateTime)
