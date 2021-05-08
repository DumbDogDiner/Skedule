import kr.entree.spigradle.kotlin.paper
import kr.entree.spigradle.kotlin.papermc

plugins {
	kotlin("jvm") version "1.4.32"
	id("com.diffplug.spotless") version "5.8.2"
	id("kr.entree.spigradle") version "2.2.3"
	id("org.jetbrains.dokka") version "1.4.32"
	id("maven-publish")
}

group = "com.dumbdogdiner.skedule"
version = "1.3.0"

repositories {
	jcenter()
	mavenCentral()
	papermc()
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")
	// papermc
	compileOnly(paper())
	// testing deps
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	// kotlin test framework
	testImplementation(kotlin("test"))
	testImplementation(paper())
	// mockito
	testImplementation("org.mockito:mockito-core:3.9.0")
}

spotless {
	kotlin {
		target(fileTree(".") {
			include("**/*.kt")
			exclude("**/.gradle/**")
		})
		// see https://github.com/shyiko/ktlint#standard-rules
		ktlint().userData(mapOf("max_line_length" to "120", "insert_final_newline" to "true"))
		licenseHeaderFile("${rootDir}/LICENSE_HEADER")
	}
}

val sourcesJar by tasks.creating(Jar::class) {
	archiveClassifier.set("sources")
	from(sourceSets.getByName("main").allSource)
}

val dokkaJavadocJar by tasks.creating(Jar::class) {
	dependsOn(tasks.dokkaJavadoc)
	from(tasks.dokkaJavadoc.get().outputDirectory.get())
	archiveClassifier.set("javadoc")
}

// define the publication for github packages
publishing {
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/DumbDogDiner/SkGame")
			credentials {
				username = extra.properties.getOrDefault("gpr.user", System.getenv("GITHUB_ACTOR")).toString()
				password =  extra.properties.getOrDefault("gpr.user", System.getenv("GITHUB_ACTOR")).toString()
			}
		}
	}

	publications {
		register<MavenPublication>("gpr") {
			artifactId = "${rootProject.name}-${project.name}"

			from(components["java"])
			// include the sources, and javadoc in the publication
			artifact(sourcesJar)
			artifact(dokkaJavadocJar)

			// configure pom for the output
			pom {
				scm {
					connection.set("scm:git:https://github.com/DumbDogDiner/SkGame.git")
					developerConnection.set("scm:git:https://github.com/DumbDogDiner/SkGame.git")
					url.set("https://github.com/DumbDogDiner/SkGame")
				}
				issueManagement {
					system.set("GitHub Issues")
					url.set("https://github.com/DumbDogDiner/SkGame/issues")
				}
				ciManagement {
					system.set("GitHub Actions")
					url.set("https://github.com/DumbDogDiner/SkGame/actions")
				}
			}
		}
	}
}


tasks {
	compileJava {
		targetCompatibility = JavaVersion.VERSION_14.toString()
		sourceCompatibility = JavaVersion.VERSION_14.toString()
	}

	compileKotlin {
		kotlinOptions.jvmTarget = "14"
		kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
	}

	test {
		useJUnitPlatform()
	}

	generateSpigotDescription {
		enabled = false
	}
}
