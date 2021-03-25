import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Class containing a set of test-cases.
 * @author Dr. Suppawong Tuarob (copyrighted)
 *
 */
public class KProducer {
	
	//*********************** DO NOT MODIFY THESE VARIABLES *****************************//
	public static final String testCorpus = "./data/lisa";
	public static int k = 1;
	public static List<Double> PJ, RJ, F1J, PT, RT, F1T;
	public static final String[] testQueries = new String[] {
			""
			,"Information Retrieval"
			,"Machine Learning"
			,"Deep Learning"
			,"I AM INTERESTED IN INFORMATION ON THE PROVISION OF CURRENT AWARENESS BULLETINS, ESPECIALLY SDI SERVICES IN ANY INSTITUTION, E.G. ACADEMIC LIBRARIES, INDUSTRY, AND IN ANY SUBJECT FIELD. SDI, SELECTIVE DISSEMINATION OF INFORMATION, CURRENT AWARENESS BULLETINS, INFORMATION BULLETINS."
			,"THE WHITE HOUSE CONFERENCE ON LIBRARY AND INFORMATION SERVICES, 1979. SUMMARY, MARCH 1980. FOR AN ABSTRACT OF THIS REPORT SEE 81/795. REPORT NOT AVAILABLE FROM NTIS."
	};
	
	//*********************** DO NOT MODIFY THIS METHOD *****************************//
	public static void getAllK(String corpus)
	{
		SearcherEvaluator s = new SearcherEvaluator(corpus);
		Searcher jSearcher = new JaccardSearcher(testCorpus+"/documents.txt");
		Searcher tSearcher = new TFIDFSearcher(testCorpus+"/documents.txt");
		while(k<=50) {
			double[] jResults = s.getAveragePRF(jSearcher, k);
			double[] tResults = s.getAveragePRF(tSearcher, k);
			PJ.add(jResults[0]);
			RJ.add(jResults[1]);
			F1J.add(jResults[2]);
			PT.add(tResults[0]);
			RT.add(tResults[1]);
			F1T.add(tResults[2]);
			System.out.println("@@@ Jaccard at k="+k+": "+Arrays.toString(jResults));
			System.out.println("@@@ TFIDF at k="+k+": "+Arrays.toString(tResults));
			k++;
		}
	}
	
	public static void writeToFile(List<Double> lis ,String dest) {
		StringBuilder s = new StringBuilder();
		for(double b : lis) {
			s.append(b+"\n");
		}
		File f = new File("./k/" + dest);
		try {
			FileUtils.writeStringToFile(f, s.toString(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{	
		/********************* Uncomment test cases you want to test ***************/
		PJ = new LinkedList<Double>();
		RJ = new LinkedList<Double>();
		F1J = new LinkedList<Double>();
		PT = new LinkedList<Double>();
		RT = new LinkedList<Double>();
		F1T = new LinkedList<Double>();
		getAllK(testCorpus);
		writeToFile(PJ, "PJ.txt");
		writeToFile(RJ, "RJ.txt");
		writeToFile(F1J, "F1J.txt");
		writeToFile(PT, "PT.txt");
		writeToFile(RT, "RT.txt");
		writeToFile(F1T, "F1T.txt");
		
	}

}
