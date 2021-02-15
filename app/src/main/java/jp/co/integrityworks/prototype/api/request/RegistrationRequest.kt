package jp.co.integrityworks.prototype.api.request

import jp.co.integrityworks.prototype.api.PolicyList

//1_契約(Self)
//01.契約登録(Self)
data class RegistrationRequest(
        //TODO null許可の検討
        var id: Int = 0,
        var message: String?,
        var messageCode: String?,
        var searchCount: String?,
        var policyList: ArrayList<PolicyList> = ArrayList()
)