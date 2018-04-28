import java.sql.*;
import java.util.*;

class DBTools
{
  public static void main(String args[])
  {
  }
  //To delete the records in the given table
  public static void clearTable(String dsn, String tname)
  {
    try
    {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection("jdbc:odbc:"+dsn);
      String sql = "Delete from "+tname;
      Statement stat = con.createStatement();
      stat.execute(sql);
      stat.close();
      con.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBTools - clearTable : "+e);
      Screen.showMessage("Error : DBTools - clearTable : "+e);
    }
  }
  // To delete the records in the given table
  public static void clearTable(String dsn, String uid,String pwd,String tname)
  {
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:" + uid +"/" + pwd+"@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Delete from "+tname;
      Statement stat = con.createStatement();
      stat.execute(sql);
      stat.close();
      con.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBTools - clearTable : "+e);
      Screen.showMessage("Error : DBTools - clearTable : "+e);
    }
  } 
  // To get row count
  public static int getRowCount(String dsn, String uid, String pwd, String tname)
  {
    int rcount = 0;
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:" + uid +"/" + pwd+"@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);

      String sql = "select count(*) from "+tname;
      Statement stat = con.createStatement();
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
      {
        rcount = rset.getInt(1);
      }
      stat.close();
      con.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBTools - getRowCount : "+e);
      Screen.showMessage("Error : DBTools - getRowCount : "+e);
    }
    return rcount;
  }
  public static int getRowCount(String dsn,String tname)
  {
    int rcount = 0;
    try
    {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection("jdbc:odbc:"+dsn);
      String sql = "select count(*) from "+tname;
      Statement stat = con.createStatement();
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
      {
        rcount = rset.getInt(1);
      }
      stat.close();
      con.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBTools - getRowCount : "+e);
      Screen.showMessage("Error : DBTools - getRowCount : "+e);
    }
    return rcount;
  }
}
