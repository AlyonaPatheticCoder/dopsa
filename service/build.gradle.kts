plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":dao"))
    implementation(project(":dto"))
    implementation(project(":entity"))
    implementation(project(":validation"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}