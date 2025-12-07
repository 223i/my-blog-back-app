plugins {
    id("java")
    id("war")
}

group = "com.iron"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {

    implementation("org.springframework:spring-webmvc:6.2.10")

    providedCompile("jakarta.servlet:jakarta.servlet-api:5.0.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("jakarta.el:jakarta.el-api:5.0.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")

    implementation("com.h2database:h2:2.2.224")

    implementation("org.springframework.data:spring-data-jdbc:3.4.1")


    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")


    testImplementation("org.springframework:spring-test:6.2.14")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("my-blog-back-app.war")
}