package edu.vt.cs5254.dreamcatcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.vt.cs5254.dreamcatcher.databinding.FragmentDreamDetailBinding

class DreamDetailFragment: Fragment() {

    private var _binding: FragmentDreamDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Fragment Dream Detail Binding is null!"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDreamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set listeners

        updateView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView() {

    }
}