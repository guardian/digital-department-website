package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models._
import com.gu.scanamo._
import org.joda.time.{ DateTimeZone, DateTime }
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{ Action, Controller }

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String) extends Controller {
  import Application._
  import Talk.jodaStringFormat

  def index = Action { req =>
    val jsFileName = "bundle.js"
    val jsLocation = routes.Assets.versioned(jsFileName).toString
    Ok(views.html.Application.app("Digital Department Website", jsLocation))
  }

  def talks() = Action {
    val talksList = Scanamo.scan[Talk](dynamoClient)(talksTableName).flatMap(_.toOption)
    Ok(views.html.talks(talksList))
  }

  def createTalkGet() = Action {
    Ok(views.html.createTalk(createTalkForm))
  }

  def createTalk() = Action(parse.form(createTalkForm)) { implicit request =>
    createTalkForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createTalk(createTalkForm)),
      talkData =>
        // TODO: Save to Dynamo
        Redirect(routes.Application.talks())
    )
  }
}

object Application {

  case class CreateTalkFormData(title: String,
    url: String,
    authors: String,
    location: String,
    date: DateTime,
    thumbnail: String)

  val createTalkForm: Form[CreateTalkFormData] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "authors" -> nonEmptyText(maxLength = 200),
      "location" -> nonEmptyText(maxLength = 200),
      "date" -> nonEmptyText(maxLength = 200)
        .transform(date => DateTime.parse(date).withZone(DateTimeZone.UTC), (date: DateTime) => date.toString()),
      "thumbnail" -> nonEmptyText(maxLength = 200)
    )(CreateTalkFormData.apply)(CreateTalkFormData.unapply)
  )

}