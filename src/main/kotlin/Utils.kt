import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonParser

object Utils {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()


    fun prettyJson(jsonString: String): String {
        return try {
            gson.toJson(JsonParser.parseString(jsonString))
        } catch (e: JsonParseException) {
            jsonString
        }
    }
}
