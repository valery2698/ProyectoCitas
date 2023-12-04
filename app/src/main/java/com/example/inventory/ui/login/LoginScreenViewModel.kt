package com.example.inventory.ui.login
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.R
import com.example.inventory.data.ItemsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel(itemsRepository: ItemsRepository): ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    fun signInWithEmailAndPassword(email: String, password: String, context: Context, home: () -> Unit) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            Log.d("Cita", "Usuario logueado correctamente")
                            home()
                        } else {
                            Log.w("Cita", "Falló el logueo con el usuario ${email}", task.exception)
                            Toast.makeText(
                                context,
                                R.string.credenciales_incorrectos,
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    } catch (e: Exception) {
                        Log.e("Cita", "Error en addOnCompleteListener: ${e.message}", e)
                    }
                }
        } catch (ex: Exception) {
            Log.e("Cita", "Error en el metodo: signInWithEmailAndPassword: ${ex.message}", ex)
        }
    }
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        home: () -> Unit
    ){
        if (_loading.value == false) {
            if (password.length >= 6) { // Verificar que la contraseña tenga al menos 6 caracteres
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            home()
                        } else {
                            when (val exception = task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> {
                                    // Manejar la excepción de credenciales inválidas
                                    Toast.makeText(
                                        context,
                                        R.string.invalid_username,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    Log.d(
                                        "Cita",
                                        "createUserWithEmailAndPassword: ${exception?.message}"
                                    )
                                    Toast.makeText(
                                        context,
                                        R.string.register_failed,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        _loading.value = false
                    }
            } else {
                Toast.makeText(
                    context,
                    R.string.invalid_password,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
    fun SignOut()
    {
        try {
            auth.signOut()
        }catch (e: Exception)
        {
            Log.e("Citas", "Error en SignOut: ${e.message}", e)
        }
    }
}