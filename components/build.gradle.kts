apply{
    from("$rootDir/android_library_build.gradle")
}
dependencies{
    "implementation"(project(Modules.core))
}