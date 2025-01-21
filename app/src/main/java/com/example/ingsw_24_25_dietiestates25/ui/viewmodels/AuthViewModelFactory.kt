package com.example.ingsw_24_25_dietiestates25.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthRepository

/*
Scopo principale: Fornire un'implementazione personalizzata di ViewModelProvider.Factory
che permette di creare un'istanza del AuthViewModel, passando la dipendenza AuthRepository.

Motivazione: Android non permette di passare direttamente parametri personalizzati ai ViewModel
quando utilizza il costruttore predefinito. Questa fabbrica risolve il problema, creando il ViewModel
con i parametri necessari (in questo caso, authRepository).
 */

// Una fabbrica personalizzata per creare istanze del ViewModel AuthViewModel
class AuthViewModelFactory(
    private val authRepository: AuthRepository // Dipendenza necessaria per il ViewModel
) : ViewModelProvider.Factory {

    // Suppressione dell'avviso di cast non controllato (Unchecked Cast)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se il ViewModel richiesto è un'istanza di AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Crea un'istanza di AuthViewModel utilizzando il repository passato nel costruttore
            return AuthViewModel(authRepository) as T
        }
        // Lancia un'eccezione se il ViewModel richiesto non è AuthViewModel
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}