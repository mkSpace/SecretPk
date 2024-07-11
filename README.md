# SecretPk

## Overview
SecretPk is a sample project designed to automate the encryption and decryption of primary keys (PK) exposed externally, without affecting the business logic. 
It ensures that developers do not need to concern themselves with PK-related logic within their code.

## Features
- **Automatic PK Encryption and Decryption**: Encrypts PKs exposed via PathVariables or QueryParameters and decrypts incoming encrypted PKs.
- **Annotation-Based Encryption**: Uses the `@SecretPK` annotation to mark fields in controller responses for automatic encryption.
- **Seamless Integration**: Ensures that PK-related logic is handled automatically, allowing developers to focus on business logic.

## Implementation Details

### Decryption of Incoming PKs
Incoming PKs, typically passed as PathVariables or QueryParameters, are automatically decrypted using a custom `HandlerMethodArgumentResolver`. 
This resolver detects PK parameters annotated with `@SecretPk` and decrypts them before passing them to the business logic.

```kotlin
// Example of decryption in a controller method in query parameter case
@GetMapping("/get")
fun get1(@SecretPk id: Long): Response<Any> {
  return Response.success(data = sampleService.getById(id))
}
```
or
```kotlin
// Example of decryption in a controller method in path variable case
@GetMapping("/get/{id}")
fun get2(@SecretPkPathVariable("id") id: Long): Response<Any> {
    return Response.success(data = sampleService.getById(id))
}
```

### Encryption of Response Fields
For controller methods that return objects with fields annotated with @SecretPK, the library uses reflection and ByteBuddy to create a new instance of the response object at runtime. 
This new instance contains the encrypted values for the annotated fields.

```kotlin
data class SampleResponse(
    @field:SecretPk val id: Long,
    val email: String,
    val nickname: String
)
```

When this response is sent back to the client, the id field will be automatically encrypted.

This allows the Response DTO or variables received as Path Variables and Query Parameters to be handled in their original PK type.

## Usage
Add the necessary dependencies for Spring AOP and ByteBuddy.

For Maven: 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy</artifactId>
    <version>1.10.20</version>
</dependency>
```

For Gradle:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-aop'
implementation 'net.bytebuddy:byte-buddy:1.10.20'
```

## Contributing
We welcome contributions! Please fork the repository and submit a pull request.


