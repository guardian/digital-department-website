package models

import org.joda.time.DateTime
import automagic._

object Forms {
  case class TalkFormData(
    title: String,
    url: String,
    authors: Seq[Author],
    location: String,
    date: DateTime,
    thumbnail: String)

  object TalkFormData {
    def apply(talk: Talk): TalkFormData = {
      transform[Talk, TalkFormData](talk)
    }
  }

  case class EventFormData(
    title: String,
    description: String,
    thumbnail: String,
    url: String,
    date: DateTime)

  object EventFormData {
    def apply(event: Event): EventFormData = {
      transform[Event, EventFormData](event)
    }
  }

  case class ProjectFormData(
    title: String,
    description: String,
    url: String,
    status: String)

  object ProjectFormData {
    def apply(project: Project): ProjectFormData = {
      transform[Project, ProjectFormData](project)
    }
  }
}

