//Name: Dujnapa Tanundet, Klinton Chhun, Arada Puengmongkolchaikit
//Section: 1
//ID: 6088105, 6088111, 6088133

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFSearcher extends Searcher
{	
	Map<Document, Map<String, Double>> docWeight = new HashMap<Document, Map<String, Double>>(); // store document -> term, weight
	Map<Document, Double> docMagnitude = new HashMap<Document, Double>(); // store document magnitude
	Map<String , Double> idf = new HashMap<String, Double>();
	
	public TFIDFSearcher(String docFilename) {
		super(docFilename);
		/************* YOUR CODE HERE ******************/
		Map<String, Integer> termFreqAll = new HashMap<String, Integer>(); // store term frequency from all documents
		Map<Document, Map<String, Integer>> vectorDoc = new HashMap<Document, Map<String, Integer>>(); // store document -> term, term frequency
		for(Document document: documents) {
			Map<String, Integer> termFreq = new HashMap<String, Integer>(); // store term frequency of each document
			List<String> allTokens = document.getTokens();
			for(String term: allTokens) {
				if(!termFreq.containsKey(term)) {
					termFreq.put(term, 1);
					if(!termFreqAll.containsKey(term)) termFreqAll.put(term, 1); 
					else termFreqAll.put(term, termFreqAll.get(term)+1);
				}
				else termFreq.put(term, termFreq.get(term)+1);
			}
			vectorDoc.put(document, termFreq);
		}
		for(String term: termFreqAll.keySet()) idf.put(term, Math.log10(1.0 + (double)documents.size() / termFreqAll.get(term))); 
		for(Document document: vectorDoc.keySet()) {
			double docMag = 0.0;
			Map<String, Double> weightTemp = new HashMap<String, Double>();
			for(String term: vectorDoc.get(document).keySet()) {
				double weight = ( 1.0 + Math.log10((double)vectorDoc.get(document).get(term)) ) * idf.get(term);
				docMag += Math.pow(weight, 2);
				weightTemp.put(term, weight);
			}
			docWeight.put(document, weightTemp);
			docMagnitude.put(document, Math.sqrt(docMag));
		}
		/***********************************************/
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
		List<String> queryToken = tokenize(queryString);
		List<SearchResult> result = new ArrayList<SearchResult>();
		Map<String, Integer> queryTermFreq = new HashMap<String, Integer>(); // store term and its frequency
		Map<String, Double> queryWeight = new HashMap<String, Double>(); // store term and its weight
		for(String term: queryToken) {
			if(!queryTermFreq.containsKey(term)) queryTermFreq.put(term, 1); 
			else queryTermFreq.put(term, queryTermFreq.get(term)+1);
		}
		double queryMagnitude = 0.0;
		for(String term: queryTermFreq.keySet()) {
			if(!idf.containsKey(term)) continue;
			double weight = ( 1.0 + Math.log10((double)queryTermFreq.get(term)) ) * idf.get(term);
			queryMagnitude += Math.pow(weight, 2);
			queryWeight.put(term, weight);
		}
		queryMagnitude = Math.sqrt(queryMagnitude);
		for(Document document: documents) {
			double cosine = 0.0;
			for(String term: queryWeight.keySet()) {
				if(docWeight.get(document).containsKey(term)) cosine += docWeight.get(document).get(term) * queryWeight.get(term);
			}
			result.add(new SearchResult(document, cosine / (queryMagnitude * docMagnitude.get(document))));
		}
		Collections.sort(result);
		return result.size() > k ? result.subList(0, k) : result;
		/***********************************************/
	}
}