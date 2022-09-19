package GoBang;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class ReviewGamePanel extends JPanel {

	private int[][] reviewBoard=new int[19][19];

	JPanel reviewFrame = new JPanel();
	JPanel jp_south=new JPanel();
	JButton last=new JButton("上一步");

	JButton delete=new JButton("清除");
	JButton next=new JButton("下一步");

	JTextArea game_process2=new JTextArea();
	JTextArea track_game=new JTextArea();
	BufferedImage table=ImageIO.read(new File("image\\table.png"));

	private ReviewHall reviewHall=null;

	int flag=0;

	int color=1;

	public ReviewGamePanel(LinkedList<Point>chess_manual) throws IOException{
		reviewInit(chess_manual);
	}
	public ReviewGamePanel(LinkedList<Point> chess_manual,int flag ) throws IOException {
		reviewInit(chess_manual);
		this.flag=flag;
	}

	class ReviewBorad extends Canvas
	{
		public void paint(Graphics g)
		{
			//绘图
			g.drawImage(table,0,0,null);
			//绘制棋子
			for (int i = 0; i < 19; i++) {
				for (int j = 0; j < 19; j++) {
					//绘制黑棋
					if(reviewBoard[i][j]==1){
						g.setColor(Color.black);
						g.drawOval(i*45+26,j*45+18,40,40);
						g.fillOval(i*45+26,j*45+18,40,40);
					}
					//绘制白棋
					if(reviewBoard[i][j]==2){
						g.setColor(Color.WHITE);
						g.drawOval(i*45+26,j*45+18,40,40);
						g.fillOval(i*45+26,j*45+18,40,40);
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

	ReviewBorad reviewGameboard=new ReviewBorad();

	public void reviewInit(LinkedList<Point> chess_manual)
	{
		//初始化显示框
		setLayout(null);
		game_process2.setFont(new Font("TimesRoman",Font.BOLD,18));
		int typetem=1;
		String tem="";
		int i=1;
		game_process2.append("\n");
		for(Point point:chess_manual)
		{
			tem=(typetem==1)?"黑方":"白方";
			if(point.getX()==-1)
			{
				tem=(typetem==2)?"黑方":"白方";
				game_process2.append("恭喜！"+tem+"胜利啦!");
			}

			else
				game_process2.append("第"+i+"步："+tem+"横："+((int)point.getX()+1)+","+"纵："+((int)point.getY()+1)+";"+"\n");
			typetem=(typetem==1)?2:1;
			i++;
		}
		track_game.setFont(new Font("TimesRoman",Font.BOLD,20));
		track_game.setForeground(Color.RED);
		track_game.append("当前为第0步\n"+"下一步为第1步");

		JPanel left=new JPanel();
		JPanel right=new JPanel();

		//记录步数
		final int[] k = {0};

		delete.setPreferredSize(new Dimension(120,50));
		last.setPreferredSize(new Dimension(120,50));
		next.setPreferredSize(new Dimension(120,50));
		delete.setFont(new Font("TimesRoman",Font.BOLD,18));
		last.setFont(new Font("TimesRoman",Font.BOLD,18));
		next.setFont(new Font("TimesRoman",Font.BOLD,18));

		jp_south.add(last);
		jp_south.add(delete);
		jp_south.add(next);
		left.setLayout(new BorderLayout());
		left.add(jp_south,BorderLayout.SOUTH);

		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p=chess_manual.get(k[0]);
				if(p.getX()==-1&&p.getY()==-1)
				{
					String text;
					text=(color==2?"黑棋胜利!!!":"白棋胜利!!!");
					JOptionPane.showConfirmDialog(reviewFrame,text,"结果",JOptionPane.DEFAULT_OPTION);
				}
				else {
					reviewBoard[(int)(p.getX())][(int)(p.getY())]=color;
					reviewGameboard.repaint();
					String tem=(color==1?"黑方":"白方");
					color=(color==1?2:1);
					track_game.setText("");
					track_game.append("当前为第"+(k[0]+1)+"步：\n"+tem+"横："+(int)(p.getX()+1)+"，"+"纵："+(int)(p.getY()+1));
				}
				k[0]++;
			}
		});
		last.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(k[0]>0)
				{
					k[0]--;
					Point p=chess_manual.get(k[0]);
					reviewBoard[(int)(p.getX())][(int)(p.getY())]=0;
					reviewGameboard.repaint();
					color=(color==1?2:1);
				}
			}
		});

		delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(delete.isEnabled())
				{
					//assert Example.linkedReviewHall.peek() != null;
					int result=JOptionPane.showOptionDialog(Example.reviewHall.reviewHallFrame,"是否确定清除此对局记录","选项"
							,JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"确定","取消"},"确定");
					switch (result)
					{
						case 0:
							ReviewHall tem=new ReviewHall();
							try {
								Example.jdbc.delete_game_record(Example.dBlogin.userName,Example.fileName.get(flag));
								File file=new File(Example.fileName.get(flag));
								if(file.isFile())
								{
									System.out.println(file.delete());
								}
							} catch (SQLException ex) {
								throw new RuntimeException(ex);
							}
							if(Example.fileName.size()!=0&&Example.fileName.size()!=1)
							{
								try {
									tem.init();
								} catch (IOException | ClassNotFoundException | SQLException ex) {
									throw new RuntimeException(ex);
								}
								Example.linkedReviewHall.add(tem);
								Example.linkedReviewHall.peek().reviewHallFrame.dispose();
								Example.linkedReviewHall.remove();
							}
							else
							{
								Example.linkedReviewHall.add(tem);
								assert Example.linkedReviewHall.peek() != null;
								Example.linkedReviewHall.peek().reviewHallFrame.dispose();
								Example.linkedReviewHall.remove();
								JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame,"Sorry,您的对局记录已经清空.","通知",JOptionPane.DEFAULT_OPTION);
							}

							break;
						case 1:
							break;

					}
				}
			}
		});
		//right.setPreferredSize(new Dimension(270,800));
		game_process2.setPreferredSize(new Dimension(270,2000));
		track_game.setPreferredSize(new Dimension(270,200));

		reviewGameboard.setPreferredSize(new Dimension(900,893));
		left.add(reviewGameboard);

		JScrollPane jScrollPane=new JScrollPane(game_process2,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		right.setLayout(new BorderLayout());
		right.add(jScrollPane);
		right.add(track_game,BorderLayout.SOUTH);

		left.setBorder(new LineBorder(Color.gray,1));


		left.setBounds(10,5,900,943);
		right.setBounds(920,5,270,943);

		this.add(left);
		this.add(right);


		this.setPreferredSize(new Dimension(1300,953));
		this.setVisible(true);
	}

	void Restart()
	{
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				reviewBoard[i][j]=0;
		color=1;
		game_process2.setText("");
	}
}
