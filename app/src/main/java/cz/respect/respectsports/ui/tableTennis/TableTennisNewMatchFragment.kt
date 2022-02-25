package cz.respect.respectsports.ui.tableTennis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import com.facebook.crypto.keychain.KeyChain
import com.facebook.soloader.SoLoader
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.FragmentTableTennisNewMatchBinding
import cz.respect.respectsports.domain.Player
import cz.respect.respectsports.ui.encryption.DataEncryption


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

        Log.i("MY_INFO","USER ID: "+(activity as? MainActivity)!!.userId)
        Log.i("MY_INFO","USER TOKEN: "+(activity as? MainActivity)!!.userToken)
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

            binding.llButton?.setOnClickListener {


                /*
                SoLoader.init(activity, false)
                val keyChain: KeyChain = SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256)
                val crypto = AndroidConceal.get().createDefaultCrypto(keyChain)
                //if (crypto.isAvailable) {
                val plainText: String = "MY_TEXT" //ByteArray(1024)
                val entity = Entity.create("mytext")
                val cipherText = crypto.encrypt(plainText.toByteArray(), entity)
                val decipherText = crypto.decrypt(cipherText, entity)
                Log.i("MY_INFO", "ENCRYPTED: " + cipherText.toString())
                Log.i("MY_INFO", "DECRYPTED: " + decipherText.toString())

                 */
                //}
            }
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.page_table_tennis_new_match)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}