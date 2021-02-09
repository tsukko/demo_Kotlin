package jp.co.integrityworks.prototype.ui.contact

import jp.co.integrityworks.prototype.db.entity.Person

interface ContactInfoCallback {
    fun onClick(person: Person)
    fun onSettingRelationship(relationship: Int) :String
}
