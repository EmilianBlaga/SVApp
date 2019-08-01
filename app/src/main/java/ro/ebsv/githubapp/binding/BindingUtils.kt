package ro.ebsv.githubapp.binding

class BindingUtils {

    companion object {
        @JvmStatic
        fun isNullOrEmpty(value: String?) = value.isNullOrEmpty()

        @JvmStatic
        fun getPasswordErrorString() = "Password format is invalid"

        @JvmStatic
        fun getUserErrorString() = "Username format is invalid"

        @JvmStatic
        fun intToString(value: Int?) = value.toString()
    }

}