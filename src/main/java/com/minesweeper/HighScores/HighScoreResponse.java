package com.minesweeper.HighScores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/*
The HighScoreResponse class is needed to handle certain REST responses that deliver JSON objects with an embedded Map.
To obtain the list of HighScores, which we are actually interested in, from the JSON object, we first need to get the
map from the _embedded property. We can then get the list by performing get on the map, using for example
beginnerHighScores as the key.

This class provides a good opportunity to utilize generics. This class can perform operations on any generic type T, as
long as T extends HighScore. This way we don't have to write separate classes for each of the high score classes.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
class HighScoreResponse<T extends HighScore> {

    @JsonProperty("_embedded")
    private Map<String, List<T>> _embedded;

    HighScoreResponse() {
    }

    Map<String, List<T>> get_Embedded() {
        return _embedded;
    }

    void set_Embedded(Map<String, List<T>> embedded) {
        this._embedded = embedded;
    }
}
