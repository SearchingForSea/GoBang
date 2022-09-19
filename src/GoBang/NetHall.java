package GoBang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class NetHall  {
	JFrame netHallFrame=new JFrame("网络对战");
	JButton createHall=new JButton("创建房间");
	JButton joinHall=new JButton("加入对局");
;	JLabel wel=new JLabel();

	public void init()
	{
		netHallFrame.setLayout(null);
		netHallFrame.add(createHall);
		netHallFrame.add(joinHall);
		netHallFrame.add(wel);

		wel.setText("欢迎来到联机模式！");

		wel.setBounds(240,120,260,50);
		createHall.setBounds(90,200,120,70);
		joinHall.setBounds(270,290,120,70);
		wel.setFont(new Font("TimesRoman",Font.BOLD,20));
		createHall.setFont(new Font("TimesRoman",Font.BOLD,20));
		joinHall.setFont(new Font("TimesRoman",Font.BOLD,20));

		createHall.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
//					String port= JOptionPane.showInputDialog(Example.netHall.netHallFrame, "请输入你要使用的端口号（0-65535）");
//					boolean panduan=true;
//					for (int i=0;i<port.length();i++)
//					{
//						if(!(port.charAt(i)<='9'&&port.charAt(i)>='0'))
//						{
//							panduan=false;
//							break;
//						}
//					}
//					if(!panduan||port.length()>5||Integer.parseInt(port)<0||Integer.parseInt(port)>65535)
//					{
//						JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame,"请输入正确端口号","确认",JOptionPane.DEFAULT_OPTION);
//					}
//					else {
						Connention temconnection=new Connention("8080");
						temconnection.start();
						Example.linkedConnection.add(temconnection);
						Example.mainGame.Restart();
						Example.mainGame.f.setVisible(true);
						Example.netHall.netHallFrame.dispose();
						Example.gameHall.gameHallFrame.dispose();
//					}
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		joinHall.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String s = JOptionPane.showInputDialog(Example.netHall.netHallFrame, "请输入你要连接的IP地址");
				//String port= JOptionPane.showInputDialog(Example.netHall.netHallFrame, "请输入你要连接的端口号（0-65535）");

//				boolean panduan=true;
//				for (int i=0;i<port.length();i++)
//				{
//					if(!(port.charAt(i)<='9'&&port.charAt(i)>='0'))
//					{
//						panduan=false;
//						break;
//					}
//				}
				if(s.isEmpty())
				{
					JOptionPane.showConfirmDialog(Example.gameHall.gameHallFrame,"请检查输入的IP和端口号是否正确","检查",JOptionPane.DEFAULT_OPTION);
				}
				else {
					Client temclient = new Client(s,"8080");
					temclient.start();
					Example.linkedClien.add(temclient);
					Example.mainGame.Restart();
					Example.mainGame.f.setVisible(true);
					Example.netHall.netHallFrame.dispose();
					Example.gameHall.gameHallFrame.dispose();
				}
			}
		});

		netHallFrame.setLocation(720,250);//设置初始位
		netHallFrame.setResizable(false);//禁止修改窗口大小
		netHallFrame.setSize(500,500);
		//netHallFrame.setVisible(true);//显示
	}
}
