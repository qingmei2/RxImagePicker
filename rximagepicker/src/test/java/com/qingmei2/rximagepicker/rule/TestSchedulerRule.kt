package com.qingmei2.rximagepicker.rule

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

class TestSchedulerRule : TestRule {

    private var testScheduler: TestScheduler = TestScheduler()

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { testScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
                RxJavaPlugins.setSingleSchedulerHandler { testScheduler }
                try {
                    base?.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                }
            }
        }
    }

    fun advanceTimeTo(delayTime: Long, timeUnit: TimeUnit) {
        testScheduler.advanceTimeTo(delayTime, timeUnit)
    }

    fun advanceTimeBy(delayTime: Long, timeUnit: TimeUnit) {
        testScheduler.advanceTimeBy(delayTime, timeUnit)
    }

    fun triggerActions() {
        testScheduler.triggerActions()
    }

    fun getScheduler(): TestScheduler {
        return testScheduler
    }
}
