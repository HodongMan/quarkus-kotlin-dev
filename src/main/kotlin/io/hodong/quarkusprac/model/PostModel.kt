package io.hodong.quarkusprac.model

import java.time.LocalDateTime

data class PostModel(
       val id: String,
       val title: String,
       val content: String,
       val createdAt: LocalDateTime,
       val updatedAt: LocalDateTime)