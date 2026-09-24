plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
}

val publishDir: String = (findProperty("publishDir") as String?)
    ?: "C:/Users/Zutha/Documents/CyrusWebsitePublish/Cyrus-Website"

val assembleSite by tasks.registering(Sync::class) {
    group = "publishing"
    dependsOn(":webApp:wasmJsBrowserDistribution", ":mobileWebApp:wasmJsBrowserDistribution")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    val dist = "build/dist/wasmJs/productionExecutable"
    from(project(":webApp").layout.projectDirectory.dir(dist)) { exclude("index.html") }
    from(project(":mobileWebApp").layout.projectDirectory.dir(dist))  { exclude("index.html") }
    from(layout.projectDirectory.dir("site"))

    into(publishDir)
    preserve { include(".git/**") }
}