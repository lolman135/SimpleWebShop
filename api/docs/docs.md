@startuml

    skinparam style strictuml
    left to right direction
    skinparam nodesep 50
    skinparam ranksep 50
    
    ' ==================== User ====================
    together {
    entity User #99CC66
    entity User.id #CDE7B0
    entity User.username #CDE7B0
    entity User.email #CDE7B0
    entity User.password #CDE7B0
    User.id --* User
    User.username --* User
    User.email --* User
    User.password --* User
    }
    
    ' ==================== Role ====================
    together {
    entity Role #FFCC66
    entity Role.id #FFE7B0
    entity Role.name #FFE7B0
    Role.id --* Role
    Role.name --* Role
    }
    
    ' ==================== Product ====================
    together {
    entity Product #FF99CC
    entity Product.id #FFCCE5
    entity Product.name #FFCCE5
    entity Product.description #FFCCE5
    entity Product.price #FFCCE5
    entity Product.image_url #FFCCE5
    Product.id --* Product
    Product.name --* Product
    Product.description --* Product
    Product.price --* Product
    Product.image_url --* Product
    }
    
    ' ==================== Order ====================
    together {
    entity Order #99FF99
    entity Order.id #CCFFCC
    entity Order.created_at #CCFFCC
    entity Order.total_cost #CCFFCC
    Order.id --* Order
    Order.created_at --* Order
    Order.total_cost --* Order
    }
    
    ' ==================== Feedback ====================
    together {
    entity Feedback #99CCFF
    entity Feedback.id #CCE5FF
    entity Feedback.rate #CCE5FF
    entity Feedback.review #CCE5FF
    Feedback.id --* Feedback
    Feedback.rate --* Feedback
    Feedback.review --* Feedback
    }
    
    ' ==================== link_entities=============
    entity User_Role #CDEF60
    entity Order_Product #EF9460
    
    ' ===============================================
    ' ==================== links ====================
    ' ===============================================
    
    User "1,1" -- "0,*" User_Role
    User_Role "0,*" -- "1,1" Role
    
    Order "1,1" -- "0,*" Order_Product
    Order_Product "0,*" -- "1,1" Product
    
    User "1,1" -- "0,*" Order
    
    Product "1,1" -- "0,*" Feedback
    User "1,1" -- "0,*" Feedback

@enduml