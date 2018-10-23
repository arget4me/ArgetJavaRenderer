package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;

public class CampaignEditorState extends GameState {
	
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> campaign = new ArrayList<String>();
	private int[] currentSelection = new int[2];
	private int switchRepeatDelay = 0;
	private final int delayTime = (int)(PlatformGame.global_ups * 0.2);
	private String selection = "";
	private boolean browsingMaps = true, ignoreSelect = false;
	private int currentIndex = 0;
	

	public CampaignEditorState(GameStateManager gsm) {
		super(gsm);
	}
	
	protected void switched(boolean active){
		if(active){
			if(Keyboard.getKey(KeyEvent.VK_ENTER)){
				ignoreSelect = true;
			}
			try {
				Scanner input = new Scanner(new File("res/maps/storyOrder"));
				campaign.clear();
			    while (input.hasNext()) {
				    String name = input.next();
					if(name.endsWith(Tilemap.FILE_TYPE)){
						name = name.substring(0, name.length() - ".".length() -Tilemap.FILE_TYPE.length());
						if(name.length() > 0){
							campaign.add(name);
						}
					}
			    }
			    input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			scanForMaps();
			if(list.isEmpty() && !campaign.isEmpty()){
				currentIndex = 1;
			}
			if(list.isEmpty() && campaign.isEmpty()){
				// TODO: Error handling if no maps exist.
			}
		}else {
			if(campaign.isEmpty())return;
			try {
				FileOutputStream fos = new FileOutputStream(new File("res/maps/storyOrder"));
				String output = "";
				for(int i = 0; i < campaign.size(); i++){
					output = campaign.get(i) + "." + Tilemap.FILE_TYPE + "\n";
					fos.write(output.getBytes(), 0, output.getBytes().length);
				}
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void scanForMaps(){
		list.clear();
		currentIndex = 0;
		currentSelection[0] = 0;
		currentSelection[1] = 0;
		switchRepeatDelay = 0;
		scanFolderForMaps(new File("res/maps/"));
	}
	
	private void scanFolderForMaps(File directory){
//		if(directory.isDirectory()){
//			for(File f : directory.listFiles() ){
//				scanFolderForMaps(f);
//			}
//		}else {
		for(File f : directory.listFiles() ) {
			if(!f.isFile())return;
			String name = f.getName();
			if(name.endsWith(Tilemap.FILE_TYPE)){
				name = name.substring(0, name.length() - ".".length() -Tilemap.FILE_TYPE.length());
				if(name.length() > 0 && !campaign.contains(name)  && !list.contains(name)){
					list.add(name);
				}
			}
		}
	}

	private void switchSelection(int delta, int index){
		if(switchRepeatDelay > 0)return;
		switchRepeatDelay += delayTime;
		if(index == 0){
			if(list.isEmpty())return;
			currentSelection[0] += delta;
			currentSelection[0] %= list.size();
			if(currentSelection[0] < 0)
				currentSelection[0] += list.size();
		}else if(index == 1){
			if(campaign.isEmpty())return;
			currentSelection[1] += delta;
			currentSelection[1] %= campaign.size();
			if(currentSelection[1] < 0)
				currentSelection[1] += campaign.size();
		}
	}
	
	private void replaceSelection(int delta, int index){
		if(switchRepeatDelay > 0)return;
		switchRepeatDelay += delayTime;
		
		if(index == 0){
			int prevIndex = currentSelection[0];
			currentSelection[0] += delta;
			currentSelection[0] %= list.size();
			if(currentSelection[0] < 0)
				currentSelection[0] += list.size();
			
			String next = list.get(currentSelection[0]);
			list.remove(prevIndex);
			list.add(prevIndex, next);
			list.remove(currentSelection[0]);
			list.add(currentSelection[0], selection);
			
		}else if(index == 1){
			int prevIndex = currentSelection[1];
			currentSelection[1] += delta;
			currentSelection[1] %= campaign.size();
			if(currentSelection[1] < 0)
				currentSelection[1] += campaign.size();
			
			String next = campaign.get(currentSelection[1]);
			campaign.remove(prevIndex);
			campaign.add(prevIndex, next);
			campaign.remove(currentSelection[1]);
			campaign.add(currentSelection[1], selection);
		}
	}
	
	private void moveIndex(int index){
		if(index == currentIndex)return;
		currentIndex = index;
		
		if(index == 0){
			currentSelection[0] = list.size();
			
			campaign.remove(currentSelection[1]);
			list.add(list.size(), selection);
			currentSelection[1] = 0;
		}else if(index == 1){
			currentSelection[1] = campaign.size();
			
			list.remove(currentSelection[0]);
			campaign.add(campaign.size(), selection);
			currentSelection[0] = 0;
		}
	}

	
	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
		
		if(switchRepeatDelay > 0)switchRepeatDelay--;
		if(Keyboard.getKey(KeyEvent.VK_ENTER)){
			if(ignoreSelect)return;
			ignoreSelect = true;
			if(selection.length() == 0){
				ArrayList<String> current = list;
				if(currentIndex == 1)current = campaign;
				if(current.size() > currentSelection[currentIndex]){
					selection = current.get(currentSelection[currentIndex]);
				}
			}else {
				selection = "";
			}
		}else {
			ignoreSelect = false;
		}
		
		if(selection.length() == 0){
			if(Keyboard.getKey(KeyEvent.VK_UP) || Keyboard.getKey(KeyEvent.VK_W))
				switchSelection(-1, currentIndex);
			else if(Keyboard.getKey(KeyEvent.VK_DOWN) || Keyboard.getKey(KeyEvent.VK_S))
				switchSelection(+1, currentIndex);
			if(Keyboard.getKey(KeyEvent.VK_LEFT) || Keyboard.getKey(KeyEvent.VK_A)){
				if(campaign.size() > 0)currentIndex = 1;
			}else if(Keyboard.getKey(KeyEvent.VK_RIGHT) || Keyboard.getKey(KeyEvent.VK_D)){
				if(list.size() > 0)currentIndex = 0;
			}
		}else {
			if(Keyboard.getKey(KeyEvent.VK_UP) || Keyboard.getKey(KeyEvent.VK_W))
				replaceSelection(-1, currentIndex);
			else if(Keyboard.getKey(KeyEvent.VK_DOWN) || Keyboard.getKey(KeyEvent.VK_S))
				replaceSelection(+1, currentIndex);
			if(Keyboard.getKey(KeyEvent.VK_LEFT) || Keyboard.getKey(KeyEvent.VK_A)){
				moveIndex(1);
			}else if(Keyboard.getKey(KeyEvent.VK_RIGHT) || Keyboard.getKey(KeyEvent.VK_D)){
				moveIndex(0);
			}
		}
			
	}
	
	private void drawList(Renderer2D renderer, ArrayList<String> list, String title, boolean active, int center, int top){
		
		PlatformGame.textRenderer.drawText(renderer, center + (2 - title.length()*6)/2, top + 1, 8, title, -1, 0xFF000000);
		PlatformGame.textRenderer.drawText(renderer, center + (0 - title.length()*6)/2, top + 0, 8, title, -1, 0xFFFFFFFF);
		top += 20;
		
		for(int i = 0; i < list.size(); i++){
			int color = 0xFF0000FF;
			String text = list.get(i);
			if(i == currentSelection[currentIndex] && active){
				color = 0xFF00FF00;
				if(selection.length() > 0) {
					text = "< " + text + " >";
				}
			}
			PlatformGame.textRenderer.drawText(renderer, center + (2 - text.length()*6)/2, top + 1 + 20*i, 8, text, -1, 0xFF000000);
			PlatformGame.textRenderer.drawText(renderer, center + (0 - text.length()*6)/2, top + 0 + 20*i, 8, text, -1, color);
		}
	}

	@Override
	public void draw(Renderer2D renderer) {
		drawList(renderer, list, "Maps", currentIndex == 0, PlatformGame.globalWidth*3/4, 50);
		drawList(renderer, campaign, "Campaign", currentIndex == 1, PlatformGame.globalWidth/4, 50);
	}

}
