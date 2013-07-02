package net.anzix;

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.Plugin

import org.gradle.api.tasks.TaskState
import org.gradle.api.publish.maven.MavenPublication

class UploadPlugin implements Plugin<Project> {

  void apply(Project project) {
    project.apply(plugin: 'maven-publish')

    if (!project.hasProperty("repoDir")) {
      project.ext.repoDir = new File(project.rootProject.buildDir, "/repo").getAbsolutePath()
    }


    project.publishing {
     
/*      publications {
	mavenJava(MavenPublication) {
	  from components.java
	}
      }*/
      repositories {
	maven {
	  url "file://" + project.repoDir
	}
      }
    }
    project.publishing.publications.create("mavenJava", MavenPublication) {
       from project.components.java
    }

   

  }
}

