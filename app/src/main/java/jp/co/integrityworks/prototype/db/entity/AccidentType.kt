package jp.co.integrityworks.prototype.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO 当初、別データと思っていたが、Insuranceの情報で表せるので、統一したい
@Entity(tableName = "accidentTypes")
data class AccidentType(
        @PrimaryKey
        val id: Int,
        //契約ありかどうか
        val haveContract: Boolean,
        //契約詳細を表示しているかどうか
        var showDetail: Boolean
)
