package net.yuzumone.bergamio.api

import io.reactivex.Observable
import net.yuzumone.bergamio.model.CouponResult
import net.yuzumone.bergamio.model.LogResult
import net.yuzumone.bergamio.model.ToggleCouponInfo
import net.yuzumone.bergamio.model.ToggleResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MioponClient @Inject constructor(client: OkHttpClient) {

    private val service: MioponService

    init {
        val uri = "https://api.iijmio.jp/mobile/d/v1/"
        val retrofit = Retrofit.Builder().client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

    fun putToggleCoupon(developer: String, token: String, body: ToggleCouponInfo): Observable<ToggleResult> {
        return service.putToggleCoupon("application/json", developer, token, body)
    }
}