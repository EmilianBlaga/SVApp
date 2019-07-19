package ro.ebsv.githubapp.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    private var credentials: String? = null

    fun setCredentials(user: String, password: String) {
        this.credentials = Credentials.basic(user, password)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (request.header("Accept") == null)
            requestBuilder.addHeader("Accept", "application/vnd.github.v3+json")

        if (request.header("Authorization") == null) {
            credentials?.let {
                requestBuilder.addHeader("Authorization", it)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}