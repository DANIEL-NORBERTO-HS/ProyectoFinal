package com.example.proyectoosiris

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoosiris.databinding.ActivityHomeBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.android.synthetic.main.activity_home.*

//proyecto creado por Daniel norberto Hernández Santiago

//creacion de un enum
enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //config
        val bundle= intent.extras
        val email= bundle?.getString("email")
        val provider= bundle?.getString("provider")
        setup(email ?:"", provider?:"")

        //Guardado de Datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()//creación de una constante con el gestor de preferencias
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

        //Remote Config
        botonError.visibility = View.INVISIBLE
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val showErrorButton = Firebase.remoteConfig.getBoolean("show_boton_error")
                val errorButtonText = Firebase.remoteConfig.getString("boton_error_text")
                if (showErrorButton){
                    botonError.visibility = View.VISIBLE
                }
                botonError.text = errorButtonText
            }
        }
    }
    private fun setup(email: String, provider: String){
        title = "Inicio" //esto da nombre a la pantalla en la barra superior
        binding.emailTextView.text = email
        binding.providerTextView.text= provider

        binding.botonFin.setOnClickListener {

            //Borrado de Datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            if (provider == ProviderType.FACEBOOK.name){
                LoginManager.getInstance().logOut()
            }

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
        botonError.setOnClickListener {
            //Enviar información adicional
            FirebaseCrashlytics.getInstance().setUserId(email)
            FirebaseCrashlytics.getInstance().setCustomKey("provider",provider)

            //Enviar log de contexto
            FirebaseCrashlytics.getInstance().log("Se ha pulsado el botón Forzar error")

            //Forzado de error
            //throw RuntimeException("Forzado de Error")
        }
        saveB.setOnClickListener {
            db.collection("users").document(email).set(
                hashMapOf("provider" to provider, "address" to addressTextView.text.toString(),"phone" to phoneTextView.text.toString())
            )
        }
        botonGet.setOnClickListener {
            db.collection("users").document(email).get().addOnSuccessListener {
                addressTextView.setText(it.get("address")as String?)
                phoneTextView.setText(it.get("phone")as String?)
            }
        }
        botonDel.setOnClickListener {
            db.collection("users").document(email).delete()
        }
    }
}