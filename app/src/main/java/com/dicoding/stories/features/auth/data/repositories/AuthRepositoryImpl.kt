package com.dicoding.stories.features.auth.data.repositories

import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.features.auth.data.remote.AuthService
import com.dicoding.stories.features.auth.domain.exceptions.AuthException
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val sessionManager: SessionManager,
  private val authService: AuthService,
) : AuthRepository {

  override fun getSession(): Flow<Result<Session?>> {
    return flow {
      delay(1000L)
      sessionManager.load()
      sessionManager.state.collect {
        emit(Result.success(it))
      }
    }
  }

  override suspend fun login(email: String, password: String): Result<Session> {
    var result: Result<Session> =
      Result.failure(AuthException.BadRequestSignIn())

    authService.login(email, password)
      .suspendOnSuccess {
        if (data.error) {
          result = Result.failure(AuthException.InvalidCredentials())
          return@suspendOnSuccess
        }

        val session = Session(
          id = data.loginResult.userId,
          name = data.loginResult.name,
          token = data.loginResult.token
        )
        sessionManager.save(session)
        result = Result.success(session)
      }
      .suspendOnError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        result = when (statusCode) {
          400 -> Result.failure(AuthException.InvalidCredentials())
          else -> result
        }
      }

    return result
  }

  override suspend fun register(
    name: String,
    email: String,
    password: String,
  ): Result<Boolean> {
    var result: Result<Boolean> =
      Result.failure(AuthException.BadRequestSignUp())

    authService.register(name, email, password)
      .suspendOnSuccess {
        if (data.error) {
          result = Result.failure(AuthException.DuplicatedCredentials())
          return@suspendOnSuccess
        }

        result = Result.success(true)
      }
      .suspendOnError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        result = when (statusCode) {
          400 -> Result.failure(AuthException.DuplicatedCredentials())
          else -> result
        }
      }

    return result
  }
}
