package net.yuzumone.bergamio.api

import net.yuzumone.bergamio.model.CouponResult
import net.yuzumone.bergamio.model.LogResult
import retrofit2.http.GET
import retrofit2.http.Header
import rx.Observable

interface MioponService {
    @GET("coupon/")
    fun getCoupon(@Header("Context-type") type: String,
                  @Header("X-IIJmio-Developer") developer: String,
                  @Header("X-IIJmio-Authorization") token: String): Observable<CouponResult>

    @GET("log/packet/")
    fun getLog(@Header("X-IIJmio-Developer") developer: String,
               @Header("X-IIJmio-Authorization") token: String): Observable<LogResult>
}