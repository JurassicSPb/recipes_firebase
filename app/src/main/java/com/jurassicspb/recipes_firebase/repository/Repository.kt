package com.jurassicspb.recipes_firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jurassicspb.recipes_firebase.extensions.addListener
import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.StorageLayer
import com.jurassicspb.recipes_firebase.util.FavoritesGenericTypeIndicator
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import tools.DisposableMaybeObserverAdapter

class Repository(
    private val storageLayer: StorageLayer,
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {

    private val authSubject = PublishSubject.create<AuthResult>()
    private val favoritesSubject = PublishSubject.create<FavoritesResult>()

    fun register(email: String, password: String): Completable = Completable.fromAction {
        auth.createUserWithEmailAndPassword(email, password).addListener(
            onSuccess = {
                authSubject.onNext(AuthResult(true))
            },
            onFailure = {
                authSubject.onNext(AuthResult(false, it.message))
            }
        )
    }

    fun signIn(email: String, password: String): Completable = Completable.fromAction {
        auth.signInWithEmailAndPassword(email, password).addListener(
            onSuccess = {
                authSubject.onNext(AuthResult(true))
            },
            onFailure = {
                authSubject.onNext(AuthResult(false, it.message))
            })
    }

    fun getAuthUser(): Maybe<Boolean> = Maybe.fromCallable {
        auth.currentUser != null
    }

    fun getAuthResult(): Observable<AuthResult> = authSubject

    fun favIdsResult(): Observable<FavoritesResult> = favoritesSubject

    fun saveRecipeItems(items: List<RecipeItem>): Completable = initFavorites(items)

    private fun initFavorites(items: List<RecipeItem>): Completable {
        return Completable.fromAction {
            val db = database.getReference("favorites")
            db.child(db.push().key ?: "").addListener(
                onDataChange = { it ->
                    val favIds = it.child("favorites").getValue(FavoritesGenericTypeIndicator()) ?: emptyList()
                    getRecipes(items, favIds, "")
                }, onError = { error ->
                    getRecipes(items, emptyList(), error.message)

                })
        }
    }

    private fun getRecipes(items: List<RecipeItem>, favIds: List<Long>, message: String?) {
        return storageLayer.saveRecipes(items, favIds, message)
            .andThen(storageLayer.getRecipes()
                .doOnSuccess {
                    favoritesSubject.onNext(FavoritesResult(it, message))
                })
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableMaybeObserverAdapter<List<RecipeItem>>() {})
    }

    data class AuthResult(
        var isSuccessful: Boolean = false,
        var message: String? = ""
    )

    data class FavoritesResult(
        var favorites: List<RecipeItem> = emptyList(),
        var message: String? = ""
    )
}
