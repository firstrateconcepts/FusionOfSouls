import org.gradle.internal.os.OperatingSystem.*

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.beryx.runtime") version "1.12.6"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
}

application {
    mainClass.set("net.firstrateconcepts.fusionofsouls.FosApplication")
}

tasks.wrapper {
    gradleVersion = "7.2"
}

runtime {
    options.set(listOf("--compress", "2", "--no-header-files", "--no-man-pages", "--strip-debug"))

    launcher {
        noConsole = true
    }

    jpackage {
        val currentOs = current()

        installerOptions.addAll(listOf(
            "--resource-dir", "src/main/resources",
            "--vendor", "First Rate Concepts"
        ))

        imageOutputDir = file("$buildDir/package/image")
        installerOutputDir = file("$buildDir/package/install")

        imageName = "Fusion of Souls"
        installerName = "Fusion of Souls"

        if (currentOs.isWindows) {
            installerOptions.addAll(listOf(
                "--win-dir-chooser",
                "--win-menu",
                "--win-upgrade-uuid", "786a1694-b2cd-4fc5-a823-d5625177903c"
            ))
            installerType = "msi"
        } else if (currentOs.isLinux) {
            installerOptions.addAll(listOf("--linux-package-name", "fusion-of-souls", "--linux-shortcut"))
        } else if (currentOs.isMacOsX) {
            installerOptions.addAll(listOf(
                "--mac-package-name", "Fusion of Souls",
                "--mac-package-identifier", "fusion-of-souls"
            ))
            appVersion = (version as String).replace("0.", "1.")
        }
    }
}
