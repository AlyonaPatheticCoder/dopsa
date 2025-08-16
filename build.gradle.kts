plugins {
    id("java")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

//tasks.test {
//    useJUnitPlatform()
//    doFirst {
//        jvmArgs = [
//            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
//            "--add-opens", "java.base/java.util=ALL-UNNAMED",
//            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED"
//        ]
//    }
//}

tasks.withType<Test>().all {
    useJUnitPlatform()
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }


    java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
        testImplementation("org.mockito:mockito-core:5.12.0")
//        testImplementation("org.mockito:mockito-inline:5.2.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    }

    tasks.withType<Test>().all {
        useJUnitPlatform()
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
    }

}
