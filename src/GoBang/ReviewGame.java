package GoBang;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class ReviewGame {

	JFrame reviewFrame = new JFrame("复盘");

	public ReviewGame() throws IOException {
	}

	public void reviewInit(LinkedList<Point> chess_manual) throws IOException {
		ReviewGamePanel reviewGamePanel=new ReviewGamePanel(chess_manual);
		reviewGamePanel.delete.setEnabled(false);

		reviewFrame.add(reviewGamePanel);

		reviewFrame.setBounds(400,20,1250,1010);
		reviewFrame.setResizable(false);//禁止修改窗口大小
		reviewFrame.setVisible(true);
	}
}
