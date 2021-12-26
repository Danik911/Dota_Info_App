apply{
    from("$rootDir/android_library_build.gradle")
}
dependencies{
    "implementation"(project(Modules.heroUseCases))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.components))


    "implementation"(Coil.coil)

    "androidTestImplementation"(project(Modules.heroDataSourceTest))
    "androidTestImplementation"(ComposeTest.uiTestJunit4)
    "debugImplementation"(ComposeTest.uiTestManifest)
    "androidTestImplementation"(Junit.junit4)
}