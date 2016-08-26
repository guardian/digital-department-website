package controllers

import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

class Application(wsClient: WSClient) extends Controller {
  def index = Action { req =>

    val jsFileName = "bundle.js"
    val jsLocation = routes.Assets.versioned(jsFileName).toString

    Ok(views.html.Application.app("Digital Department Website", jsLocation))
  }
}
