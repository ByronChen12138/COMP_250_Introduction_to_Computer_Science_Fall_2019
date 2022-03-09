public abstract class Unit { 							// super class Unit
	
	//private fields:
	private Tile position; 								// A Tile representing the position of the unit in the game.
	private double hp; 									// A double representing the health points (hp) of the unit.
	private int movingRange; 							// An int representing the available moving range of the unit.
	private String faction; 							// The faction determines to which group of people this unit belongs.
	
	//public methods:
	public Unit (Tile p, double h, int mr, String f) {
		this.position = p;
		this.hp = h;
		this.movingRange = mr;
		this.faction = f;
		
		if (p.addUnit(this) != true) {
			throw new IllegalArgumentException ("Cannot add your army on the position with an enemy's army!!");
		}
		
	}
	
	public final Tile getPosition() {
		return this.position;
	}
	
	public final double getHP() {
		return this.hp;
	}
	
	public final String getFaction() {
		return this.faction;
	}
	
	public boolean moveTo (Tile destination) {
		boolean isMoved  = false;
		Tile oldPosition = this.getPosition();
		
//		System.out.println("old " + oldPosition);
//		System.out.println("new " + destination);
		
		boolean inRange = (Tile.getDistance(oldPosition, destination) < (this.movingRange + 1));
		boolean added = destination.addUnit(this);
		
//		System.out.println("inRange " + inRange);
//		System.out.println("added " + added);
		if (inRange && added){ 											//check if there is different army and add it to the tile
			
			System.out.println("in moveTo with " + this.getClass());
			this.position = destination;
			oldPosition.removeUnit(this);
			isMoved = true;
		}
		return isMoved;
	}
	
	public void receiveDamage(double d) {
		if (this.getPosition().isCity() == true){ 						//if the unit is stationed on a city, then the damage should be reduced by 10%
			d = d * 0.9;
		}
		this.hp = this.hp - d;
		
		if (this.getHP() <= 0) { 										//if the unit is killed
			this.getPosition().removeUnit(this);
		}
		
	}
	
	public abstract void takeAction(Tile t);
	
	public boolean equals(Object obj) {																					//Override equals
		
		if (!(obj instanceof Unit)) {
			return false;
		}
		Unit other = (Unit) obj;
		if	((other.getFaction().equals(this.getFaction())) &&
			(other.getHP() == this.getHP()) &&
			(other.getPosition().equals(this.getPosition()))) {
			return true;
		}else {
			return false;
		}
	}
	
}
