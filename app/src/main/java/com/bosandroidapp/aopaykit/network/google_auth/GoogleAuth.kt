package com.bosandroidapp.aopaykit.network.google_auth

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleAuth {
    companion object {
        private lateinit var googleSignInClient: GoogleSignInClient
        private val RC_SIGN_IN = 1001

        fun initialize(activity: AppCompatActivity) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()

            googleSignInClient = GoogleSignIn.getClient(activity, gso)
        }

        fun onActivityResult(requestCode: Int, data: Intent?): String? {

            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    val details = StringBuilder()
                    details.append("ID: ${account?.id}\n")
                    details.append("Email: ${account?.email}\n")
                    Log.d("GoogleAuth", details.toString())
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleSignInClient.revokeAccess()
                    }
                    return details.toString()
                } catch (e: ApiException) {
                    return "Sign-in failed: ${e.localizedMessage}"
                }
            }
            return null
        }

        fun AppCompatActivity.startActivityForAuth() {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
}