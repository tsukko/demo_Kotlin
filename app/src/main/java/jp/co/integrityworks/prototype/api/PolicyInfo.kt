package jp.co.integrityworks.prototype.api

/**
 * 契約情報
 *   契約リスト（PolicyList）の子
 */
class PolicyInfo(
        var policyInfoNo: String,
        var insuranceCompanyCode: String,
        var insuranceCompanyName: String,
        var insuranceTypeCode: String,
        var insuranceTypeName: String,
        var insuranceTradeName: String,
        var policyNumber: String,
        var policyNumberBranch: String,
        var maturityDate: String
)