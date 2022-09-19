package hr.ferit.kristiankliskovic.projektGolf.ui.deleteuserFragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Delete
import hr.ferit.kristiankliskovic.projektGolf.databinding.DeleteuserlayoutBinding
import hr.ferit.kristiankliskovic.projektGolf.utils.myUserState

class deleteUserFragment : Fragment() {
    private lateinit var binding: DeleteuserlayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DeleteuserlayoutBinding.inflate(layoutInflater)
        binding.confirmDeleteUserButton.setOnClickListener {
            if (myUserState.myUser!!.password == binding.enterPassET.text.toString()) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Jeste li sigurni da želite obrisati svoj korisnički račun?")
                builder.setItems(arrayOf("Da", "Ne"),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) {
                            myUserState.deleteUser()
                            val action =
                                deleteUserFragmentDirections.actionDeleteUserFragmentToLoginFragment()
                            findNavController().navigate(action)
                            Toast.makeText(context,
                                "Vaš korisnički račun je obrisan",
                                Toast.LENGTH_SHORT).show()
                        }
                    })
                builder.show()
            } else {
                Toast.makeText(context, "Unesena lozinka nije ispravna", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}