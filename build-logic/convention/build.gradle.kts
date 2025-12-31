plugins {
    `kotlin-dsl`
   /* id("java-library")
    id("java-gradle-plugin")
    alias(libs.plugins.jetbrains.kotlin.jvm)*/
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle.tool)
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


gradlePlugin {
    plugins {
        register("AndroidApplicationHiltPlugin") {
            id = "com.refactoring.plugin.hilt"
            implementationClass = "com.refactoring_android.AndroidApplicationHiltConventionPlugin"
        }
    }
    plugins {

    }
}
