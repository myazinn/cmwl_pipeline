package cromwell.pipeline.utils

object LogInfoUtils {

  def getConfigMessage(
    mongoConfig: MongoConfig,
    wsConfig: WebServiceConfig,
    gitLabConfig: GitLabConfig,
    authConfig: AuthConfig,
    postgreConfig: PostgreConfig
  ): String = {

    val secretData = "******"

    def concealData(data: Any): String = data match {
      case Array() => ""
      case ""      => ""
      case _       => secretData
    }
    s"""
----------------------------------------------------
Application starts with the following configuration
----------------------------------------------------
{
  "MongoConfig": {
    "user": "${mongoConfig.user}",
    "password": "${concealData(mongoConfig.password)}",
    "host": "${mongoConfig.host}",
    "port": "${mongoConfig.port}",
    "authenticationDatabase": "${mongoConfig.authenticationDatabase}",
    "database": "${mongoConfig.database}",
    "collection": "${mongoConfig.collection}"
  },
  "WebServiceConfig": {
    "interface": "${wsConfig.interface}",
    "port": "${wsConfig.port}"
  },
  "GitLabConfig": {
    "url": "${gitLabConfig.url}",
    "token": "${concealData(gitLabConfig.token.head._1)}",
    "defaultFileVersion": "${gitLabConfig.defaultFileVersion}",
    "defaultBranch": "${gitLabConfig.defaultBranch}"
  },
  "PostgreConfig": {
    "serverName": "${postgreConfig.serverName}",
    "portNumber": "${postgreConfig.portNumber}",
    "databaseName": "${postgreConfig.databaseName}",
    "user": "${postgreConfig.user}",
    "password": "${concealData(postgreConfig.password)}"
  },
  "AuthConfig": {
    "secretKey": "${concealData(authConfig.secretKey)}",
    "hmacAlgorithm": "${authConfig.hmacAlgorithm}",
    "expirationTimeInSeconds": {
      "accessToken": "${authConfig.expirationTimeInSeconds.accessToken}",
      "refreshToken": "${authConfig.expirationTimeInSeconds.refreshToken}",
      "userSession": "${authConfig.expirationTimeInSeconds.userSession}"
    }
  }
}"""
  }
}
