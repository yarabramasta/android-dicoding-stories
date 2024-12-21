package com.dicoding.stories.shared.domain.business

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in Params, Success>(
  private val dispatcher: CoroutineDispatcher,
) {

  operator fun invoke(
    params: Params,
  ): Flow<Result<Success>> = execute(params)
    .catch { ex ->
      Log.e(
        "FlowUseCase",
        "An error occurred while executing flow use case.",
        ex
      )
      emit(Result.failure(ex))
    }
    .flowOn(dispatcher)

  protected abstract fun execute(params: Params): Flow<Result<Success>>
}
