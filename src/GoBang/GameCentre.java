package GoBang;


import java.io.IOException;


public class GameCentre extends DBlogin {

	GameCentre(){
		Example.dBlogin.init();
	}


	public static void main(String[] args) throws IOException {
			new GameCentre();
	}
}
