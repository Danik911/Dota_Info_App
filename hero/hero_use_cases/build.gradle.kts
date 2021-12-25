apply{
    from("$rootDir/library_build.gradle")
}
dependencies{
    "implementation" (project(Modules.core))
    "implementation" (project(Modules.heroDataSource))
    "implementation" (project(Modules.heroDomain))


    "implementation" (Kotlinx.coroutinesCore)

    "testImplementation" (project(Modules.heroDataSourceTest))
    "testImplementation" (Junit.junit4)
    "testImplementation" (Ktor.ktorClientMock)
    "testImplementation" (Ktor.clientSerialization)
}