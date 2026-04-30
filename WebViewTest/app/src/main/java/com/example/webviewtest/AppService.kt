package com.example.webviewtest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @GET("get_data.json")
    fun getAppData(): Call<List<App>>
}



interface ExampleHeaderService {

    @GET("{page}/get_data.json")
    fun getDataPath(@Path("page") page: Int): Call<App>

    // path 拼接
    @DELETE("data/{id}")
    fun deleteData(@Path("id") id: String) : Call<ResponseBody>

    @GET("get_data.json")
    fun getDataQuery(@Query("u") user: String, @Query("t") token: String) : Call<App>

    // Post
    @POST("data/create")
    fun createData(@Body data: App) : Call<ResponseBody>



    // 请求Header 添加参数
    @Headers("User-Agent : okHttp", "Cache-Control: max-age=0")
    @GET("get_data.json")
    fun getJsonData(): Call<App>

    @GET("geet_data.json")
    fun getData(@Header("User-Agent") userAgent: String,
                @Header("Cache-Control") cacheControl: String): Call<App>
}

