package GoBang;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class Example
{
	//记录数据库里查到的文件名
	public static LinkedList<String>fileName=new LinkedList<>();
	public static JDBC jdbc;
	public static Register register=new Register();

	static {
		try {
			jdbc = new JDBC();
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static int dbnum=0;
	//public static Client client;
	public  static int akchsah=0;

	public static boolean isAI=false;

	public static int aiboard_type=1;
	public static LinkedList<Client>linkedClien=new LinkedList<>();

	public static LinkedList<Connention>linkedConnection=new LinkedList<>();
	//public static Connention connection;

	//public static boolean noConnection=false;
	public static boolean isRestart=false;
	public static boolean isRegret=false;
	public static boolean yoursRound=false;

	public static int webboard_type=1;

	public static boolean isConnected=false;

	public static String sendinfor="";
	public static boolean isSend=false;


	public static boolean inputEnd=false;

	//public static GameCentre gameCentre=new GameCentre();
	public static MainGame mainGame;
	public static DBlogin dBlogin=new DBlogin();

	public static Personal personal=new Personal();
	public static GameHall gameHall=new GameHall();

	public static AI ai=new AI();
	public static ReviewHall reviewHall=new ReviewHall();
	public static LinkedList<ReviewHall> linkedReviewHall=new LinkedList<>();

	public static LinkedList<LinkedList<Point>> historyComp=new LinkedList<>();

	public  static NetHall netHall=new NetHall();

	//public static  ReviewHall reviewHallFrame=new ReviewHall();

	static
	{
		linkedReviewHall.add(reviewHall);
		try {
			mainGame = new MainGame();
			mainGame.init();
			netHall.init();
			personal.init();
			//reviewHallFrame.init();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
