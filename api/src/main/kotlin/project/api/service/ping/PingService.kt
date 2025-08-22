package project.api.service.ping

import org.springframework.stereotype.Service

@Service
class PingService {

    fun ping(): String{
        return "ping"
    }
}