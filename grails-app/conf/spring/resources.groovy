// Place your Spring DSL code here
beans = {

    tmdbApi(com.stehno.tmdb.TmdbApiClient){
        mymdbConfigService = ref('mymdbConfigService')
    }

    tmdbMovieDataProvider(com.stehno.mymdb.fetch.TmdbMovieDataProvider){
        api = ref('tmdbApi')
    }

    localMovieDataProvider(com.stehno.mymdb.fetch.LocalMovieDataProvider){
        appName = "${application.metadata['app.name']}"
    }
}