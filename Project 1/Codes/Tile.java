public class Tile {
	
	//Private fields:
		private int x; 														//An int indicating the x-coordinate of the tile.
		private int y; 														//An int indicating the y-coordinate of the tile.
		private boolean isCityBuilt; 										//A boolean indicating whether or not a city has been built on the tile.
		private boolean isImprovementReceived; 								//A boolean indicating whether or not the tile received some ¡°improvements¡±.
		private ListOfUnits unitsPositioned; 								//A ListOfUnits containing all the units positioned on the tile.
		
	//Public methods:
		public Tile(int x, int y) { 										//create a constructor
			this.x = x;
			this.y = y;
			this.isCityBuilt = false;
			this.isImprovementReceived = false;
			this.unitsPositioned = new ListOfUnits();
		}
		
		public int getX() { 												//return x-coordinate
			return this.x;
		}
		
		public int getY() { 												//return y-coordinate
			return this.y;
		}
		
		public boolean isCity() { 											//returns whether or not the tile is a city
			return this.isCityBuilt;
		}
		
		public boolean isImproved() {										//returns whether or not the tile is a city
			return this.isImprovementReceived;
		}
		
		public void foundCity() { 											//turns the tile into a city if it wasn¡¯t already
			if (!this.isCity()){
				this.isCityBuilt = true;
				}
		}
		
		public void buildImprovement() { 									//improves the tile if it wasn¡¯t already
			if (!this.isImproved()){
				this.isImprovementReceived = true;
				}		
		}
		
		public boolean addUnit(Unit u) { 									//takes as input a unit and adds it to the tile¡¯s ListOfUnits
			Unit[] armyOnTheTile = this.unitsPositioned.getArmy();
			int i = 0;
			boolean isAdded = false;
			
			if(u instanceof MilitaryUnit) {
				if(armyOnTheTile.length <= 0) {
					isAdded = true;
				}
				while(i < armyOnTheTile.length) {
					if(!(armyOnTheTile[i].getFaction().equalsIgnoreCase(u.getFaction()))) {
						isAdded = false;
						break;
					}else {
						isAdded = true;
					}
					i++;
				}
				
			}else {
				isAdded = true;
			}
			
			if (isAdded) {
				this.unitsPositioned.add(u);
			}
			return isAdded;
			
//			boolean isAdded = false;
//			boolean isEnemyArmy = false;
//			Unit[] armyOnTheTile = this.unitsPositioned.getArmy(); 			//get all the army on the tile
//			int i = 0;
//			
//			while(i < armyOnTheTile.length) { 								//in the range of the 
//				if (!(armyOnTheTile[i].getFaction().equals(u.getFaction()))) { 		//if the army on the tile is an enemy
//					isEnemyArmy = true;
//					break;
//				}
//				i ++;
//				
//			}if ((u instanceof MilitaryUnit) && (isEnemyArmy = false)) { 	//test if any enemies' army on the tile
//				this.unitsPositioned.add(u);
//				isAdded = true; 												// add only if the army on the tile isn't enemies'
//				
//			}else if ((u instanceof Worker)||(u instanceof Settler)) { 		//test if is a in-army unit
//				this.unitsPositioned.add(u);
//				isAdded = true;
//			}
//			return isAdded;
		}

		public boolean removeUnit(Unit u) {
			boolean isRemoved = false;
			isRemoved = this.unitsPositioned.remove(u);
			return isRemoved;
		}
		
		public Unit selectWeakEnemy(String f) {
			Unit u = null;
			Unit[] units = this.unitsPositioned.getUnits();
			
			for (int i = 0; (i < units.length); i++) { 						//in the range of all the army in the tile
				if ((units[i].getFaction() != f) && 
						((u == null) || (units[i].getHP() < u.getHP()))){ 	//if the army is enemy and (if u has no value or u has higher HP)
					u = units[i];
				}
			}
			return u; 														//if no available unit, that would be null
		}
		
		public static double getDistance (Tile A, Tile B) {
			double distance = 0;
			int xa = A.getX();
			int ya = A.getY();
			int xb = B.getX();
			int yb = B.getY();
			
			distance = Math.pow(((double)Math.sqrt(xa-xb) + (double)Math.sqrt(xa-xb)),(double)(1/2));
			//System.out.println("distance  " + distance);
			return distance;
		}
//		
//		public String toString() {
//			return "("+ this.x + "," + this.y + ")";
//		}
		
		
		
		
		
}