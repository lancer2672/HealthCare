package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.models.MeditationModel
import com.example.healthcareapp.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MeditationRepository {
    private val  db : FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
        var instance: MeditationRepository? = null
            get() {
                if (field == null) {
                    field = MeditationRepository()
                }
                return field
            }
            private set
    }
    suspend fun getMeditationAudios(onSuccess: (List<MeditationModel>) -> Unit) {
        val query = db.collection("meditation_audio")
        try {
            val snapshot = query.get().await()
            val meditationAudios = snapshot.documents.mapNotNull { it.toObject(MeditationModel::class.java) }
            onSuccess(meditationAudios)
        } catch (e: Exception) {
            Log.w("MEDITATION_AUDIO", "Error getting meditation audios", e)
        }
    }
}