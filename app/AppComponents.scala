import controllers._
import play.api.ApplicationLoader.Context
import play.api.i18n.{ DefaultLangs, DefaultMessagesApi, MessagesApi }
import play.api.{ BuiltInComponents, BuiltInComponentsFromContext }
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import router.Routes
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{ InstanceProfileCredentialsProvider, SystemPropertiesCredentialsProvider, EnvironmentVariableCredentialsProvider, AWSCredentialsProviderChain }
import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient

trait AWSComponent { self: BuiltInComponents =>
  private val awsCreds = new AWSCredentialsProviderChain(
    new EnvironmentVariableCredentialsProvider(),
    new ProfileCredentialsProvider("developerPlayground"),
    new InstanceProfileCredentialsProvider()
  )

  private val awsRegion = {
    val r = configuration.getString("aws.region").map(Regions.fromName).getOrElse(Regions.EU_WEST_1)
    Region.getRegion(r)
  }

  val dynamoClient = new AmazonDynamoDBAsyncClient(awsCreds)

  val talksTableName = "digital-development-website-talks"
}

trait ControllersComponent {
  self: BuiltInComponents with AWSComponent =>
  val messagesApi: MessagesApi = new DefaultMessagesApi(environment, configuration, new DefaultLangs(configuration))
  val appController = new Application(dynamoClient, talksTableName)

  val assets = new controllers.Assets(httpErrorHandler)
  val router: Router = new Routes(httpErrorHandler, appController, assets)
}

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
  with AhcWSComponents
  with ControllersComponent
  with AWSComponent
