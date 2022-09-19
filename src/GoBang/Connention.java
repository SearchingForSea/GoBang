package GoBang;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connention extends Thread {
	//创建服务器SOCKET对象

	Socket s = null;
	ServerSocket ss = null;
	String port;

	public Connention(String port) throws IOException {
		this.port=port;
	}

	@Override
	public void run() {

		try {
			ss = new ServerSocket(Integer.parseInt(port));
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
		Example.mainGame.chat.append("等待连接。。。\n");
		try {
			s = ss.accept();

			Client.restartFun();
			Example.mainGame.game_process.setText("");
			Example.mainGame.chat.append("连接成功!!!\n");

			int result = JOptionPane.showOptionDialog(Example.mainGame.f, "请选择您的棋色",
					"选择", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
					null, new String[]{"黑", "白"}, "黑");
			Example.webboard_type = result + 1;
			Example.isConnected = true;

			OutputStream ostype = s.getOutputStream();
			if (Example.webboard_type == 1) {
				ostype.write("黑".getBytes());
				ostype.flush();
				Example.yoursRound = true;
				Example.mainGame.chat.append("您执黑，对方执白\n");
			} else {
				ostype.write("白".getBytes());
				ostype.flush();
				Example.yoursRound = false;
				Example.mainGame.chat.append("您执白，对方执黑\n");
			}
			String text = (Example.yoursRound) ? "您的回合：\n" : "对方回合：\n";
			Example.mainGame.game_process.append(text);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		while (true) {
//			try {
//				Socket tem = ss.accept();
//			} catch (IOException e) {
//				Example.mainGame.f.dispose();
//				JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame, "对方已断开连接.", "通知", JOptionPane.DEFAULT_OPTION);
//
//				assert Example.linkedConnection.peek() != null;
//				Example.linkedConnection.peek().interrupt();
//				Example.linkedConnection.remove();
//
//
//				Example.isConnected = false;
//				Example.yoursRound = false;
//				Example.isSend = false;
//				Example.sendinfor = "";
//				Example.mainGame.chat.setText("");
//			}

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
;
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
//

//						assert Example.linkedConnection.peek() != null;
//						Example.linkedConnection.peek().interrupt();
//						Example.linkedConnection.remove();
//						System.out.println(Example.akchsah);
						if(Example.linkedConnection.size()!=0)
						{
							assert Example.linkedConnection.peek() != null;
							try {
								Example.linkedConnection.peek().ss.close();
								assert Example.linkedConnection.peek() != null;
								if (Example.linkedConnection.peek().s!=null)
									Example.linkedConnection.peek().s.close();
							} catch (IOException ex) {
								throw new RuntimeException(ex);
							}
							finally {
								assert Example.linkedConnection.peek() != null;
								Example.linkedConnection.peek().interrupt();
								Example.linkedConnection.remove();
								Example.isConnected = false;
								Example.yoursRound = false;
								Example.mainGame.chat.setText("");
								Example.mainGame.game_process.setText("");
								Example.isSend=false;
								Example.gameHall.gameHallFrame.setVisible(true);
							}

						}
					}
					else if(data.equals("++++++reg"))
					{
						int result=JOptionPane.showOptionDialog(Example.mainGame.f,"是否同意对方悔棋","选项对话框",
								JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
								null,new String[]{"同意","不同意"},"不同意");
						switch (result)
						{
							case 0:
								assert Example.mainGame.chess_manual.peekLast() != null;//断言
								Example.mainGame.board[Example.mainGame.chess_manual.peekLast().x][Example.mainGame.chess_manual.peekLast().y]=0;
								Example.mainGame.game_process.append("对方悔棋成功！\n");
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
								Example.mainGame.game_process.append("对方悔棋失败！\n");
								os.write(("++++++" + Example.sendinfor).getBytes());
								os.flush();
								Example.isSend=false;
								break;
						}
					}
					else if (data.equals("++++++agr"))
					{
						Example.mainGame.regret.setEnabled(true);

						assert Example.mainGame.chess_manual.peekLast() != null;//断言
						Example.mainGame.board[Example.mainGame.chess_manual.peekLast().x][Example.mainGame.chess_manual.peekLast().y] = 0;
						Example.mainGame.game_process.append("您悔棋成功！\n您的回合：\n");
						Example.mainGame.chess_manual.pollLast();
						Example.mainGame.chessBorad.repaint();
						Example.yoursRound=true;
					}
					else if(data.equals("++++++dis"))
					{
						Example.mainGame.regret.setEnabled(true);
						JOptionPane.showConfirmDialog(Example.mainGame.f, "对方不同意悔棋", "结果", JOptionPane.DEFAULT_OPTION);
						Example.mainGame.game_process.append("您悔棋失败！\n");
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
								int result2 = JOptionPane.showOptionDialog(Example.mainGame.f, "请选择您的棋色",
										"选择", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
										null, new String[]{"黑", "白"}, "黑");
								Example.webboard_type = result2 + 1;;

								OutputStream ostype = s.getOutputStream();
								if (Example.webboard_type == 1) {
									ostype.write("黑".getBytes());
									ostype.flush();
									Example.yoursRound = true;
									Example.mainGame.chat.append("您执黑，对方执白\n");
								} else {
									ostype.write("白".getBytes());
									ostype.flush();
									Example.yoursRound = false;
									Example.mainGame.chat.append("您执白，对方执黑\n");
								}
								String text = (Example.yoursRound) ? "您的回合：\n" : "对方回合：\n";
								Example.mainGame.game_process.append(text);
								Example.isSend=false;
								Example.isRestart=false;
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
						Example.mainGame.Restart();
						Example.mainGame.chessBorad.repaint();
						int result = JOptionPane.showOptionDialog(Example.mainGame.f, "请选择您的棋色",
								"选择", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
								null, new String[]{"黑", "白"}, "黑");
						Example.webboard_type = result + 1;

						OutputStream ostype = s.getOutputStream();
						if (Example.webboard_type == 1) {
							ostype.write("黑".getBytes());
							ostype.flush();
							Example.yoursRound = true;
							Example.mainGame.chat.append("您执黑，对方执白\n");
						} else {
							ostype.write("白".getBytes());
							ostype.flush();
							Example.yoursRound = false;
							Example.mainGame.chat.append("您执白，对方执黑\n");
						}
						String text = (Example.yoursRound) ? "您的回合：\n" : "对方回合：\n";
						Example.mainGame.restart.setEnabled(true);
						Example.mainGame.game_process.append(text);
						Example.isSend=false;
						//Example.isRestart=false;
					}
					else if(data.equals("++++++ds"))
					{
						Example.mainGame.restart.setEnabled(true);
						JOptionPane.showConfirmDialog(Example.mainGame.f, "对方不同意重新开始", "结果", JOptionPane.DEFAULT_OPTION);

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
							else Example.mainGame.chat.append("您执白。对方执黑\n");
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
						Example.isRestart=false;
						Example.isSend = false;
						Example.sendinfor = "";
						Example.yoursRound = false;
						if(!Example.isRegret)
							Example.mainGame.game_process.append("对方回合：\n");
						Example.isRegret=false;
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
