package cz.respect.respectsports.ui.tableTennis

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisMatchDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TableTennisMatchDetailFragment : Fragment() {

    private val args: TableTennisMatchDetailFragmentArgs by navArgs()
    private var _binding: FragmentTableTennisMatchDetailBinding? = null

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

        val tableTennisMatchDetailViewModel = MainActivity.TableTennisMatchDetailViewModelFactory(requireActivity().application,matchId).create(TableTennisMatchDetailViewModel::class.java)//,(activity as? MainActivity)!!.userId,(activity as? MainActivity)!!.userToken).create(TableTennisMatchDetailViewModel::class.java)
        //    activity?.let { MainActivity.TableTennisMatchViewModelFactory(it.application,id) }
        //ViewModelProvider(this)[TableTennisMatchViewModel::class.java]


        _binding = FragmentTableTennisMatchDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tableTennisMatchDetailViewModel.match.observe(viewLifecycleOwner) {
            binding.homePlayerName = it[0].homePlayerName
            binding.visitorPlayerName = it[0].visitorPlayerName
            binding.result =  it[0].result
            binding.date =  SimpleDateFormat("d.M.yyyy").format(it[0].date.toDouble().toLong()).toString()
        }


        tableTennisMatchDetailViewModel.message.observe(viewLifecycleOwner){
            showResultMessage(it)
        }

        tableTennisMatchDetailViewModel.loggedUser.observe(viewLifecycleOwner) {
            Log.i("MY_INFO", "LOGGGED USER OBTAINED")
            tableTennisMatchDetailViewModel.refreshMatchDetailFromRepository(it.token!!,matchId)
        }

        tableTennisMatchDetailViewModel.tokenError.observe(viewLifecycleOwner) {
            showResultMessage("Chyba při ověření uživatele - přihlaste se")
            val action = TableTennisMatchDetailFragmentDirections.actionLogout()
            findNavController().navigate(action)
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        (activity as MainActivity?)!!.setActionBarTitle(getString(R.string.page_table_tennis_detail))
    }

}