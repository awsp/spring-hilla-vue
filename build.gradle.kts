plugins {
  java
  id("org.springframework.boot") version "3.3.5"
  id("io.spring.dependency-management") version "1.1.6"
  id("com.vaadin") version "24.5.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

extra["vaadinVersion"] = "24.5.0"

dependencies {
  implementation("com.vaadin:vaadin-spring-boot-starter")
  implementation("org.projectlombok:lombok")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
  implementation("com.webauthn4j:webauthn4j-spring-security-core:0.10.0.RELEASE")
  implementation("com.surrealdb:surrealdb:0.2.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
  imports {
    mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
