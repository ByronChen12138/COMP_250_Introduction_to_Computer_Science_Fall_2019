public class Worker extends Unit {
	private int numberOfJobs; 											// An int indicating the number of jobs already performed by the worker. 
	
	// public method:
	public Worker(Tile p, double h, String f) {
		super(p, h, 2, f);
		this.numberOfJobs = 0;
	}
	
		@Override
		public void takeAction(Tile t) {
			if ((this.getPosition().equals(t)) && (!t.isImproved())) {
				t.buildImprovement();
				this.numberOfJobs ++;	
			
				if (this.numberOfJobs >= 10) { 							//removed when improve 10 times
					t.removeUnit(this);
				}
			}else {
				return;
			}
		}
		
		public boolean equals(Object obj) {
				
			if (obj instanceof Worker) {
				
				final Worker other = (Worker) obj;	
				if((this.numberOfJobs == other.numberOfJobs)) {
					return super.equals(obj);
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		
}