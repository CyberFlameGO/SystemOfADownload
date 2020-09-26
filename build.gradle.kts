
plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application.
    application
    id("net.minecrell.licenser") version "0.4.1"
    checkstyle
}

repositories {
    jcenter()
    maven(url = "https://repo.eclipse.org/content/groups/releases/")
}

dependencies {
    implementation("com.google.guava:guava:28.0-jre")
    implementation("com.graphql-java:graphql-java:15.0")
    implementation("com.sparkjava:spark-core:2.8.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.spongepowered:configurate-gson:3.7.1") {
        exclude(group = "com.google.inject", module = "guice")
    }

    // Update this to 4.2.4 or 4.3, whatever comes next once we can.
    // Currently getting "illegal reflective access operation" - this
    // is due to J11 telling us it's about to be stricter.
    //
    // (i.e., waiting for this to be pulled into Guice:
    // https://github.com/google/guice/pull/1298)
    implementation("com.google.inject:guice:4.2.3")
    implementation("org.slf4j:slf4j-simple:1.7.9")

    // JGit
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.7.0.202003110725-r")

    // Database handling
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.postgresql:postgresql:42.2.14")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:3.3.3")
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
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