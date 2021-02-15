package jp.co.integrityworks.prototype.ui.contract

import jp.co.integrityworks.prototype.db.entity.Insurance

interface InForceContractCallback {
    fun onClick(insurance: Insurance)
}
