# precogmeter
Continuously measure your precognitive skill by moving the mouse competing against randomness to raise the wave - feels like trading stocks really fast

<img src="https://github.com/benrayfield/precogmeter/blob/master/images/precogmeter_0.1.png?raw=true"/>

This text needs updating since the math isnt there yet but its still useful for measuring precog, just not normalized onto a constant bell curve yet.

Move the mouse up and down in this panel, competing against randomness,
trying to raise the colored area higher. If you can predict randomness in advance,
especially recorded randomness that you just dont know about yet,
then you are precognitive. This is measured on a bell curve
which you can compare to percentile statistics,
such as 2 standard deviations are about a 1 in 20 chance, and 3 are 1 in 300. 
Its a graph display moving outward from a vertical line in center.

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
