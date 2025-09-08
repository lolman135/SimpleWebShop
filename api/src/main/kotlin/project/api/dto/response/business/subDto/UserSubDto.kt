package project.api.dto.response.business.subDto

import java.util.UUID

data class UserSubDto(
    val id: UUID,
    val username: String,
    val email: String
)