import kr.entree.spigradle.kotlin.paper
import kr.entree.spigradle.kotlin.papermc

plugins {
	kotlin("jvm") version "1.4.32"
	id("com.diffplug.spotless") version "5.8.2"
	id("kr.entree.spigradle") version "2.2.3"
	id("org.jetbrains.dokka") version "1.4.32"
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
	ratchetFrom = "origin/master"
	kotlin {
		ktlint()
		licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
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
