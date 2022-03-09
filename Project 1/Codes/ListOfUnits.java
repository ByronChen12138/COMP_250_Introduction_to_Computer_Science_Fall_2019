public class ListOfUnits {
	
	//Private fields:
	private Unit[] myUnits;
	private int SIZE;
	
	//public methods:
	public ListOfUnits () {
		this.myUnits = new Unit[10];
		SIZE = 0;
	}
	
	public int size() {
		//for (int i = 0; (i < (this.myUnits.length)) && this.myUnits[i] != null; i++) { 		// check if i is in the range of the array and if it is the last position of the array.
		//	this.SIZE = i + 1; 																		// The size
		//}										
		return this.SIZE;
	}
	
	public Unit[] getUnits() { 																	//  returns an array containing all the units that are part of this ListOfUnits. 
		Unit[] unitsGot = new Unit[this.size()];
		for (int i = 0; i < this.size(); i++) { 											// check if i is the position with unit.
			unitsGot[i] = this.myUnits[i];
		}
		return unitsGot;
	}

	public Unit get(int m) {
		if ((m < 0) || (m >= this.size())) {
			throw new IndexOutOfBoundsException ("Input is out of range!"); 	// return when input is out of range.	
		}else{
			return this.myUnits[m];											// returns the unit at the specified position in this list
		}
	}
	
	public void add(Unit u) {
		int old_capacity = this.myUnits.length;
		
		if (this.size() < old_capacity) { 										//Test if there is a space
			this.myUnits[this.size()] = u;
		}else{
			int new_capacity = old_capacity + old_capacity/2 + 1;
			Unit[] temp = new Unit [new_capacity];
			
			for (int i = 0; i < old_capacity; i++) { 							//for all the units in the list
				temp[i] = this.myUnits[i]; 										// copy all the units to temp array
			}
			
			temp[this.size()] = u;
			this.myUnits = new Unit[new_capacity];
			this.myUnits = temp;
		}
		this.SIZE = this.SIZE + 1;												//!!!!!!!!!
	}
	
	public int indexOf(Unit u) { 												// returns an int indicating the position of the first occurrence of the specified element in this list. 
		int pos = -1;
		
		for(int i = 0; i < this.size(); i++){ 
			if (u.equals(this.myUnits[i])) {
				pos = i;
			}
		}
		return pos;
	}
	
	public boolean remove(Unit u) {
		int pos = this.indexOf(u);
		boolean shiftLeft = false;
		
		if (pos == -1 || this.size() <= 0) {
			return false;
		}else {
			Unit[] tmp = new Unit[this.myUnits.length];
			int boundary = this.myUnits.length;
      		this.SIZE = this.SIZE - 1;														//size is 1 smaller
			int a = 0;
			
			for (int i = 0; i < boundary; i++) {
				if (i == pos) {																//when pos is the unit's which should be removed
					continue;
				}else{
					tmp[a] = this.myUnits[i];										
					a++;
					}
				}
			this.myUnits = tmp;
			return true;
	}
		
//		int boundary = this.size();
//		for (int i = 0; i < boundary; i++) {
//			if (this.myUnits[i].equals(u)) {
//				if (i < boundary - 1) {
//					for (int j = i; j < boundary - 1; j++) {
//						this.myUnits[j] = this.myUnits[j + 1];
//					}
//				}
//				this.myUnits[boundary - 1] = null;
//				this.SIZE --;
//				return true;
//			}
//		}
//		return false;
		
		
	//	boolean isRemoved = false;
	//	boolean isMiniseOne = false;
	//	
	//	for(int i = 0; i < this.size(); i++){ 
	//		if (u.equals(this.myUnits[i])) {
	//			
	//			Unit[] temp = new Unit[this.myUnits.length];
	//			
	//			for(int j = 0; j < this.size(); j++) {
	//				if (j != i) {
	//					if (isMiniseOne = false) {
	//						temp[j] = this.myUnits[j]; 							// insert the units before removing any unit
	//					}else {
	//						temp[j-1] = this.myUnits[j]; 						// shift all the units to left
	//					}
	//				}else {
	//					isMiniseOne = true; 									// avoid inserting the one needed to be removed and tells to shift all the units to the left.
	//				}
	//			}
	//			this.myUnits = temp;
	//			isRemoved = true; 												// the unit is removed
	//		}
	//	}
	//	if (isRemoved = true) {
	//		this.SIZE = this.SIZE - 1;
	//	}
	//	return isRemoved;
}		

	public MilitaryUnit[] getArmy() { 													//  returns an array of MilitaryUnit array
		int counter = 0;
		
		for (int i = 0; i < this.size(); i++) {
			if (this.myUnits[i] instanceof MilitaryUnit) {
				counter++;
			}
		}
		
		MilitaryUnit[] militaryUnitsArray = new MilitaryUnit[counter];
		int a = 0;
		
		for (int j = 0; j < this.size(); j++) {
			if (this.myUnits[j] instanceof MilitaryUnit) {
				militaryUnitsArray[a] = (MilitaryUnit) this.myUnits[j];
				a++;
			}
		}
		return militaryUnitsArray;
		
//		MilitaryUnit[] temp = new MilitaryUnit[this.myUnits.length];
//		int num = 0;
//
//		for (int i = 0; i < SIZE; i++) {
//			if (this.myUnits[i] instanceof MilitaryUnit) { 						// if the unit is a military unit, insert it to an array
//				temp[num] = (MilitaryUnit)this.myUnits[i];
//				num++; 															// count how many military units we have
//			}
//		}
//		
//		MilitaryUnit[] militaryUnitsArray = new MilitaryUnit[num];
//		for (int j = 0; j < num; j++) { 										// check if j is the position with unit.
//			militaryUnitsArray[j] = temp[j];
//			}
	}

}
