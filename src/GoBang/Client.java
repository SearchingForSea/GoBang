package GoBang;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;


public class Client extends Thread {
	Socket s ;
	String ip;
	String port;
	Client(String ip,String port) {
		this.ip=ip;
		this.port=port;
	}
	//封装
	public void run() {

		try {
			s=new Socket(ip, Integer.parseInt(port));
		} catch (IOException e) {
			Example.mainGame.f.dispose();
			Example.gameHall.gameHallFrame.setVisible(true);
			JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame, "对局未创建", "结果", JOptionPane.DEFAULT_OPTION);
			//throw new RuntimeException(e);
		}
		Example.mainGame.chat.setText("");
		Example.mainGame.chat.append("连接成功!!!\n等待对方选择棋色\n");
		try {
			InputStream istype = s.getInputStream();
			byte[] bys = new byte[1024];
			int len = istype.read(bys);
			String datatype = new String(bys, 0, len);
			Example.webboard_type = (datatype.equals("黑") ? 2 : 1);
			if (Example.webboard_type == 1)
			{
				Example.yoursRound = true;
				Example.mainGame.chat.append("您执黑，对方执白\n");
			}
			else Example.mainGame.chat.append("您执白，对方执黑\n");

			restartFun();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Example.isConnected = true;

		String texthuihe=(Example.yoursRound)?"您的回合：\n":"对方回合：\n";
		Example.mainGame.game_process.append(texthuihe);

		while (true) {

			try {

				InputStream is = s.getInputStream();
				OutputStream os = s.getOutputStream();
				if (is.available() != 0) {
					byte[] bys = new byte[1024];
					int len = is.read(bys);
					String data = new String(bys, 0, len);
					if (data.length() > 9 && (new String(bys, 0, 6).equals("++++++"))) {
						int type = Integer.parseInt(data.substring(data.indexOf(";") + 1, data.indexOf(".")));//xpos
						int xpos = Integer.parseInt(data.substring(6, data.indexOf(",")));//ypos
						int ypos = Integer.parseInt(data.substring(data.indexOf(",") + 1, data.indexOf(";")));//type

						Example.mainGame.board[xpos][ypos] = type;
						Example.mainGame.chess_manual.add(new Point(xpos, ypos));
						Example.mainGame.chessBorad.repaint();

						String game_text = (type == 1) ? "黑方：" : "白方：";
						Example.mainGame.game_process.append("第" + Example.mainGame.chess_manual.size() + "步："
								+ game_text + "横" + (xpos + 1) + "，"
								+ "纵" + (ypos + 1) + ";" + "\n");
						Example.mainGame.game_process.append("您的回合：\n");
						Example.yoursRound = true;

						if (Function.isWin(xpos, ypos, Example.mainGame.board, type)) {
							Example.mainGame.chess_manual.add(new Point(-1, -1));
							String text;
							text = (type == 1 ? "黑棋胜利!!!" : "白棋胜利!!!");
							Example.mainGame.game_process.append(text);
							JOptionPane.showConfirmDialog(Example.mainGame.f, text, "结果", JOptionPane.DEFAULT_OPTION);
						}
					}else if (data.equals("++++++-")) {
						Example.mainGame.f.dispose();
						Example.gameHall.gameHallFrame.setVisible(true);
						JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame, "对方已断开连接.", "通知", JOptionPane.DEFAULT_OPTION);

//						assert Example.linkedConnection.peek() != null;
//						Example.linkedConnection.peek().interrupt();
//						Example.linkedConnection.remove();
//
//						Example.yoursRound=false;
//						Example.isSend=false;
//						Example.sendinfor="";
//						Example.mainGame.chat.setText("");
						try {
							assert Example.linkedClien.peek() != null;
							Example.linkedClien.peek().s.close();
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
						finally {
							assert Example.linkedClien.peek() != null;
							Example.linkedClien.peek().interrupt();

							Example.linkedClien.remove();
							System.out.println(Example.linkedClien.size());
							Example.isConnected = false;
							Example.yoursRound = false;
							Example.mainGame.chat.setText("");
							Example.mainGame.game_process.setText("");
						}
					}else if(data.equals("++++++reg"))
					{
						int result=JOptionPane.showOptionDialog(Example.mainGame.f,"是否同意对方悔棋","选项对话框",
								JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
								null,new String[]{"同意","不同意"},"不同意");
						switch (result)
						{
							case 0:
								assert Example.mainGame.chess_manual.peekLast() != null;//断言
								Example.mainGame.board[Example.mainGame.chess_manual.peekLast().x][Example.mainGame.chess_manual.peekLast().y]=0;
								Example.mainGame.game_process.append("悔棋成功！\n");
								Example.mainGame.chess_manual.pollLast();
								Example.mainGame.chessBorad.repaint();
								Example.sendinfor="agr";
								os.write(("++++++" + Example.sendinfor).getBytes());
								os.flush();
								Example.yoursRound=false;
								Example.isSend=false;
								break;
							case 1:
								Example.sendinfor="dis";
								os.write(("++++++" + Example.sendinfor).getBytes());
								os.flush();
								Example.isSend=false;
								break;
						}
					}
					else if (data.equals("++++++agr"))
					{

						assert Example.mainGame.chess_manual.peekLast() != null;//断言
						Example.mainGame.board[Example.mainGame.chess_manual.peekLast().x][Example.mainGame.chess_manual.peekLast().y] = 0;
						Example.mainGame.game_process.append("悔棋成功！\n您的回合：");
						Example.mainGame.chess_manual.pollLast();
						Example.mainGame.chessBorad.repaint();
						Example.yoursRound=true;
					}
					else if(data.equals("++++++dis"))
					{
						JOptionPane.showConfirmDialog(Example.mainGame.f, "对方不同意悔棋", "结果", JOptionPane.DEFAULT_OPTION);
						Example.mainGame.game_process.append("悔棋失败！\n");
						Example.mainGame.game_process.append("对方回合：\n");
					}
					else if(data.equals("++++++res"))
					{
						int result=JOptionPane.showOptionDialog(Example.mainGame.f,"是否同意重新开始","选项对话框",
								JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
								null,new String[]{"同意","不同意"},"不同意");
						switch (result)
						{
							case 0:
								Example.mainGame.Restart();
								Example.mainGame.chessBorad.repaint();
								Example.sendinfor="ags";
								os.write(("++++++" + Example.sendinfor).getBytes());
								os.flush();
								Example.isSend=false;
								Example.isRestart=true;
								break;
							case 1:
								Example.sendinfor="ds";
								Example.mainGame.game_process.append("您已经拒绝重新开始！\n");
								os.write(("++++++" + Example.sendinfor).getBytes());
								os.flush();
								Example.isSend=false;
								break;
						}
					}
					else if(data.equals("++++++ags"))
					{
						Example.mainGame.restart.setEnabled(true);
						Example.mainGame.Restart();
						Example.mainGame.chessBorad.repaint();
					}
					else if(data.equals("++++++ds"))
					{
						Example.mainGame.restart.setEnabled(true);
						JOptionPane.showConfirmDialog(Example.mainGame.f, "对方不同意重新开始", "结果", JOptionPane.DEFAULT_OPTION);
						Example.mainGame.game_process.append("对方不同意重新开始");
					}
					else {
						if(Example.isRestart)
						{
							Example.webboard_type = (data.equals("黑") ? 2 : 1);
							if (Example.webboard_type == 1)
							{
								Example.yoursRound = true;
								Example.mainGame.chat.append("您执黑,对方执白\n");
							}
							else
							{
								Example.yoursRound = false;
								Example.mainGame.chat.append("您执白。对方执黑\n");
							}
							String texthuihestart=(Example.yoursRound)?"您的回合：\n":"对方回合：\n";
							Example.mainGame.game_process.append(texthuihestart);
							Example.isRestart=false;
						}
						else {
							String chatInfor=new String(data);
							int j=0;
							for(int i=0;i<chatInfor.length();i++)
							{
								Example.mainGame.chat.append(String.valueOf(chatInfor.charAt(i)));
								j++;
								if(j==23)
								{
									Example.mainGame.chat.append("\n"+"  ");
									j=2;
								}

							}
							Example.mainGame.chat.append("\n");
						}
					}
				} else {
					if (Example.inputEnd) {
						os.write((Example.dBlogin.userName + ": " + Example.mainGame.inputchat.getText()).getBytes());
						os.flush();
						Example.inputEnd = false;
						Example.mainGame.inputchat.setText("");
					} else if (Example.isSend) {
						//System.out.println("正在传输");
						os.write(("++++++" + Example.sendinfor).getBytes());
						os.flush();
						Example.isSend = false;
						Example.sendinfor = "";
						Example.yoursRound = false;
						if(!Example.isRegret)
							Example.mainGame.game_process.append("对方回合：\n");
						Example.isRegret=false;
					}
				}
				//Example.mainGame.chat.append(Example.dBlogin.userName + is.readUTF());
			} catch (IOException e) {

//				assert Example.linkedClien.peek() != null;
//				Example.linkedClien.peek().interrupt();
//				Example.linkedClien.remove();
//
//				Example.mainGame.f.dispose();
//				JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame,"对方已经断开连接");
//
//				Example.isConnected = false;
//				Example.yoursRound = false;
//				Example.isSend = false;
//				Example.sendinfor = "";
//				Example.mainGame.chat.setText("");
				throw new RuntimeException(e);
			}
		}
	}

	static void restartFun() {
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				Example.mainGame.board[i][j]=0;
		Example.mainGame.board_type=1;
		Example.mainGame.game_process.setText("");
		while(Example.mainGame.chess_manual.size()!=0)
			Example.mainGame.chess_manual.pollLast();

		Example.mainGame.chessBorad.repaint();
	}
}
