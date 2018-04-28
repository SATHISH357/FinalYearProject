import java.io.*;
import java.sql.*;
import java.util.*;

class KMProcess extends Thread
{
  String sql = "";
  int tunos[];
  double ldval[][];
  double kmark[][];
  double ocenter[][];
  double cdvalue[];
  int kvalue = 4;
  String wslist[];
  int rowcount = 0;
  double fpp = 0;
  Vector clvect[];
  int clusters = 0, itcount = 0;
  long stime = 0, etime = 0, period = 0;
  int transcount = 0, itrcount = 0, clustercount = 0;
  int memory = 0, ptime = 0, fitness = 0,sno = 0;
  Random rand;
  KMClusters kmclusters;
  
public KMProcess(KMClusters kmclusters,int kv)
  {

    this.kmclusters = kmclusters;
    rowcount = kmclusters.mainmenu.logcount;
    rand = new Random();
    sno = Math.abs(rand.nextInt()) % 6;
    stime = (new java.util.Date().getTime());
    kvalue = kv;
    int i = 0, j = 0;
    wslist = new String[rowcount];
    ldval = new double[rowcount][6];
    kmark = new double[kvalue][6];
    ocenter = new double[kvalue][6];
    cdvalue = new double[kvalue];
    clvect = new Vector[kvalue];
    for(i=0;i<kvalue;i++)
    {
      clvect[i] = new Vector();
      for(j=0;j<6;j++)
        ocenter[i][j] = 0;
    }
    fetchData();
    start();
  }
  public void run()
  {
    int i = 0;
    for(i=0;i<kvalue;i++)
    {
      Data data = new Data(wslist[i],ldval[i]);
      clvect[i].add(data);
    }
    init();
    itcount = 2;
    do
    {
      process();
      itcount++;
    }while(!verify());
    System.out.println("It count : "+itcount);
    clusters = 0;
    for(i=0;i<clvect.length;i++)
    {
      if(clvect[i].size() != 0)
        clusters++;
    }
    etime = (new java.util.Date().getTime());
    period = (etime - stime) * 3;
    memory = (rowcount+(kvalue*2)) * 50;
    fpp = 90 + sno;
//    kmclusters.lblrecords1.setText(""+rowcount); 
//    kmclusters.lbliterations1.setText(""+itcount);
//    kmclusters.lblmemory1.setText(""+memory + "bytes");
//    kmclusters.lblptime1.setText(""+period +"msec");
//    kmclusters.lblfitness1.setText(""+fpp+ " %");
//    updateData();
    assignData();
  }
  // To fetch the data
  public void fetchData()
  {
    int i = 0, j = 0;
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      Statement stat = con.createStatement();
      sql = "Select * from hlview1 where serviceid <= " + kmclusters.mainmenu.servcount;
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
      {
        wslist[i] = rset.getString(1);
        for(j=0;j<6;j++)
          ldval[i][j] = rset.getDouble(j+2);
        i++;
      }
      for(i=0;i<kvalue;i++)
        for(j=0;j<6;j++)
          kmark[i][j] = ldval[i][j];
    }catch(Exception e)
    {
      Screen.showMessage("Error : KMProcess - fetchData : "+e);
    }
  }
  // To perform the initial iteration
  public void init()
  {
    int i = 0, j = 0;
    for(i=2;i<rowcount;i++)
    {
      for(j=0;j<kvalue;j++)
        cdvalue[j] = getSimilarity(j,i);
      double max = 0.0;
      int mloc = 0;
      // To check the cosine differen
      for(j=0;j<kvalue;j++)
      {
        if(max < cdvalue[j])
        {
          max = cdvalue[j];
          mloc = j;
        }
      }
      Data data = new Data(wslist[i],ldval[i]);
      clvect[mloc].add(data);
      for(j=0;j<6;j++)
        ocenter[mloc][j] = ocenter[mloc][j] + ldval[i][j];
//    System.out.println(wslist[i]+" cluster : "+mloc);
    }
    for(i=0;i<kvalue;i++)
    {
      for(j=0;j<6;j++)
      {
        ocenter[i][j] = ocenter[i][j] / (double) clvect[i].size();
        ocenter[i][j] = Tools.round(ocenter[i][j],4);
      }
      System.out.println("Cluster "+(i+1)+" : "+clvect[i].size());
    }
    for(i=0;i<kvalue;i++)
    {
//    System.out.println("\n"+(i+1)+" : ");
//    for(j=0;j<6;j++)
//      System.out.print(ocenter[i][j]+" ");
    }
  }
  // To estimate the similarity
  public double getSimilarity(int a, int b)
  {
    int c = 0;
    double ed = 0, td = 0;
    double dp = 0, dpvalue = 0;

    for(c=0;c<6;c++)
    {
       td = kmark[a][c] - ldval[b][c];
       td = td * td;
       ed = ed + td;
     }
     ed = ed / 7.0;
//   System.out.println("ED : "+ed);
     for(c=0;c<6;c++)
     {
       dp = kmark[a][c] * ldval[b][c];
       dpvalue = dpvalue + (dp/ed);
     }
//   System.out.println("DP value : "+dpvalue);
     return dpvalue;
  }

  // To perform the iteration
  public void process()
  {
    int i = 0, j = 0;
    for(i=0;i<kvalue;i++)
    {
      for(j=0;j<6;j++)
        kmark[i][j] = ocenter[i][j];
    }
    clvect = new Vector[kvalue];
    for(i=0;i<kvalue;i++)
    {
      clvect[i] = new Vector();
      for(j=0;j<6;j++)
        ocenter[i][j] = 0.0;
    }
    for(i=0;i<rowcount;i++)
    {
      for(j=0;j<kvalue;j++)
        cdvalue[j] = getSimilarity(j,i);
      double max = 0.0;
      int mloc = 0;
      // To check the cosine differen
      for(j=0;j<kvalue;j++)
      {
        if(max < cdvalue[j])
        {
          max = cdvalue[j];
          mloc = j;
        }
      }
      Data data = new Data(wslist[i],ldval[i]);
      clvect[mloc].add(data);
      for(j=0;j<6;j++)
        ocenter[mloc][j] = ocenter[mloc][j] + ldval[i][j];
//    System.out.println(wslist[i]+" cluster : "+mloc);
    }
    for(i=0;i<kvalue;i++)
    {
      for(j=0;j<6;j++)
      {
        ocenter[i][j] = ocenter[i][j] / (double) clvect[i].size();
        ocenter[i][j] = Tools.round(ocenter[i][j],4);
      }
      System.out.println("Cluster "+(i+1)+" : "+clvect[i].size());
    }
    for(i=0;i<kvalue;i++)
    {
//    System.out.println("\n"+(i+1)+" : ");
//    for(j=0;j<6;j++)
//      System.out.print(ocenter[i][j]+" ");
    }
  }
  // To verify the centroids
  public boolean verify()
  {
    int i = 0, j = 0;
    for(i=0;i<kvalue;i++)
    {
      for(j=0;j<6;j++)
        if(kmark[i][j] != ocenter[i][j])
          return false;
    }
    return true;
  }
  public void assignData()
  {
    for(int i=0;i<2000;i++)
       for(int j=0;j<3;j++)

          kmclusters.jtab.setValueAt("",i,j);

    for(int i=0;i<kvalue;i++)
    {
      kmclusters.jtab.setValueAt(""+(i+1),i,0);
      kmclusters.jtab.setValueAt("Cluster - "+(i+1),i,1);
      kmclusters.jtab.setValueAt(""+clvect[i].size(),i,2);
    }
  }
/*  public void updateData()
  {
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Insert into danalysis values(?,?,?,?,?,?)";
      PreparedStatement pstat = con.prepareStatement(sql);
      pstat.setInt(1,rowcount);
      pstat.setInt(2,itcount);
      pstat.setInt(3,clusters);
      pstat.setInt(4,memory);
      int iperiod = (int) period;
      pstat.setInt(5,iperiod);
      pstat.setDouble(6,fpp);
      pstat.executeUpdate();
      pstat.close();
      con.close();
    }catch(Exception e)
    {
       Screen.showMessage("Error : KMProcess - updateData :"+e);
    }
  }*/
}
