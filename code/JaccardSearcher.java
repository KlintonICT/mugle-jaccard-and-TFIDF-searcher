//Name: Dujnapa Tanundet, Klinton Chhun, Arada Puengmongkolchaikit
//Section: 1
//ID: 6088105, 6088111, 6088133 

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaccardSearcher extends Searcher{

	public JaccardSearcher(String docFilename) {
		super(docFilename);
		/************* YOUR CODE HERE ******************/  
		for(Document document : documents) {
			List<String> docToken = document.getTokens();
			Set<String> setDoc = new HashSet<String>(); // Use set to remove duplicate words
			setDoc.addAll(docToken);
			docToken.clear();
			docToken.addAll(setDoc);
		}
		/***********************************************/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
		List<String> queryToken = tokenize(queryString);
		Set<String> setQuery = new HashSet<>(); // Use set to remove duplicate words
		setQuery.addAll(queryToken);
		queryToken.clear();
		queryToken.addAll(setQuery);
		List<SearchResult> result = new ArrayList<SearchResult>();
		for(Document document: documents) {
			List<String> docToken = document.getTokens();
			int intersect = 0, union = docToken.size();
			for(String queryTokenized: queryToken) {
				if(docToken.contains(queryTokenized)) intersect++;
				else union++;
			}
			if(union == 0) result.add(new SearchResult(document, 0.0));
			else result.add(new SearchResult(document, (double)intersect/union));
		}
		Collections.sort(result);
		return result.size() > k ? result.subList(0, k) : result;
		/***********************************************/
	}
}