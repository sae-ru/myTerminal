package com.example.myterminal.model

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myterminal.network.AuthApi
import com.example.myterminal.network.AuthPersonRequest
import com.example.myterminal.network.AuthTokenRequest
import com.example.myterminal.network.OCRApi
import com.example.myterminal.network.OCRPassportFieldsRequest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

enum class ApiStatus { LOADING, ERROR, DONE }

class DocViewModel : ViewModel() {

    //----------MAIN_FIELDS----------

    // Passport number in format as "1234 567890" without space
    private val _passportID = MutableLiveData<Int>()
    val passportID: LiveData<Int> = _passportID

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

    // Face photo in format String as Base64
    private val _passportFacePhotoBase64String = MutableLiveData<String?>()
    val passportFacePhotoBase64String: LiveData<String?> = _passportFacePhotoBase64String

    // OCR face photo from passport image in format String as Base64
    private val _passportOCRFacePhotoBase64String = MutableLiveData<String?>()
    val passportOCRFacePhotoBase64String: LiveData<String?> = _passportOCRFacePhotoBase64String

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
     * Set the face photo String as Base64.
     *
     * @param faceBase64String is face photo in format Base64 string
     */
    fun setPassportFacePhotoBase64String(faceBase64String: String) {
        _passportFacePhotoBase64String.value = faceBase64String
    }

    /**
     * Set the OCR face photo String as Base64.
     *
     * @param faceOCRBase64String is face photo from OCR in format Base64 string
     */
    private fun setPassportOCRFacePhotoBase64String(faceOCRBase64String: String) {
        _passportOCRFacePhotoBase64String.value = faceOCRBase64String
    }

    //----------END_MAIN_FIELDS----------

    // Passport Image as String in format Base64
    private val _passportImageBase64String = MutableLiveData<String?>()
    val passportImageBase64String: LiveData<String?> = _passportImageBase64String

    /**
     * Set the passport image String as Base64.
     *
     * @param passportImageString is passport image in format Base64 string
     */
    fun setPassportImageBase64String(passportImageString: String) {
        _passportImageBase64String.value = "b'$passportImageString'"
    }

    init {
        // Set initial values for the form
        resetForm()
    }

    /**
     * Reset the form.
     */
    private fun resetForm() {
        _passportID.value = 0
        _name.value = ""
        _surname.value = ""
        _patronymic.value = ""
        _birthday.value = ""
        _gender.value = "Муж"
        _passportFacePhotoBase64String.value = null
        _passportOCRFacePhotoBase64String.value = null
        _passportImageBase64String.value = null
    }

    private val tokenRequestBody by lazy {
        AuthTokenRequest("admin", "admin" /*, "", ""*/)
    }

    private val passportBodyForAuth by lazy {
        AuthPersonRequest(
            //gender.value.toString(),

            series = _passportID.value.toString().substring(0, 4),
            docNumber = _passportID.value.toString().substring(4),
            description = "${_surname.value} ${_name.value} ${_patronymic.value}",
            birthday = _birthday.value,

            note = _passportFacePhotoBase64String.value ?: "",
            note2 = _passportOCRFacePhotoBase64String.value ?: ""
        )
    }

    private val passportBodyForOCR by lazy {
        OCRPassportFieldsRequest(_passportImageBase64String.value ?: "")
//        OCRPassportFieldsRequest("'/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/wAALCAAgACABAREA/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/9oACAEBAAA/APKqKKKKKKKKKKKKKKK//9k='"/*_passportImageBase64String.value ?: ""*/)
    }


    //----------Post data(passport image) to OCR service----------

    // The internal MutableLiveData that stores the status of the most recent request
    private val _ocrPostStatus = MutableLiveData<ApiStatus>()
    // The external immutable LiveData for the request status
    val ocrPostStatus: LiveData<ApiStatus> = _ocrPostStatus

    /**
     * Post data to OCR service
     */
    fun postOCRData() {
        viewModelScope.launch {
            _ocrPostStatus.value = ApiStatus.LOADING
            try {
                /*Log.d("MYTAG", "passportImageBase64String.length: ${_passportImageBase64String.value?.length ?: ""}")
                Log.d(
                    "MYTAG",
                    "_passportImageBase64String.value[first n elements]: ${
                        _passportImageBase64String.value?.substring(0, 33)
                    }"
                )
                Log.d(
                    "MYTAG",
                    "_passportImageBase64String.value[last n elements]: ${
                        _passportImageBase64String.value?.substring(
                            _passportImageBase64String.value?.lastIndex?.minus(10) ?: 0,
                            _passportImageBase64String.value?.lastIndex?.plus(1) ?: 0
                        )
                    }"
                )*/
                val ocrResponse = OCRApi.retrofitOCRService.postPassportImage(passportBodyForOCR)
//                Log.d("MYTAG", "ocrResponse: $ocrResponse")

                //handle the response
                setPassportID(ocrResponse.passportID.toInt())
                setPassportSurname(ocrResponse.passportSurname)
                setPassportName(ocrResponse.passportName)
                setPassportPatronymic(ocrResponse.passportPatronymic)
                setPassportBirthday(ocrResponse.passportBirthday)
                setPassportGender(ocrResponse.passportGender)
                setPassportOCRFacePhotoBase64String(ocrResponse.passportFacePhoto)

                _ocrPostStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _ocrPostStatus.value = ApiStatus.ERROR
//                Log.d("MYTAG", "Failure OCR POST: $e")
            }
        }
    }

    //----------Post data to Auth service----------

    // The internal MutableLiveData that stores the status of the most recent request
    private val _authLoginPostStatus = MutableLiveData<ApiStatus>()
    // The external immutable LiveData for the request status
    val authLoginPostStatus: LiveData<ApiStatus> = _authLoginPostStatus

    private var personalToken: String = ""

    /**
     * Post Authorization
     */
    fun postAuthLogin() {
        viewModelScope.launch {
            _authLoginPostStatus.value = ApiStatus.LOADING
            try {
                val authTokenResponse = AuthApi.retrofitAuthService.postAuthorization(tokenRequestBody)
                personalToken = authTokenResponse.token
//                Log.d("MYTAG", "personalToken: $personalToken")
                _authLoginPostStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
//                Log.d("MYTAG", "ERROR getting Token: $e")
                _authLoginPostStatus.value = ApiStatus.ERROR
                //handle the failure
                personalToken = ""
            }
        }
    }

    // The internal MutableLiveData that stores the status of the most recent request
    private val _authPassportDataPostStatus = MutableLiveData<ApiStatus>()
    // The external immutable LiveData for the request status
    val authPassportDataPostStatus: LiveData<ApiStatus> = _authPassportDataPostStatus

    /**
     * Post passport data to ESM
     */
    fun postAuthPassportData() {
        viewModelScope.launch {
            _authPassportDataPostStatus.value = ApiStatus.LOADING
            try {
                AuthApi.retrofitAuthService.postPassportData(
                    passportBodyForAuth,
                    personalToken
                )
//                Log.d("MYTAG", "Success Auth Passport Data POST: ")
                _authPassportDataPostStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
//                Log.d("MYTAG", "Failure Auth Passport Data POST: $e")
                _authPassportDataPostStatus.value = ApiStatus.ERROR
            }
        }
    }

    // Utils

    //TODO: move this fun to other place (class/file)
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
}

fun ByteArray.toBase64(): String =
    Base64.encodeToString(this, Base64.DEFAULT)