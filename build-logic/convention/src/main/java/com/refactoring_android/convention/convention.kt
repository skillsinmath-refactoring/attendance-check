package com.refactoring_android.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType


val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")


fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}
fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}

