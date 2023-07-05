package com.example.myterminal.model

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myterminal.network.AuthApi
import com.example.myterminal.network.OCRApi
import com.example.myterminal.network.PassportOCRFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

enum class ApiStatus { LOADING, ERROR, DONE }

class DocViewModel : ViewModel() {

    //----------MAIN_FIELDS----------

    // Passport number in format as "1234 567890" without space
    private val _passportID = MutableLiveData<Int?>()
    val passportID: LiveData<Int?> = _passportID

    // Name (first name) as in passport
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    // Surname (last name) as in passport
    private val _surname = MutableLiveData<String>()
    val surname: LiveData<String> = _surname

    // Patronymic (middle name) as in passport
    private val _patronymic = MutableLiveData<String>()
    val patronymic: LiveData<String> = _patronymic

    // Birthday date as in passport
    private val _birthday = MutableLiveData<String>()
    val birthday: LiveData<String> = _birthday

    // Gender as in passport
    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> = _gender

    // Face Image Uri
    private val _passportFacePhotoByteArray = MutableLiveData<ByteArray?>()
    val passportFacePhotoByteArray: LiveData<ByteArray?> = _passportFacePhotoByteArray

    //----------MAIN_FIELDS_SETTERS----------

    /**
     * Set the passport's ID number.
     * The length of the number is exactly 10 digits.
     * The first 4 digits are the passport series, the remaining 6 are the number.
     *
     * @param passportID is number in format as "1234 567890" without space
     */
    fun setPassportID(passportID: Int) {
        _passportID.value = passportID
    }

    /**
     * Set the name (first name) as in the passport.
     *
     * @param passportName is the name as a string
     */
    fun setPassportName(passportName: String) {
        _name.value = passportName
    }

    /**
     * Set the surname (last name) as in the passport.
     *
     * @param passportSurname is the surname as a string
     */
    fun setPassportSurname(passportSurname: String) {
        _surname.value = passportSurname
    }

    /**
     * Set the patronymic (middle name) as in the passport.
     *
     * @param passportPatronymic is the surname as a string
     */
    fun setPassportPatronymic(passportPatronymic: String) {
        _patronymic.value = passportPatronymic
    }

    /**
     * Set the gender as in the passport.
     *
     * @param passportBirthday is the gender as a enum class Genders
     */
    fun setPassportBirthday(passportBirthday: String) {
        _birthday.value = passportBirthday
    }

    /**
     * Set the gender as in the passport.
     *
     * @param passportGender is the gender as a enum class Genders
     */
    fun setPassportGender(passportGender: String) {
        _gender.value = passportGender
    }

    /**
     * Set the face photo ByteArray.
     *
     * @param facePhotoBytes is the gender as a enum class Genders
     */
    fun setPassportFacePhotoByteArray(facePhotoBytes: ByteArray) {
        _passportFacePhotoByteArray.value = facePhotoBytes
    }

    //----------END_MAIN_FIELDS----------

    // Passport Image Uri
    private val _passportImageBase64String = MutableLiveData<String?>()
    val passportImageBase64String: LiveData<String?> = _passportImageBase64String

    /**
     * Set the passport image String as base64.
     *
     * @param passportImageString is String in format base64
     */
    fun setPassportImageBase64String(passportImageString: String) {
        _passportImageBase64String.value = passportImageString
    }

    init {
        // Set initial values for the form
        resetForm()
    }

    /**
     * Reset the form.
     */
    private fun resetForm() {
        _passportID.value = null
        _name.value = ""
        _surname.value = ""
        _patronymic.value = ""
        _birthday.value = ""
        _gender.value = "Муж"
        _passportFacePhotoByteArray.value = null
        _passportImageBase64String.value = null
    }

    private val authBody = mapOf(
        "login" to "admin",
        "password" to "admin",
//        "imei" to "",
//        "card" to ""
    )

    private val passportBodyForAuth = mapOf(
        "passportID" to passportID.value.toString(),
        "passportName" to name.value.toString(),
        "passportSurname" to surname.value.toString(),
        "passportPatronymic" to patronymic.value.toString(),
        "passportBirthday" to birthday.value.toString(),
        "passportGender" to gender.value.toString(),
        "passportFacePhoto" to passportFacePhotoByteArray.value.toString()
    )

    private val passportBodyForOCR = mapOf(
        "data" to _passportImageBase64String.value.toString()
    )

    //----------Post data(photo) to OCR service----------

    // The internal MutableLiveData that stores the status of the most recent request
    private val _ocrPostStatus = MutableLiveData<String>()
    // The external immutable LiveData for the request status
    val ocrPostStatus: LiveData<String> = _ocrPostStatus

