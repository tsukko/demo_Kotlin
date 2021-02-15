package jp.co.integrityworks.prototype.ui.contact

import jp.co.integrityworks.prototype.db.entity.Person

interface ContactSettingClickCallback {
    fun onClickDel(person: Person)
    fun onClickEdit(person: Person)
    fun onClickEditContact(person: Person)
}
