import java.io.*;
import java.sql.*;
import java.util.*;

public class DBConvert2
{
  public static String wsdladdr = "", provname = "", country = "";
  public static int serviceid = 0;
  public static double price = 0.0;
  public static Random rand;

  public static void main(String args[])
  {
    String data = "";
    StringTokenizer stk;
    DBTools.clearTable("CloudRank","CloudRank","CloudRank","servicelist");
    rand = new Random();
    try
    {
      FileInputStream fin = new FileInputStream("wslist.dat");
      DataInputStream dis = new DataInputStream(fin);
      while((data = dis.readLine()) != null)
      {
         stk = new StringTokenizer(data,"\t");
         serviceid = Integer.parseInt(stk.nextToken().trim());
         serviceid++;
         System.out.println("Service ID : "+serviceid);
         wsdladdr = stk.nextToken();
         provname = stk.nextToken();
         country = stk.nextToken();
         int pno = Math.abs(rand.nextInt()) % 90;
         pno = pno + 5;
         price = pno * 10.0;
         updateData();
         if(serviceid == 2000)
           break;
       }
       System.out.println("Service details updated");
       dis.close();
       fin.close();
    }catch(Exception e)
    {
      System.out.println("Error : DBConvert2 - main : "+e);
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
      String sql = "insert into servicelist values(?,?,?,?,?)";
      pstat = con.prepareStatement(sql);
      pstat.setInt(1,serviceid);
      pstat.setString(2,wsdladdr);
      pstat.setString(3,provname);
      pstat.setString(4,country);
      pstat.setDouble(5,price);
      pstat.executeUpdate();
      pstat.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : DBConvert2 - updateData : "+e);
    }

  }

}
