package io.hodong.quarkusprac.model

import java.util.UUID;
import java.time.LocalDateTime

data class PostModel(
       val id: UUID,
       val title: String,
       val content: String,
       val createdAt: LocalDateTime,
       val updatedAt: LocalDateTime)