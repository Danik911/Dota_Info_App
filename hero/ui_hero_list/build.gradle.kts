apply{
    from("$rootDir/android_library_build.gradle")
}
dependencies{

    //modules
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.heroUseCases))

    "implementation"(SqlDelight.androidDriver)

}