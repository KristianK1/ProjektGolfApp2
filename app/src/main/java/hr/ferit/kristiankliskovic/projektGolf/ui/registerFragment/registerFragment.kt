package hr.ferit.kristiankliskovic.projektGolf.ui.registerFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.ferit.kristiankliskovic.projektGolf.databinding.RegisterlayoutBinding
import hr.ferit.kristiankliskovic.projektGolf.model.user
import hr.ferit.kristiankliskovic.projektGolf.utils.firebaseComm
import hr.ferit.kristiankliskovic.projektGolf.utils.myUserState

class registerFragment : Fragment() {

    private lateinit var binding: RegisterlayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = RegisterlayoutBinding.inflate(layoutInflater)
        binding.backToLoginBt.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.registerBt.setOnClickListener {
            register()
        }
        return binding.root
    }

    private fun register() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val password2 = binding.etPassword2.text.toString()

        firebaseComm.users.forEach {
            if (it.username == username && it.password == password) {
                Toast.makeText(
                    context,
                    "Postoji korisnik sa istim korisničkim imenom",
                    Toast.LENGTH_SHORT
                ).show()
                return;
            }
        }

        if (username.length < 5) {
            Toast.makeText(context, "Korisničko ime sadrži manje od 5 znakova", Toast.LENGTH_SHORT).show()
        } else if (password.length < 5) {
            Toast.makeText(context, "Lozinka sadrži manje od 5 znakova", Toast.LENGTH_SHORT).show()
        } else if (password != password2) {
            Toast.makeText(context, "Lozinke nisu jednake", Toast.LENGTH_SHORT).show()
        } else {
            val newUser: user = user(username, password, arrayListOf())
            firebaseComm.addUser(newUser)
            myUserState.login(newUser, true, null)
            val action = registerFragmentDirections.actionRegisterFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }
}