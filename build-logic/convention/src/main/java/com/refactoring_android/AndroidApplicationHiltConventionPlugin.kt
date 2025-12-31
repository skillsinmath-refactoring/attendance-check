package com.refactoring_android

import com.refactoring_android.convention.implementation
import com.refactoring_android.convention.ksp
import com.refactoring_android.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidApplicationHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
            }
            plugins.apply("com.google.devtools.ksp")
            dependencies {
                implementation(libs.findLibrary("hilt-android").get())   //add("implementation", target.libs.findLibrary("hilt-android").get())
                ksp(libs.findLibrary("hilt-android-compiler").get())
            }
        }
    }
}
