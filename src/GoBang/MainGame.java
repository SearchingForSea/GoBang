package GoBang;

import sun.util.resources.LocaleData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class MainGame
{
	final JFrame f=new JFrame("五子棋");
	//声明BufferedImage对象记录图片,并初始化
	BufferedImage black;
	BufferedImage white;
	BufferedImage table=ImageIO.read(new File("image\\table.png"));

	final int TABLE_WIDTH=900;
	final int TABLE_HEIGHT=893;

	//声明棋盘横纵数为19
	final int BOARD_SIZE=19;

	//声明每个棋子占用棋盘的比率
	final int ONE_TABLE_WIDTH=45;

	//声明棋子对于x方向和y方向的偏移量
	final int X_OFFSET=26;
	final int Y_OFFSET=18;

	//右边聊天和记录框的参数
	int RIGHT_WIDTH=270;
	int RIGHT_HEIGHT=500;

	//声明二维数组,0为空，1为黑，2为白
	int[][]board=new int[BOARD_SIZE][BOARD_SIZE];

	//记录棋谱
	LinkedList<Point> chess_manual=new LinkedList<>();
	//声明变量记录当前下棋的颜色
	int board_type=1;//默认黑
	int saveNum=1;//使复盘界面只能开启一次

	public MainGame() throws IOException {
	}

	//
	void Restart()
	{
		for(int i=0;i<BOARD_SIZE;i++)
			for(int j=0;j<BOARD_SIZE;j++)
				board[i][j]=0;
		board_type=1;
		game_process.setText("");
		while(chess_manual.size()!=0)
			chess_manual.pollLast();
		saveNum=1;
		chat.setText("");
		inputchat.setText("");
	}
	//自定义类继承Canvas
	class ChessBorad extends Canvas
	{
		public void paint(Graphics g)
		{
			//绘图
			g.drawImage(table,0,0,null);
			//绘制棋子
			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					//绘制黑棋
					if(board[i][j]==1){
						g.setColor(Color.black);
						g.drawOval(i*ONE_TABLE_WIDTH+X_OFFSET,j*ONE_TABLE_WIDTH+Y_OFFSET,40,40);
						g.fillOval(i*ONE_TABLE_WIDTH+X_OFFSET,j*ONE_TABLE_WIDTH+Y_OFFSET,40,40);
					}
					//绘制白棋
					if(board[i][j]==2){
						g.setColor(Color.WHITE);
						g.drawOval(i*ONE_TABLE_WIDTH+X_OFFSET,j*ONE_TABLE_WIDTH+Y_OFFSET,40,40);
						g.fillOval(i*ONE_TABLE_WIDTH+X_OFFSET,j*ONE_TABLE_WIDTH+Y_OFFSET,40,40);
					}
				}
			}
		}
		public void update(Graphics g){
			//双缓冲技术
			Image DbBuffer=createImage(getWidth(),getHeight());
			Graphics GraImage=DbBuffer.getGraphics();
			paint(GraImage);
			GraImage.dispose();
			g.drawImage(DbBuffer,0,0,null);
		}
	}
	ChessBorad chessBorad=new ChessBorad();

	//包含棋盘和三个按钮的panel
	JPanel middle=new JPanel();

	//声明底部用到的组件
	JPanel jp1=new JPanel();
	JButton restart=new JButton("重新开始");
	JButton regret=new JButton("悔棋");

	JButton returnButton=new JButton("退出");

	JButton review=new JButton("复盘");

	JButton saveReview=new JButton("保存");

	//文本域
	JTextArea game_process=new JTextArea();
	//输入框
	//聊天
	JTextArea chat=new JTextArea();
	JTextField inputchat=new JTextField();
	JButton confirm_send=new JButton("发送");


	public void init() throws IOException {
		//中
		saveReview.setPreferredSize(new Dimension(120,50));
		restart.setPreferredSize(new Dimension(120,50));
		regret.setPreferredSize(new Dimension(120,50));
		review.setPreferredSize(new Dimension(120,50));
		returnButton.setPreferredSize(new Dimension(120,50));
		restart.setFont(new Font("TimesRoman",Font.BOLD,20));
		regret.setFont(new Font("TimesRoman",Font.BOLD,20));
		review.setFont(new Font("TimesRoman",Font.BOLD,20));
		returnButton.setFont(new Font("TimesRoman",Font.BOLD,20));
		saveReview.setFont(new Font("TimesRoman",Font.BOLD,20));

		inputchat.setFont(new Font("TimesRoman",Font.BOLD,20));
		chat.setFont(new Font("TimesRoman",Font.BOLD,18));

		jp1.add(restart);
		jp1.add(saveReview);
		jp1.add(review);
		jp1.add(regret);
		jp1.add(returnButton);

		middle.setLayout(new BorderLayout());
		middle.add(jp1,BorderLayout.SOUTH);

		//组装棋盘
		//table=ImageIO.read(new File("D:\\CODE\\IJ\\GoBang\\image\\table.png"));
		chessBorad.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
		middle.add(chessBorad,BorderLayout.CENTER);
		f.add(middle,BorderLayout.CENTER);

		//右
		JLabel inforLeft=new JLabel("对局信息");
		JLabel leftlabel=new JLabel();
		leftlabel.setPreferredSize(new Dimension(260,20));
		inforLeft.setFont(new Font("TimesRoman",Font.BOLD,18));
		inforLeft.setPreferredSize(new Dimension(260,50));
		JPanel left=new JPanel();
		game_process.setPreferredSize(new Dimension(230,2000));
		game_process.setFont(new Font("TimesRoman",Font.BOLD,18));
		JScrollPane jScrollPane=new JScrollPane(game_process);
		left.setLayout(new BorderLayout());
		left.add(inforLeft,BorderLayout.NORTH);
		left.add(jScrollPane,BorderLayout.CENTER);
		left.add(leftlabel,BorderLayout.SOUTH);
		f.add(left,BorderLayout.WEST);

		chat.setPreferredSize(new Dimension(260,815));
		JPanel chatSendPanel=new JPanel();
		JPanel chatPanel=new JPanel();

		confirm_send.setFont(new Font("TimesRoman",Font.BOLD,18));
		confirm_send.setPreferredSize(new Dimension(80,50));
		inputchat.setPreferredSize(new Dimension(180,50));
		JLabel infor=new JLabel("聊天");
		JLabel rightlabel=new JLabel();
		rightlabel.setPreferredSize(new Dimension(260,20));
		infor.setFont(new Font("TimesRoman",Font.BOLD,18));
		infor.setPreferredSize(new Dimension(260,50));
		chatSendPanel.setLayout(new BorderLayout());
		chatSendPanel.add(confirm_send,BorderLayout.EAST);
		chatSendPanel.add(inputchat);


		JPanel rightmid=new JPanel();
		rightmid.setLayout(new BorderLayout());
		rightmid.add(chat,BorderLayout.NORTH);
		rightmid.add(chatSendPanel);
		rightmid.setBorder(new LineBorder(Color.black,1));
		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(rightlabel,BorderLayout.SOUTH);
		chatPanel.add(infor,BorderLayout.NORTH);
		chatPanel.add(rightmid,BorderLayout.CENTER);
		chatPanel.setPreferredSize(new Dimension(260,945));

		//middle.setBorder(new LineBorder(Color.white,5));
		chatPanel.setBorder(new LineBorder(Color.white,10));
		left.setBorder(new LineBorder(Color.white,3));
		middle.setBorder(new LineBorder(Color.white,10));

		f.add(chatPanel,BorderLayout.EAST);


		chessBorad.addMouseListener(new MouseAdapter() {
			//鼠标点击
			@Override
			public void mousePressed(MouseEvent e) {
				if (!Example.isConnected && !Example.isAI) {
					int xpos = (e.getX() - X_OFFSET) / ONE_TABLE_WIDTH;
					int ypos = (e.getY() - Y_OFFSET) / ONE_TABLE_WIDTH;

					if (board[xpos][ypos] == 0) {
						saveNum = 1;
						board[xpos][ypos] = board_type;

						chess_manual.add(new Point(xpos, ypos));
						String game_text = board_type == 1 ? "黑方：" : "白方：";
						game_process.append("第" + chess_manual.size() + "步：" + game_text + "横" + (xpos + 1) + "，" + "纵" + (ypos + 1) + ";" + "\n");
						chessBorad.repaint();

						if (Function.isWin(xpos, ypos, board, board_type)) {
							chess_manual.add(new Point(-1, -1));
							String text;
							text = (board_type == 1 ? "黑棋胜利!!!" : "白棋胜利!!!");
							game_process.append(text);
							JOptionPane.showConfirmDialog(f, text, "结果", JOptionPane.DEFAULT_OPTION);

						}
						board_type = (board_type == 1 ? 2 : 1);
					}
				} else if (Example.isConnected) {
					if (Example.yoursRound) {
						int xpos = (e.getX() - X_OFFSET) / ONE_TABLE_WIDTH;
						int ypos = (e.getY() - Y_OFFSET) / ONE_TABLE_WIDTH;

						if (board[xpos][ypos] == 0) {
							saveNum = 1;
							board[xpos][ypos] = Example.webboard_type;

							chess_manual.add(new Point(xpos, ypos));
							String game_text = Example.webboard_type == 1 ? "黑方：" : "白方：";
							game_process.append("第" + chess_manual.size() + "步：" + game_text + "横" + (xpos + 1) + "，" + "纵" + (ypos + 1) + ";" + "\n");
							chessBorad.repaint();


							if (Example.isConnected) {
								Example.sendinfor = (xpos + "," + ypos + ";" + Example.webboard_type + ".");
								Example.isSend = true;
							}
							//chess_manual.add(new Point(xpos, ypos));
							//game_process.append("第" + chess_manual.size() + "步：" + game_text + "横" + (xpos + 1) + "，" + "纵" + (ypos + 1) + ";" + "\n");
							chessBorad.repaint();

							if (Function.isWin(xpos, ypos, board, Example.webboard_type)) {
								chess_manual.add(new Point(-1, -1));
								String text;
								text = (Example.webboard_type == 1 ? "黑棋胜利!!!" : "白棋胜利!!!");
								game_process.append(text);
								JOptionPane.showConfirmDialog(f, text, "结果", JOptionPane.DEFAULT_OPTION);

							}
						}
					}
				} else {

					if (Example.yoursRound) {
						int xpos = (e.getX() - X_OFFSET) / ONE_TABLE_WIDTH;
						int ypos = (e.getY() - Y_OFFSET) / ONE_TABLE_WIDTH;

						if (board[xpos][ypos] == 0) {
							saveNum = 1;
							board[xpos][ypos] = Example.aiboard_type;

							chess_manual.add(new Point(xpos, ypos));
							String game_text = Example.aiboard_type == 1 ? "黑方：" : "白方：";
							game_process.append("第" + chess_manual.size() + "步：" + game_text + "横" + (xpos + 1) + "，" + "纵" + (ypos + 1) + ";" + "\n");
							chessBorad.repaint();

							Example.yoursRound = false;

							if (Function.isWin(xpos, ypos, board, Example.aiboard_type)) {
								chess_manual.add(new Point(-1, -1));
								String text;
								text = (Example.aiboard_type == 1 ? "黑棋胜利!!!" : "白棋胜利!!!");
								game_process.append(text);
								JOptionPane.showConfirmDialog(f, text, "结果", JOptionPane.DEFAULT_OPTION);

							}
						}
					}
					if (!Example.yoursRound&&chess_manual.peekLast().x!=-1) {
						Example.ai.ai();
					}
				}
			}
		});
		restart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Example.isConnected&&Example.mainGame.restart.isEnabled()) {
					Example.mainGame.restart.setEnabled(false);
					Example.isRestart=true;
					Example.mainGame.game_process.append("等待对方回应重新开始请求\n");
					Example.sendinfor="res";
					Example.isSend = true;
				}
				else {
					Restart();
					chessBorad.repaint();
					if(Example.isAI)
						Example.ai.init();
				}
			}
		});

		review.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(chess_manual.size()==0)
				{
					JOptionPane.showConfirmDialog(f,"Sorry,您还没有下棋.","通知",JOptionPane.DEFAULT_OPTION);
				}
				//	复盘能多次开
				else {
					try {
						new ReviewGame().reviewInit(chess_manual);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}


			}
		});
		regret.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (regret.isEnabled())
				{
					if (Example.isConnected) {
						if(!Example.yoursRound)
						{
							Example.mainGame.regret.setEnabled(false);
							Example.isRegret=true;
							Example.mainGame.game_process.append("等待对方回应悔棋请求\n");
							Example.sendinfor="reg";
							Example.isSend = true;
						}
					}
					else {
						if(chess_manual.size()==0)
						{
							JOptionPane.showConfirmDialog(f,"Sorry,您还没有下棋.","通知",JOptionPane.DEFAULT_OPTION);
						}
						else
						{
							String text="";
							//因为一方下棋结束后,board_type已经切换至另一方，所以需要变换
							if(board_type==2)text="黑方";
							if(board_type==1)text="白方";
							int result=JOptionPane.showOptionDialog(f,"是否同意"+text+"悔棋","选项对话框",
									JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
									null,new String[]{"同意","不同意"},"不同意");
							switch (result)
							{
								case 0:
									assert chess_manual.peekLast() != null;//断言
									board[chess_manual.peekLast().x][chess_manual.peekLast().y]=0;
									game_process.append("悔棋成功！\n");
									chess_manual.pollLast();
									chessBorad.repaint();
									board_type=(board_type==1?2:1);
									break;
								case 1:
									break;
							}
						}
					}
				}
			}
		});
		returnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Example.gameHall.gameHallFrame.setVisible(true);
				f.dispose();
				if(Example.isAI)
				{
					Example.isAI=false;
					Example.mainGame.chat.setText("");
					Example.mainGame.regret.setEnabled(true);
				}
				if (Example.isConnected) {
					Example.sendinfor="-";
					Example.isSend = true;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				}
				if(Example.linkedConnection.size()!=0)
				{
					assert Example.linkedConnection.peek() != null;
					try {
						Example.linkedConnection.peek().ss.close();
//						assert Example.linkedConnection.peek() != null;
//						if (Example.linkedConnection.peek().s!=null)
//							Example.linkedConnection.peek().s.close();
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					finally {
						assert Example.linkedConnection.peek() != null;
						Example.linkedConnection.peek().interrupt();
						Example.linkedConnection.remove();
						Example.isConnected=false;
						Example.yoursRound=false;
						Example.mainGame.chat.setText("");
						Example.mainGame.game_process.setText("");
						Example.gameHall.gameHallFrame.setVisible(true);
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						throw new RuntimeException(ex);
					}

				}
				if(Example.linkedClien.size()!=0)
				{
					assert Example.linkedClien.peek() != null;
					try {
						Example.linkedClien.peek().s.close();
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					finally {
						assert Example.linkedClien.peek() != null;
						Example.linkedClien.peek().interrupt();
						Example.linkedClien.remove();
						Example.isConnected=false;
						Example.yoursRound=false;
						Example.mainGame.chat.setText("");
						Example.mainGame.game_process.setText("");
					}
				}
			}
		});
		saveReview.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(saveNum==1)
				{
					if(chess_manual.size()==0)
						JOptionPane.showConfirmDialog(f,"没有棋谱","通知",JOptionPane.DEFAULT_OPTION);
					else
					{

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
						SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");

						String filename="Save"+formatter2.format(new Date(System.currentTimeMillis())).toString()+".out";
						System.out.println(filename);
						try {
							Example.jdbc.add_game_record(Example.dBlogin.userName,"AI","人机对战","黑",
									formatter.format(new Date(System.currentTimeMillis())).toString(),"负",filename);

							FileOutputStream fos = new FileOutputStream(filename);
							ObjectOutputStream oos = new ObjectOutputStream(fos);
							oos.writeObject(chess_manual);
							oos.flush();
							oos.close();

							try {
								Example.jdbc.historycomp(Example.dBlogin.userName);
							} catch (SQLException ex) {
								throw new RuntimeException(ex);
							}
						} catch (IOException | SQLException ex) {
							throw new RuntimeException(ex);
						}

						Example.historyComp.add(new LinkedList<>(chess_manual));

						JOptionPane.showConfirmDialog(f,"保存成功","通知",JOptionPane.DEFAULT_OPTION);
						saveNum=-1;
					}
				}
				else JOptionPane.showConfirmDialog(f,"此谱已经存在","通知",JOptionPane.DEFAULT_OPTION);
			}
		});
		confirm_send.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String chatInfor=new String(Example.dBlogin.userName+": "+Example.mainGame.inputchat.getText());
				int j=0;
				for(int i=0;i<chatInfor.length();i++)
				{
					Example.mainGame.chat.append(String.valueOf(chatInfor.charAt(i)));
					j++;
					if(j==24)
					{
						Example.mainGame.chat.append("\n"+"  ");
						j=2;
					}

				}
				Example.mainGame.chat.append("\n");
				Example.inputEnd=true;
				if (!Example.isConnected)
					Example.mainGame.inputchat.setText("");
			}
		});

		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Example.gameHall.gameHallFrame.setVisible(true);
				f.dispose();
				if(Example.isAI)
				{
					Example.isAI=false;
					Example.mainGame.chat.setText("");
					Example.mainGame.regret.setEnabled(true);
				}
				if (Example.isConnected) {
					Example.sendinfor = "-";
					Example.isSend = true;
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				}
				if (Example.linkedConnection.size() != 0) {
					try {
						Example.linkedConnection.peek().ss.close();
//						assert Example.linkedConnection.peek() != null;
//						if (Example.linkedConnection.peek().s != null)
//							Example.linkedConnection.peek().s.close();
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					} finally {
						assert Example.linkedConnection.peek() != null;
						Example.linkedConnection.peek().interrupt();
						Example.linkedConnection.remove();
						Example.isConnected = false;
						Example.yoursRound = false;
						Example.mainGame.chat.setText("");
						Example.mainGame.game_process.setText("");
						Example.gameHall.gameHallFrame.setVisible(true);
					}
				} else {
					try {
						assert Example.linkedClien.peek() != null;
						Example.linkedClien.peek().s.close();
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					} finally {
						assert Example.linkedClien.peek() != null;
						Example.linkedClien.peek().interrupt();

						Example.linkedClien.remove();
						System.out.println(Example.linkedClien.size());
						Example.isConnected = false;
						Example.yoursRound = false;
						Example.mainGame.chat.setText("");
						Example.mainGame.game_process.setText("");
					}
				}
			}
		});
		//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭就退出程序
		f.setLocation(220,5);//设置初始位
		f.setResizable(false);//禁止修改窗口大小
		//f.pack();//自动调节窗口大小
		f.setSize(1465,1000);
		//f.setVisible(true);//显示
	}
}

