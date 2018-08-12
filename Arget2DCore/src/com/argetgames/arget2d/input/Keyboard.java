package com.argetgames.arget2d.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Keyboard implements KeyListener{
	
	private static Keyboard keyboard;
	protected Map<Integer, Boolean> keys;
	
	private Keyboard() {
		keys = new HashMap<Integer, Boolean>();
	}
	
	public static Keyboard getKeyboard(){
		if(keyboard == null)keyboard = new Keyboard();
		return keyboard;
	}
	
	public static boolean getKey(int keyCode){
		if(!keyboard.keys.containsKey(keyCode)){
			keyboard.registerKey(keyCode, false);
			return false;
		}
		return keyboard.keys.get(keyCode);
	}
	
	protected void registerKey(int keyCode, boolean state){
		keys.put(keyCode, state);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(keys.containsKey(e.getKeyCode())){
			keys.put(e.getKeyCode(), true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(keys.containsKey(e.getKeyCode())){
			keys.put(e.getKeyCode(), false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
