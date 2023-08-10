package com.example.proyectoosiris

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.example.proyectoosiris.databinding.ActivityAuthBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SIGNIN = 100
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var binding: ActivityAuthBinding
    //Crea una instancia de FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setTheme(R.style.Theme_ProyectoOsiris)
        //Splash
        Thread.sleep(2000)//HACK:
        setTheme(R.style.Theme_ProyectoOsiris)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Envio de Eventos personalizados a google analytics
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        //Remote Config
        //aqui se añade el invervalo de tiempo que recargara de los valores de la app
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        //de esta forma se tiene la instancia general de firebase remote config
        val firebaseConfig = Firebase.remoteConfig
        //con esto ya se accede y se añade nuestra configuración
        firebaseConfig.setConfigSettingsAsync(configSettings)
        firebaseConfig.setDefaultsAsync(mapOf("show_boton_error" to false,"boton_error_text" to "Forzar error"))

        //configuración
        notification()
        setup()
        sesión()



    }


    //asegurar que en el momento que se vuelva a la pantalla autLayout si hacemos logaut se vuelva mostrar
    override fun onStart() {
        super.onStart()
        autLayout.visibility = View.VISIBLE
    }
    //Solicita los permisos de ubicación al usuario:
    private fun requestLocationPermission() {
        val REQUEST_LOCATION_PERMISSION = 0
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
    }
    //Obtiene la ubicación actual del usuario:
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Haz algo con la ubicación obtenida
            }
            .addOnFailureListener { exception: Exception ->
                // Maneja la excepción aquí
            }
    }

    private fun sesión() {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            autLayout.visibility =
                View.INVISIBLE//esto es para el caso de que exista una sesión iniciada
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun notification(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener{
        it.result?.let {
            println("este es el token del dispositivo: ${it}")
        }
        /*task: Task<String> ->
            if (!task.isSuccessful){
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("PUSH_TOKEN","Este es el token del dispositivo: $token")*/
        }
        //Temas (Topics)
        FirebaseMessaging.getInstance().subscribeToTopic("racoons")
        //Recuperar información
        val url = intent.getStringExtra("url")

        url?.let {
            println("Ha llegado informacion en una push: ${url}")
        }
    }

    private fun setup() {
        title = "Autenticación"
        binding.regButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                    } else {
                        showAlert()
                    }
                }
            }
        }
        binding.loginButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {
                    //showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        reload()
                    } else {
                        showAlert()
                    }
                }
            }
        }
        googleButton.setOnClickListener {
            //confuguración de la autenticación
            val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConfig)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGNIN)
        }
        /*Nota:  el "setOnClickListener" sirve para que en el momento que hagamos click en boton se desencadene el proceso de autenticacion */
        binding.fbButton.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult>/*es una operacion que se invoca a modo de callback en el momento que finalice la aut con fb*/{
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val token = it.accessToken

                        val credential = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                            if (it.isSuccessful) {
                                showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                            } else {
                                showAlert()
                            }
                        }
                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                    showAlert()
                }
            })
        }

    }

    private fun reload(){
        val prueba = Intent(this, MenuActivity::class.java)
        this.startActivity(prueba)
    }

    //esta funcion muestra una alerta si se produce un error al autenticar al usuario
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //función que obtiene el email y el proveedor que se ha autenticado
    private fun showHome(email: String, provider: ProviderType) {
        //
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            //se le pasan el email y el proveedor a este parametro
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        //con esta forma se realiza la navegación
        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGNIN) {
            /* nota: se puede decir que este login es en 2 partes
            primero nos autenticamos en google y despues que esa autenticacion quede reflejada en la consola de firebase
            por eso primero se hace la autenticacion y despues se le pasa una credencial de autenticación a firebase*/
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)/*esta linea de codigo puede producir un error en el caso
            de que no sea capaz de recuperar una cuenta por ello se añade todo lo de aqui en el try cach*/
                //comprueba que la cuenta que se acaba de recuperar es distinta de nulo
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert()// en caso de que salgo un error se llama a esta variable
            }
        }
    }
}