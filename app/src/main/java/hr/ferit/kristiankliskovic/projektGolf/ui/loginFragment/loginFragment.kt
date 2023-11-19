package hr.ferit.kristiankliskovic.projektGolf.ui.loginFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import hr.ferit.kristiankliskovic.projektGolf.databinding.LoginlayoutBinding
import hr.ferit.kristiankliskovic.projektGolf.model.user
import hr.ferit.kristiankliskovic.projektGolf.utils.genericListener
import hr.ferit.kristiankliskovic.projektGolf.utils.firebaseComm
import hr.ferit.kristiankliskovic.projektGolf.utils.myUserState

class loginFragment : Fragment() {

    private lateinit var binding: LoginlayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = LoginlayoutBinding.inflate(layoutInflater)
        binding.loginBt.setOnClickListener {
            this.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }
        binding.registerInsteadBt.setOnClickListener {
            val action = loginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        val firebaseReadyObserver = Observer<Boolean>{
            Log.i("usersList", "obsUpdate")
            if(it == true){
                Log.i("usersList", "obsUpdate2")
                val listener: genericListener =
                    object: genericListener {
                        override fun callEnded() {
                            Log.i("autoLoginDone", "now")
                            binding.progressBar.visibility = View.GONE
                            if(myUserState.myUser != null){
                                try{
                                    val action = loginFragmentDirections.actionLoginFragmentToMainFragment()
                                    findNavController().navigate(action)
                                } catch (e: Throwable){}
                            }
                        }
                    }
                myUserState.autoLogin(listener)
            }
        }
        firebaseComm.firebaseReady.observe(viewLifecycleOwner, firebaseReadyObserver)
        return binding.root
    }

    private fun login(username: String, password: String) {
        val myUser = user(username, password, arrayListOf())
        val userInBase = firebaseComm.checkCreds(myUser)
        if (userInBase == null) {
            Toast.makeText(context, "Incorrect login data", Toast.LENGTH_SHORT).show()
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        val loggedInListener: genericListener =
            object: genericListener {
                override fun callEnded() {
                    val action = loginFragmentDirections.actionLoginFragmentToMainFragment()
                    findNavController().navigate(action)
                }
            }
        Log.i("userInBase", Gson().toJson(userInBase))
        myUserState.login(userInBase, true, loggedInListener)
    }

}