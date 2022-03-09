public class Settler extends Unit {
	
	// public method:
	public Settler(Tile p, double h, String f) {
		super(p, h, 2, f);
	}
	
	@Override
	public void takeAction(Tile t) {
		if ((this.getPosition().equals(t)) && 
				(!t.isCity())){ 					//if settler is on the same tile and there is no city
			t.foundCity(); 
			t.removeUnit(this);
		}else {
			return;
		}
	}
	
	public boolean equals(Object obj) {
		
		if ((obj instanceof Settler)) {
			return super.equals(obj);
		}else {
			return false;
		}
	}
	
	
}
