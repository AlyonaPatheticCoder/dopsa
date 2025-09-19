plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":entity"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}