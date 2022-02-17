package cz.respect.respectsports.ui.tableTennis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisMatchBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TableTennisMatchFragment : Fragment() {

    val args: TableTennisMatchFragmentArgs by navArgs()
    private var _binding: FragmentTableTennisMatchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    /*
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

         */
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = args.id

        val tableTennisMatchViewModel = MainActivity.TableTennisMatchViewModelFactory(requireActivity().application,id).create(TableTennisMatchViewModel::class.java)
        //    activity?.let { MainActivity.TableTennisMatchViewModelFactory(it.application,id) }
        //ViewModelProvider(this)[TableTennisMatchViewModel::class.java]


        _binding = FragmentTableTennisMatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.page_table_tennis_detail)


        val result: TextView = binding.detailResult
        tableTennisMatchViewModel.text.observe(viewLifecycleOwner) {
            result.text = it
        }

        tableTennisMatchViewModel.match.observe(viewLifecycleOwner) {
            Log.i("MY_INFO","MATCH GOOOOOOOOOOOOT: " + it.toString())
            //binding.detailResult.text = it[0].id
        }


        tableTennisMatchViewModel.message.observe(viewLifecycleOwner){
            showResultMessage(it)
        }



        Log.i("MY_INFO", "MATCH DETAIL - PARAMETER = $id")

        //tableTennisMatchViewModel.matchId = id

        //tableTennisMatchViewModel.refreshMatchDetailFromRepository(id)

        return root
    }
/*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MatchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TableTennisMatchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

 */

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