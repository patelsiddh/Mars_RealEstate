
package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    // this commented converted factory type converts JSON response to string
    // that we did use at very beginning of  Ch3-Lesson3-Concept 7
    //.addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {

    /*
    The function commented below is correspond to the change mention above for retrofit object addConverterFactory(ScalarsConverterFactory.create())
    Please refer to those comment
     */
    /*@GET("realestate")
    fun getProperties(): Call<String>*/

    @GET("realestate")
    fun getProperties(): List<MarsProperty>  //Deferred<List<MarsProperty>>
}

object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}


