plugins {
    id("com.github.johnrengelman.shadow")
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    forge.apply {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

        mixinConfig("singularity-common.mixins.json")
        mixinConfig("singularity.mixins.json")
    }
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentForge: Configuration by configurations.getting

configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentForge.extendsFrom(common)
}

dependencies {
    forge("net.minecraftforge:forge:${rootProject.forge_version}")

    modApi("dev.architectury:architectury-forge:${rootProject.architectury_version}")
    modApi("me.shedaniel.cloth:cloth-config-forge:${rootProject.cloth_version}")

    common(project(":common", "namedElements")) {
        isTransitive = false
    }

    shadowCommon(project(":common", "transformProductionForge")) {
        isTransitive = false
    }
}

processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand(mapOf(
            "version" to project.version,
        ))
    }
}

tasks.shadowJar {
    exclude("fabric.mod.json")

    configurations = listOf(project.configurations.shadowCommon)
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    injectAccessWidener.set(true)
    inputFile.set(tasks.shadowJar.get().archiveFile)
    dependsOn(shadowJar)
    archiveClassifier.set(null as String?)
}

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.sourcesJar {
    val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")
    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
}

components.getByName("java") {
    this.withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenForge") {
            artifactId = rootProject.property("archives_base_name") + "-" + project.name
            from(components["java"])
        }
    }

    repositories {

    }
}