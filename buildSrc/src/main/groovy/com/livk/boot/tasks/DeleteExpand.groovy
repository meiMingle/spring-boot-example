package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * <p>
 * DeleteExpand
 * </p>
 *
 * @author livk
 *
 */
abstract class DeleteExpand implements Plugin<Project> {

    public static final String CLEAN_ALL_TASK_NAME = "cleanAll"

    @Override
    void apply(Project project) {
        project.tasks.withType(Delete.class).configureEach {
            it.delete(project.projectDir.absolutePath + "/build")
            it.delete(project.projectDir.absolutePath + "/out")
            it.delete(project.projectDir.absolutePath + "/bin")
        }

        project.tasks.register(CLEAN_ALL_TASK_NAME, Delete.class) {
            setGroup(LifecycleBasePlugin.BUILD_GROUP)
            it.delete(project.projectDir.absolutePath + "/src/main/generated")
            it.delete(project.projectDir.absolutePath + "/src/test/generated_tests")
        }

    }
}
