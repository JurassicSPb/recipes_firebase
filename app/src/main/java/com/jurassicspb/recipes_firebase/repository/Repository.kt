package com.jurassicspb.recipes_firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.StorageLayer
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class Repository(
    private val storageLayer: StorageLayer,
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {

    private val subject = PublishSubject.create<AuthResult>()

    fun register(email: String, password: String): Completable = Completable.fromAction {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener {
                subject.onNext(AuthResult(false, it.message))
            }
            .addOnSuccessListener {
                subject.onNext(AuthResult(true))
            }
    }

    fun signIn(email: String, password: String): Completable = Completable.fromAction {
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                subject.onNext(AuthResult(false, it.message))
            }
            .addOnSuccessListener {
                subject.onNext(AuthResult(true))
            }
    }

    fun getAuthUser(): Maybe<Boolean> = Maybe.fromCallable {
        auth.currentUser != null
    }

    fun getAuthResult(): Observable<AuthResult> = subject

    fun saveRecipeItems(items: List<RecipeItem>): Maybe<List<RecipeItem>> {
        return storageLayer.saveRecipes(items).andThen(storageLayer.getRecipes())
    }

    fun getFavorites(): Completable {
        return Completable.fromAction {
            val a = database.getReference("favorites")
            a.setValue("d")
//                .addValueEventListener(object : ValueEventListener{
//                    override fun onCancelled(p0: DatabaseError) {"d
//                        println("SSSSSS " + p0)
//                    }
//
//                    override fun onDataChange(p0: DataSnapshot) {
//                        println("SSSSSS2 " + p0)
//                    }
//
//                })

        }
    }

    data class AuthResult(
        var isSuccessful: Boolean = false,
        var message: String? = ""
    )
}
