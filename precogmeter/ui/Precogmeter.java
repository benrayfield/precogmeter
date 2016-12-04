/** Ben F Rayfield offers this software opensource GNU GPL 2+ */
package precogmeter.ui;
import static humanaicore.common.CommonFuncs.*;
import java.util.*;
import java.util.function.Supplier;

import humanaicore.common.*;
import humanaicore.err.Todo;
import humanaicore.ui.ColorFlowFromCenterColumn;


/** Move the mouse up and down in this panel, competing against randomness,
trying to raise the colored area higher. If you can predict randomness in advance,
especially recorded randomness that you just dont know about yet,
then you are precognitive. This is measured on a bell curve
which you can compare to percentile statistics,
such as 2 standard deviations are about a 1 in 20 chance, and 3 are 1 in 300. 
Its a graph display moving outward from a vertical line in center.
<br><br>
First I'll explain it as bits, then generalize to scalars.
The vertical fraction of your mouse chooses (1-fraction) heads and fraction tails,
all the heads then all the tails, spread over a small fraction of a second.
At every time theres 2 coins landed: 1 from mouse and 1 from randomness.
If the 2 coins equal, you win a point. If they're opposite, you lose a point.
The standard deviation of score is exactly squareRoot(coins)
if all possible ways they could land happen exactly once,
and I've verified this in simulation up to 30 coins which had 2^30 flips.
Average((heads-tails)^2) = heads+tails, for all possible flips once.
StdDev = sqrt(heads-tails).
Display plus/minus 4 stdDevs on screen vertically.
If you reach either, you're precognitive.
Thats if the accurateStatistics var is true, else the stdDev
rises over time since you're more likely to end up farther away from where you started if not normed,
but thats more fun of a game since you can hold at any position by moving mouse to center height.
I'll work on the math to find something thats both a constant bell curve
and doesnt feel that you're losing ground just by holding position.
Maybe I made a mistake in counting more coins as flipped while mouse is at (or near) center,
since an equal amount of heads and tails will be added regardless of the randomness.
Yes thats got to be the problem. I'll look into it.
But for now this is still equally likely to go up vs down so if you repeatably get it to the top
much more often than the bottom then you're precognitive.
*/
public class Precogmeter extends ColorFlowFromCenterColumn{
	
	//public final Supplier<Boolean> rand;
	
	public double heads, tails;
	
	public static final double coinsPerSecond=10000;
	
	/** else is a more intuitive game. TODO figure out why this is more intuitive, and make the statistics still more accurate.
	Probably its the smoothness instead of showing all the jittering up and down every few pixels.
	*/
	public static final boolean accurateStatistics = false;
	
	public static final boolean mouseAsScalarsInsteadOfBits = true;
	
