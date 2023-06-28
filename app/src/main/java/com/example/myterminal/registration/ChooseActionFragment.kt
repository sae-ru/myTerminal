package com.example.myterminal.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myterminal.R
import com.example.myterminal.databinding.FragmentChooseActionBinding

/**
 * Start fragment for get pass
 */
class ChooseActionFragment : Fragment() {

    // Binding object instance corresponding to the fragment_choose_action.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentChooseActionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentChooseActionBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.chooseActionFragment = this
    }

    /**
     * Start getting a pass.
     * Go to chooseWriteActionFragment
     */
    fun getPass() {
        // Navigate to the next destination to choose scan passport or write manually
        findNavController().navigate(R.id.action_chooseActionFragment_to_chooseWriteActionFragment)
    }

    /**
     * Go to settings
     */
    fun goToSettings() {
        // Navigate to the next destination to choose scan passport or write manually
        findNavController().navigate(R.id.action_chooseActionFragment_to_settingFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
