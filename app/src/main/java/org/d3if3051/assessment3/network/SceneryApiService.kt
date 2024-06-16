package org.d3if3051.assessment3.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3051.assessment3.model.MessageResponse
import org.d3if3051.assessment3.model.Scenery
import org.d3if3051.assessment3.model.SceneryCreate
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://sceneries.vercel.app/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface SceneryApiService {
    @POST("sceneries/")
    suspend fun addScenery(
        @Body scenery: SceneryCreate
    ): MessageResponse

    @GET("sceneries/")
    suspend fun getAllScenery(
        @Query("user_email") email: String,
    ): List<Scenery>

    @DELETE("sceneries/{scenery_id}")
    suspend fun deleteScenery(
        @Path("scenery_id") id: Int,
        @Query("email") email: String
    ): MessageResponse
}


object Api {
    val userService: SceneryApiService by lazy {
        retrofit.create(SceneryApiService::class.java)
    }

}

enum class ApiStatus { LOADING, SUCCESS, FAILED }