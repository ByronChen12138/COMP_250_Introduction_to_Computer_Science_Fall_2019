/* Name: Byron.Chen
 * 
 * Discussed with: Christine Yang, Firzana Sadik 
 */

import java.util.ArrayList;
import java.util.Iterator;
public class KDTree implements Iterable<Datum>{ 
	
	ArrayList <KDNode> leaves = new ArrayList<>();			//create an arraylist for storing the leaves
	KDNode 		rootNode;		
	int    		k; 				//dimension
	int			numLeaves;		//number of leaves
	
	
	// constructor

	public KDTree(ArrayList<Datum> datalist) throws Exception {

		Datum[]  dataListArray  = new Datum[ datalist.size() ]; 

		if (datalist.size() == 0) {
			throw new Exception("Trying to create a KD tree with no data");
		}
		else
			this.k = datalist.get(0).x.length;

		int ct=0;				//counter
		for (Datum d :  datalist) {
			dataListArray[ct] = datalist.get(ct);
			ct++;
		}
		
	//   Construct a KDNode that is the root node of the KDTree.

		rootNode = new KDNode(dataListArray);
		
		this.getLeaves(rootNode);
	}
	
	//   KDTree methods
	
	public Datum nearestPoint(Datum queryPoint) {
		return rootNode.nearestPointInNode(queryPoint);
	}
	

	public int height() {
		return this.rootNode.height();	
	}

	public int countNodes() {
		return this.rootNode.countNodes();	
	}
	
	public int size() {
		return this.numLeaves;	
	}

	//-------------------  helper methods for KDTree   ------------------------------

	public static long distSquared(Datum d1, Datum d2) {

		long result = 0;
		for (int dim = 0; dim < d1.x.length; dim++) {
			result +=  (d1.x[dim] - d2.x[dim])*((long) (d1.x[dim] - d2.x[dim]));
		}
		// if the Datum coordinate values are large then we can easily exceed the limit of 'int'.
		return result;
	}

	public double meanDepth(){
		int[] sumdepths_numLeaves =  this.rootNode.sumDepths_numLeaves();
		return 1.0 * sumdepths_numLeaves[0] / sumdepths_numLeaves[1];
	}

//--------------------------------KDNode-----------------------------------------------------------------------------------------------
	class KDNode { 

		boolean leaf;
		Datum leafDatum;           //  only stores Datum if this is a leaf
		
		//  the next two variables are only defined if node is not a leaf

		int splitDim;      // the dimension we will split on
		int splitValue;    // datum is in low if value in splitDim <= splitValue, and high if value in splitDim > splitValue  

		KDNode lowChild, highChild;   //  the low and high child of a particular node (null if leaf)
		  //  You may think of them as "left" and "right" instead of "low" and "high", respectively

		KDNode(Datum[] datalist) throws Exception{
			boolean allDulplicate;
			
			/*
			 *  This method takes in an array of Datum and returns 
			 *  the calling KDNode object as the root of a sub-tree containing  
			 *  the above fields.
			 */
			
			if (datalist.length == 1) {
				numLeaves++;
				this.leaf = true;
				this.leafDatum = datalist[0];

			}else {
				allDulplicate = true;
			
				for(int i = 0; i < datalist.length - 1; i++) {		//Check if all the data left is the same
					if(!datalist[i].equals(datalist[i + 1])) {
						allDulplicate = false;
						break;										//return even one of them is different
					}
				}
				
				if(allDulplicate) {									//get the first element if all of them are duplicates
					leaf = true;
					leafDatum = datalist[0];
					numLeaves++;
				
				}else {											 
					    ArrayList <Datum> lowChildList = new ArrayList<>();
						ArrayList <Datum> highChildList = new ArrayList<>();
						
						this.leaf = false;
						this.splitDim = biggestDimension(datalist);
						
						int[] MaxMin = getMaxAndMin(datalist, this.splitDim);
						
						int Max = MaxMin[0];
						int Min = MaxMin[1];
					
						if((Max + Min) < 0 && (Max + Min) % 2 != 0) {		// Special cases handling
							splitValue = (Max + Min) / 2 - 1;
						} else {
							splitValue = (Max + Min) / 2;
						}
						
						for(int i = 0; i < datalist.length; i++) {
							Datum curDatum = datalist[i];
							int curValue = curDatum.x[splitDim];
							
							if(curValue <= splitValue) {
								lowChildList.add(curDatum);
							
							}else {
								highChildList.add(curDatum);
								
							}
						}
						
						lowChild  = new KDNode ( lowChildList.toArray(new Datum[lowChildList.size()]));
						highChild = new KDNode (highChildList.toArray(new Datum[highChildList.size()]));
					}
			}
		
	}
		
		
		
		
//-----------------------------Method Extended---------------------------------------------------------------------------------------
		public int biggestDimension(Datum[] datalist) {			//it will take the Datum[], and find splitDim
			int dimension = 0;
			int biggestRange = 0;
			
			for	(int i = 0; i < k; i++) {
				int curRange = getMaxAndMin(datalist, i)[0] - getMaxAndMin(datalist, i)[1];
				
				if (i == 0) {
					biggestRange = curRange;	//get the biggest range for mean range
					dimension = i;			//set the splitDim
				}
				if (biggestRange < curRange) {
					biggestRange = curRange;	//get the biggest range for mean range
					dimension = i;			//set the splitDim
				}
			}
			
			return dimension;
		}
		
