import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

version = "0.0.7"

kotlin {
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "even_odd_kmp"
//        binaries.executable()
//        browser {
//            webpackTask {
//                output.libraryTarget = "umd"
//            }
//        }
//    }

    js {
        moduleName = "even_odd_kmp"
        binaries.executable()
        browser {
            webpackTask {
                output.libraryTarget = "umd"
            }
            runTask {
                output.libraryTarget = "umd"
            }
        }
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "EvenOddKmp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

android {
    namespace = "dev.sunnat629.evenoddkmp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

tasks.register("npmPublishTask") {
    group = "publishing"
    description = "Assemble the project and publish to npm"

    dependsOn("jsBrowserProductionWebpack")

    doLast {
        val projectDir = rootProject.layout.projectDirectory.asFile
        val buildDirectory = layout.buildDirectory.get().asFile

        val distributionsDir = projectDir.resolve("build/js/packages/even_odd_kmp/")
        val packageJsonFile = distributionsDir.resolve("package.json")
        println("distributionsDir: $distributionsDir")

        val packageJsonContent = """
        {
            "name": "@sunnat629/even_odd_kmp",
            "version": "${project.version}",
            "main": "kotlin/even_odd_kmp.js",
            "dependencies": {
                "format-util": "^1.0.5"
            }
        }
    """.trimIndent()

        packageJsonFile.writeText(packageJsonContent)

        copy {
            from(distributionsDir)
            into(buildDirectory.resolve("npm-publish"))
            include("kotlin/even_odd_kmp.js")
            include("kotlin/even_odd_kmp.js.map")
            include("package.json")
        }

        exec {
            workingDir = buildDirectory.resolve("npm-publish")
            commandLine = listOf("npm", "publish", "--access", "public")
        }
    }
}
