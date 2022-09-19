package GoBang;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class AI
{
	HashMap<String,Integer> AI_sit_values =new HashMap<>();
	int[][]ChessValue=new int[19][19];

	int AIboard_type;
	public void init() {
		Example.mainGame.regret.setEnabled(false);
		//用example.isconnection标记是否需要联机
		int result = JOptionPane.showOptionDialog(Example.mainGame.f, "请选择您的棋色",
				"选择", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, new String[]{"黑", "白"}, "黑");
		Example.aiboard_type = result + 1;
		if (Example.aiboard_type == 1) {
			Example.yoursRound = true;
			Example.mainGame.chat.append("您执黑，对方执白\n");
		} else {
			Example.yoursRound = false;
			Example.mainGame.chat.append("您执白，对方执黑\n");
		}
		AIboard_type=(Example.aiboard_type==1)?2:1;

		if (AIboard_type==1)
			ai();
	}
	public void ai() {//人机对战，电脑怎么下棋判定
		//创建权值表
			AI_sit_values.put("1", 10);
			AI_sit_values.put("11", 100);
			AI_sit_values.put("111", 5000);
			AI_sit_values.put("1111", 8000);

			AI_sit_values.put("12", 5);
			AI_sit_values.put("112", 80);
			AI_sit_values.put("1112", 3000);
			AI_sit_values.put("11112", 10000);

			AI_sit_values.put("21", 11);
			AI_sit_values.put("211", 110);
			AI_sit_values.put("2111", 1100);
			AI_sit_values.put("21111", 11000);

			AI_sit_values.put("2", 20);
			AI_sit_values.put("22", 200);
			AI_sit_values.put("222", 8000);
			AI_sit_values.put("2222", 10000);

			AI_sit_values.put("221", 100);
			AI_sit_values.put("2221", 3000);
			AI_sit_values.put("22221", 12000);
			AI_sit_values.put("122", 5);
			AI_sit_values.put("1222", 500);
			AI_sit_values.put("12222", 10000);


		for(int i=0;i<Example.mainGame.board.length;i++) {
			for(int j=0;j<Example.mainGame.board[i].length;j++) {
				if(Example.mainGame.board[i][j]==0) {//判断是否有棋子
					StringBuilder code= new StringBuilder();
					int color=0;
					for(int k=i+1;k<Example.mainGame.board.length;k++) {//向右开始时遍历
						if(Example.mainGame.board[k][j]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[k][j];
								code.append(Example.mainGame.board[k][j]);
							}else if(color==Example.mainGame.board[k][j]) {
								code.append(Example.mainGame.board[k][j]);
							}else {
								code.append(Example.mainGame.board[k][j]);
								break;
							}
						}

					}
					Integer value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int k=i-1;k>0;k--) {//向左开始时遍历
						if(Example.mainGame.board[k][j]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[k][j];
								code.append(Example.mainGame.board[k][j]);
							}else if(color==Example.mainGame.board[k][j]) {
								code.append(Example.mainGame.board[k][j]);
							}else {
								code.append(Example.mainGame.board[k][j]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int k=j-1;k>0;k--) {//向上开始时遍历
						if(Example.mainGame.board[i][k]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[i][k];
								code.append(Example.mainGame.board[i][k]);
							}else if(color==Example.mainGame.board[i][k]) {
								code.append(Example.mainGame.board[i][k]);
							}else {
								code.append(Example.mainGame.board[i][k]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int k=j+1;k<Example.mainGame.board.length;k++) {//向下开始时遍历
						if(Example.mainGame.board[i][k]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[i][k];
								code.append(Example.mainGame.board[i][k]);
							}else if(color==Example.mainGame.board[i][k]) {
								code.append(Example.mainGame.board[i][k]);
							}else {
								code.append(Example.mainGame.board[i][k]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int o=i-1,k=j-1;k>0&&o>0;k--,o--) {//向左上开始时遍历
						if(Example.mainGame.board[o][k]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[o][k];
								code.append(Example.mainGame.board[o][k]);
							}else if(color==Example.mainGame.board[o][k]) {
								code.append(Example.mainGame.board[o][k]);
							}else {
								code.append(Example.mainGame.board[o][k]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int o=i+1,k=j-1;k>0&&o<Example.mainGame.board.length;k--,o++) {//向右上开始时遍历
						if(Example.mainGame.board[o][k]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[o][k];
								code.append(Example.mainGame.board[o][k]);
							}else if(color==Example.mainGame.board[o][k]) {
								code.append(Example.mainGame.board[o][k]);
							}else {
								code.append(Example.mainGame.board[o][k]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int o=i+1,k=j+1;k<Example.mainGame.board.length&&o<Example.mainGame.board.length;k++,o++) {//向右下开始时遍历
						if(Example.mainGame.board[o][k]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[o][k];
								code.append(Example.mainGame.board[o][k]);
							}else if(color==Example.mainGame.board[o][k]) {
								code.append(Example.mainGame.board[o][k]);
							}else {
								code.append(Example.mainGame.board[o][k]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
					code = new StringBuilder();
					color=0;
					value=null;
					for(int o=i-1,k=j+1;k<Example.mainGame.board.length&&o>0;k++,o--) {//向左下开始时遍历
						if(Example.mainGame.board[o][k]==0) {
							break;
						}else {
							if(color==0) {
								color=Example.mainGame.board[o][k];
								code.append(Example.mainGame.board[o][k]);
							}else if(color==Example.mainGame.board[o][k]) {
								code.append(Example.mainGame.board[o][k]);
							}else {
								code.append(Example.mainGame.board[o][k]);
								break;
							}
						}

					}
					value= AI_sit_values.get(code.toString());
					if(value != null) {
						ChessValue[i][j]+=value;
					}
				}
			}
		}
		//找到ChessValue中的最大值点让电脑下棋
		int max=0;
		int goalx = 0,goaly = 0;
		for(int i=0;i<Example.mainGame.board.length;i++) {
			for(int j=0;j<Example.mainGame.board.length;j++) {
				if(ChessValue[i][j]>max) {
					max=ChessValue[i][j];
					goalx=i;
					goaly=j;
				}
			}
		}
		//清空权值
		for(int i=0;i<Example.mainGame.board.length;i++) {
			for(int j=0;j<Example.mainGame.board.length;j++) {
				ChessValue[i][j]=0;
			}
		}
		if(max !=0) {
			Example.mainGame.board[goalx][goaly]=AIboard_type;
			Example.mainGame.chess_manual.add(new Point(goalx,goaly));
			Example.mainGame.chessBorad.repaint();
			String game_text = Example.ai.AIboard_type == 1 ? "黑方：" : "白方：";
			Example.mainGame.game_process.append("第" + Example.mainGame.chess_manual.size() + "步：" + game_text + "横" + (goalx+ 1) + "，" + "纵" + (goaly + 1) + ";" + "\n");
			Example.yoursRound=true;
			if (Function.isWin(goalx, goaly, Example.mainGame.board, Example.ai.AIboard_type)) {
				Example.mainGame.chess_manual.add(new Point(-1, -1));
				String text;
				text = (Example.ai.AIboard_type == 1 ? "黑棋胜利!!!" : "白棋胜利!!!");
				Example.mainGame.game_process.append(text);
				JOptionPane.showConfirmDialog(Example.mainGame.f, text, "结果", JOptionPane.DEFAULT_OPTION);
			}
		}
		else {
			Random r=new Random();
			int x=r.nextInt(19)+1;
			int y=r.nextInt(19)+1;
			Example.mainGame.board[x][y]=AIboard_type;
			Example.mainGame.chess_manual.add(new Point(x,y));
			Example.mainGame.chessBorad.repaint();
			String game_text = Example.ai.AIboard_type == 1 ? "黑方：" : "白方：";
			Example.mainGame.game_process.append("第" + Example.mainGame.chess_manual.size() + "步：" + game_text + "横" + (goalx+ 1) + "，" + "纵" + (goaly + 1) + ";" + "\n");
			Example.yoursRound=true;
		}

	}
}
