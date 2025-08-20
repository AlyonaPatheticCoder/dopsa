plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management")
    id("application")
}

application {
    mainClass.set("com.laba.Main")
}

dependencies {
    implementation(project(":controller"))
    implementation(project(":service"))
    implementation(project(":dao"))
    implementation(project(":entity"))
    implementation(project(":dto"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}