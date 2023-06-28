package com.example.myterminal.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myterminal.R
import com.example.myterminal.databinding.FragmentFillDataBinding
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

        val spinner: Spinner = binding?.genderSpinner!!
        ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            R.array.genders_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                viewModel.setPassportGender(parent.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        binding?.sendDataButton?.setOnClickListener { sendData() }

        getData()
    }

    /**
     * Getting recognized data (passport fields) from the OCR service.
     */
    private fun getData() {
        //TODO: connect to internet and get data
        //viewModel.getOCRData()
    }

    private fun sendData() {
        if (isDataCorrect()) {
            //TODO: connect to internet and send part of data (or all data? or nothing?)
            //viewModel.postAuthData()

            findNavController().navigate(R.id.action_fillDataFragment_to_takeFacePhotoFragment)
        } else
            Toast.makeText(
                context,
                getString(R.string.error_fields_toast_text),
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun isDataCorrect(): Boolean {
        val passportID = binding?.passportIDTextInputET?.text.toString()
        if (passportID.length == 10) {
            setErrorPassportIDTextField(binding?.passportIDTextField!!, false)
            viewModel.setPassportID(passportID.toInt())
        } else {
            setErrorPassportIDTextField(binding?.passportIDTextField!!, true)
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

        val passportSurname = binding?.passportSurnameTextInputET?.text.toString()
        if (passportSurname.isNotEmpty()) {
            setErrorEmptyTextField(binding?.passportSurnameTextField!!, false)
            viewModel.setPassportSurname(passportSurname)
        } else {
            setErrorEmptyTextField(binding?.passportSurnameTextField!!, true)
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
            textField.editText?.text = null
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
            textField.editText?.text = null
        }
    }

}