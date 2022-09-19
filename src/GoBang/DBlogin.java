package GoBang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class DBlogin extends Register {

	String userName;
	String userSecret;
	JFrame dbloginFrame=new JFrame("登录");
	JLabel account =new JLabel("账号");
	JLabel secret=new JLabel("密码");
	JTextField accountText =new JTextField();
	JButton login=new JButton("登录");
	JButton close=new JButton("退出");
	JLabel name=new JLabel("五子棋");

	JLabel register=new JLabel("如无账号点此注册");

	JPasswordField secretText=new JPasswordField();

	Function.BackGround bg=new Function.BackGround("image\\background.jpg");
	public void init()
	{
		dbloginFrame.setLayout(null);

		name.setFont(new Font("TimesRoman",Font.BOLD,50));
		account.setFont(new Font("TimesRoman",Font.BOLD,25));
		accountText.setFont(new Font("TimesRoman",Font.BOLD,30));
		secret.setFont(new Font("TimesRoman",Font.BOLD,25));
		secretText.setFont(new Font("TimesRoman",Font.BOLD,30));
		login.setFont(new Font("TimesRoman",Font.BOLD,25));
		close.setFont(new Font("TimesRoman",Font.BOLD,25));
		register.setFont(new Font("TimesRoman",Font.BOLD,15));

		name.setBounds(310,50,400,300);
		account.setBounds(90,380,100,50);
		accountText.setBounds(200,380,460,50);
		secret.setBounds(90,480,100,50);
		secretText.setBounds(200,480,460,50);
		login.setBounds(240,590,100,50);
		close.setBounds(440,590,100,50);
		register.setBounds(328,660,180,30);

		register.setBackground(new Color(238,238,238));

		dbloginFrame.add(name);
		dbloginFrame.add(account);
		dbloginFrame.add(secret);
		dbloginFrame.add(accountText);
		dbloginFrame.add(secretText);
		dbloginFrame.add(login);
		dbloginFrame.add(close);
		dbloginFrame.add(register);


		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		login.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				userName= accountText.getText();
				userSecret=new String(secretText.getPassword());
//				System.out.println(userName);
//				System.out.println(userSecret);
				//if(!accountText.getText().isEmpty()&&secretText.getPassword().length!=0)
				try {
					if(Example.jdbc.delogin(userName,userSecret))
					{
						try {
							Example.jdbc.historycomp(Example.dBlogin.userName);
						} catch (SQLException ex) {
							throw new RuntimeException(ex);
						}
						if(Example.dbnum==0)
						{
							System.out.println("账号为"+accountText.getText());
							System.out.println("密码为"+ Arrays.toString(secretText.getPassword()));
							Example.gameHall.init();
							dbloginFrame.dispose();
							Example.dbnum=1;
						}
						else
						{
							Example.gameHall.gameHallFrame.setVisible(true);
							Example.gameHall.wel.setText("亲爱的"+Example.dBlogin.userName+"，您好！");
							Example.personal.showSign.setText("qianming");
							dbloginFrame.dispose();
						}
					}
					else JOptionPane.showMessageDialog(dbloginFrame,"请确认您输入的账号和密码是否正确");
				} catch (ClassNotFoundException | SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Example.register.init();
			}
		});

		dbloginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭就退出程序
		dbloginFrame.setLocation(550,80);//设置初始位
		dbloginFrame.setResizable(false);//禁止修改窗口大小
		//dbloginFrame.pack();//自动调节窗口大小
		dbloginFrame.setSize(800,800);

		bg.setBounds(0,0,800,800);
		dbloginFrame.add(bg);

		dbloginFrame.setVisible(true);//显示


	}
}
