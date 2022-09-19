package GoBang;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Function
{

	static class BackGround extends JPanel {
		ImageIcon imag;

		BackGround(String path) {
			imag = new ImageIcon(path);
			this.setOpaque(true);
		}

		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(imag.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
	public static boolean isWin(int x, int y, int[][]board, int color)
	{
		int count=1;
		//左右
		for(int xpos=x-1;xpos>=0;xpos--)
		{
			if(board[xpos][y]==color)
			{
				count++;
				if(count>=5)
					return true;
			}
			else break;
		}
		for(int xpos=x+1;xpos<19;xpos++)
		{
			if(board[xpos][y]==color)
			{
				count++;
				if(count>=5)
					return true;
			}
			else
			{
				count=1;
				break;
			}
		}
		//上下
		for(int ypos=y-1;ypos>=0;ypos--)
		{
			if(board[x][ypos]==color)
			{
				count++;
				if(count>=5)
					return true;
			}
			else break;
		}
		for(int ypos=y+1;ypos<19;ypos++)
		{
			if(board[x][ypos]==color)
			{
				count++;
				if(count>=5)
					return true;
			}
			else
			{
				count=1;
				break;
			}
		}
		//左上右下
		for(int xpos=x-1, ypos=y-1;xpos>=0&&ypos>=0;xpos--,ypos--)
		{
			if(board[xpos][ypos]==color)
			{
				count++;
				if(count>=5)
				{
					return true;
				}
			}
			else break;
		}
		for(int xpos=x+1, ypos=y+1;xpos<19&&ypos<19;xpos++,ypos++)
		{
			if(board[xpos][ypos]==color)
			{
				count++;
				if(count>=5)
					return true;
			}
			else
			{
				count=1;
				break;
			}
		}
		//左下右上
		for(int xpos=x-1, ypos=y+1;xpos>=0&&ypos<19;xpos--,ypos++)
		{
			if(board[xpos][ypos]==color)
			{
				count++;
				if(count>=5)
				{
					count=1;
					return true;
				}
			}
			else break;
		}
		for(int xpos=x+1, ypos=y-1;xpos<19&&ypos>=0;xpos++,ypos--)
		{
			if(board[xpos][ypos]==color)
			{
				count++;
				if(count>=5)
				{
					count=1;
					return true;
				}
			}
			else
			{
				count=1;
				break;
			}
		}
		return false;
	}

}
