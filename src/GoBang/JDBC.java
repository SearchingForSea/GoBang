package GoBang;

import java.sql.*;
import java.util.LinkedList;

public class JDBC
{
	Connection conn=null;
	Statement stmt=null;
	ResultSet rs=null;
	CallableStatement clbStmt = null;
	JDBC() throws ClassNotFoundException, SQLException {

		//注册驱动
		//Class.forName("com.mysql.cj.jdbc.Driver");
		//获取连接对象
		conn= DriverManager.getConnection
				("jdbc:mysql://localhost:3306/game_player",
						"root","123456.");
	}

	public  boolean delogin(String address,String secret) throws ClassNotFoundException, SQLException {
		clbStmt = conn.prepareCall("select delogin('"+address+"','"+secret+"')");
		rs=clbStmt.executeQuery();
		// 获取输出参数值
		while (rs.next())
			if (rs.getBoolean(1))
				return true;
		return false;
	}
	//判断账号是否重复
	public  boolean verify_account(String account) throws SQLException {
		String regstr="\\d{8}";
		if(account.matches(regstr))
		{
			clbStmt = conn.prepareCall("select register_yanzhengzhanghao(" + account + ")");
			rs = clbStmt.executeQuery();
			// 获取输出参数值
			while (rs.next())
				if (rs.getBoolean(1))
					return true;
			return false;
		}
		else return false;
	}
	//判断密码是否满足条件
	public  boolean verify_secret(String secret) throws SQLException {
		String regstr="\\w{6,9}";
		return secret.matches(regstr);
	}
	public  boolean verify_nickname(String nickname) throws SQLException {
			clbStmt = conn.prepareCall("select register_yanzhengnicheng('"+nickname+"')");
			rs=clbStmt.executeQuery();

			while (rs.next())
				if (rs.getBoolean(1))
					return true;
			return false;
	}
	public void log_out(String account, String secret) throws SQLException {
		clbStmt = conn.prepareCall("call delete_player('"+account+"','"+secret+"')");
		clbStmt.execute();

	}
	public void add_game_record(String account,String dui_account,String com_type,String qise,String jieguo,String time,String ser) throws SQLException {
		clbStmt = conn.prepareCall("call insert_game_record('"+account+"','"+dui_account+"','"+com_type+"','"+qise+"','"+jieguo+"','"+time+"','"+ser+"')");
		clbStmt.execute();
	}
	public void delete_game_record(String account,String ser) throws SQLException {
		stmt = conn.createStatement();
		stmt.execute("delete from game_record where 对局序列化码='"+ser+"'"+" and 账号='"+account+"'");
	}

	public void historycomp(String account) throws SQLException {
		//获取执行SQL对象
		stmt = conn.createStatement();
		//执行SQL
		rs = stmt.executeQuery("select 对局序列化码 from game_record where 账号='"+account+"'");
		// 获取输出参数值
		int count=1;
		while (rs.next())
		{
			//System.out.println(rs.getString(count));
			Example.fileName.add(rs.getString(count));
		}
	}
	public void register_player(String playerID,String nickname,String secret,String sex,String mail,String qianming,String IP) throws SQLException {
		//System.out.println("call register_player("+playerID+","+nickname+","+secret+","+sex+","+mail+","+qianming+","+IP+")");
		clbStmt = conn.prepareCall("call register_player('"+playerID+"','"+nickname+"','"+secret+"','"+sex+"','"+mail+"','"+qianming+"','"+IP+"')");
			clbStmt.execute();
	}
}
