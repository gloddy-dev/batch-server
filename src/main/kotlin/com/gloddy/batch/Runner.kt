package com.gloddy.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Runner(
    private val jobLauncher: JobLauncher,
    private val job: Job
) {

    @Bean
    fun runBatchJob(): CommandLineRunner {
        val now = LocalDateTime.now()
        val jobParameters = JobParametersBuilder()
            .addString("txTime", now.toString())
            .toJobParameters()
        return CommandLineRunner {
            jobLauncher.run(job, jobParameters)
        }
    }
}