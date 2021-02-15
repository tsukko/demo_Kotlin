package jp.co.integrityworks.prototype.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import jp.co.integrityworks.prototype.db.entity.AccidentType

// 事故の種類、もしもの場合画面で表示される内容
class AccidentTypeViewModel(application: Application) : AndroidViewModel(application) {
    var accidentTypeList = ObservableField<List<AccidentType>>()
    private val mAccidentTypes: MutableList<AccidentType> = mutableListOf()

    init {
        // TODO 保有契約の情報から持ってくるが、仕様不明確なため固定に設定
        mAccidentTypes.add(AccidentType(0, true, showDetail = false))
        mAccidentTypes.add(AccidentType(1, true, showDetail = false))
        mAccidentTypes.add(AccidentType(2, false, showDetail = false))
        mAccidentTypes.add(AccidentType(3, false, showDetail = false))
        mAccidentTypes.add(AccidentType(4, false, showDetail = false))
        this.accidentTypeList.set(mAccidentTypes)
    }

    fun setVisible(accidentType: AccidentType) {
        mAccidentTypes[accidentType.id].showDetail = !accidentType.showDetail
        this.accidentTypeList.set(mAccidentTypes)
    }
}
