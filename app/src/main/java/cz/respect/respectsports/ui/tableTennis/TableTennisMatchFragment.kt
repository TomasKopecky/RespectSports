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
import androidx.navigation.fragment.navArgs
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisMatchBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TableTennisMatchFragment : Fragment() {

    private val args: TableTennisMatchFragmentArgs by navArgs()
    private var _binding: FragmentTableTennisMatchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val matchId = args.id

        val tableTennisMatchViewModel = MainActivity.TableTennisMatchViewModelFactory(requireActivity().application,matchId,"","").create(TableTennisMatchViewModel::class.java)
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
            binding.result = it[0].result
            //binding.detailResult.text = it[0].id
        }


        tableTennisMatchViewModel.message.observe(viewLifecycleOwner){
            showResultMessage(it)
        }



        Log.i("MY_INFO", "MATCH DETAIL - PARAMETER = $matchId")

        //tableTennisMatchViewModel.matchId = id

        //tableTennisMatchViewModel.refreshMatchDetailFromRepository(id)

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