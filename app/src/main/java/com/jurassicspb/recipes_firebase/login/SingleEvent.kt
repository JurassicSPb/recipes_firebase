package com.jurassicspb.recipes_firebase.login

class SingleEvent<E : Any>(private var _value: E) {

    var value: E
        get() {
            needToShow = false
            return _value
        }
        set(value) {
            needToShow = true
            _value = value
        }

    var needToShow: Boolean = false
}