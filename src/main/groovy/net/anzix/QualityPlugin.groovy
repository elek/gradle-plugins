package net.anzix;

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.Plugin

import org.gradle.api.tasks.TaskState

class QualityPlugin implements Plugin<Project> {

  void apply(Project project) {
    project.apply(plugin: 'pmd')
    project.apply(plugin: 'checkstyle')
    def checkstyle = project.file("$project.buildDir/checkstyle.xml")
    project.task('codestyleDownload') << {

      def File destDir = project.file("$project.buildDir")
      if (!destDir.exists()) {
         destDir.mkdirs();
      }
      if (!checkstyle.exists()) {
        project.ant.get(src: "https://raw.github.com/elek/codestyle/master/checkstyle.xml", dest: checkstyle)
      }
      checkstylexsl = project.file("${project.buildDir}/checkstyle.xsl")
      if (!checkstylexsl.exists()) {
        project.ant.get(src: "https://raw.github.com/elek/codestyle/master/checkstyle.xsl", dest: checkstylexsl)
      }
    }

    project.checkstyleMain.dependsOn(project.codestyleDownload)

    project.gradle.taskGraph.afterTask { Task task, TaskState state ->
      if(task.name == 'checkstyleMain') {
        if (project.file("${project.buildDir}/reports/checkstyle/main.xml").exists()) {
          project.ant.xslt(in: "${project.buildDir}/reports/checkstyle/main.xml",
          style:"${project.buildDir}/checkstyle.xsl",
          out:"${project.buildDir}/reports/checkstyle/checkstyle_main.html"
                          )
        }
      }
    }


    project.checkstyle {
      configFile = checkstyle
    }
   }
}

