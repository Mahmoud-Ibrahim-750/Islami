package com.mis.route.islami.data.api

import com.mis.route.islami.ui.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private var retrofit: Retrofit? = null

    fun getRadiosService(): RadiosService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.RADIOS_API_BASE_URL)
                .build()
        }

        return retrofit!!.create(RadiosService::class.java)
    }
}