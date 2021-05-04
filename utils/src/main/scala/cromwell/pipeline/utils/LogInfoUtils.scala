package cromwell.pipeline.utils

object LogInfoUtils {

  def getStartingConfigMessage(
    dbConfig: MongoConfig,
    wsConfig: WebServiceConfig,
    gitLabConfig: GitLabConfig
  ): String = {

    val secretData = "*****"

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
        "user": "${dbConfig.user}",
        "password": "${concealData(dbConfig.password)}",
        "host": "${dbConfig.host}",
        "port": "${dbConfig.port}",
        "authenticationDatabase": "${dbConfig.authenticationDatabase}",
        "database": "${dbConfig.database}",
        "collection": "${dbConfig.collection}"
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
      }
    }"""
  }
}
