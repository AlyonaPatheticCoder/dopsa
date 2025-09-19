plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":service"))
    implementation(project(":entity"))
    implementation(project(":dto"))
    implementation(project(":validation"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
}