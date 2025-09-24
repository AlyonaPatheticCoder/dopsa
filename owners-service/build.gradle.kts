plugins {
    id("org.springframework.boot") version "3.2.5"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:postgresql")
    implementation(project(":entity"))
    implementation(project(":entityDto"))
    implementation(project(":amqp"))
    implementation(project(":validation"))
}

tasks.test {
    useJUnitPlatform()
}