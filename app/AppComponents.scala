
import controllers._
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import router.Routes

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) with AhcWSComponents {

  lazy val applicationController = new Application(wsClient)

  override lazy val router: Router = new Routes(httpErrorHandler, applicationController, new controllers.Assets(httpErrorHandler))

}
