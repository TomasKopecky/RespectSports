package cz.respect.respectsports.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.respect.respectsports.databinding.FragmentGalleryBinding
import cz.respect.respectsports.domain.Match

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val matchesListView: ListView = binding.matchesList
        galleryViewModel.matchesList.observe(viewLifecycleOwner) {
        }

        val textViewNew: TextView = binding.textsHome
        galleryViewModel.matchesList.observe(viewLifecycleOwner) {
            textViewNew.text = it.toString()
        }

        galleryViewModel.message.observe(viewLifecycleOwner, Observer {
            showResultMessage(it)
        })

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