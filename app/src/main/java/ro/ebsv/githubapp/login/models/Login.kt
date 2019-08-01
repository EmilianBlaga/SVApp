package ro.ebsv.githubapp.login.models

data class Login (
    var username: String,
    var password: String
) {

    fun isUsernameValid(): Boolean = !username.isNullOrEmpty()

    fun isPasswordValid(): Boolean = !password.isNullOrEmpty()

}