package no.fk.fint.employee;

import no.fk.Ansatt;

import java.util.Collection;

/**
 * Created by FSjovatsen on 01.05.2016.
 */
public class RestResponse {

    public int totalResults;
    public Collection<Ansatt> results;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Collection<Ansatt> getResults() {
        return results;
    }

    public void setResults(Collection<Ansatt> results) {
        this.results = results;
    }
}
