/** Ben F Rayfield offers this software opensource GNU GPL 2+ */
package precogmeter;
import static humanaicore.common.CommonFuncs.*;
import java.awt.event.WindowListener;
import java.util.*;

import javax.swing.*;

import humanaicore.common.*;
import precogmeter.rands.RecordedRand;
import precogmeter.ui.Precogmeter;

public class Start{
	
	public static void main(String[] args){
		JFrame window = new JFrame("Precogmeter - raise it by subconsciously feeling how to move mouse up/down competing against randomness (opensource GNU GPL 2+, unzip this jar file to get source code)");
		window.add(new Precogmeter(new RecordedRand(Rand.strongRand, 10000000)));
		window.setSize(800, 600);
		ScreenUtil.moveToScreenCenter(window);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

}
