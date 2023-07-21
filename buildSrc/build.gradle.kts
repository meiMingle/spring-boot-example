plugins {
	id("java-gradle-plugin")
	alias(libs.plugins.kotlin.jvm)
}

repositories {
	maven { setUrl("https://repo.spring.io/milestone/") }
	maven { setUrl("https://repo.spring.io/libs-snapshot/") }
	maven { setUrl("https://plugins.gradle.org/m2/") }
	maven { setUrl("https://repo.spring.io/release") }
	maven { setUrl("https://maven.aliyun.com/repository/public") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.spring.boot.get()}")
}

gradlePlugin {
	plugins {
		create("bomPlugin") {
			id = "com.livk.bom"
			implementationClass = "com.livk.boot.BomPlugin"
		}
		create("modulePlugin") {
			id = "com.livk.module"
			implementationClass = "com.livk.boot.ModulePlugin"
		}
		create("commonPlugin") {
			id = "com.livk.common"
			implementationClass = "com.livk.boot.CommonPlugin"
		}
		create("rootProjectPlugin") {
			id = "com.livk.root"
			implementationClass = "com.livk.boot.RootPlugin"
		}
		create("servicePlugin") {
			id = "com.livk.service"
			implementationClass = "com.livk.boot.ServicePlugin"
		}
		create("deployedPlugin") {
			id = "com.livk.mvn.deployed"
			implementationClass = "com.livk.boot.maven.DeployedPlugin"
		}
	}
}

tasks.withType<Jar> {
	manifest.attributes.putIfAbsent(
		"Created-By",
		System.getProperty("java.version") + " (" + System.getProperty("java.specification.vendor") + ")"
	)
	manifest.attributes.putIfAbsent("Gradle-Version", GradleVersion.current())
}
