package project.api.repository.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.api.entity.Order
import java.util.UUID

@Repository
interface OrderRepository: JpaRepository<Order, UUID> {
}