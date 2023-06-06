/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.boot

import com.livk.boot.dependency.CompileProcessorPlugin
import com.livk.boot.dependency.ManagementPlugin
import com.livk.boot.dependency.OptionalPlugin
import com.livk.boot.info.ManifestPlugin
import com.livk.boot.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * CorePlugin
 * </p>
 *
 * @author livk
 */
class CorePlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.pluginManager.apply(DeleteExpand.class)
		project.pluginManager.apply(ManagementPlugin.class)
		project.pluginManager.apply(OptionalPlugin.class)
		project.pluginManager.apply(CompileProcessorPlugin.class)
		project.pluginManager.apply(ManifestPlugin.class)
	}
}
