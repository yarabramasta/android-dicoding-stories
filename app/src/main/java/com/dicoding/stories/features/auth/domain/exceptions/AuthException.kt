package com.dicoding.stories.features.auth.domain.exceptions

sealed class AuthException : Throwable() {
  
  data class BadRequestSignIn(
    override val message: String = "BadRequestSignIn",
  ) : AuthException()

  data class BadRequestSignUp(
    override val message: String = "BadRequestSignUp",
  ) : AuthException()

  data class DuplicatedCredentials(
    override val message: String = "DuplicatedCredentials",
  ) : AuthException()

  data class InvalidCredentials(
    override val message: String = "InvalidCredentials",
  ) : AuthException()
}
