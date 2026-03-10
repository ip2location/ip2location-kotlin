plugins {
    alias(libs.plugins.kotlin.jvm)
    id("com.vanniktech.maven.publish") version "0.36.0"
    id("org.jetbrains.dokka") version "2.1.0"
    `java-library`
    id("signing")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.apache.commons:commons-csv:1.14.1")
    api("com.google.code.gson:gson:2.13.2")
    implementation("org.apache.commons:commons-lang3:3.20.0")
}

mavenPublishing {
    configureBasedOnAppliedPlugins()
    coordinates("com.ip2location", "ip2location-kotlin", "8.6.0")
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("IP2Location Kotlin")
        description.set("IP2Location Kotlin Library enables applications to get info from IP address such as the visitor’s country, region, city, latitude, longitude, ZIP code, ISP name, domain name, time zone, connection speed, IDD code, area code, weather station code, weather station name, MCC, MNC, mobile brand name, elevation, usage type, address type, IAB category, district, autonomous system number (ASN), autonomous system (AS), AS domain, AS usage type and AS CIDR.")
        inceptionYear.set("2026")
        url.set("https://github.com/ip2location/ip2location-kotlin")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("ip2location")
                name.set("IP2Location")
                email.set("support@ip2location.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/ip2location/ip2location-kotlin.git")
            developerConnection.set("scm:git:ssh://github.com/ip2location/ip2location-kotlin.git")
            url.set("https://github.com/ip2location/ip2location-kotlin")
        }
    }
}

signing {
    // Call the 'gpg' command on Windows 11 so Kleopatra pops up.
    useGpgCmd()

    // This ensures we only sign when we are actually publishing
    val isPublishing = gradle.taskGraph.allTasks.any { it.name.contains("publish", ignoreCase = true) }
    setRequired(isPublishing)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
