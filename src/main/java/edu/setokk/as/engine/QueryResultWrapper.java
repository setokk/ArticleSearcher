package edu.setokk.as.engine;

import java.util.ArrayList;
import java.util.List;

public class QueryResultWrapper {
	private final long totalHits;
	private final List<Result> results;

	public QueryResultWrapper(long totalHits) {
		this.totalHits = totalHits;
		results = new ArrayList<>();
	}

	public void addResult(Result result) {
		this.results.add(result);
	}

	public long getTotalHits() {
		return totalHits;
	}

	public List<Result> getResults() {
		return this.results;
	}
}
