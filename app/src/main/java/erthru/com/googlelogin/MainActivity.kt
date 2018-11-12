package erthru.com.googlelogin

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var gClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleSignInSetup()
        btnLogin.setOnClickListener(this)
        btnLogout.setOnClickListener(this)

    }


    private fun googleSignInSetup(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gClient = GoogleSignIn.getClient(this,gso)

    }

    private fun updateUI(account:GoogleSignInAccount?){

        if(account!=null){
            userImg.visibility = View.VISIBLE
            userName.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
            btnLogout.visibility = View.VISIBLE
            userName.text = account.displayName+"\n"+account.email
            Glide.with(this).load(account.photoUrl).into(userImg)
        }

    }

    override fun onStart() {
        super.onStart()

        val user = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(user)

    }

    override fun onClick(p0: View?) {
        when(p0){

            btnLogin ->{

                val login = gClient.signInIntent
                startActivityForResult(login,1)

            }

            btnLogout ->{

                AlertDialog.Builder(this)
                    .setTitle("Confirmation ?")
                    .setMessage("Logout from this account ?")
                    .setPositiveButton("LOGOUT", { dialogInterface, i ->
                        gClient.signOut()
                        recreate()
                    }).setNegativeButton("CANCEL", { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .show()

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val user = task.getResult(ApiException::class.java)
            updateUI(user)

        }

    }

}
