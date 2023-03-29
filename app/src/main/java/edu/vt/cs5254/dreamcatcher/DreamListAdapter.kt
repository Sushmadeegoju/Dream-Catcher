package edu.vt.cs5254.dreamcatcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.vt.cs5254.dreamcatcher.databinding.ListItemDreamBinding
import java.util.UUID

class DreamHolder(private val binding : ListItemDreamBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(dream: Dream, onDreamClicked: (dreamId: UUID) -> Unit) {
        binding.listItemTitle.text = dream.title
        val reflectionCount = dream.entries.count {it.kind == DreamEntryKind.REFLECTION}
        binding.listItemReflectionCount.text = "Reflections: $reflectionCount"

        with(binding.listItemImage) {
            when {
                dream.isDeferred -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.dream_deferred_icon)
                }
                dream.isFulfilled -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.dream_fulfilled_icon)
                }
                else -> {
                    visibility = View.GONE
                }
            }
        }
        binding.root.setOnClickListener {
            onDreamClicked(dream.id)
        }

    }
}

class DreamListAdapter(
    private val dreamList: List<Dream>,
    private val onDreamClicked: (dreamId: UUID) -> Unit
) : RecyclerView.Adapter<DreamHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ListItemDreamBinding = ListItemDreamBinding.inflate(inflater, parent, false)
        return DreamHolder(binding)
    }

    override fun getItemCount(): Int {
        return dreamList.size
    }

    override fun onBindViewHolder(holder: DreamHolder, position: Int) {
        holder.bind(dreamList[position], onDreamClicked)
    }
}


