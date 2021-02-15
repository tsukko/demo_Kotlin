package jp.co.integrityworks.prototype.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "persons")
data class Person(
        //契約者ID
//      val contractorId: Int
        //続柄 ：契約者、妻、長男、次男、叔父など
        var relationship: Int,
        var nameKanji: String,
        var nameKana: String,
        //誕生日
        var birthday: String, //TODO ほんとはDate型で
        //住所
        var address1: String?, //契約者のみ
        var address2: String?, //契約者のみ
        var address3: String?, //契約者のみ
        //連絡先 Tel
        var tel: String?,
        var sms: String?,
        var pcMail: String?,
        var eMail: String?
) : Serializable {
        //Parson ID, 登録ID
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0

        constructor() : this(
                relationship = 1,
                nameKanji = "",
                nameKana = "",
                address1 = null,
                address2 = null,
                address3 = null,
                birthday = "1990/01/01",
                pcMail = null,
                tel = null,
                sms = null,
                eMail = null)
}
