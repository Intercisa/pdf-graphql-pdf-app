import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	id ("io.github.lognet.grpc-spring-boot") version "5.1.5"
}

group = "com.epam.chat"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("io.lettuce:lettuce-core:6.3.2.RELEASE")
	implementation("commons-codec:commons-codec:1.17.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation ("io.github.lognet:grpc-spring-boot-starter:5.1.5")
	implementation("io.minio:minio:8.5.9")
	implementation("org.apache.pdfbox:pdfbox:2.0.31")
	implementation("org.apache.pdfbox:pdfbox-tools:2.0.31")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
