apply{
    from("$rootDir/library_build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    id(SqlDelight.plugin)
}

dependencies{
    "implementation"(project(Modules.heroDomain))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)

    "implementation"(SqlDelight.runtime)

}

sqldelight{
    database(name = "HeroDatabase"){
        packageName = "com.example.hero_datasource.cache"
        sourceFolders = listOf("sqldelight")
    }
}