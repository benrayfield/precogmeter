package precogmeter.rands;
import java.util.Random;
import java.util.function.Supplier;

import humanaicore.err.Err;

public class RecordedRand implements Supplier<Boolean>{

	public final long bits;
	private final byte recorded[];
	private long bitsUsed;
	
	public RecordedRand(Random recordMe, long bits){
		this.bits = bits;
		recorded = new byte[(int)((bits+7)/8)];
		recordMe.nextBytes(recorded);
	}
	
	public boolean get(long bitIndex){
		return (recorded[(int)(bitIndex>>>3)] & (128>>(bitIndex&7))) != 0;
	}
	
	public synchronized Boolean get(){
		if(bits == bitsUsed) throw new Err("Used all "+bits+" recorded random bits.");
		return get(bitsUsed++);
	}

}
