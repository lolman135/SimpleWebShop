package project.api.repository.feedback

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.api.entity.Feedback
import java.util.*

@Repository
interface FeedbackRepository : JpaRepository<Feedback, UUID> {
}