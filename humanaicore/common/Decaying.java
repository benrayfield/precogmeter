/** Ben F Rayfield offers HumanAiCore opensource GNU LGPL */
package humanaicore.common;

/** A decay rate (fraction per second) on an object such as a DecayBell */
public class Decaying<T>{
	
	public final double decay;
	
	public final T ob;
	
	public Decaying(T ob, double decay){
		this.ob = ob;
		this.decay = decay;
	}
	
	public static final double logOf2 = Math.log(2);
	
	public static double halfLifeToDecayRate(double halfLife){
		return logOf2/halfLife;
	}
	
	public static double decayRateToHalfLife(double decay){
		return logOf2/decay;
	}

}
