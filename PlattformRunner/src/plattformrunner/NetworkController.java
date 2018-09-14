package plattformrunner;


public class NetworkController {

	public enum NetworkKey{
		LEFT, RIGHT, JUMP
	}

	private boolean LEFT, RIGHT, JUMP;

	
	public NetworkController(){
		LEFT = false;
		RIGHT = false;
		JUMP = false;
	}
	int i = 0;
	
	public void registerInput(String msg){
		i++;
		String[] parts = msg.split(",");
		if(parts.length >= 4){
			int left = Integer.parseInt(parts[1]);
			int right = Integer.parseInt(parts[2]);
			int jump  = Integer.parseInt(parts[3]);
			
			if(left == 1) LEFT = true;
			else LEFT = false;
			
			if(right == 1) RIGHT = true;
			else RIGHT = false;

			if(jump == 1) JUMP = true;
			else JUMP = false;
			if(i % 30 == 0)
				System.out.println("Left: " + LEFT + ", RIGHT: " + RIGHT + ", JUMP: " + JUMP);
		}
	}
	
	public boolean get(NetworkKey key){
		switch(key){
		case LEFT: return LEFT;
		case RIGHT: return RIGHT;
		case JUMP: return JUMP;
		default: return false;
		}
	}
	
	
}
