plugins {
    kotlin("multiplatform") version "2.1.0"
    `maven-publish`
}

group = "com.mylosoftworks"
version = "1.0"

repositories {
    mavenCentral()
}

publishing {
    publications {
        // KMP automatically registers the publications. This library is intended to be distributed through Jitpack (https://www.jitpack.io/)
    }
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }

    sourceSets {
        jvmTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}