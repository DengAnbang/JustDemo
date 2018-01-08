package com.home.dab.justdemo

/**
 * Created by dab on 2018/1/8 0008 09:13
 */
class Singleton private constructor() {
    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Singleton()
        }
    }
}
