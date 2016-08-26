package controllers

import play.api.mvc.{ Action, Controller }
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models.Talk
import com.gu.scanamo._
import com.gu.scanamo.syntax._

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String) extends Controller {

  def index = Action { req =>
    val jsFileName = "bundle.js"
    val jsLocation = routes.Assets.versioned(jsFileName).toString
    Ok(views.html.Application.app("Digital Department Website", jsLocation))
  }

  def talks() = Action {
    val talks = List.empty //Scanamo.scan[Talk](dynamoClient)(talksTableName)
    Ok(views.html.talks(talks))
  }
}
