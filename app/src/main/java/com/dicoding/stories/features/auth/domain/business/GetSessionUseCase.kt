package com.dicoding.stories.features.auth.domain.business

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.dicoding.stories.shared.domain.business.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val authRepository: AuthRepository,
) : FlowUseCase<Unit, Session?>(
  dispatcher = dispatcher
) {

  override fun execute(
    params: Unit,
  ): Flow<Result<Session?>> = authRepository.getSession()
}
