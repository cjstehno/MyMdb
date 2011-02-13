// Place your Spring DSL code here
beans = {

    tmdbApi(com.stehno.tmdb.TmdbApiClient){
        configPropertyService = ref('configPropertyService')
    }

    movieDataProvider(com.stehno.mymdb.fetch.TmdbMovieDataProvider){
        api = ref('tmdbApi')
    }
}