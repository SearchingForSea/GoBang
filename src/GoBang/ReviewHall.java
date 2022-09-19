package GoBang;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.LinkedList;

public class ReviewHall
{
	JFrame reviewHallFrame =new JFrame();

	JTabbedPane tabbedPane=null;

	ReviewHall(){}

	public void init() throws IOException, ClassNotFoundException, SQLException {
		//tabbedPane.setLayout(null);
		tabbedPane=new JTabbedPane(SwingConstants.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setFont(new Font("TimesRoman",Font.BOLD,20));
		//System.out.println(Example.gameCentre.historyComp.size());
		Example.fileName.clear();
		Example.jdbc.historycomp(Example.dBlogin.userName);
		int count=1;
		for(String s:Example.fileName) {
			FileInputStream fis = new FileInputStream(s);
			ObjectInputStream ois = new ObjectInputStream(fis);
			tabbedPane.addTab("            对局" + (count++) + "            ",
					null, new ReviewGamePanel(new LinkedList<>((LinkedList<Point>) ois.readObject()), tabbedPane.getSelectedIndex() + 1));
			ois.close();
			fis.close();
		}


//		for(int i=0;i<Example.historyComp.size();i++)
//		{
//			tabbedPane.addTab("            对局"+(count+i)+"            ",
//					null,new ReviewGamePanel(Example.historyComp.get(i),tabbedPane.getSelectedIndex()+1));
//		}

		//tabbedPane.setSelectedIndex(1);//默认选择
		reviewHallFrame.add(tabbedPane);
		reviewHallFrame.setBounds(300,10,1440,1010);

		reviewHallFrame.setVisible(true);
		reviewHallFrame.setResizable(false);
	}
}
