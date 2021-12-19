apply{
    from("$rootDir/android_library_build.gradle")
}
dependencies{
    "implementation"(project(Modules.heroUseCases))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))


    "implementation"(Coil.coil)
}