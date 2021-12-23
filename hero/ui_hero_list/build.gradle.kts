apply{
    from("$rootDir/android_library_build.gradle")
}
dependencies{

    //modules
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.heroUseCases))
    "implementation"(project(Modules.components))

    "implementation"(SqlDelight.androidDriver)

    "implementation"(Hilt.android)
    "kapt"(Hilt.compiler)

    "implementation"(Coil.coil)


}