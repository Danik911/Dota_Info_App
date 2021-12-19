apply{
    from("$rootDir/library_build.gradle")
}
dependencies{
    "implementation"(project(Modules.core))

}