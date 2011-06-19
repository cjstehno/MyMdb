
// Place your Spring DSL code here
beans = {

    tmdbApi(com.stehno.tmdb.TmdbApiClient){
        mymdbConfigService = ref('mymdbConfigService')
    }

    rottenApi(com.stehno.rotten.RottenTomatoesApiClient){
        mymdbConfigService = ref('mymdbConfigService')
    }

    tmdbMovieDataProvider(com.stehno.mymdb.fetch.TmdbMovieDataProvider){
        api = ref('tmdbApi')
    }

    rottenTomatoesMovieDataProvider(com.stehno.mymdb.fetch.RottenTomatoesMovieDataProvider){
        api = ref('rottenApi')
    }

    localMovieDataProvider(com.stehno.mymdb.fetch.LocalMovieDataProvider){
        appName = "${application.metadata['app.name']}"
    }

    // SECURITY

    credentialMatcher(org.apache.shiro.authc.credential.Sha512CredentialsMatcher) {
        storedCredentialsHexEncoded = true
    }
}