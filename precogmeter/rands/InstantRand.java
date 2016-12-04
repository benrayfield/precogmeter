package precogmeter.rands;
import java.util.Random;
import java.util.function.Supplier;

import humanaicore.err.*;

public class InstantRand implements Supplier<Boolean>{
	
	public synchronized Boolean get(){
		throw new Todo("use microphone through jsoundcard, System.nanoTime, new Object().hashCode(), or something like that. Make sure its low lag, whatever it is.");
	}

}
