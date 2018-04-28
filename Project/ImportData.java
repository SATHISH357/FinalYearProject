import java.io.*;
import java.util.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class ImportData extends JDialog implements ActionListener
{
  JLabel lbltitle;
  JLabel lblusers, lblservices, lblstarttime, lblendtime, lblrecords;
  JLabel lblstarttime1,lblendtime1,lblrecords1;
  JComboBox jcbusers, jcbservices;
  String uclist[] = {"50","100"};
  String sclist[] = {"100","200","300","400","500","600","700","800","900","1000"};
  JPanel panel;
  Font afont,tfont,ofont;
  JButton btimport, btback;

  File mdgfile;
  StringTokenizer stk;
  String mdgname = "", tsessionid = "", pageurl = "", rdate = "",tspantime = "";
  String reqtime = "", rtime = "";
  Vector linklist;
  String stime = "", etime = "";
  String hdate = "", pagetype = "";
  int spantime = 0,sessionid = 0, reccount = 0;

  WindowListener wlist;
  String str = "", prodname = "";
  long mdgsize = 0;
  int tmcount = 0, tlcount = 0, userid = 0;
  double longitude = 0.0, latitude = 0.0;
  String ipaddr = "", country = "";
  LoadProperties loadproerties;
  MainMenu mainmenu;
  int selno = 0, usercount = 0, servcount = 0;

  ImportData(MainMenu mainmenu)
  {
    super(mainmenu,"Import Data", true);
    this.mainmenu = mainmenu;
    linklist = new Vector();
    panel = (JPanel)getContentPane();
    panel.setLayout(null);


    tfont = new Font("Times New Roman",Font.PLAIN,14);
    ofont = new Font("Times New Roman",Font.BOLD,14);
    afont = new Font("Times New Roman",Font.BOLD + Font.ITALIC,28);


    lbltitle = new JLabel("Import Data",JLabel.CENTER);
    lbltitle.setFont(afont);
    lbltitle.setSize(800,30);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);
   
    lblusers = new JLabel("Users:");
    lblusers.setSize(150,30);
    lblusers.setLocation(100,100);
    lblusers.setFont(tfont);
    panel.add(lblusers);

    jcbusers = new JComboBox(uclist);
    jcbusers.setSize(70,30);
    jcbusers.setLocation(220,100);
    jcbusers.setFont(tfont);
    panel.add(jcbusers);

    lblservices = new JLabel("Services:");
    lblservices.setSize(150,30);
    lblservices.setLocation(100,150);
    lblservices.setFont(tfont);
    panel.add(lblservices);

    jcbservices = new JComboBox(sclist);
    jcbservices.setSize(70,30);
    jcbservices.setLocation(220,150);
    jcbservices.setFont(tfont);
    panel.add(jcbservices);

    lblstarttime = new JLabel("Start Time:");
    lblstarttime.setSize(150,30);
    lblstarttime.setLocation(100,200);
    lblstarttime.setFont(tfont);
    panel.add(lblstarttime);

    lblstarttime1 = new JLabel("00:00:00");
    lblstarttime1.setSize(150,30);
    lblstarttime1.setLocation(220,200);
    lblstarttime1.setFont(tfont);
    panel.add(lblstarttime1);


    lblendtime = new JLabel("End Time:");
    lblendtime.setSize(150,30);
    lblendtime.setLocation(100,250);
    lblendtime.setFont(tfont);
    panel.add(lblendtime);

    lblendtime1 = new JLabel("00:00:00");
    lblendtime1.setSize(150,30);
    lblendtime1.setLocation(220,250);
    lblendtime1.setFont(tfont);
    panel.add(lblendtime1);

    lblrecords = new JLabel("Records:");
    lblrecords.setSize(150,30);
    lblrecords.setLocation(100,300);
    lblrecords.setFont(tfont);
    panel.add(lblrecords);

    lblrecords1 = new JLabel("0");
    lblrecords1.setSize(150,30);
    lblrecords1.setLocation(220,300);
    lblrecords1.setFont(tfont);
    panel.add(lblrecords1);

    btimport = new JButton("Import");
    btimport.setMnemonic('I');
    btimport.setSize(100,30);
    btimport.setLocation(275,350);
    btimport.setFont(ofont);
    btimport.addActionListener(this);
    btimport.setVisible(true);
    panel.add(btimport);


    btback = new JButton("Back");
    btback.setMnemonic('B');
    btback.setSize(80,30);
    btback.setLocation(425,350);
    btback.setFont(ofont);
    btback.addActionListener(this);
    btback.setVisible(true);
    panel.add(btback);

    wlist = new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        dispose();
      }
    };
    addWindowListener(wlist);
    Screen.update1(this);
    if(Screen.flag == 0)
      Screen.refresh();

    setSize(800,600);
    setVisible(true);
  }
  public void actionPerformed(ActionEvent ae)
  {
    if(ae.getSource() == btimport)
    {
      selno = jcbusers.getSelectedIndex();
      if(selno == -1)
      {
        Screen.showMessage("Select an user count from the list");
        jcbusers.requestFocus();
        return;
      }
      str = (String) jcbusers.getSelectedItem();
      usercount = Integer.parseInt(str);
      selno = jcbservices.getSelectedIndex();
      if(selno == -1)
      {
        Screen.showMessage("Select a service count from the list");
        jcbservices.requestFocus();
        return;
      }
      str = (String) jcbservices.getSelectedItem();
      servcount = Integer.parseInt(str);
      mainmenu.usercount = usercount;
      mainmenu.servcount = servcount;

      stime = Tools.getTime();
      lblstarttime1.setText(stime);
      DBTools.clearTable("CloudRank","CloudRank","CloudRank","stprtlist");
      DBTools.clearTable("CloudRank","CloudRank","CloudRank","dtprtlist");
      DBTools.clearTable("CloudRank","CloudRank","CloudRank","swlist");
//      LoadProperties loadprop = new LoadProperties(usercount, servcount);
//      loadprop.loadSP();
//      loadprop.loadDP();
      loadData();
      etime = Tools.getTime();
      lblendtime1.setText(etime);
      lblrecords1.setText(""+servcount);
      mainmenu.getServiceList();
      Screen.showMessage("Service properties loaded");
    }
    if(ae.getSource() == btback)
    {
      dispose();
    }
  }
  public void loadData()
  {
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "drop table stprtlist";
      Statement stat = con.createStatement();
      stat.execute(sql);
      sql = "Create table stprtlist as select userid, serviceid, tpvalue, rtvalue from stprtlist1 where serviceid<="+servcount;
      stat.execute(sql);
      sql = "drop table dtprtlist";
      stat.execute(sql);
      sql = "Create table dtprtlist as select userid,serviceid,opercount,frate,insno,tsno,tpvalue,rtvalue from dtprtlist1 where serviceid<="+servcount;
      stat.execute(sql);
      sql = "drop table swlist";
      stat.execute(sql);
      sql = "Create table swlist as select * from swlist1 where serviceid<="+servcount;
      stat.execute(sql);
      sql = "drop table srlist";
      stat.execute(sql);
      sql = "Create table srlist as select * from srview2 where serviceid<="+servcount;
      stat.execute(sql);


      sql = "select * from servicelist where serviceid <=" + mainmenu.servcount;
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
      {
        int tsid = rset.getInt(1);
        String tsname = rset.getString(2);
      }
      stat.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : ImportData - loadData : "+e);
    }
  }
}
