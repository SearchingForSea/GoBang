package GoBang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public class GameHall {
	JButton AI =new JButton("人机对战");
	JButton local=new JButton("本地对战");
	JButton net =new JButton("联机对战");
	JButton reviewGame=new JButton("历史对局");
	JFrame gameHallFrame=new JFrame("大厅");

	JButton wel= new JButton();

	Function.BackGround bg=new Function.BackGround("image\\background.jpg");
	void init()
	{
		gameHallFrame.setLayout(null);

		wel.setFocusPainted(false);
		wel.setBorderPainted(false);
		wel.setBackground(new Color(238,238,238));
		wel.setText("亲爱的"+Example.dBlogin.userName+"，您好！");

		wel.setFont(new Font("TimesRoman",Font.BOLD,20));
		local.setFont(new Font("TimesRoman",Font.BOLD,25));
		AI.setFont(new Font("TimesRoman",Font.BOLD,25));
		reviewGame.setFont(new Font("TimesRoman",Font.BOLD,25));
		net.setFont(new Font("TimesRoman",Font.BOLD,25));

		wel.setBounds(460,160,320,50);
		local.setBounds(190,320,150,100);
		AI.setBounds(440,320,150,100);
		reviewGame.setBounds(190,520,150,100);
		net.setBounds(440,520,150,100);

		gameHallFrame.add(wel);
		gameHallFrame.add(local);
		gameHallFrame.add(AI);
		gameHallFrame.add(reviewGame);
		gameHallFrame.add(net);

		local.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Example.mainGame.chat.setText("");
				Example.mainGame.Restart();
				Example.mainGame.f.setVisible(true);
				gameHallFrame.dispose();
			}
		});
		reviewGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Example.fileName.size()==0)
					JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame,"Sorry,您还没有棋谱.","通知",JOptionPane.DEFAULT_OPTION);
				else try {
					assert Example.linkedReviewHall.peek() != null;
					Example.linkedReviewHall.peek().init();
				} catch (IOException | ClassNotFoundException | SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		net.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//Example.netHall.netHallFrame.dispose();
				Example.netHall.netHallFrame.setVisible(true);

			}
		});
		AI.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Example.mainGame.Restart();
				Example.mainGame.f.setVisible(true);
				Example.isAI=true;
				Example.ai.init();
			}
		});
		wel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Example.personal.setWebname(Example.dBlogin.userName);
				Example.personal.personalFrame.setVisible(true);
			}
		});

		//gameHallFrame.getContentPane().setBackground(Color.getHSBColor());
		gameHallFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭就退出程序
		gameHallFrame.setLocation(570,100);//设置初始位
		gameHallFrame.setResizable(false);//禁止修改窗口大小
		//gameHallFrame.pack();//自动调节窗口大小
		gameHallFrame.setSize(800,800);
		bg.setBounds(0,0,800,800);
		gameHallFrame.add(bg);
		gameHallFrame.setVisible(true);//显示

	}
}
