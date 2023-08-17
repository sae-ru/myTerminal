package com.example.myterminal.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthPersonResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "flags")
    val flags: Int?,
    @Json(name = "devparent")
    val devparent: Int?,
    @Json(name = "devaddr")
    val devaddr: Int?,
    @Json(name = "docNumber")
    val docNumber: String?,
    //date of person's creation
    @Json(name = "issuedWhen")
    val issuedWhen: String?,
    @Json(name = "birthPlace")
    val birthPlace: String?,
    @Json(name = "citizenship")
    val citizenship: String?,
    @Json(name = "series")
    val series: String?,
    //place of registration
    @Json(name = "placeOfResidence")
    val placeOfResidence: String?,
    @Json(name = "note")
    val note: String?,
    @Json(name = "birthday")
    val birthday: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    //amount of workers
    @Json(name = "employeeNumber")
    val employeeNumber: String?,
    @Json(name = "document")
    val document: String?,
    //Кем выдан
    @Json(name = "issuedBy")
    val issuedBy: String?,
    //ФИО
    @Json(name = "description")
    val description: String?,
    @Json(name = "insearch")
    val insearch: Int?,
    @Json(name = "category")
    val category: Int?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "fingerPrint1")
    val fingerPrint1: String?,
    @Json(name = "fingerPrint2")
    val fingerPrint2: String?,
    @Json(name = "fingerPrint3")
    val fingerPrint3: String?,
    @Json(name = "fingerPrint4")
    val fingerPrint4: String?,
    @Json(name = "fingerPrint5")
    val fingerPrint5: String?,
    @Json(name = "fingerPrint6")
    val fingerPrint6: String?,
    @Json(name = "fingerPrint7")
    val fingerPrint7: String?,
    @Json(name = "fingerPrint8")
    val fingerPrint8: String?,
    @Json(name = "fingerPrint9")
    val fingerPrint9: String?,
    @Json(name = "fingerPrint10")
    val fingerPrint10: String?,
    @Json(name = "postref")
    val postref: Int?,
    @Json(name = "phone")
    val phone: String?,
    @Json(name = "documentscan")
    val documentscan: String?,
    @Json(name = "canworkout")
    val canworkout: Int?,
    @Json(name = "employmentContract")
    val employmentContract: String?,
    @Json(name = "placeOfActualResidence")
    val placeOfActualResidence: String?,
    @Json(name = "phone2")
    val phone2: String?,
    @Json(name = "note2")
    val note2: String?,
    @Json(name = "note3")
    val note3: String?,
    @Json(name = "note4")
    val note4: String?,
    @Json(name = "note5")
    val note5: String?,
    @Json(name = "note6")
    val note6: String?,
    @Json(name = "subsystem_ids")
    val subsystemIds: String?, //"{}"
    @Json(name = "remote_guid")
    val remoteGuid: String?
)