		public int[] getMaxAndMin(Datum[] datalist, int d) {			// get Datum[] and the index of the dimension and returns the max
			int maxValue = datalist[datalist.length - 1].x[d];			// get the last Datum as initial
			int minValue = datalist[datalist.length - 1].x[d];			
			int curValue = 0;
			int [] MaxMin  = new int[2];
			
			for (int j = 0; j < datalist.length - 1; j++) {				// get coordinate at d for each Datum
				curValue = datalist[j].x[d];
				maxValue = Math.max(maxValue, curValue);
				minValue = Math.min(minValue, curValue);
			}
			
			MaxMin[0] = maxValue;
			MaxMin[1] = minValue;
				
			return MaxMin;
			
		}
		
		
//-------------------------------nearestPointInNode----------------------------------------------------------------------------------
//		ArrayList <KDNode> searchPath = new ArrayList<>();
		
		public Datum nearestPointInNode(Datum queryPoint) {
			Datum nearestPoint, nearestPoint_otherSide;
			
			double squaredDistanceNQ;			//the square of the distance between the queryPoint and the nearestPoint
			double squaredDistanceQS;			//the square of the distance between the queryPoint and the split value
			double DistanceQS;
			double squaredDistanceNOQ;			//the square of the distance between the queryPoint and the otherSide_nearestPoint
			
			
			if (this.leaf) {
				return this.leafDatum;
			}
			
			if (queryPoint.x[this.splitDim] <= this.splitValue) {
				nearestPoint = this.lowChild.nearestPointInNode(queryPoint);
				
				squaredDistanceNQ = distSquared(nearestPoint, queryPoint);
				
				
				DistanceQS = this.splitValue - queryPoint.x[this.splitDim];			// the distance between splitValue and queryPoint
				squaredDistanceQS = DistanceQS * DistanceQS;
				
				if (squaredDistanceNQ < squaredDistanceQS) {
					return nearestPoint;
				}else {
					nearestPoint_otherSide = this.highChild.nearestPointInNode(queryPoint);
					
					squaredDistanceNOQ = distSquared(nearestPoint_otherSide, queryPoint);
					
					if (squaredDistanceNOQ < squaredDistanceNQ) {
						return nearestPoint_otherSide;
					}else {
						return nearestPoint;
					}
				}
				
			}else {
				nearestPoint = this.highChild.nearestPointInNode(queryPoint);
				
				squaredDistanceNQ = distSquared(nearestPoint, queryPoint);
				DistanceQS = this.splitValue - queryPoint.x[this.splitDim];			// the distance between splitValue and queryPoint
				squaredDistanceQS = DistanceQS * DistanceQS;
				
				if (squaredDistanceNQ < squaredDistanceQS) {
					return nearestPoint;
				}else {
					nearestPoint_otherSide = this.lowChild.nearestPointInNode(queryPoint);
					
					squaredDistanceNOQ = distSquared(nearestPoint_otherSide, queryPoint);
					
					if (squaredDistanceNOQ < squaredDistanceNQ) {
						return nearestPoint_otherSide;
					}else {
						return nearestPoint;
					}
				}
			
			}
		}
		
		
//		public Datum nearestPointInNode(Datum queryPoint) {
//			int[] wrongCase = new int[2];
//			wrongCase[0] = 234;
//			wrongCase[1] = 536;
//			
//			if (queryPoint.x.equals(wrongCase)) {
//				System.out.println("===========================In the wrong case============================");
//			}
//			
//			Datum nearestPoint, nearestPoint_otherSide;
//			searchPath = new ArrayList<>();
//			ArrayList <KDNode> nodesChecked = new ArrayList<>();
//			long squaredDistanceNQ;			//the square of the distance between the queryPoint and the current nearestPoint
//			long squaredDistanceQS;		//the square of the distance between the queryPoint and the split value
//			long DistanceQS;
//			
//			nearestPoint = searchNearestPointOnOneSide(queryPoint, rootNode);
//			
//			if(nearestPoint.equals(queryPoint)) {
//				return nearestPoint;
//			}
//			
//			while (!searchPath.isEmpty()) {
//					KDNode nodeForCheck = searchPath.get(0);
//					squaredDistanceNQ = distSquared(nearestPoint, queryPoint);
//					DistanceQS = nodeForCheck.splitValue - queryPoint.x[nodeForCheck.splitDim];	// the distance between splitValue and queryPoint
//					
//					if (DistanceQS < 0) {									// when DistanceQS is smaller than 0, make it positive
//						DistanceQS *= -1;
//						squaredDistanceQS = (long) Math.sqrt(DistanceQS);	
//					
//					}else if (DistanceQS == 0) {							// when DistanceQS is 0, don't do Math.sqrt
//						squaredDistanceQS = 0;
//					
//					}else {													// Calculate the square of the DistanceQS
//						squaredDistanceQS = (long) Math.sqrt(DistanceQS);	
//					}
//					
//					if(!nodesChecked.contains(nodeForCheck)) {			//if the node haven't be checked
//						if(squaredDistanceNQ >= squaredDistanceQS) {
//							if(queryPoint.x[nodeForCheck.splitDim] <= nodeForCheck.splitValue) { // if it was split to low
//								nearestPoint_otherSide = searchNearestPointOnOneSide(queryPoint, nodeForCheck.highChild);		// go to high
//							}else {																					// otherwise
//								nearestPoint_otherSide = searchNearestPointOnOneSide(queryPoint, nodeForCheck.lowChild);		// go to low
//							}
//							if(squaredDistanceNQ >= distSquared(nearestPoint_otherSide, queryPoint)) {
//								nearestPoint = nearestPoint_otherSide;		//set new nearestPoint if it is the new point is the nearest one
//							}
//						}
//					}
//					nodesChecked.add(nodeForCheck);					//record the nodes that are checked
//					searchPath.remove(0);							//remove the nodes that are checked in searchPath
//				}
//			
//			// searchNearestPointOnOneSide on the other side of the tree 
//			// when the squaredDistanceNQ is >= squaredDistanceQS
//			
//			// if smaller than or equal to splitValue, goes to highChild
//			// get the points that is checked, don't check them again
//			return nearestPoint;
//		}
//		
//		public Datum searchNearestPointOnOneSide(Datum qurePoint, KDNode root) {
//			if(!root.leaf) {
//				if (qurePoint.x[root.splitDim] <= root.splitValue) {					//test which side should the qurePoint be
//					searchPath.add(0, root);											//add the node it path at the first of the array list
//					return searchNearestPointOnOneSide(qurePoint, root.lowChild);
//				}else {
//					searchPath.add(0, root);
//					return searchNearestPointOnOneSide(qurePoint, root.highChild);
//				}
//			}
//			return root.leafDatum;						//if the current node is a leaf node, return it
//		}
//		
		
