public class Archer extends MilitaryUnit{
	
	//Private fields:
	private int numberOfArrows;
	
	//Public method:
	public Archer(Tile p, double h, String f) {
		super(p, h, 2, f, 15.0, 2, 0);
			this.numberOfArrows = 5;
			}
	
	@Override
	public void takeAction(Tile t) {
		if (this.numberOfArrows <= 0) {
			this.numberOfArrows = 5;
		}else {
			super.takeAction(t);
			this.numberOfArrows --;
		}
		
		
		
	}
	
	public boolean equals(Object obj) {
			
			
		if ((obj instanceof Archer)) {
			final Archer other = (Archer) obj;
			if(this.numberOfArrows == other.numberOfArrows) {
				return super.equals(obj);
			}else {
				return false;
			}
		}else {
			return false;
		
		}
	}
			
}
	
	

