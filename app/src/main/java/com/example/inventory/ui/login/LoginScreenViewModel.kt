package com.example.inventory.ui.login
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel(itemsRepository: ItemsRepository): ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    fun signInWithEmailAndPassword(email: String, password:String, home:
        ()-> Unit)
            = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Log.d("Cita", "signInWithEmailAndPassword logueado")
                                home()
                    }
                    else{
                        Log.d("Cita", "signInWithEmailAndPassword:${task.result.toString()}")
                    }
                }
        }
        catch (ex:Exception){
            Log.d("Cita", "signInWithEmailAndPassword:${ex.message}")
        }
    }
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        home()
                    }
                    else{
                        Log.d("Cita", "createUserWithEmailAndPassword:${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }
}