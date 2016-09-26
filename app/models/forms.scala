package models

import org.joda.time.DateTime

object Forms {
  case class TalkFormData(
    title: String,
    url: String,
    authors: Seq[AuthorFormData],
    location: String,
    date: DateTime,
    thumbnail: String)

  object TalkFormData {
    def apply(talk: Talk): TalkFormData = {
      TalkFormData(
        title = talk.title,
        url = talk.url,
        authors = talk.authors.foldLeft(Seq.empty: Seq[AuthorFormData]) { (seq, author) => seq :+ AuthorFormData(author) },
        location = talk.location,
        date = talk.date,
        thumbnail = talk.thumbnail
      )
    }
  }

  case class AuthorFormData(
    name: String,
    url: Option[String],
    avatar: Option[String])

  object AuthorFormData {
    def apply(author: Author): AuthorFormData = {
      AuthorFormData(
        name = author.name,
        url = author.url,
        avatar = author.avatar
      )
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
      EventFormData(
        title = event.title,
        description = event.description,
        thumbnail = event.thumbnail,
        url = event.url,
        date = event.date)
    }
  }

  case class ProjectFormData(
    title: String,
    description: String,
    url: String,
    status: String)
}
