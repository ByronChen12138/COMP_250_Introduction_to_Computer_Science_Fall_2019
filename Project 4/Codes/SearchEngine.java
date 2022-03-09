import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (word, list of urls contain it) (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;
	public String firstUrl = null;
	
	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	
	public void crawlAndIndex(String url) throws Exception {
		ArrayList <String> links = this.parser.getLinks(url);					// get all the links contained in this web page
		ArrayList <String> words = this.parser.getContent(url);					// get all the words contained in this web page
		ArrayList <String> newListForWordIndex;		
		
		if (firstUrl == null) {										// if this is the first time it enter this method
			firstUrl = url;											// change the firstUrl
			internet.addVertex(url);								// add first web page as a vertex into the graph
			
			for(String u : internet.vertexList.keySet()) {			// reset all the visited value to false 
				internet.setVisited(u, false);
			}
		}
		
		internet.setVisited(url, true);								// set the web page as visited
		
		
		for(String w : words) {										// for each word in this web page
			newListForWordIndex = wordIndex.get(w);					
			
			if(newListForWordIndex == null) {						// if this word isn't created
				newListForWordIndex = new ArrayList<String>();		
				newListForWordIndex.add(url);						
				wordIndex.put(w, newListForWordIndex);				// put the array list with this url into this word hashmap
			
			}else if(!newListForWordIndex.contains(url)) {			// if this word is created in the map and this url isn't in the list
				newListForWordIndex.add(url);						
				wordIndex.replace(w, newListForWordIndex);			// update the list of urls
			}
		}
		
		for(String l : links) {										// for each links this web page can go to
			internet.addVertex(l);									// add this link as a vertex into the graph
			internet.addEdge(url, l);								// add edge between the link found and this web page
			
			if (!internet.getVisited(l)) {							// check if the next web page is visited in order to avoid infinite loop
				this.crawlAndIndex(l);
			}
		}
	}
	
	
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		ArrayList <Double> ranks = new ArrayList <>();						// to store the ranks computed
		ArrayList <String> vertices = internet.getVertices();					// store all the vertices that need to be ranked
		boolean flag = false;
		
		for (String v : vertices) {						// initialize all the pageRank to 1
			internet.setPageRank(v, 1);
		}
			
		while(!flag) {
			int index = 0;
			flag = true;
			ranks = computeRanks(vertices);
			
			for (double r : ranks) {
				double judgeValue = internet.getPageRank(vertices.get(index)) - r;			// if |judgeValue| < epsilon, test pass
				
				if (Math.abs(judgeValue) >= epsilon) flag = false;		// if one of them doesn't satisfy the criteria, compute them again
				
				internet.setPageRank(vertices.get(index), r);								// set vertices with new ranks							
				index++;
			}
		}
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		ArrayList <Double> ranks = new ArrayList <>();						// to store the ranks computed
		
		for (String v : vertices) {
			ranks.add(calculateRank(internet.getEdgesInto(v)));
		}
		
		return ranks;
	}
	
	public double calculateRank (ArrayList<String> linksInTo) {						// input all the links go into one vertex and return the rank of this page
		double part = 0;
		
		for (String l : linksInTo) {
//			if (internet.getPageRank(l) == 0) internet.setPageRank(l, 1);			// if the rank needed is 0, set it to 1
			part = part + (internet.getPageRank(l) / internet.getOutDegree(l));		// the sum of PR/OUT
		}
		
		return (0.5 + 0.5 * part);													
	}
	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		ArrayList <String> pages = wordIndex.get(query);
		if(pages.size() == 0) return pages;					// if no sites for this word return it directly 
		
		HashMap<String, Double> ranks = new HashMap<String, Double>();
		
		for (String p : pages) {
			ranks.put(p, internet.getPageRank(p));
		}
		
		pages = Sorting.fastSort(ranks);
				
		return pages;
	}
}
