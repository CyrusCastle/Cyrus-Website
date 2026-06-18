import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.browser)
            implementation(libs.kotlinx.datetime)

            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            implementation(libs.richeditor.compose)
            implementation(libs.drawbox.enhanced)

            implementation("dev.nucleusframework:pdfium:local")
//            implementation("org.jetbrains.kotlin:kotlin-reflect")

//            implementation("com.dshatz.pdfmp:pdfmp-compose:1.0.4")
//            implementation("androidx.pdf:pdf-viewer-fragment:1.0.0-alpha12")
//            implementation("androidx.compose.material:material-icons-extended")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


