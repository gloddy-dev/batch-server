package com.gloddy.batch.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.LocalDateTime

class UserAdapterEvent(
    val userId: Long,
    val eventType: String,
    val eventDateTime: LocalDateTime
)

fun UserAdapterEvent.toMap(): Map<String, Any> {
    val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    val typeRef = object : TypeReference<Map<String, Any>>() {}
    return objectMapper.convertValue(this, typeRef)
}