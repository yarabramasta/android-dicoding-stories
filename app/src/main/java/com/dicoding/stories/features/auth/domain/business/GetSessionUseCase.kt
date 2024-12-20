package com.dicoding.stories.features.auth.domain.business

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.shared.domain.business.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val sessionManager: SessionManager,
) : FlowUseCase<Unit, Session?>(
  dispatcher = dispatcher
) {
  override fun execute(params: Unit): Flow<Result<Session?>> {
    return flow {
      delay(1000L)
      sessionManager.load()
      sessionManager.state.collect {
        emit(Result.success(it))
      }
    }
  }
}
