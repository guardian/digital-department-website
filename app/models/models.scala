package models

import java.util.UUID

import models.Forms.{ EventFormData, ProjectFormData, TalkFormData, AuthorFormData }
import org.joda.time._
import com.gu.scanamo._
import automagic._

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
    transform[AuthorFormData, Author](authorData,
      "id" -> UUID.randomUUID().toString
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
    transform[TalkFormData, Talk](talkData,
      "id" -> someId,
      "authors" -> talkData.authors.foldLeft(Seq.empty: Seq[Author]) { (seq, authorFormData) => seq :+ Author(authorFormData) }
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
    transform[ProjectFormData, Project](projectData,
      "id" -> UUID.randomUUID().toString
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
  def apply(id: Option[String] = None, eventData: EventFormData): Event = {
    val someId = id match {
      case Some(value) => value
      case None => UUID.randomUUID().toString
    }
    transform[EventFormData, Event](eventData,
      "id" -> someId
    )
  }
}

