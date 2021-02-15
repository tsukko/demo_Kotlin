package jp.co.integrityworks.prototype.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "insurances")
data class Insurance(
        //契約者ID
//        val contractorId: String,
        //事故の種類　もしもの場合で、どこに分類するかを示す（おそらく〇〇のみ持つ）
        val accidentTypeId: Int,

        //契約情報
        //契約情報通番
        val policyInfoNo: String,
        //〇〇会社コード
        val insuranceCompanyCode: String,
        //〇〇会社名
        val insuranceCompanyName: String,
        //〇〇種類コード
        val insuranceTypeCode: String,
        //〇〇種類名
        val insuranceTypeName: String,
        //〇〇商品名
        val insuranceTradeName: String,
        //証券番号
        val policyNumber: String,
        //証券番号枝番
        val policyNumberBranch: String,
        //満期日
        val maturityDate: String,

        //自動車情報
        //車名
        val carName: String,
        //登録番号
        val registNumber: String
) : Serializable {
    // 保有契約ID　（ワランティ、クレジットカード含め一意な値）
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
