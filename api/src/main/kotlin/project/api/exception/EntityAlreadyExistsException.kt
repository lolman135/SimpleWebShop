package project.api.exception

class EntityAlreadyExistsException(override val message: String) : Exception(message) {
}