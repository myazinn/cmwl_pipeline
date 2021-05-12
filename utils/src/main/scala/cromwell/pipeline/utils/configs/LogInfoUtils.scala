package cromwell.pipeline.utils.configs

import cromwell.pipeline.utils._
import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{ JsString, Json, Writes, __ }

trait LogInfoUtils {
  def getConfigMessage: String
}

object LogInfoUtils {

  implicit val secretWrites: Writes[SecretData] = Writes { (secret: SecretData) =>
    JsString(secret.secretData.toUpperCase())
  }

  case class SecretData(secretData: String, owner: String)

  def getConfigMessage(applicationConfig: ApplicationConfig): String = {

    implicit val wsWrites: Writes[WebServiceConfig] =
      ((__ \ "interface").write[String] ~
        (__ \ "port").write[Int])(unlift(WebServiceConfig.unapply))

    implicit val expTimeWrites: Writes[ExpirationTimeInSeconds] =
      ((__ \ "accessToken").write[Long] ~
        (__ \ "refreshToken").write[Long] ~
        (__ \ "userSession").write[Long])(unlift(ExpirationTimeInSeconds.unapply))

    implicit val authWrites: Writes[AuthConfig] =
      ((__ \ "secretKey").write[String] ~
        (__ \ "hmacAlgorithm").write[String] ~
        (__ \ "expirationTimeInSeconds").write[ExpirationTimeInSeconds])(
        authConfig =>
          unlift(AuthConfig.unapply)(authConfig)
            .copy(_1 = authConfig.secretKey.map(_ => "*").mkString(""), _2 = authConfig.hmacAlgorithm.toString)
      )

    implicit val gitLabWrites: Writes[GitLabConfig] =
      ((__ \ "url").write[String] ~
        (__ \ "token").write[String] ~
        (__ \ "defaultFileVersion").write[String] ~
        (__ \ "defaultBranch").write[String])(
        gitLabConfig =>
          unlift(GitLabConfig.unapply)(gitLabConfig).copy(_2 = gitLabConfig.token.head._2.map(_ => "*").mkString(""))
      )

    implicit val mongoWrites: Writes[MongoConfig] =
      ((__ \ "user").write[String] ~
        (__ \ "password").write[String] ~
        (__ \ "host").write[String] ~
        (__ \ "port").write[Int] ~
        (__ \ "authenticationDatabase").write[String] ~
        (__ \ "database").write[String] ~
        (__ \ "collection").write[String])(
        mongoConfig =>
          unlift(MongoConfig.unapply)(mongoConfig).copy(_2 = mongoConfig.password.map(_ => "*").mkString(""))
      )

    implicit val postgreWrites: Writes[PostgreConfig] =
      ((__ \ "serverName").write[String] ~
        (__ \ "portNumber").write[Int] ~
        (__ \ "databaseName").write[String] ~
        (__ \ "user").write[String] ~
        (__ \ "password").write[String])(
        postgreConfig =>
          unlift(PostgreConfig.unapply)(postgreConfig).copy(_5 = postgreConfig.password.map(_ => "*").mkString(""))
      )

    implicit val appConfigWrites: Writes[ApplicationConfig] =
      ((__ \ "WebServiceConfig").write[WebServiceConfig] ~
        (__ \ "AuthConfig").write[AuthConfig] ~
        (__ \ "GitLabConfig").write[GitLabConfig] ~
        (__ \ "MongoConfig").write[MongoConfig] ~
        (__ \ "PostgreConfig").write[PostgreConfig])(unlift(ApplicationConfig.unapply))

    val messageHeader =
      "\n---------------------------------------------------\n" +
        "Application starts with the following configuration\n" +
        "---------------------------------------------------\n"

    s"$messageHeader${Json.prettyPrint(Json.toJson(applicationConfig))}"
  }

}
