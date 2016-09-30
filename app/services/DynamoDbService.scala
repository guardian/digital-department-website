package services

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.gu.scanamo.{ DynamoFormat, Scanamo }
import com.gu.scanamo.syntax._
import models._
import org.joda.time.{ DateTimeZone, DateTime }

class DynamoDbService(client: AmazonDynamoDB, talksTableName: String, eventsTableName: String, projectsTableName: String) {

  implicit val jodaStringFormat =
    DynamoFormat.coercedXmap[DateTime, String, IllegalArgumentException](DateTime.parse(_).withZone(DateTimeZone.getDefault))(_.toString)

  // TALKS operations
  def scanTalks(): Seq[Talk] = {
    Scanamo.scan[Talk](client)(talksTableName).flatMap(_.toOption)
  }

  def put(item: Talk): Unit = {
    Scanamo.put(client)(talksTableName)(item)
  }

  def queryTalks(id: String): Option[Talk] = {
    Scanamo.query[Talk](client)(talksTableName)('id -> id).flatMap(_.toOption).headOption
  }

  def deleteTalk(id: String): Unit = {
    Scanamo.delete(client)(talksTableName)('id -> id)
  }

  // EVENTS operations
  def scanEvents(): Seq[Event] = {
    Scanamo.scan[Event](client)(eventsTableName).flatMap(_.toOption)
  }

  def put(item: Event): Unit = {
    Scanamo.put(client)(eventsTableName)(item)
  }

  def queryEvents(id: String): Option[Event] = {
    Scanamo.query[Event](client)(eventsTableName)('id -> id).flatMap(_.toOption).headOption
  }

  // PROJECTS operations
  def scanProjects(): Seq[Project] = {
    Scanamo.scan[Project](client)(projectsTableName).flatMap(_.toOption)
  }

  def put(item: Project): Unit = {
    Scanamo.put(client)(projectsTableName)(item)
  }

  def queryProjects(id: String): Option[Project] = {
    Scanamo.query[Project](client)(projectsTableName)('id -> id).flatMap(_.toOption).headOption
  }
}
