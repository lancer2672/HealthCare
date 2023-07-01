package com.example.healthcareapp.ui.activities.auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.healthcareapp.R
import com.example.healthcareapp.data.repositories.AuthRepository
import com.example.healthcareapp.databinding.ActivitySignInBinding
import com.example.healthcareapp.ui.activities.MainActivity
import com.example.healthcareapp.viewmodels.AuthViewModel
import com.example.healthcareapp.viewmodels.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.runBlocking

class SignInActivity : AppCompatActivity() {
    private var signInViewModel: SignInViewModel? = null
    private lateinit var binding: ActivitySignInBinding
    private var gso: GoogleSignInOptions? = null
    private var gsc: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        binding.setViewmodel(signInViewModel)
        binding.loginButton.setOnClickListener { signInViewModel!!.login() }
        binding.tvForgot.setOnClickListener { show() }
        signInViewModel!!.isLoading.observe(this){

        }
        signInViewModel!!.isAuthenticated.observe(this){isAuthenticated->
            if (isAuthenticated) {
                navigateToMainActivity()
            }
        }
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso!!)
        binding.loginGGBtn.setOnClickListener { signInWithGoogle() }
    }

    fun show() {
        val bottomSheetDialog = Dialog(this)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.forgot_dialog)
        val edt = bottomSheetDialog.findViewById<EditText>(R.id.email)
        val btn = bottomSheetDialog.findViewById<ImageView>(R.id.send_forgot_email)
        btn.setOnClickListener {
            AuthRepository.getInstance().sendResetPasswordEmail(edt.text.toString())
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
        bottomSheetDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        bottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window!!.attributes.windowAnimations = R.style.BottomSheetAnimation
        bottomSheetDialog.window!!.setGravity(Gravity.CENTER)
    }
    private fun signInWithGoogle() {
        val intent = gsc!!.signInIntent
        startActivityForResult(intent, 100)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.i("ID TOKEN", account.idToken!!)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.i(
                                "LOGIN WITH EMAIL", FirebaseAuth.getInstance().currentUser!!
                                    .uid
                            )
                            runBlocking {
                                AuthViewModel.getUserFromDB(FirebaseAuth.getInstance().currentUser!!.uid){
                                    //create user if not exist
                                }
                            }
                            Toast.makeText(this@SignInActivity, "Succeeded", Toast.LENGTH_LONG)
                                .show()
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this@SignInActivity, "Error", Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: ApiException) {
                Log.e("SIGN IN WITH GOOGLE", "Error signing in with Google " + e.message, e)
                Toast.makeText(this@SignInActivity, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}