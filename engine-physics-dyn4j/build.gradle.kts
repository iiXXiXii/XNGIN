plugins {
    id("java")
}

group = "org.xngin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":engine-physics-api"))
    implementation(project(":engine-math"))

    // Dyn4j physics engine
    implementation("org.dyn4j:dyn4j:5.0.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
