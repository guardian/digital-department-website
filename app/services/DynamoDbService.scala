package services

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.gu.scanamo.Scanamo
import com.gu.scanamo
import com.gu.scanamo._
import com.gu.scanamo.syntax._

class DynamoDbService[A](client: AmazonDynamoDB, tableName: String) {
  import models.DbFormats.jodaStringFormat

  def scan(): Seq[A] = {
    Scanamo.scan[A](client)(tableName).flatMap(_.toOption)
  }

  def put(item: A): Unit = {
    Scanamo.put(client)(tableName)(item)
  }

  def query(id: String): Option[A] = {
    Scanamo.query[A](client)(tableName)('id -> id).flatMap(_.toOption).headOption
  }

  def update(id: String, item: A): Unit = {
    //Scanamo.update(client)(tableName)('id -> id, set(item))
  }
}
