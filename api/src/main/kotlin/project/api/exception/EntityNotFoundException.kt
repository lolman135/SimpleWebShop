package project.api.exception

class EntityNotFoundException(override val message: String) : IllegalArgumentException(message)