		// -----------------  KDNode helper methods (might be useful for debugging) -------------------

		public int height() {
			if (this.leaf) 	
				return 0;
			else {
				return 1 + Math.max( this.lowChild.height(), this.highChild.height());
			}
		}

		public int countNodes() {
			if (this.leaf)
				return 1;
			else
				return 1 + this.lowChild.countNodes() + this.highChild.countNodes();
		}
		
		/*  
		 * Returns a 2D array of ints.  The first element is the sum of the depths of leaves
		 * of the subtree rooted at this KDNode.   The second element is the number of leaves
		 * this subtree.    Hence,  I call the variables  sumDepth_size_*  where sumDepth refers
		 * to element 0 and size refers to element 1.
		 */
				
		public int[] sumDepths_numLeaves(){
			int[] sumDepths_numLeaves_low, sumDepths_numLeaves_high;
			int[] return_sumDepths_numLeaves = new int[2];
			
			/*     
			 *  The sum of the depths of the leaves is the sum of the depth of the leaves of the subtrees, 
			 *  plus the number of leaves (size) since each leaf defines a path and the depth of each leaf 
			 *  is one greater than the depth of each leaf in the subtree.
			 */
			
			if (this.leaf) {  // base case
				return_sumDepths_numLeaves[0] = 0;
				return_sumDepths_numLeaves[1] = 1;
			}
			else {
				sumDepths_numLeaves_low  = this.lowChild.sumDepths_numLeaves();
				sumDepths_numLeaves_high = this.highChild.sumDepths_numLeaves();
				return_sumDepths_numLeaves[0] = sumDepths_numLeaves_low[0] + sumDepths_numLeaves_high[0] + sumDepths_numLeaves_low[1] + sumDepths_numLeaves_high[1];
				return_sumDepths_numLeaves[1] = sumDepths_numLeaves_low[1] + sumDepths_numLeaves_high[1];
			}	
			return return_sumDepths_numLeaves;
		}
		
	}

//---------------iterator-----------------------------------------
	public Iterator<Datum> iterator() {
		return new KDTreeIterator();
	}
	
	private class KDTreeIterator implements Iterator<Datum> {
		
		int index = -1;
		int size = leaves.size();
		
		@Override
		public boolean hasNext() {
			index++;
			if(index < size) return true;
			else return false;
		}

		
		@Override
		public Datum next() {
			KDNode value;
			value = leaves.get(index);
			return value.leafDatum;
		}
	}
	
	public void getLeaves(KDNode root) {
		if (root.lowChild != null) getLeaves(root.lowChild);			//go to low child if there still a one
		if (root.leaf == true) this.leaves.add(root);						//get the root if it is a leaf
		if ((root.highChild != null)) getLeaves(root.highChild);		//go to high child if there still a one
	}
}



