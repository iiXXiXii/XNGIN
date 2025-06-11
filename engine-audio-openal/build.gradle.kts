plugins {
    id("java")
}

group = "org.xngin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":engine-audio-api"))

    // LWJGL OpenAL (already included in platform-lwjgl but needed here for direct access)
    val lwjglVersion = "3.3.3"
    val lwjglNatives = "natives-linux"

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl", "lwjgl-openal")
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
