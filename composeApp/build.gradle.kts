import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
//    js { // TODO we should re-target this once I've sorted out the drawbox package
//        browser()
//        binaries.executable()
//    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.10.1")
            implementation("org.jetbrains.compose.foundation:foundation:1.10.1")
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation("org.jetbrains.compose.ui:ui:1.10.1")
            implementation("org.jetbrains.compose.components:components-resources:1.10.1")
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.1")
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc13")
            implementation("io.github.markyav.drawbox:drawbox:1.4.1")
            implementation("io.github.vinceglb:filekit-core:0.12.0")
            implementation("io.github.vinceglb:filekit-dialogs:0.12.0")
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.12.0")
//            implementation("com.dshatz.pdfmp:pdfmp-compose:1.0.4")
//            implementation("androidx.pdf:pdf-viewer-fragment:1.0.0-alpha12")
//            implementation("androidx.compose.material:material-icons-extended")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


