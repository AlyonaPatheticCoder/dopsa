plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.4"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.5")
        }
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
        testImplementation("org.mockito:mockito-core:5.12.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
        implementation("me.paulschwarz:spring-dotenv:4.0.0")
    }

    tasks.withType<Test>().all {
        useJUnitPlatform()
    }
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }
}


