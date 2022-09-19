package GoBang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Arrays;

public class Register
{
	JFrame registerFrame=new JFrame("注册");
	JLabel label= new JLabel("注册");
	JLabel registerAccount=new JLabel("账号*:");
	JLabel registerSex= new JLabel("性别*:");
	JLabel registerNickname= new JLabel("昵称*:");
	JLabel registersecret= new JLabel("密码*:");
	JLabel registermail= new JLabel("邮箱*:");
	JLabel registerShowsign= new JLabel("签名 :");
	JTextField account=new JTextField();
	JTextField sex=new JTextField();
	JTextField nickname=new JTextField();
	JTextField showsign=new JTextField();
	JTextField mail=new JTextField();
	JTextField localIP=new JTextField();
	JTextField secret=new JTextField();

	JButton reYes=new JButton("确认");
	void init()
	{
		registerFrame.setLayout(null);

		label.setFont(new Font("TimesRoman",Font.BOLD,50));
		registerAccount.setFont(new Font("TimesRoman",Font.BOLD,25));
		registerNickname.setFont(new Font("TimesRoman",Font.BOLD,25));
		registerSex.setFont(new Font("TimesRoman",Font.BOLD,25));
		registermail.setFont(new Font("TimesRoman",Font.BOLD,25));
		registerShowsign.setFont(new Font("TimesRoman",Font.BOLD,25));
		registersecret.setFont(new Font("TimesRoman",Font.BOLD,25));
		reYes.setFont(new Font("TimesRoman",Font.BOLD,25));

		for (JTextField textField : Arrays.asList(account, sex, nickname, showsign,mail, localIP,secret)) {
			textField.setFont(new Font("TimesRoman",Font.BOLD,25));
		}


		label.setBounds(200,100,200,50);
		registerAccount.setBounds(80,210,100,50);
		registerNickname.setBounds(80,290,100,50);
		registersecret.setBounds(80,370,100,50);
		registerSex.setBounds(80,450,100,50);
		registermail.setBounds(80,530,100,50);
		registerShowsign.setBounds(80,610,100,50);
		account.setBounds(170,220,240,40);
		nickname.setBounds(170,300,240,40);
		secret.setBounds(170,380,240,40);
		sex.setBounds(170,460,240,40);
		mail.setBounds(170,540,240,40);
		showsign.setBounds(170,620,240,40);
		reYes.setBounds(170,720,150,40);


		registerFrame.add(label);
		registerFrame.add(registerNickname);
		registerFrame.add(registerSex);
		registerFrame.add(registerAccount);
		registerFrame.add(registermail);
		registerFrame.add(registerShowsign);
		registerFrame.add(account);
		registerFrame.add(nickname);
		registerFrame.add(sex);
		registerFrame.add(mail);
		registerFrame.add(showsign);
		registerFrame.add(secret);
		registerFrame.add(registersecret);
		registerFrame.add(reYes);

		reYes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String regstr="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"  ;
					if(!Example.jdbc.verify_account(account.getText()))
					{
						JOptionPane.showMessageDialog(registerFrame,"请正确输入8位可以由数字字母组成的账号");
					}
					else if (!Example.jdbc.verify_secret(secret.getText()))
						JOptionPane.showMessageDialog(registerFrame,"请正确输入6到8位由数字字母组成的密码");
					else if(!Example.jdbc.verify_nickname(nickname.getText()))
						JOptionPane.showMessageDialog(registerFrame,"昵称已重复");

					else if(!mail.getText().matches(regstr))
					{
						JOptionPane.showMessageDialog(registerFrame,"请正确邮箱");
					}
					else {
						Example.jdbc.register_player(account.getText(),nickname.getText(),
								secret.getText(),sex.getText(),mail.getText(),showsign.getText(),
								InetAddress.getLocalHost().getHostAddress());
						JOptionPane.showMessageDialog(registerFrame,"注册成功！");
					}

				} catch (SQLException | UnknownHostException ex) {
					throw new RuntimeException(ex);
				}
			}
		});


		registerFrame.setLocation(650,60);//设置初始位
		registerFrame.setResizable(false);//禁止修改窗口大小
		registerFrame.setSize(500,850);
		registerFrame.setVisible(true);//显示
	}

}
