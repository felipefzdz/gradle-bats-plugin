plugins {
    id("com.gradle.enterprise") version "3.5"
}

rootProject.name = "gradle-bats-plugin"
include("bats")

gradleEnterprise {
    buildScan {
        publishAlways()
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
