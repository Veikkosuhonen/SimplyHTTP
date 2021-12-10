@file:OptIn(ExperimentalTime::class)

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

object Request {
    private val client = HttpClient.newHttpClient()
    private val validateBuilder = HttpRequest.newBuilder()
    private val headerRegex = Regex("([\"\']?[A-Za-z]+\\-?)+[\"\']?:\\s*[^:]+")

    private var currentIndex = 0

    fun send(requestData: RequestData): CompletableFuture<HttpResponse<String>> {
        val request = HttpRequest
            .newBuilder(URI(requestData.url))
            .method(requestData.method, HttpRequest.BodyPublishers.noBody())
            .headersString(requestData.headers)
            .timeout(java.time.Duration.ofSeconds(10L))
            .build()
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun HttpRequest.Builder.headersString(input: String): HttpRequest.Builder {
        input.split('\n').forEach {
            val parts = it.split(':')
            if (parts.size != 2) return this
            this.header(parts.component1(), parts.component2())
        }
        return this
    }

    public fun validateUrl(url: String): Boolean {
        return try {
            validateBuilder.uri(URI(url))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validateHeaders(headers: String) = headers.isBlank() || headers.split('\n').all {
        headerRegex.matches(it)
    }

    val METHODS = listOf(
        "GET", "POST", "PUT", "DELETE", "HEAD"
    )

    private var index = 0
    fun nextIndex() = index++
}

data class RequestData(
    val method: String,
    val url: String,
    val headers: String = "",
    val body: String = "",
    val index: Int = Request.nextIndex(),
)

data class ResponseData(
    val requestData: RequestData,
    val response: HttpResponse<String>?,
    val duration: Duration
)
