package app.paradigmatic.paradigmaticapp.data.supabase


import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val PUBLIC_SUPABASE_URL = ""
    private const val PUBLIC_SUPABASE_ANON_KEY = ""

    val client = createSupabaseClient(
        supabaseUrl = PUBLIC_SUPABASE_URL,
        supabaseKey = PUBLIC_SUPABASE_ANON_KEY
    ) {
        install(GoTrue)
        install(Postgrest)
    }
}