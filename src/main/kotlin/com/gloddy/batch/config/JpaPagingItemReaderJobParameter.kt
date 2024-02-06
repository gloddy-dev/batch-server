package com.gloddy.batch.config

import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@StepScope
class JpaPagingItemReaderJobParameter(
    @Value("#{jobParameters[txDate]}")
    val txDate: String
)