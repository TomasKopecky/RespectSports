package cz.respect.respectsports.ui.tableTennis

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.databinding.FragmentTableTennisMatchesBinding
import cz.respect.respectsports.domain.Match
import java.util.*


class TableTennisMatchesFragment : Fragment() {

    private var _binding: FragmentTableTennisMatchesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tableTennisMatchesViewModel =
            ViewModelProvider(this).get(TableTennisMatchesViewModel::class.java)

        _binding = FragmentTableTennisMatchesBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.floatingActionButton3.setOnClickListener {
            val navController = Navigation.findNavController(requireView())
            navController.navigate(cz.respect.respectsports.R.id.action_new_match)
        }

        tableTennisMatchesViewModel.matchesList.observe(viewLifecycleOwner) {
            val itemArrayList: ArrayList<Match> = arrayListOf()
            for (match in it) {
                itemArrayList.add(match)
            }
            setupGridView(itemArrayList, root)
        }

        tableTennisMatchesViewModel.loggedUser.observe(viewLifecycleOwner) {
            //Log.i("MY_INFO", "LOGGGED USER OBTAINED")
            tableTennisMatchesViewModel.refreshMatchesFromRepository(it.token!!)
        }

        tableTennisMatchesViewModel.tokenError.observe(viewLifecycleOwner) {
            showResultMessage("Chyba při ověření uživatele - přihlaste se")
            val action = TableTennisMatchesFragmentDirections.actionLogout()
            findNavController().navigate(action)
        }

        tableTennisMatchesViewModel.message.observe(viewLifecycleOwner, Observer {
            showResultMessage(it)
        })

        return root
    }

    private fun setupGridView(item: ArrayList<Match>, view: View) {
        val mainAdapter =
            context?.let { ImageListAdapter(it.applicationContext, R.layout.test_list_item, item) }
        binding.myGrid.adapter = mainAdapter

        binding.myGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                Log.i("MY_INFO", "id: ${item[position].id}")
                val action =
                    TableTennisMatchesFragmentDirections.navigateToMatchDetail(item[position].id!!)
                findNavController().navigate(action)
            }
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
        (activity as MainActivity).setActionBarTitle(getString(cz.respect.respectsports.R.string.page_table_tennis))
    }
}