plugins {
	com.livk.common
	com.livk.mvn.deployed
}

description = "spring commons packages"

dependencies {
	optional("org.springframework:spring-webflux")
	optional("org.springframework:spring-tx")
	optional("io.projectreactor.netty:reactor-netty-http")
	optional("org.springframework.boot:spring-boot-starter")
	optional("com.github.pagehelper:pagehelper")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("com.squareup.okhttp3:okhttp")
	optional("com.googlecode.aviator:aviator")
	optional("org.apache.commons:commons-jexl3")
	optional("org.mvel:mvel2")
	optional("org.freemarker:freemarker")
	optional("io.micrometer:micrometer-tracing")
	api("org.apache.commons:commons-lang3")
	api("com.github.spotbugs:spotbugs-annotations")
	api("com.google.guava:guava")
	api("org.springframework:spring-core")
	api("com.fasterxml.jackson.core:jackson-databind")
	api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	compileProcessor(project(":spring-auto-service"))
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
	testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
}
