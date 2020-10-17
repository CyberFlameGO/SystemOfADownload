
plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application.
    application
    id("net.minecrell.licenser") version "0.4.1"
    checkstyle
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}
tasks.test {
    useJUnitPlatform()
}
dependencies {
    implementation("com.google.guava:guava:28.0-jre")
    implementation("com.graphql-java:graphql-java:15.0")
    implementation("com.sparkjava:spark-core:2.8.0")
    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.mockito:mockito-core:3.3.3")
}

java {
    targetCompatibility = JavaVersion.VERSION_15
    sourceCompatibility = JavaVersion.VERSION_15
}

application {
    // Define the main class for the application.
    mainClassName = "org.spongepowered.downloads.App"
}

checkstyle {

}
val organization: String by project
val url: String by project

license {
    header = project.file("HEADER.txt")
    ext {
        this["name"] = project.name
        this["organization"] = organization
        this["url"] = url
    }
    newLine = false
}