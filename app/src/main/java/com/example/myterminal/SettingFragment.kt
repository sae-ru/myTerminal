package com.example.myterminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myterminal.databinding.FragmentSettingBinding
import com.example.myterminal.network.AUTH_SERVICE_URL
import com.example.myterminal.network.OCR_SERVICE_URL

/**
 * Setting fragment
 */
class SettingFragment : Fragment() {

    // Binding object instance corresponding to the fragment_choose_action.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentSettingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSettingBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the viewModel for data binding - this allows the bound layout access
        // to all the data in the VieWModel

        binding?.saveSettingsButton!!.setOnClickListener { saveSettings() }

        getSettings()
    }

    private fun getSettings() {
        binding?.apply {
            ocrServiceTextInputET.setText(OCR_SERVICE_URL)
            authServiceTextInputET.setText(AUTH_SERVICE_URL)
        }
    }

    private fun saveSettings() {
        binding?.apply {
            OCR_SERVICE_URL = ocrServiceTextInputET.text.toString()
            AUTH_SERVICE_URL = authServiceTextInputET.text.toString()
        }
    }

}