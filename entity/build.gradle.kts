plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("jakarta.persistence:jakarta.persistence-api")
    //implementation(project(":dto"))
//    compileOnly("org.projectlombok:lombok")
//    annotationProcessor("org.projectlombok:lombok")
}