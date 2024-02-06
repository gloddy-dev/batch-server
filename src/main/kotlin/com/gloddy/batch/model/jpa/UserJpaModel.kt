package com.gloddy.batch.model.jpa

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "user")
@Table(name = "users")
class UserJpaModel(
    @Id
    val id: Long,
)