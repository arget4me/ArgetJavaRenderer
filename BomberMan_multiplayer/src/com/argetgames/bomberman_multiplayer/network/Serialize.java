package com.argetgames.bomberman_multiplayer.network;

public class Serialize {

	public static int deserializeBoolean(byte[] data, int index, boolean[] values){
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
	
	public static int serializeBoolean(byte[] data, int index, boolean[] values){
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
	
	
	public static int deserializeShort(byte[] data, int index, short[] values){
		for(int i = 0; i < values.length; i++){
			for(int shift = 0; shift < Short.BYTES; shift++){
				values[i] |= ((data[index++] & 0xFF) << (shift * 8));
			}
		}
		return index;
	}
	
	public static int serializeShort(byte[] data, int index, short[] values){
		for(int i = 0; i < values.length; i++){
			values[i] = 0;
			for(int shift = 0; shift < Short.BYTES; shift++){
				data[index++] = (byte)((values[i] >>> (shift * 8)) & 0xff);
			}
		}
		return index;
	}
	
	public static int deserializeInteger(byte[] data, int index, int[] values){
		for(int i = 0; i < values.length; i++){
			values[i] = 0;
			for(int shift = 0; shift < Integer.BYTES; shift++){
				values[i] |= ((data[index++] & 0xFF) << (shift * 8));
			}
		}
		return index;
	}
	
	public static int serializeInteger(byte[] data, int index, int[] values){
		for(int i = 0; i < values.length; i++){
			for(int shift = 0; shift < Integer.BYTES; shift++){
				data[index++] = (byte)((values[i] >>> (shift * 8)) & 0xff);
			}
		}
		return index;
	}

}
