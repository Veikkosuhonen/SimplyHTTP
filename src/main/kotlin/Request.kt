import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

object Request {
    private val client = HttpClient.newHttpClient()
    private val validateBuilder = HttpRequest.newBuilder()
    private val headerRegex = Regex("([A-Z][a-z]*\\-?)+:\\s[^:]+")

    fun send(method: String, url: String, headers: String = "", body: String = ""): RequestInfo {
        val request = HttpRequest
            .newBuilder(URI(url))
            .method(method, HttpRequest.BodyPublishers.noBody())
            .headersString(headers)
            .build()
        return RequestInfo(
            request,
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        )
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
}

data class RequestInfo(
    val request: HttpRequest,
    val response: CompletableFuture<HttpResponse<String>>
)
