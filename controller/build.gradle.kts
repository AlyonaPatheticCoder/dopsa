plugins {
    application
    java
}
application {
    mainClass.set("com.Alena.Main")
}

dependencies {
    implementation(project(":service"))
    implementation(project(":entity"))
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}