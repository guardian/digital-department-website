package models

import java.util.UUID

import models.Forms.{ EventFormData, ProjectFormData, TalkFormData }
import org.joda.time.DateTime
import automagic._

case class Author(
  name: String,
  url: Option[String],
  avatar: Option[String])

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
      "id" -> someId
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

  def apply(id: Option[String] = None, projectData: ProjectFormData): Project = {
    val someId = id match {
      case Some(value) => value
      case None => UUID.randomUUID().toString
    }
    transform[ProjectFormData, Project](projectData,
      "id" -> someId
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

