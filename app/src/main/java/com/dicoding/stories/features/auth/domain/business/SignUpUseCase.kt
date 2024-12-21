package com.dicoding.stories.features.auth.domain.business

import androidx.compose.runtime.Immutable
import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.dicoding.stories.shared.domain.business.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val authRepository: AuthRepository,
) : UseCase<SignUpUseCase.Params, Boolean>(dispatcher) {

  override suspend fun execute(
    params: Params,
  ): Result<Boolean> = authRepository.register(
    name = params.name,
    email = params.email,
    password = params.password
  )

  @Immutable
  data class Params(
    val name: String,
    val email: String,
    val password: String,
  )
}
