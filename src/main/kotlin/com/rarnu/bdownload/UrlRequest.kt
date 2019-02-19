package com.rarnu.bdownload

import com.rarnu.kt.common.httpAsync
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import org.json.JSONObject
import java.util.*
import kotlin.random.Random


object UrlRequest {

    fun getBilibiliDownloadUrl(av: String, callback: (String, String?, String, String) -> Unit) = httpAsync {
        url = "http://api.bilibili.com/playurl?aid=$av&vtype=mp4&type=json"
        cookie = CookieJarImpl()
        onSuccess { _, text, _ -> parseUrl(text) { u, u2, u3, u4 -> callback(u, u2, u3, u4) } }
        onFail {
            val err = "Error"
            callback(err, err, err, err)
        }
    }

    private fun parseUrl(jsonString: String?, callback: (String, String?, String, String) -> Unit) = try {
        println(jsonString)
        val json = JSONObject(jsonString)
        val durl = json["durl"] as JSONObject
        val sec0 = durl["0"] as JSONObject
        val u = sec0["url"] as String
        var u2: String? = null
        if (sec0.has("backup_url")) {
            val bu = sec0["backup_url"] as JSONObject
            if (bu.has("0")) {
                u2 = bu["0"] as String
            }
        }
        val u3 = json["img"] as String
        val u4 = json["cid"] as String
        callback(u, u2, u3, u4)
    } catch (e: Throwable) {
        val err = "Error"
        callback(err, err, err, err)
    }

    private const val domain = "bilibili.com"
    private val cookieList = mutableListOf(
        Cookie.Builder().domain(domain).name("LIVE_BUVID").value(generateLiveBuvid()).build(),
        Cookie.Builder().domain(domain).name("buvid3").value(generateBuvid3()).build()
    )

    private class CookieJarImpl : CookieJar {
        override fun saveFromResponse(httpUrl: HttpUrl, list: MutableList<Cookie>) {}
        override fun loadForRequest(httpUrl: HttpUrl) = cookieList
    }

    private fun generateLiveBuvid(): String {
        val id = System.currentTimeMillis()
        val sur = Random.nextInt(0, 9)
        return "AUTO11$id$sur"
    }

    private fun generateBuvid3(): String {
        val id = UUID.randomUUID().toString()
        val sur = Random.nextInt(10000, 99999)
        return "$id${sur}infoc"
    }

}