import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.0.5"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.7.22"
  kotlin("plugin.spring") version "1.7.22"
}

group = "me.wonsik"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()

  repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  developmentOnly("org.springframework.boot:spring-boot-devtools")

  implementation("io.projectreactor.netty:reactor-netty5:2.0.0-M3")

  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//  runtimeOnly("com.h2database:h2")
//  runtimeOnly("io.r2dbc:r2dbc-h2")

  runtimeOnly("org.postgresql:r2dbc-postgresql")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("io.projectreactor.tools:blockhound:1.0.7.RELEASE")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_13)) {
    jvmArgs?.add("-XX:+AllowRedefinitionToAddDeleteMethods")
  }
}
