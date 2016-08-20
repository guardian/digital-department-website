package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models.Talk
import com.gu.scanamo._
import com.gu.scanamo.syntax._
import play.api.mvc.{ Action, Controller }

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String) extends Controller {

  def index() = Action {
    Ok(views.html.index())
  }

  def talks() = Action {
    val talks = List.empty //Scanamo.scan[Talk](dynamoClient)(talksTableName)
    Ok(views.html.talks(talks))
  }
}

