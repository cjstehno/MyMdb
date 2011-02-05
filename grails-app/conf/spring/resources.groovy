// Place your Spring DSL code here
beans = {

    tmdbApi(com.stehno.tmdb.TmdbApiClient){}

    movieDataProvider(com.stehno.mymdb.fetch.TmdbMovieDataProvider){
        api = ref('tmdbApi')
    }
}