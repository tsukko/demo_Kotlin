package jp.co.integrityworks.prototype.api

import jp.co.integrityworks.prototype.api.response.PoliciesResponse
import jp.co.integrityworks.prototype.api.response.PolicyLifeResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

//TODO 02.契約一覧取得(Self)以外は未実装
interface ApiClient {
    //1_契約(Self)
    //01.契約登録(Self)
    @Headers("Accept: application/json", "connection: close")
    @POST("/mypage-api/api/policies/self/insert/1")
    fun putPolicies(@Query("contractorId") id: Int): Observable<ArrayList<PoliciesResponse>>

    //02.契約一覧取得(Self)
    @Headers("Accept: application/json", "connection: close")
    @GET("/api/v1/policies/self/")
    fun getPolicies(@Query("contractorId") contractorId: String): Observable<ArrayList<PoliciesResponse>>

    //03.生保契約詳細取得(Self)
    @Headers("Accept: application/json", "connection: close")
    @GET("/api/v1/policies/life/self/1/")
    fun getLifePolicies(@Query("contractorId") contractorId: String, @Query("policyInfoNo") policyInfoNo: String):
            Observable<ArrayList<PolicyLifeResponse>>

    //2_契約(Wise)
    //01.契約一覧取得(Wise)
    @Headers("Accept: application/json", "connection: close")
    @GET("/api/v1/policies/wise/")
    fun getWisePolicies(@Query("contractorId") id: Int): Observable<ArrayList<PoliciesResponse>>

    //02.生保契約詳細取得(Wise)
    @Headers("Accept: application/json", "connection: close")
    @GET("/api/v1/policies/life/wise/1/")
    fun getWiseLifePolicies(@Query("contractorId") id: Int): Observable<ArrayList<PoliciesResponse>>
}