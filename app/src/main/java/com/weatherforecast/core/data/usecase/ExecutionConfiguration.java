package com.weatherforecast.core.data.usecase;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

public class ExecutionConfiguration {

    private final Scheduler executionScheduler;
    private final Scheduler postExecutionScheduler;

    public ExecutionConfiguration(@NonNull final Scheduler executionScheduler,
                                  @NonNull final Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    public Scheduler execution() {
        return executionScheduler;
    }

    public Scheduler postExecution() {
        return postExecutionScheduler;
    }

}
