plugins {
    id("java")
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:4.0.0")

    runtimeOnly("com.h2database:h2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    testImplementation("org.springframework:spring-test:6.2.14")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}