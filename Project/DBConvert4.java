import java.io.*;
import java.sql.*;
import java.util.*;

public class DBConvert4
{
  public static String servtype = "";
  public static int serviceid = 0;


  public static void main(String args[])
  {
    String data = "";
    StringTokenizer stk;
    DBTools.clearTable("CloudRank","CloudRank","CloudRank","servicetypes");
    try
    {
      FileInputStream fin = new FileInputStream("wstypes.dat");
      DataInputStream dis = new DataInputStream(fin);
      while((data = dis.readLine()) != null)
      {
         stk = new StringTokenizer(data,"|");
         serviceid = Integer.parseInt(stk.nextToken().trim());
         serviceid++;
         System.out.println("Service ID : "+serviceid);
         servtype = stk.nextToken();
         updateData();
         if(serviceid == 500)
           break;
       }
       System.out.println("Service details updated");
       dis.close();
       fin.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBConvert4 - main : "+e);
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
      String sql = "insert into servicetypes values(?,?)";
      pstat = con.prepareStatement(sql);
      pstat.setInt(1,serviceid);
      pstat.setString(2,servtype);
      pstat.executeUpdate();
      pstat.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : DBConvert4 - updateData : "+e);
    }

  }

}
