package no.fk.fint.employee;

import no.fk.Ansatt;

import java.util.Collection;

public class RestResponse {

    public int totalResults;
    public Collection<Ansatt> results;

    public RestResponse(int totalResults, Collection<Ansatt> results) {
        this.totalResults = totalResults;
        this.results = results;
    }

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
