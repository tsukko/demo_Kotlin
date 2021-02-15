package jp.co.integrityworks.prototype.db

import jp.co.integrityworks.prototype.api.response.*
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.db.entity.Person

/**
 * サンプルデータを追加
 */
object DataGenerator {

    // 連絡先の人情報（契約者、妻など）
    fun generatePersons(): List<Person> {
        return arrayListOf(
                Person(
                        relationship = 0,
                        nameKanji = "テスト　太郎",
                        nameKana = "テスト　たろう",
                        address1 = "〒140−0001",
                        address2 = "東京都品川区",
                        address3 = "1-2-3",
                        birthday = "1990/01/01",
                        pcMail = null,
                        tel = "03-1234-5678",
                        sms = "090-1234-5678",
                        eMail = "xxx@xxxx.ne.jp"),
                Person(
                        relationship = 1,
                        nameKanji = "テスト　花子",
                        nameKana = "テスト　はなこ",
                        address1 = null,
                        address2 = null,
                        address3 = null,
                        birthday = "2000/12/12",
                        pcMail = null,
                        tel = "03-1234-0000",
                        sms = null,
                        eMail = "xxx1@xxxx.ne.jp")
        )
    }

    // 契約している情報（１契約者あたり複数）
    fun generateInsurance(): List<Insurance> {
        return arrayListOf(
                Insurance(
                        accidentTypeId = 1,
                        policyInfoNo = "1",
                        insuranceCompanyCode = "insuranceCompanyCode",
                        insuranceCompanyName = "[local]〇〇〇〇株式会社",
                        insuranceTypeCode = "automobile",
                        insuranceTypeName = "xxxxTypeName",
                        insuranceTradeName = "car_〇〇サービス",
                        policyNumber = "1234567890",
                        policyNumberBranch = "policyNumberBranch",
                        maturityDate = "H33.10.31",
                        carName = "アルフォード",
                        registNumber = "チヨダクCサ1234"),
                Insurance(
                        accidentTypeId = 2,
                        policyInfoNo = "2",
                        insuranceCompanyCode = "insuranceCompanyCode",
                        insuranceCompanyName = "[local]△△△〇〇株式会社",
                        insuranceTypeCode = "life",
                        insuranceTypeName = "xxTypeName",
                        insuranceTradeName = "life_〇〇サービス",
                        policyNumber = "1234567890_life",
                        policyNumberBranch = "policyNumberBranch",
                        maturityDate = "H34.10.31",
                        carName = "",
                        registNumber = ""),
                Insurance(
                        accidentTypeId = 3,
                        policyInfoNo = "3",
                        insuranceCompanyCode = "insuranceCompanyCode",
                        insuranceCompanyName = "[local]□□□〇〇株式会社",
                        insuranceTypeCode = "house",
                        insuranceTypeName = "fooTypeName",
                        insuranceTradeName = "fire_〇〇サービス",
                        policyNumber = "1234567890_fire",
                        policyNumberBranch = "policyNumberBranch",
                        maturityDate = "H34.1.1",
                        carName = "",
                        registNumber = "")
                )
    }

