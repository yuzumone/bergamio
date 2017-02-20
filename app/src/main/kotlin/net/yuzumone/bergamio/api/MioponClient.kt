package net.yuzumone.bergamio.api

import net.yuzumone.bergamio.model.CouponResult
import net.yuzumone.bergamio.model.LogResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MioponClient @Inject constructor(client: OkHttpClient) {

    private val service: MioponService

    init {
        val uri = "https://api.iijmio.jp/mobile/d/v1/"
        val retrofit = Retrofit.Builder().client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(uri)
                .build()
        service = retrofit.create(MioponService::class.java)
    }

    fun getCoupon(developer: String, token: String): Observable<CouponResult> {
        return service.getCoupon("application/json", developer, token)
    }

    fun getLog(developer: String, token: String): Observable<LogResult> {
        return service.getLog(developer, token)
    }
}