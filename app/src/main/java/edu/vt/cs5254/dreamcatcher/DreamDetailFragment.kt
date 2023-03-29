package edu.vt.cs5254.dreamcatcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.vt.cs5254.dreamcatcher.databinding.FragmentDreamDetailBinding
import android.text.format.DateFormat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DreamDetailFragment: Fragment() {

    private val args : DreamDetailFragmentArgs by navArgs()

    private val vm : DreamDetailViewModel by viewModels() {
        DreamDetailViewModelFactory(args.dreamId)
    }

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.dream.collect {dream ->
                    dream?.let { updateView(it) }
                }
            }
        }

        //set listeners

        setFragmentResultListener("REQUEST_KEY") { _, bundle ->
            val entryText =
                bundle.getString(ReflectionDialogFragment.BUNDLE_KEY)
                    ?: ""
            vm.updateDream { oldDream ->
                oldDream.copy().apply {
                    entries = oldDream.entries + DreamEntry(
                        kind = DreamEntryKind.REFLECTION,
                        text = entryText,
                        dreamId = oldDream.id
                    )
                }
            }
        }

        binding.addReflectionButton.setOnClickListener {
            findNavController().navigate(
                DreamDetailFragmentDirections.addReflection()
            )
        }



        binding.titleText.doOnTextChanged { text, _, _, _ ->
                vm.updateDream { oldDream ->
                    oldDream.copy(title = text.toString()).apply {
                        entries = oldDream.entries
                    }
                }
        }
        binding.deferredCheckbox.setOnClickListener {
            vm.handleDeferredClick()
        }

        binding.fulfilledCheckbox.setOnClickListener {
            vm.handleFulfilledClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView(dream: Dream) {
        val buttonList = listOf(binding.entry0Button,
            binding.entry1Button,
            binding.entry2Button,
            binding.entry3Button,
            binding.entry4Button)

        buttonList.forEach { it.visibility = View.GONE }

        buttonList.zip(dream.entries).forEach{ (button, entry) ->
            button.configureForEntry(entry)
        }

        if(binding.titleText.toString() != dream.title) {
            binding.titleText.setText(dream.title)
        }

        val dateString = DateFormat.format("yyyy-MM-dd 'at' hh:mm:ss a", dream.lastUpdated)
        binding.lastUpdatedText.text = "Last updated $dateString"

        binding.fulfilledCheckbox.isChecked = dream.isFulfilled
        binding.deferredCheckbox.isChecked = dream.isDeferred
        binding.fulfilledCheckbox.isEnabled = !dream.isDeferred
        binding.deferredCheckbox.isEnabled = !dream.isFulfilled

        if(dream.isFulfilled) {
            binding.addReflectionButton.hide()
        } else {
            binding.addReflectionButton.show()
        }
    }


}