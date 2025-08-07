package project.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class PingController {

    @GetMapping("/ping")
    fun ping(): ResponseEntity<String>{
        return ResponseEntity.ok("ping")
    }
}