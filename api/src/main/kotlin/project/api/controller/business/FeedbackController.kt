package project.api.controller.business

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.entity.Feedback
import project.api.security.CustomUserDetails
import project.api.service.business.feedback.FeedbackService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/feedbacks")
class FeedbackController(private val feedbackService: FeedbackService) {

    @PostMapping
    fun leaveFeedback(
        @RequestBody request: FeedbackDtoRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<Feedback> {
        val feedback = feedbackService.save(request, userDetails.user)
        return ResponseEntity.ok(feedback)
    }

    @GetMapping("/{id}")
    fun getFeedbackById(@PathVariable id: UUID) = ResponseEntity.ok(feedbackService.findById(id))

    @GetMapping
    fun getAllFeedbacks() = ResponseEntity.ok(feedbackService.findAll())

    @PutMapping("/{id}")
    fun updateFeedbackById(
        @PathVariable id: UUID,
        @RequestBody request: FeedbackDtoRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) : ResponseEntity<Feedback> {
        val feedback = feedbackService.updateById(id, request, userDetails.user)
        return ResponseEntity.ok(feedback)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID) = ResponseEntity.ok(feedbackService.deleteById(id))
}