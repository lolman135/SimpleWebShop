package project.api.exception.user

class UserNotFoundException(override val message: String) : IllegalArgumentException(message)