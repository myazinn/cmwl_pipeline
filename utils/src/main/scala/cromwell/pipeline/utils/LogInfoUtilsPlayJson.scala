package cromwell.pipeline.utils

import play.api.libs.json.{ Json, Writes }

case class ConfigCollector(
  mongoConfig: MongoConfig,
  webServiceConfig: WebServiceConfig,
  gitLabConfig: GitLabConfig,
  authConfig: AuthConfig,
  postgreConfig: PostgreConfig
)

object LogInfoUtilsPlayJson {

  def getConfigMessage(configCollector: ConfigCollector): String = {

    def concealData(data: Any): String = data match {
      case Array() => ""
      case ""      => ""
      case _       => "*********"
    }

    implicit val mongoWrites: Writes[MongoConfig] = (config: MongoConfig) =>
      Json.obj(
        "user" -> config.user,
        "password" -> concealData(config.password),
        "host" -> config.host,
        "port" -> config.port,
        "authenticationDatabase" -> config.authenticationDatabase,
        "database" -> config.database,
        "collection" -> config.collection
      )

    implicit val wsWrites: Writes[WebServiceConfig] = (config: WebServiceConfig) =>
      Json.obj(
        "interface" -> config.interface,
        "port" -> config.port
      )

    implicit val gitLabWrites: Writes[GitLabConfig] = (config: GitLabConfig) =>
      Json.obj(
        "url" -> config.url,
        "token" -> concealData(config.token),
        "defaultFileVersion" -> config.defaultFileVersion,
        "defaultBranch" -> config.defaultBranch
      )

    implicit val expTimeWrites: Writes[ExpirationTimeInSeconds] = (config: ExpirationTimeInSeconds) =>
      Json.obj(
        "accessToken" -> config.accessToken,
        "refreshToken" -> config.refreshToken,
        "userSession" -> config.userSession
      )

    implicit val authWrites: Writes[AuthConfig] = (config: AuthConfig) =>
      Json.obj(
        "secretKey" -> concealData(config.secretKey),
        "hmacAlgorithm" -> config.hmacAlgorithm.toString,
        "expirationTimeInSeconds" -> config.expirationTimeInSeconds
      )

    implicit val postgreWrites: Writes[PostgreConfig] = (config: PostgreConfig) =>
      Json.obj(
        "serverName" -> config.serverName,
        "portNumber" -> config.portNumber,
        "databaseName" -> config.databaseName,
        "user" -> config.user,
        "password" -> concealData(config.password)
      )

    implicit val configCollectorWrites: Writes[ConfigCollector] = (config: ConfigCollector) =>
      Json.obj(
        "MongoConfig" -> config.mongoConfig,
        "WebServiceConfig" -> config.webServiceConfig,
        "GitLabConfig" -> config.gitLabConfig,
        "AuthConfig" -> config.authConfig,
        "PostgreConfig" -> config.postgreConfig
      )

    val messageHeader =
      s"\n----------------------------------------------------\n" +
        s"Application starts with the following configuration\n" +
        s"----------------------------------------------------\n"

    messageHeader + Json.prettyPrint(Json.toJson(configCollector))
  }
}
