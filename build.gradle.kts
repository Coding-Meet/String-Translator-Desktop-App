import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
   kotlin("jvm") version "1.9.0"
   id("org.jetbrains.compose") version "1.5.0"
}


dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    // Add the org.json library for handling JSON data
    implementation("org.json:json:20210307")

}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            macOS {
                iconFile.set(project.file("translate.icns"))
            }
            windows {
                iconFile.set(project.file("translate.ico"))
            }
            linux {
                iconFile.set(project.file("translate.png"))
            }
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.coding.meet.translate"
            description = "String Translator Desktop App"
            vendor = "Meet"
            version = "1.0.0"
            licenseFile.set(project.file("LICENSE"))
            outputBaseDir.set(project.rootDir.resolve("customOutputDir"))
        }
    }
}