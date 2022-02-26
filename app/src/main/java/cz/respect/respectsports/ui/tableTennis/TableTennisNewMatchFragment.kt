package cz.respect.respectsports.ui.tableTennis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisNewMatchBinding
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.domain.Player


class TableTennisNewMatchFragment : Fragment() {

    private var _binding: FragmentTableTennisNewMatchBinding? = null
    private val binding get() = _binding!!
    private var insertButtonClicked:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tableTennisNewMatchViewModel = MainActivity.TableTennisViewModelFactory(requireActivity().application,(activity as? MainActivity)!!.userId,(activity as? MainActivity)!!.userToken).create(TableTennisNewMatchViewModel::class.java)

        //val tableTennisNewMatchViewModel = ViewModelProvider(this).get(TableTennisNewMatchViewModel::class.java)

        Log.i("MY_INFO","USER ID: "+(activity as? MainActivity)!!.userId)
        Log.i("MY_INFO","USER TOKEN: "+(activity as? MainActivity)!!.userToken)
        _binding = FragmentTableTennisNewMatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tableTennisNewMatchViewModel.players.observe(viewLifecycleOwner) {
            val players: MutableList<Player> = ArrayList()
            val homePlayer: MutableList<Player> = ArrayList()
            val otherPlayers: MutableList<Player> = ArrayList()

            val visitorAdapter: ArrayAdapter<Player>

            val homeAdapter: ArrayAdapter<Player>
            players.addAll(it)

            players.forEach { player ->
                if (player.id == (activity as? MainActivity)!!.userId) {
                    homePlayer.add(player)
                }
                else {
                    otherPlayers.add(player)
                }
            }
            //list.add("Michal Novák")
            //list.add("Item 2")
            //list.add("Item 3")

            homeAdapter = ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item, homePlayer
            )
            visitorAdapter = ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item, otherPlayers
            )

            homeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            visitorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val homeSpinner = binding.homeSpinner
            val visitorSpinner = binding.visitorSpinner
            if (homeSpinner != null) {
                homeSpinner.setAdapter(homeAdapter)
            }
            if (visitorSpinner != null) {
                visitorSpinner.setAdapter(visitorAdapter)
            }

            tableTennisNewMatchViewModel.matches.observe(viewLifecycleOwner) {
                Log.i("MY_INFO", "MATCH SUCCESSFULLY INSERTED - MATCHES")
                if (insertButtonClicked) {
                    insertButtonClicked = false
                    Log.i("MY_INFO", "MATCH EDIT DONE - GET BACK")
                    val action = TableTennisNewMatchFragmentDirections.actionNewMatchDone()
                    findNavController().navigate(action)
                }
            }

            Log.i("MY_INFO", "PLAYERS: " + it.toString())
            binding.players.text = it.toString()

            binding.matchInsertButton!!.setOnClickListener {
                val homePlayer = binding.homeSpinner!!.selectedItem.equals(homeAdapter)
                Log.i("MY_INFO", "HOME PLAYER" + homePlayer.toString())
                val visitorPlayer = binding.visitorPlayer!!.text.toString()
                val matchResult = binding.matchResult!!.text.toString()
                val matchDate = binding.matchDate!!.text.toString()
                if (homePlayer && visitorPlayer.length > 1 && matchResult.length > 2 && matchDate.length > 7) {
                    val match = Match(null, matchDate, homePlayer.toString(), "", "", visitorPlayer, "","", matchResult)
                    insertButtonClicked = true
                    Log.i("MY_INFO", "INSERTING MATCH")
                    tableTennisNewMatchViewModel.insertMatch(match)
                    }
                else {
                    showResultMessage("Vyplňte všechny položky")
                }
            }

            tableTennisNewMatchViewModel.tokenError.observe(viewLifecycleOwner) {
                Log.i("MY_INFO", "Chyba při ověření uživateleeee")
                showResultMessage("Chyba při ověření uživatele")
                //val action = TableTennisNewMatchFragmentDirections.actionLogout()
                //findNavController().navigate(action)
            }

        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.page_table_tennis_new_match)

        return root
    }

    private fun showResultMessage(apiResponseString:String) {
        Toast.makeText(
            activity,
            apiResponseString,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}