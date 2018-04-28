import java.io.*;
import java.sql.*;
import java.util.*;

public class LoadProperties
{

  double tpmat[][], rtmat[][];
  int usercount = 0, servcount = 0, userid = 0, serviceid = 0;
  double tpvalue = 0.0, rtvalue = 0.0;
  Random rand;
  String data = "";
  int opercount = 0, inscount = 0, insno = 0, tsno = 0;
  int srate = 0, drate = 0;
  double frate = 0.0;
  LoadProperties(int usercount, int servcount)
  {
    this.usercount = usercount;
    this.servcount = servcount;
    rand = new Random();
    tpmat = new double[6][servcount];
    rtmat = new double[6][servcount];
    for(int i=0;i<6;i++)
    {
      for(int j=0;j<servcount;j++)
      {
        tpmat[i][j] = 0.0;
        rtmat[i][j] = 0.0;
      }
    }
      DBTools.clearTable("CloudRank","CloudRank","CloudRank","stprtlist1");
      DBTools.clearTable("CloudRank","CloudRank","CloudRank","dtprtlist1");

    init();
    init1();
  }
  public void init()
  {
    int i = 0;
    try
    {
      FileInputStream fin = new FileInputStream("rtmatrix.dat");
      DataInputStream dis = new DataInputStream(fin);
      while((data = dis.readLine()) != null)
      {
        if(data.trim().length() == 0)
          continue;
        StringTokenizer stk =  new StringTokenizer(data);
        for(int j=0;j<servcount;j++)
        {
          String str = stk.nextToken().trim();
          double trt = Double.parseDouble(str);
          rtmat[i][j] = trt;
        }
        i++;
        if(i == 6)
          break;

      }
      dis.close();
      fin.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : LoadProperties - init : "+e);
    }
  }
  public void init1()
  {
    int i = 0;
    try
    {
      FileInputStream fin = new FileInputStream("tpmatrix.dat");
      DataInputStream dis = new DataInputStream(fin);
      while((data = dis.readLine()) != null)
      {
        if(data.trim().length() == 0)
          continue;
        StringTokenizer stk =  new StringTokenizer(data);
        for(int j=0;j<servcount;j++)
        {
          String str = stk.nextToken().trim();
          double ttp = Double.parseDouble(str);
          tpmat[i][j] = ttp;
        }
        i++;
        if(i == 6)
          break;

      }
      dis.close();
      fin.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : LoadProperties - init1 : "+e);
    }
  }

  public void loadSP()
  {
    try
    {
      for(int i=0;i<servcount;i++)
      {
        int tno = Math.abs(rand.nextInt()) % usercount;
        userid = tno + 1;
        serviceid = i + 1;
        tpvalue = tpmat[0][i];
        rtvalue = rtmat[0][i];
        srate = Math.abs(rand.nextInt()) % 11;
        updateSP();
      }
    }catch(Exception e)
    {
      Screen.showMessage("Error : LoadProperties - loadSP : "+e);
    }
  }
  public void updateSP()
  {
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Insert into stprtlist1 values(?,?,?,?,?)";
      PreparedStatement pstat = con.prepareStatement(sql);
      pstat.setInt(1, userid);
      pstat.setInt(2, serviceid);
      tpvalue = Math.abs(tpvalue);
      pstat.setDouble(3, tpvalue);
      rtvalue = Math.abs(rtvalue);
      pstat.setDouble(4, rtvalue);
      pstat.setInt(5,srate);
      pstat.executeUpdate();
      pstat.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : LoadProperties - updateSP : "+e);
    }
  }

  public void loadDP()
  {
    int tno = 0;
    try
    {
      for(int i=0;i<servcount;i++)
      {
        serviceid = i + 1;
        System.out.println("DP : "+serviceid);
        tno = Math.abs(rand.nextInt()) % 20;
        opercount = tno + 1;
        tno = Math.abs(rand.nextInt()) % 5;
        inscount = tno + 1;
        if(inscount == 1)
          inscount = 2;
        tno = Math.abs(rand.nextInt()) % 20;
        tno++;
        frate = 1.0 + ((double)tno / 10.0);
        for(int j=0;j<inscount;j++)
        {
          tno = Math.abs(rand.nextInt()) % usercount;
          userid = tno + 1;
          insno = j + 1;
          tno = Math.abs(rand.nextInt()) % 64;
          tsno = tno + 1;
          tpvalue = tpmat[j+1][i];
          rtvalue = rtmat[j+1][i];
          drate = Math.abs(rand.nextInt()) % 11;
          updateDP();
        }
      }
    }catch(Exception e)
    {
      Screen.showMessage("Error : LoadProperties - loadDP : "+e);
    }
  }
  public void updateDP()
  {
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Insert into dtprtlist1 values(?,?,?,?,?,?,?,?,?)";
      PreparedStatement pstat = con.prepareStatement(sql);
      pstat.setInt(1, userid);
      pstat.setInt(2, serviceid);
      pstat.setInt(3, opercount);
      pstat.setDouble(4, frate);
      pstat.setInt(5, insno);
      pstat.setInt(6, tsno);
      tpvalue = Math.abs(tpvalue);
      pstat.setDouble(7, tpvalue);
      rtvalue = Math.abs(rtvalue);
      pstat.setDouble(8, rtvalue);
      pstat.setInt(9,drate);
      pstat.executeUpdate();
      pstat.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : LoadProperties - updateDP : "+e);
    }
  }
}

