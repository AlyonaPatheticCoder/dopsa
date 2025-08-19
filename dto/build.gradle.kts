plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":entity"))
    implementation(project(":validation"))
    implementation("org.springframework.boot:spring-boot-starter-validation")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}