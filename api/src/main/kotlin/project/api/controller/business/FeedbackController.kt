package project.api.controller.business

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
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
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.entity.Feedback
import project.api.security.CustomUserDetails
import project.api.service.business.feedback.FeedbackService
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/feedbacks")
class FeedbackController(private val feedbackService: FeedbackService) {

    @PostMapping
    @Operation(summary = "Creating new feedback", description = "Creates a new feedback if not exists yet")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully. Returns created feedback"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "409", description = "Conflict")
        ]
    )
    fun leaveFeedback(
        @RequestBody @Valid request: FeedbackDtoRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<FeedbackDtoResponse> {
        val response = feedbackService.save(request, userDetails.user)
        val location = URI.create("/feedbacks/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    @Operation(
        summary = "Returns all feedbacks",
        description = "Returns all feedbacks as list. If nothing to return, than returns empty list")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns list of feedbacks"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getAllFeedbacks() = ResponseEntity.ok(feedbackService.findAll())

    @GetMapping("/{id}")
    @Operation(summary = "Returns feedback", description = "Returns feedback by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns feedback"),
            ApiResponse(responseCode = "404", description = "Not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getFeedbackById(@PathVariable id: UUID) = ResponseEntity.ok(feedbackService.findById(id))

    @PutMapping("/{id}")
    @Operation(summary = "Updating existed feedback", description = "Updates feedback and returns it in response")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Updated successfully. Returns updated feedback"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun updateFeedbackById(
        @PathVariable id: UUID,
        @RequestBody @Valid request: FeedbackDtoRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) : ResponseEntity<FeedbackDtoResponse> {
        val response = feedbackService.updateById(id, request, userDetails.user)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting existed feedback", description = "Deletes feedback. Returns nothing")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deleted successfully. No contend"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Void> {
        feedbackService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}