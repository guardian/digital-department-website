package models

import java.util.UUID

import models.Forms.{ EventFormData, ProjectFormData, TalkFormData, AuthorFormData }
import org.joda.time._
import com.gu.scanamo._

object DbFormats {
  implicit val jodaStringFormat =
    DynamoFormat.coercedXmap[DateTime, String, IllegalArgumentException](DateTime.parse(_).withZone(DateTimeZone.UTC))(_.toString)
}

case class Author(
  id: String,
  name: String,
  url: Option[String],
  avatar: Option[String])

object Author {
  def apply(authorData: AuthorFormData): Author = {
    Author(
      id = UUID.randomUUID().toString,
      name = authorData.name,
      url = authorData.url,
      avatar = authorData.avatar
    )
  }
}

case class Talk(
  id: String,
  title: String,
  url: String,
  authors: Seq[Author],
  location: String,
  date: DateTime,
  thumbnail: String)

object Talk {
  def apply(id: Option[String] = None, talkData: TalkFormData): Talk = {
    val someId = id match {
      case Some(value) => value
      case None => UUID.randomUUID().toString
    }
    Talk(
      id = someId,
      title = talkData.title,
      url = talkData.url,
      authors = talkData.authors.foldLeft(Seq.empty: Seq[Author]) { (seq, authorFormData) => seq :+ Author(authorFormData) },
      location = talkData.location,
      date = talkData.date,
      thumbnail = talkData.thumbnail
    )
  }
}

case class Project(
  id: String,
  title: String,
  description: String,
  url: String,
  status: String)

object Project {
  val Active = "Active"
  val Incubated = "Incubated"

  def apply(projectData: ProjectFormData): Project = {
    Project(
      id = UUID.randomUUID().toString,
      title = projectData.title,
      description = projectData.description,
      url = projectData.url,
      status = projectData.status
    )
  }
}

case class Event(
  id: String,
  title: String,
  description: String,
  thumbnail: String,
  url: String,
  date: DateTime)

object Event {
  def apply(eventData: EventFormData): Event = {
    Event(
      id = UUID.randomUUID().toString,
      title = eventData.title,
      description = eventData.description,
      thumbnail = eventData.thumbnail,
      url = eventData.url,
      date = eventData.date
    )
  }
}

