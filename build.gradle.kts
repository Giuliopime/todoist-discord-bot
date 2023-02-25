val ktorVersion: String = "2.2.3"
val kmongoVersion: String = "4.8.0"
val kotlinVersion: String = "1.8.0"

plugins {
    application
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("io.ktor.plugin") version "2.2.3"
}

group = "dev.giuliopime"
version = "1.0.0"
application {
    mainClass.set("dev.giuliopime.LauncherKt")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.4")
    implementation("com.github.minndevelopment:jda-ktx:17eb77a1")

    implementation("io.github.cdimascio:dotenv-kotlin:6.3.1")

    // implementation("redis.clients:jedis:4.3.1")

    implementation("org.litote.kmongo:kmongo-serialization:$kmongoVersion")

    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-server-resources:$ktorVersion")
    // implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:2.1.3")
    implementation("io.ktor:ktor-server-core-jvm:2.1.3")

    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

ktor {
    fatJar {
        archiveFileName.set("todoist-discord.jar")
    }
}
