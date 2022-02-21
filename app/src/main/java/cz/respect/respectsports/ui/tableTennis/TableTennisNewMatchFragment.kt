package cz.respect.respectsports.ui.tableTennis

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisNewMatchBinding
import cz.respect.respectsports.domain.Player


class TableTennisNewMatchFragment : Fragment() {

    private var _binding: FragmentTableTennisNewMatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tableTennisNewMatchViewModel =
            ViewModelProvider(this).get(TableTennisNewMatchViewModel::class.java)

        Log.i("MY_INFO","USER: "+(activity as? MainActivity)!!.user)
        _binding = FragmentTableTennisNewMatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tableTennisNewMatchViewModel.players.observe(viewLifecycleOwner) {
            val players: MutableList<Player> = ArrayList()
            val adapter: ArrayAdapter<Player>
            players.addAll(it)
            //list.add("Michal Novák")
            //list.add("Item 2")
            //list.add("Item 3")

            adapter = ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item, players
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val homeSpinner = binding.homeSpinner
            val visitorSpinner = binding.visitorSpinner
            if (homeSpinner != null) {
                homeSpinner.setAdapter(adapter)
            }
            if (visitorSpinner != null) {
                visitorSpinner.setAdapter(adapter)
            }

            Log.i("MY_INFO", "PLAYERS: " + it.toString())
            binding.players.text = it.toString()
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.page_table_tennis_new_match)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}