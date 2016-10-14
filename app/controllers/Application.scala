package controllers

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import models._
import models.Forms._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, Controller }
import services.DynamoDbService

class Application(dynamoClient: AmazonDynamoDB, talksTableName: String, eventsTableName: String, projectsTableName: String, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  import Application._

  private lazy val dynamoDbService = new DynamoDbService(dynamoClient, talksTableName, eventsTableName, projectsTableName)

  def index = Action { implicit reqest =>
    Ok(views.html.index("Digital Department Website"))
  }

  def talks() = Action { implicit request =>
    val talksList = dynamoDbService.scanTalks()
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
        dynamoDbService.put(Talk(talkData = talkData))
        Redirect(routes.Application.talks())
      }
    )
  }

  def editTalkPage(id: String) = Action { implicit request =>
    dynamoDbService.queryTalks(id) match {
      case Some(talk) => Ok(views.html.editTalk(id, talkForm.fill(TalkFormData(talk)), talk.authors.length))
      case None => NotFound
    }
  }

  def editTalk(id: String) = Action { implicit request =>
    talkForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.editTalk(id, formWithErrors, formWithErrors.get.authors.length)),
      talkData => {
        dynamoDbService.put(Talk(Some(id), talkData))
        Redirect(routes.Application.talks())
      }
    )
  }

  def deleteTalk(id: String) = Action { implicit request =>
    dynamoDbService.deleteTalk(id)
    Redirect(routes.Application.talks())
  }

  def events() = Action { implicit request =>
    val eventsList = dynamoDbService.scanEvents()
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
        dynamoDbService.put(Event(eventData = eventData))
        Redirect(routes.Application.events())
      }
    )
  }

  def editEventPage(id: String) = Action { implicit request =>
    val event = dynamoDbService.queryEvents(id)
    event match {
      case Some(event) => Ok(views.html.editEvent(id, eventForm.fill(EventFormData(event))))
      case None => NotFound
    }
  }

  def editEvent(id: String) = Action { implicit request =>
    eventForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.editEvent(id, formWithErrors)),
      eventData => {
        dynamoDbService.put(Event(Some(id), eventData))
        Redirect(routes.Application.events())
      }
    )
  }

  def deleteEvent(id: String) = Action { implicit request =>
    dynamoDbService.deleteEvent(id)
    Redirect(routes.Application.events())
  }

  def projects() = Action { implicit request =>
    val projectsList = dynamoDbService.scanProjects()
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
        dynamoDbService.put(Project(projectData = projectData))
        Redirect(routes.Application.projects())
      }
    )
  }

  def editProjectPage(id: String) = Action { implicit request =>
    val project = dynamoDbService.queryProjects(id)
    project match {
      case Some(project) => Ok(views.html.editProject(id, projectForm.fill(ProjectFormData(project))))
      case None => NotFound
    }
  }

  def editProject(id: String) = Action { implicit request =>
    projectForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.editProject(id, formWithErrors)),
      projectData => {
        dynamoDbService.put(Project(Some(id), projectData))
        Redirect(routes.Application.projects())
      }
    )
  }

  def deleteProject(id: String) = Action { implicit request =>
    dynamoDbService.deleteProject(id)
    Redirect(routes.Application.projects())
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
        )(Author.apply)(Author.unapply)
      ),
      "location" -> nonEmptyText(maxLength = 200),
      "date" -> jodaDate,
      "thumbnail" -> nonEmptyText(maxLength = 200)
    )(TalkFormData.apply)(TalkFormData.unapply)
  )

  val eventForm: Form[EventFormData] = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 200),
      "description" -> nonEmptyText(maxLength = 200),
      "thumbnail" -> nonEmptyText(maxLength = 200),
      "url" -> nonEmptyText(maxLength = 200),
      "date" -> jodaDate
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
