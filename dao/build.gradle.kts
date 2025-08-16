plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":entity"))
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
}