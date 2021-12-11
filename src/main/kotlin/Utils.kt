import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.net.http.HttpHeaders

object Utils {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()


    fun prettyJson(jsonString: String): String {
        return try {
            gson.toJson(JsonParser.parseString(jsonString))
        } catch (e: JsonParseException) {
            jsonString
        }
    }

    fun headersToString(headers: HttpHeaders) = headers.map()
        .map {
            it.key + ": " + it.value.reduce { acc, s -> "$acc; $s" }
        }.reduce { acc, s -> acc + '\n' + s }
}

object Logger {
    fun log(string: String) = println(string)
}
