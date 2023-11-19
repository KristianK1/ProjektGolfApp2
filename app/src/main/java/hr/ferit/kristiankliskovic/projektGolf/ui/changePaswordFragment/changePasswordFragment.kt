package hr.ferit.kristiankliskovic.projektGolf.ui.changePaswordFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.ferit.kristiankliskovic.projektGolf.databinding.ChangePasswordLayoutBinding
import hr.ferit.kristiankliskovic.projektGolf.utils.myUserState

class changePasswordFragment: Fragment() {
    private lateinit var binding: ChangePasswordLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChangePasswordLayoutBinding.inflate(layoutInflater)
        binding.myUsernameView.text = myUserState.myUser!!.username
        binding.changeBt.setOnClickListener {
            if(myUserState.myUser!!.password == binding.currPassET.text.toString()){
                if(binding.newPassET.text.toString() == binding.newPass2ET.text.toString()){
                    myUserState.changePassword(binding.newPassET.text.toString())
                    findNavController().navigateUp()
                }else{
                    Toast.makeText(context, "New passwords aren't equal", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Current password isn't correct", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}