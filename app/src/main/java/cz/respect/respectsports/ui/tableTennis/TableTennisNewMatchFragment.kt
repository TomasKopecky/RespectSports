package cz.respect.respectsports.ui.tableTennis

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarView
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisNewMatchBinding
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.domain.Player
import java.util.*


class TableTennisNewMatchFragment : Fragment() {

    private var _binding: FragmentTableTennisNewMatchBinding? = null
    private val binding get() = _binding!!
    private var insertButtonClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val tableTennisNewMatchViewModel = MainActivity.TableTennisViewModelFactory(requireActivity().application,(activity as? MainActivity)!!.userId,(activity as? MainActivity)!!.userToken).create(TableTennisNewMatchViewModel::class.java)
        val tableTennisNewMatchViewModel =
            ViewModelProvider(this).get(TableTennisNewMatchViewModel::class.java)
        _binding = FragmentTableTennisNewMatchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var userToken: String = ""
        var userId: String = ""

        tableTennisNewMatchViewModel.loggedUser.observe(viewLifecycleOwner) {
            userToken = it.token!!
            userId = it.id!!
            tableTennisNewMatchViewModel.refreshPlayersFromRepository(it.token)
        }

        tableTennisNewMatchViewModel.players.observe(viewLifecycleOwner) {
            val players: MutableList<Player> = ArrayList()
            val homePlayer: MutableList<Player> = ArrayList()
            val otherPlayers: MutableList<Player> = ArrayList()

            val visitorAdapter: ArrayAdapter<Player>
            val homeAdapter: ArrayAdapter<Player>
            players.addAll(it)

            players.forEach { player ->
                if (player.id == userId) {
                    homePlayer.add(player)
                } else {
                    otherPlayers.add(player)
                }
            }

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
            homeSpinner.adapter = homeAdapter
            homeSpinner.setSelection(tableTennisNewMatchViewModel.homePlayerPosition.value!!)
            visitorSpinner.adapter = visitorAdapter
            visitorSpinner.setSelection(tableTennisNewMatchViewModel.visitorPlayerPosition.value!!)

            tableTennisNewMatchViewModel.insertSuccess.observe(viewLifecycleOwner) {
                if (it == true) {
                    //Log.i("MY_INFO", "MATCH SUCCESSFULLY INSERTED - MATCHES")
                    if (insertButtonClicked) {
                        homeSpinner.setSelection(0)
                        visitorSpinner.setSelection(0)
                        insertButtonClicked = false
                        //Log.i("MY_INFO", "MATCH EDIT DONE - GET BACK")
                        tableTennisNewMatchViewModel.resetValues()
                        val action = TableTennisNewMatchFragmentDirections.actionNewMatchDone()
                        findNavController().navigate(action)
                    }
                }
            }

            binding.newMatchViewModel = tableTennisNewMatchViewModel
            binding.lifecycleOwner = viewLifecycleOwner

            tableTennisNewMatchViewModel.message.observe(viewLifecycleOwner) {
                showResultMessage(it)
            }

            tableTennisNewMatchViewModel.homePlayerPositionChange.observe(viewLifecycleOwner) {
                tableTennisNewMatchViewModel.homePlayerPosition.value =
                    binding.homeSpinner.selectedItemPosition
            }

            tableTennisNewMatchViewModel.visitorPlayerPositionChange.observe(viewLifecycleOwner) {
                tableTennisNewMatchViewModel.visitorPlayerPosition.value =
                    binding.visitorSpinner.selectedItemPosition
            }

            binding.matchInsertButton.setOnClickListener {
                val homePlayerNotSet = binding.homeSpinner.selectedItem.equals(null)
                val visitorPlayerNotSet = binding.visitorSpinner.selectedItem.equals(null)
                val homePlayerId =
                    homeAdapter.getItem(binding.homeSpinner.selectedItemPosition)!!.id
                val visitorPlayerId =
                    visitorAdapter.getItem(binding.visitorSpinner.selectedItemPosition)!!.id


                val matchDate = tableTennisNewMatchViewModel.matchDate.value

                if (tableTennisNewMatchViewModel.homeCounter.value!!.equals(0) && tableTennisNewMatchViewModel.visitorCounter.value!!.equals(
                        0
                    )
                ) {
                    showResultMessage("Nelze uložit zápas s výsledkem 0:0")
                } else if (matchDate.equals("")) {
                    showResultMessage("Nebylo zvoleno datum zápasu")
                } else if (homePlayerNotSet && visitorPlayerNotSet) {
                    showResultMessage("Nebyli zvoleni hráči")

                } else {
                    val matchResult =
                        tableTennisNewMatchViewModel.homeCounter.value.toString() + ":" + tableTennisNewMatchViewModel.visitorCounter.value.toString()
                    val match = Match(
                        null,
                        SimpleDateFormat("d.M.yyyy").parse(matchDate.toString()).time,
                        homePlayerId,
                        null,
                        null,
                        visitorPlayerId,
                        null,
                        null,
                        matchResult
                    )
                    insertButtonClicked = true
                    tableTennisNewMatchViewModel.insertMatch(userToken, match)
                }
            }

            tableTennisNewMatchViewModel.tokenError.observe(viewLifecycleOwner) {
                showResultMessage("Chyba při ověření uživatele - přihlaste se")
                val action = TableTennisNewMatchFragmentDirections.actionLogout()
                findNavController().navigate(action)
            }

        }

        //(activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.page_table_tennis_new_match)

        binding.dateButton.setOnClickListener() {
            val picker: DatePickerDialog
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val calYear = cldr[Calendar.YEAR]
            // date picker dialog
            // date picker dialog
            picker = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    tableTennisNewMatchViewModel.matchDate.value =
                        dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
                }, //binding.matchDate!!.setText(dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year) },
                calYear,
                month,
                day
            )
            picker.show()
        }
        return root

    }

    private fun showResultMessage(apiResponseString: String) {
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        (activity as MainActivity?)!!.setActionBarTitle(getString(cz.respect.respectsports.R.string.page_table_tennis_new_match))
    }
}
