public class MilitaryUnit extends Unit {
	
	//private fields:
	private double attackDamage; 								// A double indicating the attack damage of the unit. 
	private int attackRange; 									// An int indicating the attack range of the unit. 
	private int armor; 											// An int indicating the armor of the unit. 

	//Public method:
	public MilitaryUnit(Tile p, double h, int mr, String f, double ad, int ar, int a) {
		super(p, h, mr, f);
		this.attackDamage =ad;
		this.attackRange = ar;
		this.armor = a;
		
	}

	@Override
	public void takeAction(Tile t) {
		if (Tile.getDistance(t, this.getPosition()) < this.attackRange + 1) {
			
			Unit target = t.selectWeakEnemy(this.getFaction());
			
			if (target == null) {
				return;
			}
			
			double d = this.attackDamage;
			
			if (this.getPosition().isImproved()) {
				d = d * 1.05;
			}
			target.receiveDamage(d);
		}
		
	}

	public void receiveDamage(double d) {
		d = 100 / (100 + this.armor);
		super.receiveDamage(d);
	}
	
	
}
