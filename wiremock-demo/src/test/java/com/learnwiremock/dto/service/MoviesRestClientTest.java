package com.learnwiremock.dto.service;

import com.github.jenspiegsa.wiremockextension.ConfigureWireMock;
import com.github.jenspiegsa.wiremockextension.InjectServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.learnwiremock.constants.MovieAppConstants;
import com.learnwiremock.dto.Movie;
import com.learnwiremock.dto.service.MoviesRestClient;
import com.learnwiremock.exception.MovieErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.learnwiremock.constants.MovieAppConstants.ADD_MOVIE_V1;
import static com.learnwiremock.constants.MovieAppConstants.MOVIE_BY_NAME_QUERY_PARAM_V1;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WireMockExtension.class)
public class MoviesRestClientTest {

    @InjectServer
    WireMockServer wireMockServer;

    @ConfigureWireMock
    Options options = wireMockConfig()
            .port(8888)
            .notifier(new ConsoleNotifier(true))
            .extensions(new ResponseTemplateTransformer(true));


    MoviesRestClient moviesRestClient;
    WebClient webClient;

    @BeforeEach
    void setUp(){
        int port = wireMockServer.port();
        String baseUrl = String.format("http://localhost:%s/",port);
        System.out.println(baseUrl);
        webClient = WebClient.create(baseUrl);
        moviesRestClient = new MoviesRestClient(webClient);

    }

