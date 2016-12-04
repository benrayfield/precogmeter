package humanaicore.ui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import javax.swing.JPanel;

import humanaicore.common.*;

/** Paints a column 1 pixel wide using a ColorAtFraction,
then slide the left and right halfs 1 pixel away from center.
*/
public class ColorFlowFromCenterColumn extends JPanel implements MouseMotionListener, MouseListener, KeyListener{
	
	protected Function<Map,Integer> painter;
	
	protected Map params;
	
	protected long paints;
	
	public ColorFlowFromCenterColumn(Function<Map,Integer> painter){
		this(painter, new HashMap());
	}
	
	public ColorFlowFromCenterColumn(Function<Map,Integer> painter, Map firstParams){
		setPainter(painter);
		setParams(firstParams);
		addMouseMotionListener(this);
		addMouseListener(this);
		setFocusable(true); //for KeyListener
		addKeyListener(this);
		//setBackground(Color.black);
		//setForeground(Color.black);
		params.put("mouseMovedWhen", Time.time());
		params.put("heightPixels", 100);
		params.put("widthPixels", 100);
		params.put("mouseXPixels", 50);
		params.put("mouseYPixels", 50);
		params.put("mouseXFraction", .5);
		params.put("mouseYFraction", .5);
	}
	
	public void setPainter(Function<Map,Integer> painter){
		this.painter = painter;
	}
	
	public void setParams(Map params){
		this.params = params;
	}
	
	public void paint(Graphics g){
		int w = getWidth(), h = getHeight();
		g.setPaintMode();
		if(paints++ < 2){ //TODO why is fillRect ignored on the first call of paint?
			g.setColor(Color.black);
			g.fillRect(0, 0, w, h);
		}
		final int column = w/2;
		g.copyArea(1, 0, column, h, -1, 0);
		g.copyArea(column, 0, w-column, h, 1, 0);
		for(int y=0; y<h; y++){
			params.put("yFraction", (y+.5)/h);
			int color = painter.apply(params);
			g.setColor(new Color(color));
			g.fillRect(column, y, 1, 1);
		}
	}

	public void mouseDragged(MouseEvent e){ mouseMoved(e); }

	public void mouseMoved(MouseEvent e){
		int h = getHeight(), w = getWidth(), x = e.getX(), y = e.getY();
		params.put("mouseMovedWhen", Time.time());
		params.put("heightPixels", h);
		params.put("widthPixels", w);
		params.put("mouseXPixels", x);
		params.put("mouseYPixels", y);
		params.put("mouseXFraction", Math.max(0, Math.min(x/(w-1.), 1)));
		params.put("mouseYFraction", Math.max(0, Math.min(y/(h-1.), 1)));
		params.put("mouseIn", true);
	}

	public void mouseClicked(MouseEvent e){}

	public void mousePressed(MouseEvent e){
		params.put("mouseButton"+e.getButton(),true);
	}

	public void mouseReleased(MouseEvent e){
		params.remove("mouseButton"+e.getButton());
	}

	public void mouseEntered(MouseEvent e){
		if(params.containsKey("mouseYFraction")){
			params.put("mouseIn", true);
		}
	}

	public void mouseExited(MouseEvent e){
		params.remove("mouseIn");
	}

	public void keyTyped(KeyEvent e){}

	public void keyPressed(KeyEvent e){
		//String t = KeyEvent.getKeyText(e.getKeyCode());
		//System.out.println(t);
		//params.put(t,true);
		//System.out.println(e.getKeyChar());
		params.put(e.getKeyChar(),true);
	}

	public void keyReleased(KeyEvent e){
		//params.remove(KeyEvent.getKeyText(e.getKeyCode()));
		params.remove(e.getKeyChar());
	}

}

