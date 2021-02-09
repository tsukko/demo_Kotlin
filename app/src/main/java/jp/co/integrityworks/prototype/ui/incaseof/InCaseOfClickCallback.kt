package jp.co.integrityworks.prototype.ui.incaseof

import jp.co.integrityworks.prototype.db.entity.AccidentType

interface InCaseOfClickCallback {
    fun onClick(accidentType: AccidentType)
}
