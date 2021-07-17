package com.learnwiremock.dto.service;

import com.github.jenspiegsa.wiremockextension.ConfigureWireMock;
import com.github.jenspiegsa.wiremockextension.InjectServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import com.learnwiremock.constants.MovieAppConstants;
import com.learnwiremock.dto.Movie;
import com.learnwiremock.exception.MovieErrorResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class MoviesRestClient {

    private WebClient webClient;

    public MoviesRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Movie> retrieveAllMovies(){
        //http://desktop-nh0ihci:8081/movieservice/v1/allMovies

        return webClient.get().uri(MovieAppConstants.GET_ALL_MOVIES_V1)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();

    }

    public Movie retrieveMovieById(Integer movie_id){
        try {
            return webClient.get().uri(MovieAppConstants.MOVIE_BY_ID_PATH_PARAM_V1,movie_id)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }
    }

    public List<Movie> retrieveMovieByName(String name){
        String retrieveByNameUri = UriComponentsBuilder.fromUriString(MovieAppConstants.MOVIE_BY_NAME_QUERY_PARAM_V1)
                .queryParam("movie_name",name)
                .buildAndExpand()
                .toUriString();
        try {
        return webClient.get().uri(retrieveByNameUri)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }
    }

    public List<Movie> retrieveMovieByYear(String movieYear){
        String retrieveByNameUri = UriComponentsBuilder.fromUriString(MovieAppConstants.MOVIE_BY_YEAR_QUERY_PARAM_V1)
                .queryParam("year",movieYear)
                .buildAndExpand()
                .toUriString();
        try {
            return webClient.get().uri(retrieveByNameUri)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }
    }

    public Movie addMovie(Movie movie){
        try {
        return webClient.post().uri(MovieAppConstants.ADD_MOVIE_V1)
                .syncBody(movie)
                .retrieve()
                .bodyToMono(Movie.class)
                .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }
    }

    public Movie updateMovie(Integer movieId,Movie movie){
        try {
        return webClient.put().uri(MovieAppConstants.MOVIE_BY_ID_PATH_PARAM_V1,movieId)
                .syncBody(movie)
                .retrieve()
                .bodyToMono(Movie.class)
                .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }

    }
    public String deleteMovie(Integer movieId){
        try {
        return webClient.delete().uri(MovieAppConstants.MOVIE_BY_ID_PATH_PARAM_V1,movieId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }
    }

    public String deleteMovieByName(String movieName){
        try {
            String deleteMovieByNameURI = UriComponentsBuilder.fromUriString(MovieAppConstants.MOVIE_BY_NAME_QUERY_PARAM_V1)
                    .queryParam("movie_name",movieName)
                    .buildAndExpand()
                    .toUriString();
             webClient.delete().uri(deleteMovieByNameURI)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }catch (WebClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
            throw new MovieErrorResponse(ex.getStatusText(),ex);

        }catch (Exception ex){
            throw new MovieErrorResponse(ex);

        }
        return "Movie Deleted Successfully";
    }

}
