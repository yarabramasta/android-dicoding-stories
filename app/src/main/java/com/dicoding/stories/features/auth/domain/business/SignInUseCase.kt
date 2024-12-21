package com.dicoding.stories.features.auth.domain.business

import androidx.compose.runtime.Immutable
import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.dicoding.stories.shared.domain.business.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class SignInUseCase @Inject constructor(
  @IoDispatcher dispatcher: CoroutineDispatcher,
  private val authRepository: AuthRepository,
) : UseCase<SignInUseCase.Params, Session>(dispatcher) {

  override suspend fun execute(
    params: Params,
  ): Result<Session> = authRepository.login(params.email, params.password)

  @Immutable
  class Params(
    val email: String,
    val password: String,
  )
}
