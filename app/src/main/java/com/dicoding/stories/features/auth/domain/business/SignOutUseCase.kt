package com.dicoding.stories.features.auth.domain.business

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.dicoding.stories.shared.domain.business.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val authRepository: AuthRepository,
) : UseCase<Unit, Boolean>(dispatcher) {
  override suspend fun execute(params: Unit): Result<Boolean> {
    return authRepository.logout()
  }
}
