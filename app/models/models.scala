package models

import org.joda.time._
import com.gu.scanamo._
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._

case class Author(
  name: String,
  url: Option[String],
  avatar: Option[String])

case class Talk(
  title: String,
  url: String,
  authors: Seq[Author],
  location: String,
  date: String,
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
  date: DateTime)

object db {
  implicit val jodaStringFormat = DynamoFormat.coercedXmap[DateTime, String, IllegalArgumentException](
    DateTime.parse(_).withZone(DateTimeZone.UTC)
  )(
      _.toString
    )
}
