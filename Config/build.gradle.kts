plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":service"))
    implementation(project(":entity"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-crypto")
    testImplementation("org.springframework.security:spring-security-test")
}