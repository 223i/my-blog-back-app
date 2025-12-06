plugins {
    id("java")
    id("war")
}

group = "com.iron"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:6.2.14")
    implementation("org.springframework:spring-web:6.2.14")
    implementation("org.springframework.data:spring-data-jdbc:4.0.0")
    implementation("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.30")


    implementation("tools.jackson.core:jackson-databind:3.0.3")
    implementation("tools.jackson.core:jackson-core:3.0.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")

    runtimeOnly("com.h2database:h2:2.4.240")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("org.springframework:spring-test:6.2.14")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName = "my-blog-back-app.war"
}
