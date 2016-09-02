import controllers._
import controllers._
import play.api.ApplicationLoader.Context
import play.api.i18n.{ DefaultLangs, DefaultMessagesApi, MessagesApi }
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.{ BuiltInComponents, BuiltInComponentsFromContext }
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import router.Routes
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{ InstanceProfileCredentialsProvider, EnvironmentVariableCredentialsProvider, AWSCredentialsProviderChain }
import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient

trait AWSComponent { self: BuiltInComponents =>
  lazy private val awsCreds = new AWSCredentialsProviderChain(
    new EnvironmentVariableCredentialsProvider(),
    new ProfileCredentialsProvider("developerPlayground"),
    new InstanceProfileCredentialsProvider()
  )

  lazy private val awsRegion = {
    val r = configuration.getString("aws.region").map(Regions.fromName).getOrElse(Regions.EU_WEST_1)
    Region.getRegion(r)
  }

  lazy val dynamoClient = awsRegion.createClient(classOf[AmazonDynamoDBAsyncClient], awsCreds, null)
  lazy val talksTableName = "digital-department-website-talks"
}

trait ControllersComponent {
  self: BuiltInComponents with AWSComponent =>
  def messagesApi: MessagesApi = new DefaultMessagesApi(environment, configuration, new DefaultLangs(configuration))
  def appController = new Application(dynamoClient, talksTableName, messagesApi)
  val assets = new controllers.Assets(httpErrorHandler)
  val router: Router = new Routes(httpErrorHandler, appController, assets)
}

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
  with AhcWSComponents
  with ControllersComponent
  with AWSComponent
