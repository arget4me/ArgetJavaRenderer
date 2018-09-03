package com.argetgames.arget2d.input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse  implements MouseListener, MouseMotionListener{

	private static Mouse mouse;
	private int scale;
	private Point mousePosition;
	private Point[] mousePressPosition;
	private Point[] mouseReleasePosition;
	private boolean[] mousePressed;
	private boolean[] newClick;
	
	public enum MouseButton{
		LEFT, MIDDLE, RIGHT
	};
	
	private Mouse() {
		mousePosition = new Point(0, 0);
		mousePressPosition = new Point[3];
		mouseReleasePosition = new Point[3];
		mousePressed = new boolean[3];
		newClick = new boolean[3];
		scale = 1;
	}
	
	

	public static Mouse getMouse(){
		if(mouse == null)mouse = new Mouse();
		return mouse;
	}
	
	public static void setScale(int scale) {
		getMouse().scale = scale;
	}
	
	public static int getMouseX() {
		return getMouse().mousePosition.x/getMouse().scale;
	}
	
	public static int getMouseY() {
		return getMouse().mousePosition.y/getMouse().scale;
	}
	
	public boolean isButtonPress(MouseButton button){
		return mousePressed[button.ordinal()];
	}
	
	public boolean isButtonClicked(MouseButton button){
		boolean clicked = newClick[button.ordinal()];
		newClick[button.ordinal()] = false;
		return clicked;
	}
	
	public Point getPressPosition(MouseButton button){
		return mousePressPosition[button.ordinal()];
	}
	
	public Point getReleasePosition(MouseButton button){
		return mouseReleasePosition[button.ordinal()];
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//not handled for now.
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//not handled for now.
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//not handled for now.
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = (e.getButton() - 1);
		if(button >= 0 && button < 3){
			mousePressed[button] = true;
			newClick[button] = false;
			mousePressPosition[button] = mousePosition;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = (e.getButton() - 1);
		if(button >= 0 && button < 3){
			mousePressed[button] = false;
			newClick[button] = true;
			mouseReleasePosition[button] = mousePosition;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePosition = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = e.getPoint();
	}
	
}
