package com.gloddy.batch.config

import com.gloddy.batch.model.jpa.UserEvent
import com.gloddy.batch.model.jpa.UserJpaModel
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDateTime

@Configuration
@EnableBatchProcessing
class UserTransferJobConfig(
    private val entityManagerFactory: EntityManagerFactory,
    private val jobParameter: JpaPagingItemReaderJobParameter
) {

    companion object {
        const val JOB_NAME = "userTransferToDynamodbJob"
        const val STEP1_NAME = "userTransferToDynamodbStep"
    }
    @Bean
    fun job(jobRepository: JobRepository, step: Step): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .start(step)
            .build()
    }

    @Bean
    fun step(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder(STEP1_NAME, jobRepository)
            .chunk<UserJpaModel, UserEvent>(100, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean
    @StepScope
    fun reader(): JpaPagingItemReader<UserJpaModel> {
        return JpaPagingItemReaderBuilder<UserJpaModel>()
            .pageSize(100)
            .queryString("select u from user u order by id asc")
            .entityManagerFactory(entityManagerFactory)
            .name("allUserReader")
            .build()
    }

    @Bean
    @StepScope
    fun processor(): ItemProcessor<UserJpaModel, UserEvent> {
        return ItemProcessor { user ->
            UserEvent(
                event = UserAdapterEvent(
                    userId = user.id,
                    eventType = "JOIN",
                    eventDateTime = LocalDateTime.now()
                ).toMap(),
                eventType = "JOIN",
                createdAt = LocalDateTime.now(),
            )
        }
    }


    @Bean
    @StepScope
    fun writer(): JpaItemWriter<UserEvent> {
        return JpaItemWriterBuilder<UserEvent>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }
}