package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models._
import com.gu.scanamo._
import org.joda.time.{ DateTimeZone, DateTime }
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, Controller }

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  import Application._
  import Talk.jodaStringFormat

  def index = Action { req =>
    val jsFileName = "bundle.js"
    val jsLocation = routes.Assets.versioned(jsFileName).toString
    Ok(views.html.app("Digital Department Website", jsLocation))
  }

  def talks() = Action {
    val talksList = Scanamo.scan[Talk](dynamoClient)(talksTableName).flatMap(_.toOption)
    Ok(views.html.talks(talksList))
  }

  def createTalkPage() = Action { implicit request =>
    Ok(views.html.createTalk(createTalkForm))
  }

  def createTalk() = Action { implicit request =>
    createTalkForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createTalk(createTalkForm)),
      talkData => {
        val putResult = Scanamo.put(dynamoClient)(talksTableName)(Talk(talkData))
        Redirect(routes.Application.talks())
      }
    )
  }
}

object Application {

  case class CreateTalkFormData(
    title: String,
    url: String,
    authors: Seq[AuthorFormData],
    location: String,
    date: DateTime,
    thumbnail: String)

  case class AuthorFormData(
    name: String,
    url: Option[String],
    avatar: Option[String])

  val createTalkForm: Form[CreateTalkFormData] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "authors" -> seq(
        mapping(
          "name" -> nonEmptyText(maxLength = 200),
          "url" -> optional(text(maxLength = 200)),
          "avatar" -> optional(text(maxLength = 200))
        )(AuthorFormData.apply)(AuthorFormData.unapply)
      ),
      "location" -> nonEmptyText(maxLength = 200),
      "date" -> nonEmptyText(maxLength = 200)
        .transform(date => DateTime.parse(date).withZone(DateTimeZone.UTC), (date: DateTime) => date.toString()),
      "thumbnail" -> nonEmptyText(maxLength = 200)
    )(CreateTalkFormData.apply)(CreateTalkFormData.unapply)
  )

}