    // 契約している〇〇の詳細情報
    fun generatePolicyLife(): PolicyLifeResponse {
        return PolicyLifeResponse(
                contractorId = "O000000001",
                policyInfoNo = "1",
                message = "正常に処理が完了しました。",
                messageCode = "MSG00001",
                policyInfo = PolicyInfo(
                        insuranceCompany = "[local][詳細1]日本Company",
                        insuranceTrade = "[local][詳細1]商品名〜こころ〜",
                        policyNumber = "N_SYOUKEN000001",
                        policyNumberBranch = "00001",
                        contractor = "テスト　太郎",
                        applicationDate = "2018-09-10 00:00:00.0",
                        effectiveDate = "2018-11-01 00:00:00.0",
                        maturityDate = "2020-10-31 00:00:00.0"
                ),
                premiumInfo = PremiumInfo(
                        paidMode = "月払",
                        paidPremium = "10000",
                        paidDate = "30",
                        paidStartDate = "201801",
                        maturityTerminalBonus = "3000000"
                ),
                insuredInfoList = arrayListOf(
                        InsuredInfoList(
                                name = "テスト　太郎",
                                birthday = "19700101",
                                gender = "男性",
                                relationship = "本人",
                                postalCode = "1000004",
                                address = "東京都千代田区大手町２丁目２−２番")
                ),
                coverInfo = CoverInfo(
                        mainPolicyInfo = MainPolicyInfo(
                                insuranceTypeName = "◇◇〇〇",
                                insuranceAmount = "5000000",
                                premium = "4000000",
                                insuranceTermDivision = "◇◇",
                                insuranceTerm = "12",
                                paidTermDivision = "年払込　　　",
                                paidTerm = "24"
                        ),
                        riderInfoList = arrayListOf(RiderInfoList(
                                riderName = "リビングニーズ",
                                insuranceAmount = "5000000",
                                premium = "4000000",
                                insuranceTermDivision = "◇◇",
                                insuranceTerm = "20",
                                paidTermDivision = "年払込　　　",
                                paidTerm = "10"))
                ),
                policyholderInfoList = arrayListOf(
                        PolicyholderInfoList(
                                policyholder = "test image")
                ),
                agentInfo = AgentInfo(
                        agentName = "[local]お客様意向確認ナビSI用１",
                        postalCode = "1000004",
                        address = "東京都千代田区大手町２丁目２−２番",
                        phone = "09012345678",
                        homepage = "https://xxx.xxx.jp/",
                        department1 = "首都圏営業推進部",
                        personnel1 = "花丸　大吉",
                        department2 = null,
                        personnel2 = null,
                        dairitenPhone = "0312345678",
                        mail = "xx@xxx.xxx.jp"
                )
        )

    }

    fun generatePolicyLife(insurance: Insurance?): PolicyLifeResponse {
        if (insurance == null) return generatePolicyLife()

        return PolicyLifeResponse(
                contractorId = "O000000001",
                policyInfoNo = insurance.policyInfoNo,
                message = "正常に処理が完了しました。",
                messageCode = "MSG00001",
                policyInfo = PolicyInfo(
                        insuranceCompany = insurance.insuranceCompanyName,
                        insuranceTrade = insurance.insuranceTradeName,
                        policyNumber = insurance.policyNumber,
                        policyNumberBranch = insurance.policyNumberBranch,
                        contractor = "テスト　太郎",
                        applicationDate = "2018-09-10 00:00:00.0",
                        effectiveDate = "2018-11-01 00:00:00.0",
                        maturityDate = insurance.maturityDate
                ),
                premiumInfo = PremiumInfo(
                        paidMode = "月払",
                        paidPremium = "10000",
                        paidDate = "30",
                        paidStartDate = "201801",
                        maturityTerminalBonus = "3000000"
                ),
                insuredInfoList = arrayListOf(
                        InsuredInfoList(
                                name = "テスト　太郎",
                                birthday = "19700101",
                                gender = "男性",
                                relationship = "本人",
                                postalCode = "1000004",
                                address = "東京都千代田区大手町２丁目２−２番")
                ),
                coverInfo = CoverInfo(
                        mainPolicyInfo = MainPolicyInfo(
                                insuranceTypeName = "◇◇〇〇",
                                insuranceAmount = "5000000",
                                premium = "4000000",
                                insuranceTermDivision = "◇◇",
                                insuranceTerm = "12",
                                paidTermDivision = "年払込　　　",
                                paidTerm = "24"
                        ),
                        riderInfoList = arrayListOf(RiderInfoList(
                                riderName = "リビングニーズ",
                                insuranceAmount = "5000000",
                                premium = "4000000",
                                insuranceTermDivision = "◇◇",
                                insuranceTerm = "20",
                                paidTermDivision = "年払込　　　",
                                paidTerm = "10"))
                ),
                policyholderInfoList = arrayListOf(
                        PolicyholderInfoList(
                                policyholder = "data:image" )
                ),
                agentInfo = AgentInfo(
                        agentName = "["+insurance.insuranceTypeCode +"]代理店名",
                        postalCode = "1000004",
                        address = "東京都千代田区大手町２丁目２−２番",
                        phone = "09012345678",
                        homepage = "https://xxx.xxx.jp/",
                        department1 = "首都圏営業推進部",
                        personnel1 = "花丸　大吉",
                        department2 = null,
                        personnel2 = null,
                        dairitenPhone = "0312345678",
                        mail = "xx@xxx.xxx.jp"
                )
        )
    }
}
