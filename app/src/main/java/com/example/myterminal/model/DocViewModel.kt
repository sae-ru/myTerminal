package com.example.myterminal.model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myterminal.network.AuthApi
import com.example.myterminal.network.OCRApi
import com.example.myterminal.network.PassportData
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

enum class OCRApiStatus { LOADING, ERROR, DONE }

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
     * @param passportGender is the gender as a enum class Genders
     */
    fun setPassportGender(passportGender: String) {
        _gender.value = passportGender
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
     * Set the face photo ByteArray.
     *
     * @param facePhotoBytes is the gender as a enum class Genders
     */
    fun setPassportFacePhotoByteArray(facePhotoBytes: ByteArray) {
        _passportFacePhotoByteArray.value = facePhotoBytes
    }

    //----------END_MAIN_FIELDS----------

    // Passport Image Uri
    private val _passportImageByteArray = MutableLiveData<ByteArray?>()
    val passportImageByteArray: LiveData<ByteArray?> = _passportImageByteArray

    /**
     * Set the passport image ByteArray.
     *
     * @param passportImageBytes is the gender as a enum class Genders
     */
    fun setPassportImageByteArray(passportImageBytes: ByteArray) {
        _passportImageByteArray.value = passportImageBytes
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
        _passportImageByteArray.value = null
    }

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
        //TODO: learn about the
        "passportImage" to passportImageByteArray.value.toString()
    )

    //Post data(photo) to OCR service

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
                OCRApi.retrofitOCRService.postPassportPhoto(passportBodyForOCR)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            // handle the response
                            _ocrPostStatus.value = "Success send data to OCR service"
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // handle the failure
                            _ocrPostStatus.value = "Failure send data to OCR service"
                        }
                    })
            } catch (e: Exception) {
                _ocrPostStatus.value = "Failure OCR POST: ${e.message}"
            }
        }
    }

    //Get data from OCR service

    // The internal MutableLiveData that stores the status of the most recent request
    private val _ocrGetStatus = MutableLiveData<OCRApiStatus>()
    // The external immutable LiveData for the request status
    val ocrGetStatus: LiveData<OCRApiStatus> = _ocrGetStatus

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsPhoto
    // with new values
    private val _passportFields = MutableLiveData<List<PassportData>>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val passportFields: LiveData<List<PassportData>> = _passportFields

    /**
     * Get data from OCR service
     */
    fun getOCRData() {
        viewModelScope.launch {
            _ocrGetStatus.value = OCRApiStatus.LOADING
            try {
                _passportFields.value = OCRApi.retrofitOCRService.getPassportData()
                _ocrGetStatus.value = OCRApiStatus.DONE
            } catch (e: Exception) {
                _ocrGetStatus.value = OCRApiStatus.ERROR
                _passportFields.value = listOf()
            }
        }
    }

    //Post data to Auth service

    // The internal MutableLiveData that stores the status of the most recent request
    private val _authPostStatus = MutableLiveData<String>()
    // The external immutable LiveData for the request status
    val authPostStatus: LiveData<String> = _authPostStatus

    /**
     * Post data
     */
    fun postAuthData() {
        viewModelScope.launch {
            try {
                AuthApi.retrofitAuthService.postPassportData(passportBodyForAuth)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            // handle the response
                            _authPostStatus.value = "Success send data to Auth service"
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // handle the failure
                            _authPostStatus.value = "Failure send data to Auth service"
                        }
                    })
            } catch (e: Exception) {
                _authPostStatus.value = "Failure Auth POST: ${e.message}"
            }
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
}