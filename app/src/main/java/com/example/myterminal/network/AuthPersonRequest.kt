package com.example.myterminal.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthPersonRequest(
    @Json(name = "flags")
    val flags: Int? = null,
    @Json(name = "devparent")
    val devparent: Int? = null,
    @Json(name = "devaddr")
    val devaddr: Int? = null,
    @Json(name = "docNumber")
    val docNumber: String,
    //date of person's creation
    @Json(name = "issuedWhen")
    val issuedWhen: String? = null,
    @Json(name = "birthPlace")
    val birthPlace: String = "РФ",
    @Json(name = "citizenship")
    val citizenship: String = "РФ",
    @Json(name = "series")
    val series: String,
    //place of registration
    @Json(name = "placeOfResidence")
    val placeOfResidence: String = "",
    @Json(name = "note")
    val note: String = "",
    //??? Int or String ???
    @Json(name = "birthday")
    val birthday: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String? = null,
    //amount of workers
    @Json(name = "employeeNumber")
    val employeeNumber: String = "",
    @Json(name = "document")
    val document: String = "Пасспорт РФ",
    //Кем выдан
    @Json(name = "issuedBy")
    val issuedBy: String = "",
    //ФИО
    @Json(name = "description")
    val description: String,
    @Json(name = "insearch")
    val insearch: Int? = null,
    @Json(name = "category")
    val category: Int? = null,
    @Json(name = "email")
    val email: String = "",
    @Json(name = "fingerPrint1")
    val fingerPrint1: String? = null,
    @Json(name = "fingerPrint2")
    val fingerPrint2: String? = null,
    @Json(name = "fingerPrint3")
    val fingerPrint3: String? = null,
    @Json(name = "fingerPrint4")
    val fingerPrint4: String? = null,
    @Json(name = "fingerPrint5")
    val fingerPrint5: String? = null,
    @Json(name = "fingerPrint6")
    val fingerPrint6: String? = null,
    @Json(name = "fingerPrint7")
    val fingerPrint7: String? = null,
    @Json(name = "fingerPrint8")
    val fingerPrint8: String? = null,
    @Json(name = "fingerPrint9")
    val fingerPrint9: String? = null,
    @Json(name = "fingerPrint10")
    val fingerPrint10: String? = null,
    @Json(name = "postref")
    val postref: Int? = null,
    @Json(name = "phone")
    val phone: String? = null,
    @Json(name = "documentscan")
    val documentscan: String = "",
    @Json(name = "canworkout")
    val canworkout: Int? = null,
    @Json(name = "employmentContract")
    val employmentContract: String = "",
    @Json(name = "placeOfActualResidence")
    val placeOfActualResidence: String = "",
    @Json(name = "phone2")
    val phone2: String = "",
    @Json(name = "note2")
    val note2: String = "",
    @Json(name = "note3")
    val note3: String = "",
    @Json(name = "note4")
    val note4: String = "",
    @Json(name = "note5")
    val note5: String = "",
    @Json(name = "note6")
    val note6: String = "",
    @Json(name = "subsystem_ids")
    val subsystemIds: String? = null, //"{}"
    @Json(name = "remote_guid")
    val remoteGuid: String = ""
)
