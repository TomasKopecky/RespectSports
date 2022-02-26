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
import androidx.navigation.fragment.findNavController
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisNewMatchBinding
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.domain.Player
import java.util.*


class TableTennisNewMatchFragment : Fragment() {

    private var _binding: FragmentTableTennisNewMatchBinding? = null
    private val binding get() = _binding!!
    private var insertButtonClicked:Boolean = false
    private var homeScore: Int = 0
    private var visitorScore: Int = 0

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

        binding.homeScore = homeScore.toString()
        binding.visitorScore = visitorScore.toString()

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

            tableTennisNewMatchViewModel.insertSuccess.observe(viewLifecycleOwner) {
                if (it == true) {
                    Log.i("MY_INFO", "MATCH SUCCESSFULLY INSERTED - MATCHES")
                    if (insertButtonClicked) {
                        insertButtonClicked = false
                        Log.i("MY_INFO", "MATCH EDIT DONE - GET BACK")
                        val action = TableTennisNewMatchFragmentDirections.actionNewMatchDone()
                        findNavController().navigate(action)
                    }
                }
            }

            binding.homePlusButton!!.setOnClickListener() {
                homeScore++
                binding.homeScore = homeScore.toString()
            }

            binding.homeMinusButton!!.setOnClickListener() {
                if (homeScore > 0) {
                    homeScore--
                    binding.homeScore = homeScore.toString()
                }
            }

            binding.visitorPlusButton!!.setOnClickListener() {
                visitorScore++
                binding.visitorScore = visitorScore.toString()
            }

            binding.visitorMinusButton!!.setOnClickListener() {
                if (visitorScore > 0) {
                    visitorScore--
                    binding.visitorScore = visitorScore.toString()
                }
            }

            binding.matchInsertButton!!.setOnClickListener {
                val homePlayerNotSet = binding.homeSpinner!!.selectedItem.equals(null)
                val visitorPlayerNotSet = binding.visitorSpinner!!.selectedItem.equals(null)
                val homePlayerId = homeAdapter.getItem(binding.homeSpinner!!.selectedItemPosition)!!.id
                val visitorPlayerId = visitorAdapter.getItem(binding.visitorSpinner!!.selectedItemPosition)!!.id


                val matchDate = binding.matchDate!!.text

                Log.i("MY_INFO", "daTE: " + matchDate)

                if (homeScore == 0 && visitorScore == 0) {
                    showResultMessage("Nelze uložit zápas s výsledkem 0:0")
                }

                else if (matchDate == "") {
                    showResultMessage("Nebylo zvoleno datum zápasu")
                }

                else if (homePlayerNotSet && visitorPlayerNotSet) {
                    showResultMessage("Nebyli zvoleni hráči")

                    }
                else {
                    val matchResult = homeScore.toString()+":"+visitorScore.toString()
                    val match = Match(null, SimpleDateFormat("d.M.yyyy").parse(matchDate.toString()).time, homePlayerId, null, null, visitorPlayerId, null,null, matchResult)
                    insertButtonClicked = true
                    tableTennisNewMatchViewModel.insertMatch(match)
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

        binding.dateButton!!.setOnClickListener() {
            val picker: DatePickerDialog
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            // date picker dialog
            // date picker dialog
            picker = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth -> binding.matchDate!!.setText(dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year) },
                year,
                month,
                day
            )
            picker.show()
        }
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