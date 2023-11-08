import java.net.URI

tasks.register<JacocoReport>("pixel5Api30DebugAndroidTestWithCoverage") {
    this.group = "coverage"
    this.description = "Generate HTML code coverage reports for pixel5Api30"
    this.dependsOn("pixel5Api30DebugAndroidTest")
    this.mustRunAfter("pixel5Api30DebugAndroidTest")

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
    }

    val fileFilter = arrayOf(
        "**/*_*.*", // Exclude all file containing a `_` (generated code)
        "**/*$*", // Exclude Kotlin Function to Java (generated code)
        /* Common Android Scope */
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        /* Hilt/Dagger Scope */
        "**/*_GeneratedInjector.*",
        "**/*Provides*.*",
        "**/hilt/**/*.*",
        "**/dagger/**/*.*",
        "**/*InstanceHolder.*",
        /* Custom Scope */
        "**/*Application.*", // Exclude Application until there is no override methods
        "**/*Module*", // Exclude Hilt Module
        "**/model/**", // Exclude model (Should be `data class` only)
        "**/api/**", // Exclude Retrofit interface (generated code)
        "**/database/*.*", // Exclude Room Database definition (generated code)
        "**/ui/**" // Exclude UI
    )

    val debugBuildDir = "${buildDir}/intermediates/javac/debug"
    val kotlinClassesDebug = "${buildDir}/tmp/kotlin-classes/debug"
    val coverageDataDir = "outputs/managed_device_code_coverage"

    sourceDirectories.setFrom("${project.projectDir}/src/main/java")
    classDirectories.setFrom(
        files(
            project.files(debugBuildDir, kotlinClassesDebug).map {
                fileTree(it) {
                    exclude(*fileFilter)
                }
            }
        )
    )

    executionData.setFrom(
        files(
            project.files(layout.buildDirectory).map {
                fileTree(it) {
                    include("$coverageDataDir/**/*.ec")
                }
            }
        )
    )

    doLast {
        val report = File(
            project.buildDir,
            "reports/jacoco/pixel5Api30DebugAndroidTestWithCoverage/html/index.html"
        )

        println(
            "Coverage report: ${
                URI(
                    "file://${
                        report.absolutePath.replace(
                            " ",
                            "%20"
                        )
                    }"
                )
            }"
        )
        "^.*<td>Total<([^>]+>){4}([^<]*).*".toRegex().find(report.bufferedReader().use {
            it.readText()
        })?.groupValues?.get(2).also {
            println("Coverage: ${it ?: "0%"}")
        }

    }
}
