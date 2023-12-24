import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("16"))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

dependencies {
    implementation("tools.aqua:z3-turnkey:4.12.2.1")
//    implementation("com.microsoft.z3:4.11.2")
}
