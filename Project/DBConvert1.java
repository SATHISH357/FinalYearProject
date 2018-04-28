import java.io.*;
import java.sql.*;
import java.util.*;

public class DBConvert1
{
  public static  int userid = 0;
  public static  double latitude = 0.0, longitude = 0.0;
  public static String ipaddr = "", country = "";
  

  public static void main(String args[])
  {
    StringTokenizer stk;
    DBTools.clearTable("CloudRank","CloudRank","CloudRank","userlist");
    String data = "";
    try
    {
      FileInputStream fin = new FileInputStream("userlist.dat");
      DataInputStream dis = new DataInputStream(fin);
      while((data = dis.readLine()) != null)
      {
         stk = new StringTokenizer(data,"\t");
         userid = Integer.parseInt(stk.nextToken().trim());
         userid++;
         ipaddr = stk.nextToken();
         country = stk.nextToken();
         longitude =Double.parseDouble(stk.nextToken().trim());
         latitude =Double.parseDouble(stk.nextToken().trim());
         System.out.println("Userid : "+userid);
         updateData();
         if(userid == 100)
           break;
       }
       System.out.println("User details updated");
       dis.close();
       fin.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBConvert1 - main : "+e);
    }
  }
  public static void updateData()
  {
    PreparedStatement pstat;
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "insert into userlist values(?,?,?,?,?)";
      pstat = con.prepareStatement(sql);
      pstat.setInt(1,userid);
      pstat.setString(2,ipaddr);
      pstat.setString(3,country);
      pstat.setDouble(4,longitude);
      pstat.setDouble(5,latitude);
      pstat.executeUpdate();
    pstat.close();
    con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : DBConvert1 - updateData : "+e);
    }

  }

}
