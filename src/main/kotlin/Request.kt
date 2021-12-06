import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

object Request {
    private val client = HttpClient.newHttpClient()
    private val validateBuilder = HttpRequest.newBuilder()

    public fun get(url: String) = send(url, "GET")
    public fun post(url: String) = send(url, "POST")

    private fun send(url: String, method: String): RequestInfo {
        val request = HttpRequest
            .newBuilder(URI(url))
            .method(method, HttpRequest.BodyPublishers.noBody())
            .build()
        return RequestInfo(
            request,
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        )

    }

    public fun validateUrl(url: String): Boolean {
        return try {
            validateBuilder.uri(URI(url))
            true
        } catch (e: Exception) {
            false
        }
    }
}

data class RequestInfo(
    val request: HttpRequest,
    val response: CompletableFuture<HttpResponse<String>>
)
