package com.example.ingsw_24_25_dietiestates25.data.model.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Agency(
    val id: String, //id agenzia
    val name: String, //nome agenzia
    val pending: Boolean,
    val agencyEmail: String ,//email d,ell'admin dell'agenzia
    val profilePic: String? = null
)

// per controllare i dati di un agenzia bisogna
// cercare il nome dell'agenzia che è unico e ricavare il suo id
// con l'id dell'agenzia si puo cercare tutti quelli che sono collegati a tale agenzia
// vedere nella classe AgencyUser