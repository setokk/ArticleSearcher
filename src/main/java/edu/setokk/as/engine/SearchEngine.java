package edu.setokk.as.engine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import jodd.json.JsonParser;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SearchEngine {
    private static EnglishAnalyzer analyzer;
    private static Directory indexDir;
    public static int DEFAULT_NUMBER_OF_RESULTS = 10;

	public static void init(boolean clearIndex,
                            String dataPath,
                            String indexPath) throws IOException {

        File dataFile = new File(dataPath);
        File idxDir = new File(indexPath);

		// Set up analyzer
		SearchEngine.analyzer = new EnglishAnalyzer();


        System.out.println(idxDir.getAbsolutePath());
        System.out.println(dataFile.getAbsolutePath());
        SearchEngine.indexDir = FSDirectory.open(idxDir.toPath());

        if (clearIndex) {
            System.out.println("Rebuilding index...");

            IndexWriterConfig idxConfig = new IndexWriterConfig(SearchEngine.analyzer);
            idxConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter idxWriter = new IndexWriter(indexDir, idxConfig);

            // Now, populate the index
            int docs = 0;
            JsonParser jParser = new JsonParser();

            for (String line : Files.readAllLines(dataFile.toPath(), StandardCharsets.UTF_8)) {
                // On large amounts of data, this can take a while
                if (docs % 10000 == 0) {
                    System.out.println(docs);
                }
                docs++;

                // Parse JSON
                // Each line of the input file is a serialized JSON object
                Map j = jParser.parse(line);

                // Get title
                String title = (String) j.get("title");

                // Get description
                String ab = (String) j.get("abstract");

                // Iterate through each author object and get "surname" and "given_names"
                List<Map<String, String>> authors = (List<Map<String, String>>) j.get("authors");
                StringBuilder authorsConcat = new StringBuilder();
                String prefix = "";
                for (Map<String, String> author : authors) {
                    String surname = author.get("surname");
                    String givenNames = author.get("given_names");

                    // Concatenate surname and given names to create author's full name
                    String fullName = prefix + givenNames + " " + surname;
                    prefix = ", ";
                    authorsConcat.append(fullName);
                }

                // Get pmid
                String pmid = (String) j.get("pmid");

                // Get biblio
                Map biblio = (Map) j.get("biblio");

                // Get biblio->year
                String year = (String) biblio.get("year");
                // Get biblio->volume
                String volume = (String) biblio.get("volume");
                // Get biblio->issue
                String issue = (String) biblio.get("issue");
                // Get biblio->fpage
                String fpage = (String) biblio.get("fpage");
                // Get biblio-lpage
                String lpage = (String) biblio.get("lpage");

                // Get journal
                Map journal = (Map) biblio.get("journal");

                // Get journal->title
                String jTitle = (String) journal.get("title");
                // Get journal->issn
                String issn = (String) journal.get("issn");

                // Indexed fields
                Field tiField = new Field("title", title, TextField.TYPE_STORED);
                Field abField = new Field("abstract", ab, TextField.TYPE_STORED);

                // Not indexed fields
                StoredField auField = new StoredField("authors", authorsConcat.toString());
                StoredField pmidField = new StoredField("pmid", pmid);
                StoredField volumeField = new StoredField("volume", volume);
                StoredField issueField = new StoredField("issue", issue);
                StoredField fpageField = new StoredField("fpage", fpage);
                StoredField lpageField = new StoredField("lpage", lpage);
                StoredField jTitleField = new StoredField("jTitle", jTitle);
                StoredField issnField = new StoredField("issn", issn);


                Document thisDoc = new Document();
                thisDoc.add(tiField);
                thisDoc.add(abField);
                thisDoc.add(auField);
                thisDoc.add(pmidField);
                thisDoc.add(volumeField);
                thisDoc.add(issueField);
                thisDoc.add(fpageField);
                thisDoc.add(lpageField);
                thisDoc.add(jTitleField);
                thisDoc.add(issnField);

                idxWriter.addDocument(thisDoc);

            }

            System.out.println("Done!");
            System.out.println(docs + " documents indexed.");
            idxWriter.close();
        }
	}

    public static QueryResultWrapper search(String query, int numOfResults) throws IOException {
        if (query.equals(""))
            return null;

        // Open up the index for querying
        DirectoryReader reader = DirectoryReader.open(SearchEngine.indexDir);
        IndexSearcher searcher = new IndexSearcher(reader);

        Map<String, Float> fieldWeights = Map.of("title", (float) 0.7, "abstract", (float) 0.3);
        SimpleQueryParser qParser = new SimpleQueryParser(SearchEngine.analyzer, fieldWeights);

        Query q = qParser.parse(query);
        TopDocs topDocs = searcher.search(q, numOfResults);

        QueryResultWrapper queryResult = new QueryResultWrapper(topDocs.totalHits.value);
        for (ScoreDoc d : topDocs.scoreDocs) {
          Document res = reader.document(d.doc);

          Result result = Result.builder()
              .doc(d.doc)
              .score(d.score)
              .title(res.getField("title").stringValue())
              .ab(res.getField("abstract").stringValue())
              .authors(res.getField("authors").stringValue())
              .pmid(res.getField("pmid").stringValue())
              .volume(res.getField("volume").stringValue())
              .issue(res.getField("issue").stringValue())
              .fpage(res.getField("fpage").stringValue())
              .lpage(res.getField("lpage").stringValue())
              .jTitle(res.getField("jTitle").stringValue())
              .issn(res.getField("issn").stringValue())
              .build();

          queryResult.addResult(result);
        }

        return queryResult;
  }
}
