## Spring cloud contracts

En el archivo build.gradle agregar el plugin

```gradle
plugins {
    ...
    id 'org.springframework.cloud.contract' version '4.3.0'
    ...
}
```
Agregar en el main.gradle
```gradle
    ...
    contracts {
        if (project.name == 'app-service') {
            packageWithBaseClasses = 'co.com.nequi.config'
            contractsDslDir = project.file("${project.projectDir}/src/test/java/contracts")
        }
        else {
            failOnNoContracts = false
        }
        testMode = 'WebTestClient'
        testFramework = 'JUNIT5'
        basePackageForTests = 'contracts'
    }
    ...
```