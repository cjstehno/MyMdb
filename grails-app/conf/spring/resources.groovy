// Place your Spring DSL code here
beans = {

    tmdbApi(com.stehno.tmdb.TmdbApiClient){
        configPropertyService = ref('configPropertyService')
    }

    tmdbMovieDataProvider(com.stehno.mymdb.fetch.TmdbMovieDataProvider){
        api = ref('tmdbApi')
    }

    localMovieDataProvider(com.stehno.mymdb.fetch.LocalMovieDataProvider){
        appName = "${application.metadata['app.name']}"
    }
}