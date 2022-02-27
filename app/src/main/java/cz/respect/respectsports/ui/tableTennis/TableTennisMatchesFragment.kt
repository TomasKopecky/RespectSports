package cz.respect.respectsports.ui.tableTennis

import android.R
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.databinding.FragmentTableTennisMatchesBinding
import cz.respect.respectsports.domain.Match
import java.util.*
import kotlin.collections.ArrayList


class TableTennisMatchesFragment : Fragment() {

    private var _binding: FragmentTableTennisMatchesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var gridView: GridView
    private val playerNames : Array<String>
        get() = arrayOf("Cristiano Ronaldo", "Joao Felix", "Bernado Silva", "Andre Silve", "Bruno Fernandez", "William Carvalho", "Nelson Semedo", "Pepe", "Rui Patricio")
            private var playerImages = intArrayOf(R.drawable.btn_minus, R.drawable.btn_minus, R.drawable.btn_minus,
        R.drawable.btn_minus,
        R.drawable.btn_minus, R.drawable.btn_minus, R.drawable.btn_minus, R.drawable.btn_minus, R.drawable.btn_minus)

    private val itemArrayList: ArrayList<String> = arrayListOf("asdlfjk")

    private val itemList: Array<String>
        get() = arrayOf(
            "Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5",
            "Item 6",
            "Item 7",
            "Item 8",
            "Item 9",
            "Item 10",
            "Item 11",
            "Item 12",
            "Item 13",
            "Item 14",
            "Item 15",
            "Item 16",
            "Item 17",
            "Item 18",
            "Item 19",
            "Item 20",
            "Item 21",
            "Item 22"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tableTennisMatchesViewModel = ViewModelProvider(this).get(TableTennisMatchesViewModel::class.java)

        _binding = FragmentTableTennisMatchesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        tableTennisMatchesViewModel.loginResult.observe(viewLifecycleOwner) {
            Log.i("MY_INFO", "LOGIN RECEIVED")
            tableTennisMatchesViewModel.getMatches()
        }

         */

        binding.floatingActionButton3.setOnClickListener {
            val navController = Navigation.findNavController(requireView())
            navController.navigate(cz.respect.respectsports.R.id.action_new_match)
        }

        /*
        // preventing to get back to new match insert fragment
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    //Log.i("MY_INFO", "BACK PRESSED iN MATCHES")

                    findNavController().navigate(cz.respect.respectsports.R.id.nav_home)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

         */

        //val textViewNew: TextView = binding.textsHome
        tableTennisMatchesViewModel.matchesList.observe(viewLifecycleOwner) {
            //textViewNew.text = it.toString()
                //val itemArrayList: ArrayList<String> = arrayListOf()
                val itemArrayList: ArrayList<Match> = arrayListOf()
                // val matchesArray: List<Match> = arrayListOf(it)
                for (match in it) {
                    itemArrayList.add(match)
                }
                //jj = it.toList()
                setupGridView(itemArrayList, root)
        }

        tableTennisMatchesViewModel.loggedUser.observe(viewLifecycleOwner) {
            //Log.i("MY_INFO", "LOGGGED USER OBTAINED")
            tableTennisMatchesViewModel.refreshMatchesFromRepository(it.token!!)
        }

        tableTennisMatchesViewModel.tokenError.observe(viewLifecycleOwner) {
            showResultMessage("Chyba při ověření uživatele - přihlaste se")
            /*
            val newFragment: Fragment = LogoutFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(cz.respect.respectsports.R.id.table_tennis_match, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()

             */
            //val action = TableTennisFragmentDirections.navigateToMatchDetail()
            //findNavController().navigate(action)
            val action = TableTennisMatchesFragmentDirections.actionLogout()
            findNavController().navigate(action)
        }

        tableTennisMatchesViewModel.message.observe(viewLifecycleOwner, Observer {
            showResultMessage(it)
        })

        //(activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(cz.respect.respectsports.R.string.page_table_tennis)

        return root
    }

    private fun setupGridView(item: ArrayList<Match>, view: View) {
        val mainAdapter = context?.let { ImageListAdapter(it.applicationContext,  R.layout.test_list_item, item) }
        //val adapter = activity?.let { ImageListAdapter(it.applicationContext, R.layout.test_list_item, itemList) }
        binding.myGrid.adapter = mainAdapter
        //binding.myGrid.adapter = mainAdapter

        binding.myGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                Log.i("MY_INFO", "id: ${item[position].id}")
                val action = TableTennisMatchesFragmentDirections.navigateToMatchDetail(item[position].id!!)
                findNavController().navigate(action)
                /*
                val matchDetailViewModel =
                    ViewModelProvider(this).get(TableTennisMatchViewModel::class.java)
*/
                /*
                val someFragment: Fragment = TableTennisMatchFragment()
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.replace(
                    R.id.,
                    someFragment
                ) // give your fragment container id in first parameter

                transaction.addToBackStack(null) // if written, this transaction will be added to backstack

                transaction.commit()

                 */

                /*
                Toast.makeText(
                    activity, " Clicked Position: " + (position + 1),
                    Toast.LENGTH_SHORT
                ).show()

                 */


            }
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