    /**
     * Post data to OCR service
     */
    fun postOCRData() {
        viewModelScope.launch {
            try {
                OCRApi.retrofitOCRService.postPassportImage(passportBodyForOCR)
                    .enqueue(object : Callback<PassportOCRFields> {
                        override fun onResponse(call: Call<PassportOCRFields>, response: Response<PassportOCRFields>) {
                            // handle the response
                            _ocrPostStatus.value = "Success send data to OCR service"
                        }
                        override fun onFailure(call: Call<PassportOCRFields>, t: Throwable) {
                            // handle the failure
                            _ocrPostStatus.value = "Failure send data to OCR service"
                        }
                    })
            } catch (e: Exception) {
                _ocrPostStatus.value = "Failure OCR POST: $e"
            }
            Log.d("MYTAG", _ocrPostStatus.value.toString())
        }
    }

    //----------Get data from OCR service----------

//    // The internal MutableLiveData that stores the status of the most recent request
//    private val _ocrGetStatus = MutableLiveData<ApiStatus>()
//    // The external immutable LiveData for the request status
//    val ocrGetStatus: LiveData<ApiStatus> = _ocrGetStatus
//
//    // Internally, we use a MutableLiveData, because we will be updating the List of MarsPhoto
//    // with new values
//    private val _passportFields = MutableLiveData<List<PassportOCRFields>>()
//    // The external LiveData interface to the property is immutable, so only this class can modify
//    val passportFields: LiveData<List<PassportOCRFields>> = _passportFields
//
    /**
     * Get data from OCR service
     */
    /*fun getOCRData() {
        viewModelScope.launch {
            _ocrGetStatus.value = ApiStatus.LOADING
            try {
                _passportFields.value = OCRApi.retrofitOCRService.getPassportData()
                _ocrGetStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _ocrGetStatus.value = ApiStatus.ERROR
                _passportFields.value = listOf()
            }
        }
    }*/

    //----------Post data to Auth service----------

    // The internal MutableLiveData that stores the status of the most recent request
    private val _authLoginPostStatus = MutableLiveData<String>()
    // The external immutable LiveData for the request status
    val authLoginPostStatus: LiveData<String> = _authLoginPostStatus

//    private val emptyFile = File.createTempFile("tempText", ".txt")

    private var personalToken: String = ""

    /**
     * Post Authorization
     */
    fun postAuthLogin() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val orderResponse = AuthApi.retrofitAuthService.postAuthorizationAsync(authBody)
                    Log.d("MYTAG", "orderResponse: $orderResponse")
                }
                catch (e: Exception) {
                    Log.d("MYTAG", "ERROR: $e")
                }
            }
//            try {
////                AuthApi.retrofitAuthService.postAuthorization(authBody) // login & password body
//                personalToken = AuthApi.retrofitAuthService.postAuthorization(authBody).body()?.token ?: "FAILED" // login & password body
//                    Log.d("MYTAG", "token: $personalToken")
////                    .enqueue(object : Callback<AuthResponseToken> {
////                        override fun onResponse(call: Call<AuthResponseToken>, response: Response<AuthResponseToken>) {
////                            // handle the response
////                            if (response.isSuccessful) {
////                                personalToken = response.body()?.token ?: ""
////                                Log.d("MYTAG", "personalToken: $personalToken")
////                                _authLoginPostStatus.value = "Success send data to Auth service"
////                            } else {
////                                _authLoginPostStatus.value = "NOT Success send data to Auth service"
////                            }
////                        }
////                        override fun onFailure(call: Call<AuthResponseToken>, t: Throwable) {
////                            // handle the failure
////                            _authLoginPostStatus.value = "Failure send data to Auth service"
////                        }
////                    })
//            } catch (e: Exception) {
//                _authLoginPostStatus.value = "Failure Auth POST: $e"
//            }
//            Log.d("MYTAG", _authLoginPostStatus.value.toString())
        }
    }

    // The internal MutableLiveData that stores the status of the most recent request
    private val _authPassportDataPostStatus = MutableLiveData<String>()
    // The external immutable LiveData for the request status
    val authPassportDataPostStatus: LiveData<String> = _authPassportDataPostStatus

    /**
     * Post passport data to ESM
     */
    fun postAuthPassportData() {
        viewModelScope.launch {
            try {
                AuthApi.retrofitAuthService.postPassportData(passportBodyForAuth) // passportBodyForAuth
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            // handle the response
                            _authPassportDataPostStatus.value = "Success send data to Auth service"
                            Log.d("MYTAG", _authPassportDataPostStatus.value.toString())
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // handle the failure
                            _authPassportDataPostStatus.value = "Failure send data to Auth service"
                            Log.d("MYTAG", _authPassportDataPostStatus.value.toString())
                        }
                    })
            } catch (e: Exception) {
                _authPassportDataPostStatus.value = "Failure Auth POST: ${e.message}"
            }
        }
    }

    // Utils

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
}

fun ByteArray.toBase64(): String =
    Base64.encodeToString(this, Base64.DEFAULT)