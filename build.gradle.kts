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
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("jakarta.el:jakarta.el-api:5.0.0")
    implementation("org.springframework:spring-tx")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")

    implementation("com.h2database:h2:2.2.224")
    implementation("org.springframework.data:spring-data-jdbc:3.4.1")


    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")


    implementation("org.springframework:spring-context:6.2.7")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")

    testImplementation("org.springframework:spring-test:6.2.14")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("ROOT.war")
}