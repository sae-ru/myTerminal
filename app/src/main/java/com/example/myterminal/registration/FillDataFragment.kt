package com.example.myterminal.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myterminal.R
import com.example.myterminal.databinding.FragmentFillDataBinding
import com.example.myterminal.model.ApiStatus
import com.example.myterminal.model.DocViewModel
import com.google.android.material.textfield.TextInputLayout


/**
 * Fragment for enter data manually or check autofill
 */
class FillDataFragment : Fragment() {

    // Binding object instance corresponding to the fragment_choose_action.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentFillDataBinding? = null

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment.
    private val viewModel: DocViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentFillDataBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding?.lifecycleOwner = viewLifecycleOwner

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.gender_dropdown_menu_item,
            resources.getStringArray(R.array.genders_array)
        )
        binding?.genderAutoCompleteTV!!.setAdapter(adapter)

        binding?.saveDataButton!!.setOnClickListener { savePassportFields() }

        fillPassportFieldsFromViewModel()
    }

    private fun fillPassportFieldsFromViewModel() {
        binding?.apply {
            viewModel.passportID.value.let {
                passportIDTextInputET.setText(if (it == null || it == 0) "" else it.toString())
            }
            viewModel.surname.value?.let { passportSurnameTextInputET.setText(it) }
            viewModel.name.value?.let { passportNameTextInputET.setText(it) }
            viewModel.patronymic.value?.let { passportPatronymicTextInputET.setText(it) }
            viewModel.birthday.value?.let { passportBirthdayTextInputET.setText(it) }
            viewModel.gender.value?.let {
                val genders = resources.getStringArray(R.array.genders_array).toSet()
                genderAutoCompleteTV.setText(
                    if (setOf(it).intersect(genders).isEmpty()) genders.first() else it, false
                )
            }
        }
    }

    private fun savePassportFields() {
        if (isDataCorrect()) {
            connectAndGo()
        } else
            Toast.makeText(
                context,
                getString(R.string.error_fields_toast_text),
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun connectAndGo() {
        //connect to ESM server and get token
        viewModel.postAuthLogin()

        viewModel.authLoginPostStatus.observe(viewLifecycleOwner) { tokenStatus ->
            when (tokenStatus) {
                ApiStatus.ERROR -> {
                    Toast.makeText(
                        context,
                        getString(R.string.postAuthToken_error_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding?.saveDataButton!!.apply {
                        text = getString(R.string.try_again_button_text)
                        isEnabled = true
                    }
                }

                ApiStatus.DONE -> {
                    binding?.saveDataButton!!.apply {
                        text = getString(R.string.continue_button_text)
                        isEnabled = true
                    }
//                    findNavController().navigate(R.id.action_fillDataFragment_to_takeFacePhotoFragment)
                }

                else -> {
                    binding?.saveDataButton!!.apply {
                        text = getString(R.string.data_processed_button_text)
                        isEnabled = false
                    }
                }
            }
        }
    }

    /**
     * Checks the correctness of the entered data.
     *
     * For example, the passport ID must be exactly 10 digits,
     * as the series is 4 digits long + the number is 6 digits long.
     */
    private fun isDataCorrect(): Boolean {
        val passportID = binding?.passportIDTextInputET?.text.toString()
        if (passportID.length == 10) {
            setErrorPassportIDTextField(binding?.passportIDTextField!!, false)
            viewModel.setPassportID(passportID.toInt())
        } else {
            setErrorPassportIDTextField(binding?.passportIDTextField!!, true)
            return false
        }

        val passportSurname = binding?.passportSurnameTextInputET?.text.toString()
        if (passportSurname.isNotEmpty()) {
            setErrorEmptyTextField(binding?.passportSurnameTextField!!, false)
            viewModel.setPassportSurname(passportSurname)
        } else {
            setErrorEmptyTextField(binding?.passportSurnameTextField!!, true)
            return false
        }

        val passportName = binding?.passportNameTextInputET?.text.toString()
        if (passportName.isNotEmpty()) {
            setErrorEmptyTextField(binding?.passportNameTextField!!, false)
            viewModel.setPassportName(passportName)
        } else {
            setErrorEmptyTextField(binding?.passportNameTextField!!, true)
            return false
        }

        val passportPatronymic = binding?.passportPatronymicTextInputET?.text.toString()
        if (passportPatronymic.isNotEmpty()) {
            setErrorEmptyTextField(binding?.passportPatronymicTextField!!, false)
            viewModel.setPassportPatronymic(passportPatronymic)
        } else {
            setErrorEmptyTextField(binding?.passportPatronymicTextField!!, true)
            return false
        }

        val passportBirthday = binding?.passportBirthdayTextInputET?.text.toString()
        if (passportBirthday.isNotEmpty()) {
            setErrorEmptyTextField(binding?.passportBirthdayTextField!!, false)
            viewModel.setPassportBirthday(passportBirthday)
        } else {
            setErrorEmptyTextField(binding?.passportBirthdayTextField!!, true)
            return false
        }
        return true
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Sets and resets the text field error status "is empty".
     */
    private fun setErrorEmptyTextField(textField: TextInputLayout, error: Boolean) {
        if (error) {
            textField.isErrorEnabled = true
            textField.error = getString(R.string.empty_field_error)
        } else {
            textField.isErrorEnabled = false
        }
    }

    /**
     * Sets and resets the text field error status "is empty".
     */
    private fun setErrorPassportIDTextField(textField: TextInputLayout, error: Boolean) {
        if (error) {
            textField.isErrorEnabled = true
            textField.error = getString(R.string.passport_ID_field_error)
        } else {
            textField.isErrorEnabled = false
        }
    }

}