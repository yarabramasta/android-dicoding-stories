package com.dicoding.stories.helper

import com.dicoding.stories.features.auth.data.remote.AuthService
import com.dicoding.stories.features.auth.data.remote.LoginBody
import com.dicoding.stories.features.auth.domain.business.SignInUseCase
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class AuthResource(private val testResource: TestResource) {

  private var authRepository: AuthRepository? = null
  private var signInUseCase: SignInUseCase? = null

  private fun provideRepository(): AuthRepository {
    return authRepository ?: AuthRepositoryTestResource(
      testResource.provideRetrofit().create(AuthService::class.java)
    )
  }

  fun provideSignInUseCase(dispatcher: CoroutineDispatcher): SignInUseCase {
    return signInUseCase ?: SignInUseCase(
      dispatcher,
      provideRepository()
    )
  }

  internal class AuthRepositoryTestResource(
    private val service: AuthService,
  ) : AuthRepository {
    override fun getSession(): Flow<Result<Session?>> {
      throw NotImplementedError()
    }

    override suspend fun login(
      email: String,
      password: String,
    ): Result<Session> {
      var result: Result<Session> = Result.failure(Throwable())

      service.login(LoginBody(email, password))
        .onSuccess {
          val session = Session(
            id = data.loginResult.userId,
            name = data.loginResult.name,
            token = data.loginResult.token
          )
          result = Result.success(session)
        }

      return result
    }

    override suspend fun register(
      name: String,
      email: String,
      password: String,
    ): Result<Boolean> {
      throw NotImplementedError()
    }

    override suspend fun logout(): Result<Boolean> {
      throw NotImplementedError()
    }
  }
}
