package jp.co.integrityworks.prototype.api.response

//1_契約(Self)
//03.生保契約詳細取得(Self)
data class PolicyLifeResponse(
        var contractorId: String,
        var policyInfoNo: String,
        var message: String,
        var messageCode: String,
        var policyInfo: PolicyInfo,
        var premiumInfo: PremiumInfo,
        var insuredInfoList: ArrayList<InsuredInfoList>,
        var coverInfo: CoverInfo,
        var policyholderInfoList: ArrayList<PolicyholderInfoList>,
        var agentInfo: AgentInfo
)

class PolicyInfo(
        var insuranceCompany: String,
        var insuranceTrade: String,
        var policyNumber: String,
        var policyNumberBranch: String,
        var contractor: String,
        var applicationDate: String,
        var effectiveDate: String,
        var maturityDate: String
)

class PremiumInfo(
        var paidMode: String,
        var paidPremium: String,
        var paidDate: String,
        var paidStartDate: String,
        var maturityTerminalBonus: String
)

class InsuredInfoList(
        var name: String,
        var birthday: String,
        var gender: String,
        var relationship: String,
        var postalCode: String,
        var address: String
)

class CoverInfo(
        var mainPolicyInfo: MainPolicyInfo,
        var riderInfoList: ArrayList<RiderInfoList>
)

class MainPolicyInfo(
        var insuranceTypeName: String,
        var insuranceAmount: String,
        var premium: String,
        var insuranceTermDivision: String,
        var insuranceTerm: String,
        var paidTermDivision: String,
        var paidTerm: String
)

class RiderInfoList(
        var riderName: String,
        var insuranceAmount: String,
        var premium: String,
        var insuranceTermDivision: String,
        var insuranceTerm: String,
        var paidTermDivision: String,
        var paidTerm: String
)

class PolicyholderInfoList(
        var policyholder: String
)

class AgentInfo(
        var agentName: String,
        var postalCode: String,
        var address: String,
        var phone: String,
        var homepage: String,
        var department1: String,
        var personnel1: String,
        var department2: String?,
        var personnel2: String?,
        var dairitenPhone: String,
        var mail: String
)

