package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models._
import models.Forms._
import org.joda.time.{ DateTimeZone, DateTime }
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, Controller }
import services.DynamoDbService

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String, eventsTableName: String, projectsTableName: String, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  import Application._

  private val talksDynamoDbService = new DynamoDbService[Talk](dynamoClient, talksTableName)
  private val eventsDynamoDbService = new DynamoDbService[Event](dynamoClient, eventsTableName)
  private val projectsDynamoDbService = new DynamoDbService[Project](dynamoClient, projectsTableName)

  def index = Action { req =>
    val jsFileName = "bundle.js"
    val jsLocation = routes.Assets.versioned(jsFileName).toString
    Ok(views.html.app("Digital Department Website", jsLocation))
  }

  def talks() = Action {
    val talksList = talksDynamoDbService.scan()
    Ok(views.html.talks(talksList))
  }

  def createTalkPage() = Action { implicit request =>
    Ok(views.html.createTalk(talkForm))
  }

  def createTalk() = Action { implicit request =>
    talkForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createTalk(formWithErrors)),
      talkData => {
        talksDynamoDbService.put(Talk(talkData))
        Redirect(routes.Application.talks())
      }
    )
  }

  def events() = Action {
    val eventsList = eventsDynamoDbService.scan()
    Ok(views.html.events(eventsList))
  }

  def createEventPage() = Action { implicit request =>
    Ok(views.html.createEvent(eventForm))
  }

  def createEvent() = Action { implicit request =>
    eventForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createEvent(formWithErrors)),
      eventData => {
        eventsDynamoDbService.put(Event(eventData))
        Redirect(routes.Application.events())
      }
    )
  }

  def projects() = Action {
    val projectsList = projectsDynamoDbService.scan()
    val activeProjectsList = projectsList.filter(_.status == Project.Active)
    val incubatedProjectsList = projectsList.filter(_.status == Project.Incubated)
    Ok(views.html.projects(activeProjectsList, incubatedProjectsList))
  }

  def createProjectPage() = Action { implicit request =>
    Ok(views.html.createProject(projectForm))
  }

  def createProject() = Action { implicit request =>
    projectForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.createProject(formWithErrors)),
      projectData => {
        projectsDynamoDbService.put(Project(projectData))
        Redirect(routes.Application.projects())
      }
    )
  }

  def editTalkPage(id: String) = Action { implicit request =>
    talksDynamoDbService.query(id) match {
      case Some(talk) => Ok(views.html.editTalk(id, talkForm.fill(TalkFormData(talk))))
      case None => ???
    }
  }

  def editTalk(id: String) = Action { implicit request =>
    talkForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.editTalk(id, formWithErrors)),
      talkData => {
        talksDynamoDbService.update(id, Talk(talkData))
        Redirect(routes.Application.talks())
      }
    )
  }
}

object Application {
  import models.Forms._

  val talkForm: Form[TalkFormData] = Form(
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
    )(TalkFormData.apply)(TalkFormData.unapply)
  )

  val eventForm: Form[EventFormData] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "description" -> nonEmptyText(maxLength = 200),
      "thumbnail" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "date" -> nonEmptyText(maxLength = 200)
        .transform(date => DateTime.parse(date).withZone(DateTimeZone.UTC), (date: DateTime) => date.toString())
    )(EventFormData.apply)(EventFormData.unapply)
  )

  val projectForm: Form[ProjectFormData] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "description" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "status" -> nonEmptyText(maxLength = 200)
    )(ProjectFormData.apply)(ProjectFormData.unapply)
  )
}
