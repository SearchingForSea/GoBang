package GoBang;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Personal
{
	JFrame personalFrame=new JFrame("个人界面");

	JLabel avatar= new JLabel("    暂不支持头像功能");

	JLabel webname=new JLabel("昵称:");

	JLabel sign=new JLabel("签名:");

	JLabel showWebname=new JLabel();

	JLabel showSign=new JLabel("qianming");

	JLabel maker=new JLabel("by LuoGuan");

	JButton relogin=new JButton("重新登陆");

	JLabel log_out=new JLabel("注销");

	public void setWebname(String webname)
	{
		this.showWebname.setText(webname);
	}
	public void setSign(String sign)
	{
		this.showSign.setText(sign);

	}
	public void init()
	{
		personalFrame.setLayout(null);

		avatar.setBounds(160,50,180,180);
		webname.setBounds(120,280,50,20);
		sign.setBounds(120,340,50,20);
		showWebname.setBounds(200,270,190,40);
		showSign.setBounds(200,330,190,40);
		maker.setBounds(205,420,100,20);
		relogin.setBounds(320,380,100,20);
		log_out.setBounds(290,380,50,20);

		avatar.setBorder(new LineBorder(Color.black,1));
		//showWebname.setBorderPainted(false);
		showWebname.setBackground(new Color(238,238,238));
		//showSign.setBorderPainted(false);
		showSign.setBackground(new Color(238,238,238));
		relogin.setBorderPainted(false);
		relogin.setBackground(new Color(238,238,238));
		log_out.setBackground(new Color(238,238,238));

		avatar.setFont(new Font("TimesRoman",Font.BOLD,17));
		webname.setFont(new Font("TimesRoman",Font.BOLD,20));
		sign.setFont(new Font("TimesRoman",Font.BOLD,20));
		showWebname.setFont(new Font("TimesRoman",Font.BOLD,20));
		showSign.setFont(new Font("TimesRoman",Font.BOLD,20));
		maker.setFont(new Font("TimesRoman",Font.BOLD,15));
		relogin.setFont(new Font("TimesRoman",Font.BOLD,12));
		log_out.setFont(new Font("TimesRoman",Font.BOLD,12));

		maker.setForeground(Color.gray);
		relogin.setForeground(Color.gray);
		log_out.setForeground(Color.gray);

		personalFrame.add(avatar);
		personalFrame.add(webname);
		personalFrame.add(sign);
		personalFrame.add(showWebname);
		personalFrame.add(showSign);
		personalFrame.add(maker);
		personalFrame.add(relogin);
		personalFrame.add(log_out);


		showWebname.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Example.dBlogin.userName= JOptionPane.showInputDialog(Example.netHall.netHallFrame,
						"请输入修改后的昵称");
				Example.gameHall.wel.setText("亲爱的"+Example.dBlogin.userName+"，您好！");
				Example.personal.setWebname(Example.dBlogin.userName);
			}
		});
		showSign.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String temsign= JOptionPane.showInputDialog(Example.netHall.netHallFrame,
						"请输入签名");
				Example.personal.setSign(temsign);
			}
		});
		relogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Example.dBlogin.dbloginFrame.setVisible(true);
				Example.gameHall.gameHallFrame.dispose();
				Example.personal.personalFrame.dispose();
			}
		});
		log_out.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String secret= JOptionPane.showInputDialog(personalFrame, "请输入密码");
				if(Example.dBlogin.userSecret.equals(secret))
				{
					Example.dBlogin.dbloginFrame.setVisible(true);
					Example.personal.personalFrame.dispose();
					Example.gameHall.gameHallFrame.dispose();
					Example.dBlogin.accountText.setText("");
					Example.dBlogin.secretText.setText("");
					try {
						Example.jdbc.log_out(Example.dBlogin.userName,secret);
					} catch (SQLException ex) {
						throw new RuntimeException(ex);
					}
					JOptionPane.showMessageDialog(Example.dBlogin.dbloginFrame,"注销成功！");
				}
				else JOptionPane.showMessageDialog(personalFrame,"注销失败！");
			}
		});

		//personalFrame.setVisible(true);
		personalFrame.setLocation(720,250);//设置初始位
		personalFrame.setResizable(false);//禁止修改窗口大小
		personalFrame.setSize(500,500);
	}
}
