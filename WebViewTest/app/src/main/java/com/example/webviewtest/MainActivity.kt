package com.example.webviewtest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

private const val TAG = "MainActivity"
private const val TARGET_URL = "https://juejin.cn/user/207173399875662"

class MainActivity : AppCompatActivity() {
    private lateinit var responseText: TextView

//    val webView = findViewById<WebView>(R.id.button)
    private lateinit var sendRequestBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        responseText = findViewById<TextView>(R.id.responseText)
        sendRequestBtn = findViewById<Button>(R.id.sendRequestBtn)


        var getAppData = findViewById<Button>(R.id.getAppDataBtn)
        getAppData.setOnClickListener {
            sendRetrofitRequest()
        }


        sendRequestBtn.setOnClickListener {
            //sendRequestWithHttpURLConnect()

           // sendJSonRequestWithOkHttp()

           // sendCustomRequest()

            sendOkHttpRequest()

        }



//        webView.settings.javaScriptEnabled = true
//        webView.settings.domStorageEnabled = true
//        webView.webViewClient = object : WebViewClient() {
//            override fun onReceivedError(
//                view: WebView?,
//                request: WebResourceRequest?,
//                error: WebResourceError?
//            ) {
//                super.onReceivedError(view, request, error)
//                Log.e(
//                    TAG,
//                    "WebView error: code=${error?.errorCode}, description=${error?.description}, url=${request?.url}"
//                )
//            }
//        }
//        webView.loadUrl(TARGET_URL)
    }

    private fun sendRetrofitRequest() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.20.131:8000/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val appService = retrofit.create(AppService::class.java)

        val appService = ServiceCreator.create<AppService>()
        appService.getAppData().enqueue(object : retrofit2.Callback<List<App>> {
            override fun onResponse(
                call: retrofit2.Call<List<App>?>,
                response: retrofit2.Response<List<App>?>
            ) {
                 val list = response.body()
                if (list != null) {
                    for (app in list) {
                        Log.d(TAG, "id is ${app.id}")
                        Log.d(TAG, "name is ${app.name}")
                        Log.d(TAG, "version is ${app.version}")
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<App>?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private  fun sendOkHttpRequest() {
        HttpUtil.sendOkHttpRequest("http://192.168.20.131:8000/webService/get_json_data.json", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("sendOkHttpRequest: ${e.toString()}")
            }

            override fun onResponse(call: Call, response: Response) {
                println("sendCustomRequest1111: ${response.body()?.string().orEmpty()}")
            }


        })
    }
    
    private fun sendCustomRequest() {
        HttpUtil.sendHttpRequest("http://192.168.20.131:8000/webService/get_json_data.json", object : HttpCallbackListener {
            override fun onFinish(response: String) {
                println("sendCustomRequest: ${response.toString()}")
            }

            override fun onError(e: Exception) {
                println("error: ${e.toString()}")
            }
        }
        )
    }

    private fun sendJSonRequestWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://192.168.20.131:8000/webService/get_json_data.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body()?.string().orEmpty()
                if (responseData.isNotEmpty()) {
                    parseJSONWithJSONObject(responseData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseJSONWithJSONObject(jsonData: String) {
        try {

            val gson = Gson()
            val typeOf = object : TypeToken<List<App>>() {}.type
            val appList = gson.fromJson<List<App>>(jsonData, typeOf)
            for (app in appList) {
                Log.d(TAG, "id is ${app.id}")
                Log.d(TAG, "name is ${app.name}")
                Log.d(TAG, "version is ${app.version}")
            }
//            val jsonArray = JSONArray(jsonData);
//            for (i in 0 until jsonArray.length()) {
//                val jsonObject = jsonArray.getJSONObject(i)
//                val id = jsonObject.getString("id")
//                val name = jsonObject.getString("name")
//                val version = jsonObject.getString("version")
//                Log.d(TAG, "id is $id")
//                Log.d(TAG, "name is $name")
//                Log.d(TAG, "version is $version")
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun sendRequestWithOKHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://192.168.20.131:8000/webService/get_data.xml")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body()?.string().orEmpty()
                if (responseData.isNotEmpty()) {
                    parseXMLWithPull(responseData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseXMLWithPull(xmlData: String) {
        try {
            val factory = XmlPullParserFactory.newInstance()
            val xmlPullParser = factory.newPullParser()
            xmlPullParser.setInput(StringReader(xmlData))
            var eventType = xmlPullParser.eventType
            var id = ""
            var name = ""
            var version = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val nodeName = xmlPullParser.name
                when (eventType) {
                    //开始解析某个节点
                    XmlPullParser.START_TAG -> {
                        when (nodeName) {
                            "id" -> id = xmlPullParser.nextText()
                            "name" -> name = xmlPullParser.nextText()
                            "version" -> version = xmlPullParser.nextText()
                        }
                    }
                    // 完成解析 某个节点
                    XmlPullParser.END_TAG -> {
                        if ("app" == nodeName) {
                            Log.d(TAG, "id is $id")
                            Log.d(TAG, "name is $name")
                            Log.d(TAG, "version is $version")
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendRequestWithHttpURLConnect() {
        // 开始线程发起网络请求
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL("https://juejin.cn/post/7622478023082786868")
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                connection.requestMethod = "POST"
                val output = DataOutputStream(connection.getOutputStream())
                //output.writeBytes("username=admin$password=123456")
                val input = connection.getInputStream()
                // 对获取到的输入流进行读取
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                showResponse(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun showResponse(response: String) {
        runOnUiThread {
            // 在这里进行UI操作，将结果显示到界面上
            responseText.text = response
        }
    }

}
