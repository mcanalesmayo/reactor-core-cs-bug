plugins {
    java
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.5.4")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
    targetCompatibility = JavaVersion.toVersion("11")
}
