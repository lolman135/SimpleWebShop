package project.api.exception

class UserAlreadyExistsException(override val message: String) : Exception(message) {
}