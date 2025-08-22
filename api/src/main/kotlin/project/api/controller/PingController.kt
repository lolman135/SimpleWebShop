package project.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.service.ping.PingService

@RestController
@RequestMapping("/api/v1")
class PingController(private val pingService: PingService, service: PingService) {

    @GetMapping("/ping")
    fun ping(): ResponseEntity<String>{
        val response = pingService.ping()
        return ResponseEntity.ok(response)
    }
}