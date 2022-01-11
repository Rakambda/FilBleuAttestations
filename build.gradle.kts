plugins {
    idea
    java
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.names)
    alias(libs.plugins.lombok)
    alias(libs.plugins.jib)
}

group = "fr.raksrinana"
description = "FilBleuAttestations"

dependencies {
    implementation(libs.slf4j)
    implementation(libs.bundles.log4j2)

    implementation(libs.jakartaMail)

    implementation(libs.picocli)
    implementation(libs.bundles.jackson)

    implementation(libs.selenide)

    compileOnly(libs.jetbrainsAnnotations)
}

repositories {
    mavenCentral()
}

tasks {
    processResources {
        expand(project.properties)
    }

    compileJava {
        val moduleName: String by project
        inputs.property("moduleName", moduleName)

        options.encoding = "UTF-8"
        options.isDeprecation = true
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("shaded")
        archiveVersion.set("")
    }

    wrapper {
        val wrapperVersion: String by project
        gradleVersion = wrapperVersion
    }
}

application {
    val moduleName: String by project
    val className: String by project

    mainModule.set(moduleName)
    mainClass.set(className)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

jib {
    from {
        image = "eclipse-temurin:17-jdk"
        platforms {
            platform {
                os = "linux"
                architecture = "arm64"
            }
            platform {
                os = "linux"
                architecture = "amd64"
            }
        }
    }
    to {
        image = "mrcraftcod/filbleu-attestations"
        auth {
            username = project.findProperty("dockerUsername").toString()
            password = project.findProperty("dockerPassword").toString()
        }
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}
