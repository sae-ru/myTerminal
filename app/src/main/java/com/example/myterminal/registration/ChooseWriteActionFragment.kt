package com.example.myterminal.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myterminal.R
import com.example.myterminal.databinding.FragmentChooseWriteActionBinding

/**
 * Fragment for choose how to entered data
 */
class ChooseWriteActionFragment : Fragment() {

    // Binding object instance corresponding to the fragment_choose_action.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentChooseWriteActionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentChooseWriteActionBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.chooseWriteActionFragment = this
    }

    /**
     * Go to scan passport
     */
    fun scanPassport() {
        // Navigate to the next destination to scan passport
        findNavController().navigate(R.id.action_chooseWriteActionFragment_to_takePassportPhotoFragment)
    }

    /**
     * Go to write passport data manually
     */
    fun writeManually() {
        // Navigate to the next destination to scan passport
        findNavController().navigate(R.id.action_chooseWriteActionFragment_to_fillDataFragment)
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
