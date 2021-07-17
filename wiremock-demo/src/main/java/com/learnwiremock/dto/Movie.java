package com.learnwiremock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private String cast;
    private Long movie_id;
    private String name;
    private LocalDate release_date;
    private Integer year;
}

