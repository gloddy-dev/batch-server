package com.gloddy.batch.model.jpa

import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.time.LocalDateTime

@Table(name = "user_event")
@Entity
class UserEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Type(JsonType::class)
    @Column(name = "event", columnDefinition = "json")
    val event: Map<String, Any> = HashMap(),
    @Column(name = "event_type", nullable = false)
    val eventType: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    val published: Boolean = false,
    @Column(name = "published_at")
    val publishedAt: LocalDateTime? = null
)