package com.livk.boot.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

import java.util.regex.Pattern

/**
 * <p>
 * MavenRepositoryPlugin
 * </p>
 *
 * @author livk
 *
 */
abstract class MavenRepositoryPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(MavenPublishPlugin.class)
        def publishing = project.extensions.getByType(PublishingExtension.class)
        publishing.repositories.mavenLocal()
        publishing.repositories.maven { maven ->
            def releasesRepoUrl = project.property("mvn.releasesRepoUrl")
            def snapshotsRepoUrl = project.property("mvn.releasesRepoUrl")
            //使用不安全的http请求、也就是缺失SSL
            maven.setAllowInsecureProtocol(true)
            maven.url = checkSnapshot(project.version.toString()) ? snapshotsRepoUrl : releasesRepoUrl
            maven.credentials {
                it.username = project.property("mvn.username")
                it.password = project.property("mvn.password")
            }
        }
    }

    static boolean checkSnapshot(String version) {
        int index = version.lastIndexOf('-');
        if (index > -1) {
            def snapshot = version.substring(index + 1)
            def rc = Pattern.compile("RC[0-9]*")
            def m = Pattern.compile("M[0-9]*")
            return "SNAPSHOT".equalsIgnoreCase(snapshot) ||
                    rc.matcher(snapshot) ||
                    m.matcher(snapshot)
        }
        return false
    }
}
