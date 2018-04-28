import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;


class MainMenu extends JFrame implements ActionListener
{
  JLabel lbltitle;
  JButton btservices,btqosproperty,btpredict,btcompare, btshutdown;
  WindowListener wlist;
  JPanel panel;
  Random rand;
  Icon icon;
  int usercount = 100, servcount = 1000;
  int sidlist[], sclist[];
  int serviceid = 0;
  String wsdladdr = "", servicetype = "";
  String cname = "";
  int rcount = 0, logcount = 0;
  Vector wslist, sdlist;
  int sscount = 0, dscount = 0;
  DBConvert1 dbconvert1;
  DBConvert2 dbconvert2;

  MainMenu()
  {
    super("Resource and Service Consolidation Approach using History Logs");
    panel = (JPanel)getContentPane();
    panel.setLayout(null);
    panel.setBackground(new Color(255,255,255));
    rand = new Random();
    wslist = new Vector();
    sdlist = new Vector();
    DataCenterMgmt.construct();

    icon = new ImageIcon("bttitle.jpg");
    lbltitle = new JLabel(icon);
    lbltitle.setSize(800,100);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);


    icon = new ImageIcon("btmenu.jpg");
    btservices = new JButton(icon);
    btservices.setSize(icon.getIconWidth(),icon.getIconHeight());
    btservices.setLocation(300,150);
    btservices.setBorderPainted(false);
    btservices.setContentAreaFilled(false);
    btservices.addActionListener(this);
    panel.add(btservices);

    icon = new ImageIcon("btmenu2.jpg");
    btqosproperty = new JButton(icon);
    btqosproperty.setSize(icon.getIconWidth(),icon.getIconHeight());
    btqosproperty.setLocation(300,220);
    btqosproperty.setBorderPainted(false);
    btqosproperty.setContentAreaFilled(false);
    btqosproperty.addActionListener(this);
    panel.add(btqosproperty);

    icon = new ImageIcon("btmenu3.jpg");
    btpredict = new JButton(icon);
    btpredict.setSize(icon.getIconWidth(),icon.getIconHeight());
    btpredict.setLocation(300,290);
    btpredict.setBorderPainted(false);
    btpredict.setContentAreaFilled(false);
    btpredict.addActionListener(this);
    panel.add(btpredict);


    icon = new ImageIcon("btmenu4.jpg");
    btcompare = new JButton(icon);
    btcompare.setSize(icon.getIconWidth(),icon.getIconHeight());
    btcompare.setLocation(300,360);
    btcompare.setBorderPainted(false);
    btcompare.setContentAreaFilled(false);
    btcompare.addActionListener(this);
    panel.add(btcompare);
                              
    icon = new ImageIcon("btmenu5.jpg");
    btshutdown = new JButton(icon);
    btshutdown.setSize(icon.getIconWidth(),icon.getIconHeight());
    btshutdown.setLocation(300,430);
    btshutdown.setBorderPainted(false);
    btshutdown.setContentAreaFilled(false);
    btshutdown.addActionListener(this);
    panel.add(btshutdown);


    wlist = new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        System.exit(0);
      }
    };
    addWindowListener(wlist);
    Screen.update(this);
    if(Screen.flag == 0)
      Screen.refresh();
    servcount = DBTools.getRowCount("CloudRank","CloudRank","CloudRank","spview1");
    getServiceList();
    getServiceTypes();
    getLogCount();
    setSize(800,600);
    setVisible(true);
  }
  public void actionPerformed(ActionEvent ae)
  {
   if(ae.getSource() == btservices)
    {
      new SMPMenu(this);
    }
    if(ae.getSource() == btqosproperty)
    {
      new ServiceClassify(this);
    }
    if(ae.getSource() == btpredict)
    {
      new SCPMenu(this);
    }
    if(ae.getSource() == btcompare)
    {
      new MVMMenu(this);
    } 
    if(ae.getSource() == btshutdown)
    {
       System.exit(0);
    }                   
  }
  public void getServiceList()
  {
    sidlist = new int[servcount];
    sclist = new int[servcount];
    wslist.removeAllElements();
    int i = 0;
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Select distinct serviceid, wsdladdr from stprtview1 order by wsdladdr";
      Statement stat = con.createStatement();
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
      {
        serviceid = rset.getInt(1);
        wsdladdr = rset.getString(2);
        wslist.add(wsdladdr);
        sidlist[i] = serviceid;
        sclist[i] = Math.abs(rand.nextInt()) % 3;
        i++;
      }
      rset.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : MainMenu - getServiceList : "+e);
    }
  }
  public void getServiceTypes()
  {
    sdlist.removeAllElements();
    int i = 0;
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Select distinct servicetype from servicetypes order by servicetype";
      Statement stat = con.createStatement();
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
      {
        servicetype = rset.getString(1);
        sdlist.add(servicetype);
        i++;
      }
      rset.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : MainMenu - getServiceTypes : "+e);
    }
  }
  public void getLogCount()
  {
    logcount = 0;
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      Statement stat = con.createStatement();
      String sql = "select count(*) from hlview1 where serviceid <= " + servcount;
      ResultSet rset = stat.executeQuery(sql);
      while(rset.next())
        logcount = rset.getInt(1);
      rset.close();
      stat.close();
      con.close();
//      Screen.showMessage(""+ servcount+" : "+logcount);
    }catch(Exception e)
    {
      Screen.showMessage("Error : MainMenu - getLogCount : "+e);
    }
  }


  public static void main(String args[])
  {
    new MainMenu();
  }
}
