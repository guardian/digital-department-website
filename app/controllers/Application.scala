package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models._
import com.gu.scanamo._
import org.joda.time.{ DateTimeZone, DateTime }
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, Controller }

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String, eventsTableName: String, projectsTableName: String, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  import Application._
  import DbFormats.jodaStringFormat

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

  def events() = Action {
    val eventsList = Scanamo.scan[Event](dynamoClient)(eventsTableName).flatMap(_.toOption)
    Ok(views.html.events(eventsList))
  }

  def createEventPage() = Action { implicit request =>
    Ok(views.html.createEvent(createEventForm))
  }

  def createEvent() = Action { implicit request =>
    createEventForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createEvent(createEventForm)),
      eventData => {
        val putResult = Scanamo.put(dynamoClient)(eventsTableName)(eventData)
        Redirect(routes.Application.events())
      }
    )
  }

  def projects() = Action {
    val projectsList = Scanamo.scan[Project](dynamoClient)(projectsTableName).flatMap(_.toOption)
    val activeProjectsList = projectsList.filter(_.status == Project.Active)
    val incubatedProjectsList = projectsList.filter(_.status == Project.Incubated)
    Ok(views.html.projects(activeProjectsList, incubatedProjectsList))
  }

  def createProjectPage() = Action { implicit request =>
    Ok(views.html.createProject(createProjectForm))
  }

  def createProject() = Action { implicit request =>
    createProjectForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createProject(createProjectForm)),
      projectData => {
        val putResult = Scanamo.put(dynamoClient)(projectsTableName)(projectData)
        Redirect(routes.Application.projects())
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

  val createEventForm: Form[Event] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "description" -> nonEmptyText(maxLength = 200),
      "thumbnail" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "date" -> nonEmptyText(maxLength = 200)
        .transform(date => DateTime.parse(date).withZone(DateTimeZone.UTC), (date: DateTime) => date.toString())
    )(Event.apply)(Event.unapply)
  )

  val createProjectForm: Form[Project] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "description" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "status" -> nonEmptyText(maxLength = 200)
    )(Project.apply)(Project.unapply)
  )
}
