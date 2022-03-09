import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
	
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i = 0; i < N - 1; i++){
			for(int j = 0; j < N - i - 1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j + 1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j + 1));
					sortedUrls.set(j + 1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 * 
	 * Heap sort
	 */
    
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	 ArrayList<K> sortedUrls = new ArrayList<K>();
         sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls
         
         ArrayList<K> Heap = buildHeap(sortedUrls, results);
         
         int size = Heap.size() - 1;
         
         for(int i = 1; i < size; i++) {
        	 swapElement(1, size + 1 - i, Heap);
        	 downHeap(Heap, size - i, results);
         }
         
         sortedUrls = Heap;
         sortedUrls.remove(0);
         
    	return sortedUrls;
    }
    
    private static <K, V extends Comparable> void downHeap(ArrayList<K> heapBuilt, int maxIndex, HashMap<K, V> results) {
    	int i = 1;
    	int child;
    	
    	while (2*i <= maxIndex) {
    		child = 2*i;
    		if (child < maxIndex) {
    			if( results.get(heapBuilt.get(child + 1)).compareTo(results.get(heapBuilt.get(child))) < 0) {
    				child = child + 1;
    			}
    		}
    		if (results.get(heapBuilt.get(child)).compareTo(results.get(heapBuilt.get(i))) < 0){
    			swapElement(i, child, heapBuilt);
    			i = child;
    		}
    		else return;
    	}
    	
    }
    
	private static <K, V extends Comparable> ArrayList<K> buildHeap(ArrayList<K> Heap, HashMap<K, V> results) {
    	ArrayList<K> heapBuilt = new ArrayList<K>();
        heapBuilt.add(null);										// let the first element be null
    	
    	for(int k = 1; k <= Heap.size(); k++) {
    		heapBuilt.add(k, Heap.get(k - 1));
    		upHeap(heapBuilt, k, results);
    	}
    	return heapBuilt;
    }

    private static <K, V extends Comparable> void upHeap(ArrayList<K> heapBuilt, int k, HashMap<K, V> results) {
		int i = k;
		while ((i > 1) && (results.get(heapBuilt.get(i)).compareTo(results.get(heapBuilt.get(i / 2))) < 0)) {				// if this element is smaller than its child
			swapElement(i, i/2, heapBuilt);
		}
	}


	private static <K> void swapElement(int i, int j, ArrayList<K> sortedUrls) {
    	K temp = sortedUrls.get(i);
    	sortedUrls.set(i, sortedUrls.get(j));
		sortedUrls.set(j, temp);
    }

}