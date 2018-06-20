package net.yuzumone.bergamio.api

import io.reactivex.Observable
import net.yuzumone.bergamio.model.CouponResult
import net.yuzumone.bergamio.model.LogResult
import net.yuzumone.bergamio.model.ToggleCouponInfo
import net.yuzumone.bergamio.model.ToggleResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface MioponService {
    @GET("coupon/")
    fun getCoupon(@Header("Context-type") type: String,
                  @Header("X-IIJmio-Developer") developer: String,
                  @Header("X-IIJmio-Authorization") token: String): Observable<CouponResult>

    @GET("log/packet/")
    fun getLog(@Header("X-IIJmio-Developer") developer: String,
               @Header("X-IIJmio-Authorization") token: String): Observable<LogResult>

    @PUT("coupon/")
    fun putToggleCoupon(@Header("Context-type") type: String,
                  @Header("X-IIJmio-Developer") developer: String,
                  @Header("X-IIJmio-Authorization") token: String,
                  @Body body: ToggleCouponInfo): Observable<ToggleResult>
}