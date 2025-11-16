package com.dalima.paisawise.repository

import com.dalima.paisawise.data.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun signUpWithEmail(name: String, email: String, password: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("No UID"))

            // Set Firebase Authentication displayName
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            result.user?.updateProfile(profileUpdates)?.await()

            // SAVE TO FIRESTORE
            val user = User(uid, name, email)
            firestore.collection("users").document(uid).set(user).await()

            println("🔥 SAVED USER TO FIRESTORE: $user")

            Result.success(Unit)

        } catch (e: Exception) {
            println("❌ FIRESTORE SAVE FAILED: ${e.message}")
            Result.failure(e)
        }
    }


    suspend fun signInWithEmail(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(credential: AuthCredential): Result<Unit> {
        return try {
            val result = auth.signInWithCredential(credential).await()
            val user = result.user ?: return Result.failure(Exception("No User Found"))
            val userInfo = User(user.uid, user.displayName ?: "", user.email ?: "")
            firestore.collection("users").document(user.uid).set(userInfo).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
    suspend fun getUserProfile(uid: String): User? {
        return try {
            firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
    fun listenToUserProfile(uid: String, onDataChange: (User?) -> Unit) {
        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onDataChange(null)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                onDataChange(user)
            }
    }


    fun getCurrentUser() = auth.currentUser
}