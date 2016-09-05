package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models._
import com.gu.scanamo._
import play.api.mvc.{ Action, Controller }

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String) extends Controller {

  def index = Action { req =>
    val jsFileName = "bundle.js"
    val jsLocation = routes.Assets.versioned(jsFileName).toString
    Ok(views.html.Application.app("Digital Department Website", jsLocation))
  }

  def talks() = Action {
    val talksList = Scanamo.scan[Talk](dynamoClient)(talksTableName).flatMap(_.toOption)
    Ok(views.html.talks(talksList))
  }
}
