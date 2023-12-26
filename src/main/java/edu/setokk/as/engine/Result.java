package edu.setokk.as.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class Result {
	private int doc;
	private float score;
	private String title;
	private String ab;
	private String authors;
	private String pmid;
	private String volume;
	private String issue;
	private String fpage;
	private String lpage;
	private String jTitle;
	private String issn;
}
