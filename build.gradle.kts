import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.attributes.Attribute

// NOTE: Runtime must use JDK 11+ (firebase-admin 9.3.0). If you see UnsupportedClassVersionError you are launching with Java 8.

plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
}

group = "br.com.davijafcm"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	implementation("com.google.firebase:firebase-admin:9.6.0")
	implementation("com.google.guava:guava:33.1.0-jre") // force jre variant
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
// Replace previous TargetJvmEnvironment usage with manual attribute + resolution strategy
val jvmEnvAttr = Attribute.of("org.gradle.jvm.environment", String::class.java)
configurations.all {
	attributes.attribute(jvmEnvAttr, "standard-jvm")
	resolutionStrategy.eachDependency {
		if (requested.group == "com.google.guava" && requested.name == "guava") {
			useVersion("33.1.0-jre")
			because("Force jre variant to avoid android/jre ambiguity")
		}
	}
}
