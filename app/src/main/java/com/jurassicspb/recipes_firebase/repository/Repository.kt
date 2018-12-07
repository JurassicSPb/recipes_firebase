package com.jurassicspb.recipes_firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.StorageLayer
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class Repository(
    private val storageLayer: StorageLayer,
    private val auth: FirebaseAuth
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
        return Completable.fromAction {
            storageLayer.saveRecipes(items)
        }.andThen(storageLayer.getRecipes())
    }

    data class AuthResult(
        var isSuccessful: Boolean = false,
        var message: String? = ""
    )
}
