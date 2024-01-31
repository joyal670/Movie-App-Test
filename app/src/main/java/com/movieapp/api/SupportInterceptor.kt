package com.movieapp.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization",  "diCoecSW_ExSgIwHIXiZsH:APA91bHeW2d92n2TaSNm8TAmfyuP3rFWvia-Q-5aCiC2UD-XqDMP")
            .build()
        return chain.proceed(request)
    }

}
