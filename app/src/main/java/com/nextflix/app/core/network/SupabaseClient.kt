package com.nextflix.app.core.network

import com.nextflix.app.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.supabaseUrl,
        supabaseKey = BuildConfig.supabaseKey
    ) {
        //install(GoTrue)
        install(Postgrest)
        install(Auth)
//        install(ComposeAuth){
//            googleNativeLogin(serverClientId = BuildConfig.googleClientId)
//        }
        //install(Realtime)
    }
}