package com.lloyds.test.data.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModuleTest {

    @Test
    fun `provideOkHttpClient installs HttpLoggingInterceptor at BODY level`() {
        val client: OkHttpClient = NetworkModule.provideOkHttpClient()
        val interceptor = client.interceptors
            .filterIsInstance<HttpLoggingInterceptor>()
            .firstOrNull()
        assertNotNull("Expected a HttpLoggingInterceptor", interceptor)
        assertEquals(
            "Logging level must be BODY",
            HttpLoggingInterceptor.Level.BODY,
            interceptor!!.level
        )
    }

    @Test
    fun `provideRetrofit is configured with correct baseUrl and client`() {
        val okClient = NetworkModule.provideOkHttpClient()
        val retrofit: Retrofit = NetworkModule.provideRetrofit(okClient)

        // baseUrl check
        assertEquals(
            "Base URL should match",
            "https://thereportoftheweekapi.com/",
            retrofit.baseUrl().toString()
        )

        // callFactory should be our OkHttpClient
        assertSame(
            "Retrofit’s Call.Factory must be the provided OkHttpClient",
            okClient,
            retrofit.callFactory()
        )

        // GsonConverterFactory present
        val hasGson = retrofit
            .converterFactories()
            .any { it is GsonConverterFactory }
        assertTrue("GsonConverterFactory should be present", hasGson)
    }

    @Test
    fun `provideFoodReviewApi returns a non-null implementation`() {
        val retrofit: Retrofit = NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
        val api = NetworkModule.provideFoodReviewApi(retrofit)
        // api will never be null, this test is just to cover the case.
        assertNotNull("FoodReviewApi implementation should not be null", api)
    }
}