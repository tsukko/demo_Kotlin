package jp.co.integrityworks.prototype.api.response

import jp.co.integrityworks.prototype.api.PolicyList

//1_契約(Self)
//02.契約一覧取得(Self)
data class PoliciesResponse(
        var contractorId: String,
        var message: String,
        var messageCode: String,
        var searchCount: String,
        var policyList: ArrayList<PolicyList> = ArrayList()
)