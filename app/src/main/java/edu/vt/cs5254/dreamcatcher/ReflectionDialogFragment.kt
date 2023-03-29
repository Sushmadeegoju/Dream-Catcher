package edu.vt.cs5254.dreamcatcher

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import edu.vt.cs5254.dreamcatcher.databinding.FragmentReflectionDialogBinding

class ReflectionDialogFragment : DialogFragment() {
    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
        const val BUNDLE_KEY = "REQUEST_KEY"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = FragmentReflectionDialogBinding.inflate(layoutInflater)

        val positiveListener = DialogInterface.OnClickListener { _, _ ->
            val resultText = binding.reflectionText.text.toString()
            setFragmentResult(
                REQUEST_KEY,
                bundleOf(BUNDLE_KEY to resultText)
            )
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.reflection_dialog_title)
            .setPositiveButton(R.string.reflection_dialog_positive, positiveListener)
            .setNegativeButton(R.string.reflection_dialog_negative, null)
            .show()
    }
}