	public Precogmeter(final Supplier<Boolean> rand){
		super(
			(Map map)->{
				//Decaying<DecayBell> decayingBell = (Decaying<DecayBell>) map.get("decayingBell");
				double repaintedWhen = (Double) map.get("repaintedWhen");
				double now = Time.time();
				double timeSinceRepaint = Math.max(0, Math.min(now-repaintedWhen, .1));
				map.put("repaintedWhen", now);
				
				double decayRate = .1;
				
				double decay = Math.max(0, Math.min(timeSinceRepaint/decayRate, .5));
				
				double yFraction = (Double) map.get("yFraction");
				double mouseYFraction = (Double) map.get("mouseYFraction");
				
				Precogmeter p = (Precogmeter) map.get("precogmeter");
				int coinsFlipNow = (int)Math.round(timeSinceRepaint*p.coinsPerSecond);
				double playerCoinChance = Math.max(0, Math.min(1-mouseYFraction, 1));
				for(int c=0; c<coinsFlipNow; c++){
					boolean randCoinBit = rand.get();
					if(p.mouseAsScalarsInsteadOfBits){
						double randCoin = randCoinBit ? 1 : -1;
						double playerCoin = 2*playerCoinChance-1;
						double combinedCoin = randCoin*playerCoin;
						double heads = .5+.5*combinedCoin;
						double tails = .5-.5*combinedCoin;
						p.heads += heads;
						p.tails += tails;
					}else{
						boolean playerCoin = c+.5 < playerCoinChance*coinsFlipNow; //TODO c+what? 0 .5 1?
						//if(Rand.strongRand.nextFloat() < .1) playerCoin = randCoin;
						boolean combinedCoin = randCoinBit^playerCoin;
						//combinedCoin = playerCoin;
						if(combinedCoin) p.heads++;
						else p.tails++;
					}
				}
				if(accurateStatistics){
					p.heads *= 1-decay;
					p.tails *= 1-decay;
				}
				
				//double scoreFraction = (1-mouseYFraction);
				//double nextDataForBell = 1-mouseYFraction;
				//decayingBell.ob.add(nextDataForBell, decay);
				
				
				double stdDev;
				if(accurateStatistics){
					stdDev = (p.heads-p.tails)/Math.sqrt(p.heads+p.tails);
				}else{
					stdDev = (p.heads-p.tails)/Math.sqrt(coinsPerSecond*30);
				}
				
				//lg("stdDev="+stdDev);
				if(stdDev != stdDev) stdDev = 0; //if no flips yet
				
				double displayStdDevs = 2.5;
				
				//"Its corre"
				//https://www.reddit.com/r/statistics/comments/5gfppo/what_math_for_a_1d_continuous_precogmeter_about/
				
				//FIXME this shouldnt change the ave
				//double scoreFraction = Math.max(0, Math.min(.5+decayingBell.ob.devOf(nextDataForBell)/8, 1));
				double scoreFraction = Math.max(0, Math.min(.5+.5*stdDev/displayStdDevs, 1));
				
				
				//if(mouseYFraction > yFraction) return 0xffff7722;
				//if(mouseYFraction > yFraction) return 0xffffcc99;
				//if(mouseYFraction > yFraction) return 0xffffddbb;
				int color = 0xff000000;
				if(.5 < 1-yFraction){
					if(1-yFraction < scoreFraction){
						color |= Rand.strongRand.nextInt(0x10000); //paint ground below score
					}
				}else{
					if(1-yFraction > scoreFraction){
						color |= Rand.strongRand.nextInt(0x10000); //paint ground below score
					}
				}
				/*if(1-yFraction < scoreFraction){
					color |= Rand.strongRand.nextInt(0x10000); //paint ground below score
					if(.5 < 1-yFraction) color |= (Rand.strongRand.nextInt(0x100)<<16); //display whats above mid height diff color
				}
				*/
				return color;
			},
			Collections.synchronizedMap(new HashMap())
		);
		//this.rand = rand;
		params.put("repaintedWhen", Time.time());
		//params.put("decayingBell", new Decaying(new DecayBell(),.3));
		params.put("precogmeter", this);
		Thread t = new Thread(){
			public void run(){
				while(true){
					repaint();
					Double mouseMovedWhen = (Double) params.get("mouseMovedWhen");
					double notMovedHowLong = Time.time()-mouseMovedWhen;
					if(notMovedHowLong < 120){
						Time.sleepNoThrow(.01);
					}else if(notMovedHowLong < 300){
						Time.sleepNoThrow(.2);
					}else{
						Time.sleepNoThrow(3); //dont waste compute resources if not moved mouse recently
					}
				}
			}
		};
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
	
	/** returns the standard deviation of precog score. Near 0 is not precognitive.
	Farther from 0, either positive or negative, is precognitive.
	Generally you try for higher, but it takes skill to reach either.
	The challenge is randomness will have about the same number of ups and downs,
	and depending where your mouse is when, those get reversed or not or gradually between.
	*/
	public double precogStdDev(){
		throw new Todo();
	}

}
