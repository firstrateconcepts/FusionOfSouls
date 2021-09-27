import org.gradle.internal.os.OperatingSystem.*

val gdxVersion: String by project
val gdxAiVersion: String by project
val ktxVersion: String by project

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("org.beryx.runtime") version "1.12.6"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://artifactory.nimblygames.com/artifactory/ng-public-release/")
}

fun DependencyHandlerScope.implementationKotlin(vararg names: String) = names.forEach { implementation(kotlin(it)) }
fun DependencyHandlerScope.implementationGdx(vararg names: String, classifier: String = "") = names.forEach { implementation(group = "com.badlogicgames.gdx", name = it, version = gdxVersion, classifier = classifier) }
fun DependencyHandlerScope.implementationGdxNative(vararg names: String) = implementationGdx(classifier = "natives-desktop", names = names)
fun DependencyHandlerScope.implementationKtx(vararg names: String) = names.forEach { implementation(group = "io.github.libktx", name = "ktx-$it", version = ktxVersion) }

dependencies {
    implementationKotlin("stdlib", "reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    implementationGdx("gdx", "gdx-freetype", "gdx-backend-lwjgl3")
    implementationGdxNative("gdx-platform", "gdx-freetype-platform")
    implementation("com.badlogicgames.gdx:gdx-ai:$gdxAiVersion")

    implementationKtx(
        "app", "actors", "ashley", "assets", "async", "collections", "freetype", "graphics", "inject", "json", "log", "math", "preferences", "reflect", "vis", "vis-style"
    )
}

application {
    mainClass.set("net.firstrateconcepts.fusionofsouls.FusionOfSoulsLauncher")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=ktx.reflect.Reflection,kotlinx.serialization.ExperimentalSerializationApi"
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
