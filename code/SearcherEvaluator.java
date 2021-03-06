//Name: Dujnapa Tanundet, Klinton Chhun, Arada Puengmongkolchaikit
//Section: 1
//ID: 6088105, 6088111, 6088133 

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class SearcherEvaluator {
	private List<Document> queries = null;				//List of test queries. Each query can be treated as a Document object.
	private  Map<Integer, Set<Integer>> answers = null;	//Mapping between query ID and a set of relevant document IDs
	
	public List<Document> getQueries() {
		return queries;
	}

	public Map<Integer, Set<Integer>> getAnswers() {
		return answers;
	}

	/**
	 * Load queries into "queries"
	 * Load corresponding documents into "answers"
	 * Other initialization, depending on your design.
	 * @param corpus
	 */
	public SearcherEvaluator(String corpus)
	{
		String queryFilename = corpus+"/queries.txt";
		String answerFilename = corpus+"/relevance.txt";
		
		//load queries. Treat each query as a document. 
		this.queries = Searcher.parseDocumentFromFile(queryFilename);
		this.answers = new HashMap<Integer, Set<Integer>>();
		//load answers
		try {
			List<String> lines = FileUtils.readLines(new File(answerFilename), "UTF-8");
			for(String line: lines)
			{
				line = line.trim();
				if(line.isEmpty()) continue;
				String[] parts = line.split("\\t");
				Integer qid = Integer.parseInt(parts[0]);
				String[] docIDs = parts[1].trim().split("\\s+");
				Set<Integer> relDocIDs = new HashSet<Integer>();
				for(String docID: docIDs)
				{
					relDocIDs.add(Integer.parseInt(docID));
				}
				this.answers.put(qid, relDocIDs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns an array of 3 numbers: precision, recall, F1, computed from the top *k* search results 
	 * returned from *searcher* for *query*
	 * @param query
	 * @param searcher
	 * @param k
	 * @return
	 */
	public double[] getQueryPRF(Document query, Searcher searcher, int k)
	{
		/*********************** YOUR CODE HERE *************************/
		List<SearchResult> docResult = searcher.search(query.getRawText(), k);
		Set<Integer> groundTruth = answers.get(query.getId());
		int intersect = 0;
		for(SearchResult result: docResult) {
			if(groundTruth.contains(result.getDocument().getId())) intersect++;
		}
		double[] PRF = new double[3];
		PRF[0] = (double) intersect / docResult.size();
		PRF[1] = (double) intersect / groundTruth.size();
		PRF[2] = (PRF[0] != 0.0 && PRF[1] != 0.0) ? ( 2 * PRF[0] * PRF[1] ) / ( PRF[0] + PRF[1] ) : 0.0;
		return PRF;
		/****************************************************************/
	}
	
	/**
	 * Test all the queries in *queries*, from the top *k* search results returned by *searcher*
	 * and take the average of the precision, recall, and F1. 
	 * @param searcher
	 * @param k
	 * @return
	 */
	public double[] getAveragePRF(Searcher searcher, int k)
	{
		/*********************** YOUR CODE HERE *************************/
		double[] avgPRF = new double[3];
		for(Document query: queries) {
			double[] PRF = getQueryPRF(query, searcher, k); 
			for(int i = 0; i < 3; i++) avgPRF[i] += PRF[i];
		}
		for(int i = 0; i < 3; i++) avgPRF[i] /= queries.size();
		return avgPRF;
		/****************************************************************/
	}
}