    @Test
    void retrieveAllMovies(){
        //given
        stubFor(get(anyUrl())
                .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("all-movie.json")));
        //when
        List<Movie> movieList = moviesRestClient.retrieveAllMovies();
        //then
        assertTrue(movieList.size()> 0 );
    }

    @Test
    void retrieveAllMovies_matchesUrl(){
        //given
        stubFor(get(urlPathEqualTo(MovieAppConstants.GET_ALL_MOVIES_V1))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("all-movie.json")));
        //when
        List<Movie> movieList = moviesRestClient.retrieveAllMovies();
        assertTrue(movieList.size()> 0 );
    }
    @Test
    void retrieveMovieById(){
        //given
        stubFor(get(urlPathMatching("/movieservice/v1/movie/[0-9]"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("movie.json")));
        Integer movieId = 9;
        //when
        Movie movie = moviesRestClient.retrieveMovieById(movieId);
        //then
        assertNotNull(movie);
    }

    @Test
    void retrieveMovieById_responseTemplating(){
        //given
        stubFor(get(urlPathMatching("/movieservice/v1/movie/[0-9]"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("movie-template.json")));
        Integer movieId = 9;
        //when
        Movie movie = moviesRestClient.retrieveMovieById(movieId);
        //then
        assertEquals(9,movie.getMovie_id().intValue());
    }
    @Test
    void retrieveMovieById_notFound(){
        stubFor(get(urlPathMatching("/movieservice/v1/movie/[0-9]+"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("404-movieid.json")));
        Integer movieId = 100;
        Assertions.assertThrows(MovieErrorResponse.class,() ->
                moviesRestClient.retrieveMovieById(movieId));
    }
    @Test
    void retrieveMovieByName(){
        String movieName = "Avengers";
        stubFor(get(urlEqualTo(MOVIE_BY_NAME_QUERY_PARAM_V1+"?movie_name="+movieName))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("Avengers.json")));
        List<Movie> movies = moviesRestClient.retrieveMovieByName(movieName);
        String cast = "Robert Downey Jr, Chris Evans , Chris HemsWorth";
        assertEquals(cast,movies.get(0).getCast());
    }

    @Test
    void retrieveMovieByName_responseTemplating(){
        String movieName = "Avengers";
        stubFor(get(urlEqualTo(MOVIE_BY_NAME_QUERY_PARAM_V1+"?movie_name="+movieName))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("movie-byName-template.json")));
        List<Movie> movies = moviesRestClient.retrieveMovieByName(movieName);
        String cast = "Robert Downey Jr, Chris Evans , Chris HemsWorth";
        assertEquals(cast,movies.get(0).getCast());
    }

    @Test
    void retrieveMovieByName_approach2(){
        String movieName = "Avengers";
        stubFor(get(urlPathEqualTo(MOVIE_BY_NAME_QUERY_PARAM_V1))
                .withQueryParam("movie_name",equalTo(movieName))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("Avengers.json")));
        List<Movie> movies = moviesRestClient.retrieveMovieByName(movieName);
        String cast = "Robert Downey Jr, Chris Evans , Chris HemsWorth";
        assertEquals(cast,movies.get(0).getCast());
    }

    @Test
    void retrieveMovieByName_Not_Found(){
        String name = "The Nishtha";
        Assertions.assertThrows(MovieErrorResponse.class,() ->
                moviesRestClient.retrieveMovieByName(name));
    }
    @Test
    void retrieveMovieByYear(){
        String movieYear = "2012";
        List<Movie> movies = moviesRestClient.retrieveMovieByYear(movieYear);
        assertTrue(movies.size()>0);
    }

    @Test
    void retrieveMovieByYear_Not_Found(){
        String movieYear = "1950";
        Assertions.assertThrows(MovieErrorResponse.class,() ->
                moviesRestClient.retrieveMovieByYear(movieYear));
    }
    @Test
    void addMovie(){
        stubFor(post(urlEqualTo(ADD_MOVIE_V1))
                .withRequestBody(matchingJsonPath("$.name",equalTo("Toys Story")))
                .withRequestBody(matchingJsonPath("$.cast",containing("Tom")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("add-movie.json")));
        Movie movie = new Movie("Tom Hanks",null,"Toys Story",
                LocalDate.of(2019,06,20),2019);
        Movie movie1 = moviesRestClient.addMovie(movie);
        Assertions.assertTrue(movie1.getMovie_id()!=null);
    }

    @Test
    void addMovie_responseTemplating(){
        stubFor(post(urlEqualTo(ADD_MOVIE_V1))
                .withRequestBody(matchingJsonPath("$.name",equalTo("Toys Story")))
                .withRequestBody(matchingJsonPath("$.cast",containing("Tom")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("add-movie-template.json")));
        Movie movie = new Movie("Tom Hanks",null,"Toys Story",
                LocalDate.of(2019,06,20),2019);
        Movie movie1 = moviesRestClient.addMovie(movie);
        Assertions.assertTrue(movie1.getMovie_id()!=null);
    }

    @Test
    void addMovie_badRequest(){
        stubFor(post(urlEqualTo(ADD_MOVIE_V1))
                .withRequestBody(matchingJsonPath("$.cast",containing("Tom")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("400-invalid-input.json")));
        Movie movie = new Movie("Tom Hanks",null,null,
                LocalDate.of(2019,06,20),2019);
        String expectedErrorMessage = "Please pass all the input fields : [name]";
        Assertions.assertThrows(MovieErrorResponse.class,() ->
                moviesRestClient.addMovie(movie),expectedErrorMessage);
    }

    @Test
    void updateMovie(){
        Integer movieId = 3;
        String cast = "ABC";
        stubFor(put(urlPathMatching("/movieservice/v1/movie/[0-9]+"))
                .withRequestBody(matchingJsonPath("$.cast",containing("ABC")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("update-movie-template.json")));

        Movie movie = new Movie(cast,null,null,
                null,null);
        Movie updateMovie = moviesRestClient.updateMovie(movieId,movie);
        assertTrue(updateMovie.getCast().contains(cast));

    }

    @Test
    void updateMovie_Not_Found(){
        Integer movieId = 30;
        String cast = "ABC";
        stubFor(put(urlPathMatching("/movieservice/v1/movie/[0-9]+"))
                .withRequestBody(matchingJsonPath("$.cast",containing("ABC")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        Movie movie = new Movie(cast,null,null,
                null,null);
        Assertions.assertThrows(MovieErrorResponse.class,() ->
                moviesRestClient.updateMovie(movieId,movie));
    }
    @Test
    void deleteMovie(){
        Movie movie = new Movie("Toys Story 5",null,"Tom Hanks",
                LocalDate.of(2019,06,20),2012);

        stubFor(post(urlEqualTo(ADD_MOVIE_V1))
                .withRequestBody(matchingJsonPath("$.name",equalTo("Tom Hanks")))
                .withRequestBody(matchingJsonPath("$.cast",containing("Toys")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("add-for-delete-movie-template.json")));

        Movie addMovie = moviesRestClient.addMovie(movie);
        String expectedErrorResponse = "Movie Deleted Successfully";
        stubFor(delete(urlPathMatching("/movieservice/v1/movie/[0-9]+"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedErrorResponse)));

        String responseMessage = moviesRestClient.deleteMovie(addMovie.getMovie_id().intValue());

        assertEquals(expectedErrorResponse,responseMessage);
    }

    @Test
    void deleteMovie_Not_Found(){
        Integer id = 100;
        stubFor(delete(urlPathMatching("/movieservice/v1/movie/[0-9]+"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        Assertions.assertThrows(MovieErrorResponse.class,()-> moviesRestClient.deleteMovie(id));

    }

    @Test
    void deleteMovieByName(){
        Movie movie = new Movie("Toys Story 5",null,"Tom Hanks",
                LocalDate.of(2019,06,20),2012);

        stubFor(post(urlEqualTo(ADD_MOVIE_V1))
                .withRequestBody(matchingJsonPath("$.name",equalTo("Tom Hanks")))
                .withRequestBody(matchingJsonPath("$.cast",containing("Toys")))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("add-for-delete-movie-template.json")));

        Movie addMovie = moviesRestClient.addMovie(movie);
        String expectedErrorResponse = "Movie Deleted Successfully";
        stubFor(delete(urlEqualTo(MOVIE_BY_NAME_QUERY_PARAM_V1+"?movie_name=Tom%20Hanks"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedErrorResponse)));

        String responseMessage = moviesRestClient.deleteMovieByName(addMovie.getName());

        assertEquals(expectedErrorResponse,responseMessage);
        verify(postRequestedFor(urlEqualTo(ADD_MOVIE_V1))
                .withRequestBody(matchingJsonPath("$.name",equalTo("Tom Hanks")))
                .withRequestBody(matchingJsonPath("$.cast",containing("Toys"))));

        verify(deleteRequestedFor(urlEqualTo(MOVIE_BY_NAME_QUERY_PARAM_V1+"?movie_name=Tom%20Hanks")));

    }


}
