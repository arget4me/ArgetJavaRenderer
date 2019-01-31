package com.argetgames.bomberman_multiplayer.network;

import java.util.HashMap;
import java.util.Map;

import com.argetgames.arget2d.input.Keyboard;

public class NetworkController {

	public static final byte[] DATA_CHECK_CODE = ("ANETCONTROLLER").getBytes();
	private int keysUsed = 0;
	private HashMap<Integer, Boolean> controlls = new HashMap<Integer, Boolean>();
	
	public NetworkController() {
		
	}
	
	//client should update to register key states.
	public void update(){
		for(Map.Entry<Integer, Boolean> c : controlls.entrySet()){
			c.setValue(Keyboard.getKey(c.getKey()));
		}
	}
	
	public void useKeyInput(int keyCode){
		if(!controlls.containsKey(keyCode)){
			controlls.put(keyCode, false);
			keysUsed++;
		}
	}
	
	public boolean getKey(int keyCode){
		if(controlls.containsKey(keyCode)){
			return controlls.get(keyCode);
		}
		return false;
	}
	
	private int deserializeBoolean(byte[] data, int index, boolean[] values){
		int shift = 0;
		for(int i = 0; i < values.length; i++){
			values[i] = ((data[index] >>> shift++) & 1) == 1;
			if(shift > 7) {
				shift = 0;
				index++;
			}
		}
		if(shift != 0)index++;//take up entire byte if any part of it is used.
		return index;
	}
	
	private int serializeBoolean(byte[] data, int index, boolean[] values){
		int shift = 0;
		for(int i = 0; i < values.length; i++){
			if(values[i]){
				data[index] |= (1 << shift);	
			}
			shift++;
			if(shift > 7) {
				shift = 0;
				index++;
			}
		}
		if(shift != 0)index++;//take up entire byte if any part of it is used.
		return index;
	}
	
	
	private int deserializeShort(byte[] data, int index, short[] values){
		for(int i = 0; i < values.length; i++){
			for(int shift = 0; shift < Short.BYTES; shift++){
				values[i] |= (data[index++] << (shift * 8));
			}
		}
		return index;
	}
	
	private int serializeShort(byte[] data, int index, short[] values){
		for(int i = 0; i < values.length; i++){
			for(int shift = 0; shift < Short.BYTES; shift++){
				data[index++] = (byte)((values[i] >>> (shift * 8)) & 0xff);
			}
		}
		return index;
	}
	
	private int deserializeInteger(byte[] data, int index, int[] values){
		for(int i = 0; i < values.length; i++){
			for(int shift = 0; shift < Integer.BYTES; shift++){
				values[i] |= (data[index++] << (shift * 8));
			}
		}
		return index;
	}
	
	private int serializeInteger(byte[] data, int index, int[] values){
		for(int i = 0; i < values.length; i++){
			for(int shift = 0; shift < Integer.BYTES; shift++){
				data[index++] = (byte)((values[i] >>> (shift * 8)) & 0xff);
			}
		}
		return index;
	}
	
	//the server parses data input.
	public void parseInput(byte[] data){
		if(data.length > DATA_CHECK_CODE.length + Integer.BYTES + Integer.BYTES){
			int index = 0;
			for(; index < DATA_CHECK_CODE.length; index++){
				if(data[index] != DATA_CHECK_CODE[index])return;
			}
			int numBooleans = 0;
			{
				int[] values = new int[2];//{numBytes, keysUsed};
				index = deserializeInteger(data, index, values);
				if(values[0] > 0 &&	
						data.length < index + values[0])return;
				numBooleans = values[1];
			}

			{
				boolean[] values = new boolean[numBooleans];
				index = deserializeBoolean(data, index, values);
				int i = 0;
				for(Map.Entry<Integer, Boolean> c : controlls.entrySet()){
					if(i >= numBooleans)break;
					c.setValue(values[i++]);
				}
			}
		}
	}
	
	public byte[] getInputData(){
		int numBytesForBooleans = keysUsed/8;
		if(keysUsed % 8 != 0)numBytesForBooleans++;
		int numBytes = DATA_CHECK_CODE.length + Integer.BYTES + Integer.BYTES + numBytesForBooleans;
		byte data[] = new byte[numBytes];

		int index = 0;
		for(; index < DATA_CHECK_CODE.length; index++){
			data[index] = DATA_CHECK_CODE[index];
		}
		
		{
			int[] values = {numBytes, keysUsed};
			index = serializeInteger(data, index, values);
		}
		{
			boolean[] values = new boolean[keysUsed];
			int i = 0;
			for(Map.Entry<Integer, Boolean> c : controlls.entrySet()){
				if(i >= keysUsed)break;
				values[i++] = c.getValue();
			}
			index = serializeBoolean(data, index, values);
		}
		
		return data;
	}
}
