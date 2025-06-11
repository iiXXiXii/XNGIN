plugins {
    id("java")
}

group = "org.xngin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
