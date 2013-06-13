package net.anzix;

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.Plugin

import org.gradle.api.tasks.TaskState

class UploadPlugin implements Plugin<Project> {

  void apply(Project project) {
    project.apply(plugin: 'maven')

    project.configurations {
    deployerJars
    }

project.dependencies {
  deployerJars 'org.apache.maven.wagon:wagon-webdav:1.0-beta-2'
}

project.uploadArchives {
    repositories.mavenDeployer {
        def configureAuth = {
            authentication(userName: 'anzix', password: project.repopwd)
        }
        configuration = project.configurations.deployerJars
        snapshotRepository(url: "dav:https://repository-anzix.forge.cloudbees.com/snapshot/", configureAuth)
        repository(url: "dav:https://repository-anzix.forge.cloudbees.com/release/", configureAuth)
    }
}


  }
}

