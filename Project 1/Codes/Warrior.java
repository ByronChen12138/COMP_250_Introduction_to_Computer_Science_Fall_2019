public class Warrior extends MilitaryUnit{

		public Warrior(Tile p, double h, String f) {
		super(p, h, 1, f, 20.0, 1, 25);
		}
		

		public boolean equals(Object obj) {
	
			if ((obj instanceof Warrior)) {
				return super.equals(obj);
			}else {
				return false;
			}
		}
		
		
}