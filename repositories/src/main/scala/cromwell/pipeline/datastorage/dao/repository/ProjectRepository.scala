package cromwell.pipeline.datastorage.dao.repository

import cromwell.pipeline.database.PipelineDatabaseEngine
import cromwell.pipeline.datastorage.dao.entry.ProjectEntry
import cromwell.pipeline.datastorage.dto.{ Project, ProjectId }

import scala.concurrent.Future

class ProjectRepository(pipelineDatabaseEngine: PipelineDatabaseEngine, projectEntry: ProjectEntry) {

  import pipelineDatabaseEngine._
  import pipelineDatabaseEngine.profile.api._

  def getProjectById(projectId: ProjectId): Future[Option[Project]] =
    database.run(projectEntry.getProjectByIdAction(projectId).result.headOption)

  def getProjectsByName(name: String): Future[Seq[Project]] =
    database.run(projectEntry.getProjectsByNameAction(name).result)

  def addProject(project: Project): Future[ProjectId] = database.run(projectEntry.addProjectAction(project))

  def deactivateProjectById(projectId: ProjectId): Future[Int] =
    database.run(projectEntry.deactivateProjectByIdAction(projectId))

  def updateProjectName(updatedProject: Project): Future[Int] =
    database.run(projectEntry.updateProjectNameAction(updatedProject))

  def updateProjectVersion(updatedProject: Project): Future[Int] =
    database.run(projectEntry.updateProjectVersionAction(updatedProject))
}
