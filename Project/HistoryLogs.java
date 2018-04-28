import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class HistoryLogs extends JDialog implements ActionListener
{
  JLabel lbltitle;
  JTextField txtsitename,txtpages,txtduration;
  JButton btback;
  JPanel panel;
  JTable jtab;
  TableColumn tcol;
  JScrollPane jsp;
  WindowListener wlist;
  Font tfont,bfont,ofont;
  boolean tflag = false, dflag = false;
  String urlname = "";

  int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
  int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
  int row = 0,selno = 0;
  MainMenu mainmenu;
  double rtvalue = 0.0, tpvalue = 0.0;
  int userid = 0;

  HistoryLogs(MainMenu mainmenu)
  {
    super(mainmenu, "Service History Log View",true);
    this.mainmenu = mainmenu;
    panel = (JPanel)getContentPane();
    panel.setLayout(null);


    bfont = new Font("Times New Roman",Font.BOLD,14);
    ofont = new Font("Times New Roman",Font.PLAIN,14);
    tfont = new Font("Times New Roman",Font.BOLD + Font.ITALIC,28);

    lbltitle = new JLabel("Service History Log View",JLabel.CENTER);
    lbltitle.setFont(tfont);
    lbltitle.setSize(800,50);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);

            
    jtab = new JTable(mainmenu.servcount,6);
    jsp = new JScrollPane(jtab,v,h);
    jtab.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jtab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtab.setRowHeight(20);
    jtab.setFont(ofont);


    tcol = jtab.getColumn("A");
    tcol.setHeaderValue("S.No");
    tcol.setPreferredWidth(40);
    tcol.setResizable(false);

    tcol = jtab.getColumn("B");
    tcol.setHeaderValue("WSDL Address");
    tcol.setPreferredWidth(300);
    tcol.setResizable(false);

    tcol = jtab.getColumn("C");
    tcol.setHeaderValue("User ID");
    tcol.setPreferredWidth(70);
    tcol.setResizable(false);

    tcol = jtab.getColumn("D");
    tcol.setHeaderValue("CPU Response Time(s)");
    tcol.setPreferredWidth(140);
    tcol.setResizable(false);

    tcol = jtab.getColumn("E");
    tcol.setHeaderValue("I/O Throughput (KBS)");
    tcol.setPreferredWidth(140);
    tcol.setResizable(false);

    tcol = jtab.getColumn("F");
    tcol.setHeaderValue("Count");
    tcol.setPreferredWidth(50);
    tcol.setResizable(false);

    jsp.setSize(750,300);
    jsp.setLocation(25,100);
    jsp.setVisible(true);
    panel.add(jsp);

    btback = new JButton("Back");
    btback.setSize(100,30);
    btback.setLocation(350,450);
    btback.setMnemonic('B');
    btback.setFont(bfont);
    btback.addActionListener(this);
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

    assignData();
    System.out.println("finished");
    setSize(800,600);
    setVisible(true);
  }
  public void actionPerformed(ActionEvent ae)
  {
    if(ae.getSource() == btback)
    {
      dispose();
    }
  }
  public void assignData()
  {
    row = 0;
    String wsdladdr = "", data = "";
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      String sql = "Select * from spview1 where serviceid <= ? order by serviceid";
      PreparedStatement pstat = con.prepareStatement(sql);
      pstat.setInt(1,mainmenu.servcount);
      ResultSet rset = pstat.executeQuery();
      while(rset.next())
      {
        userid = rset.getInt(1);
        tpvalue = rset.getDouble(3);
        rtvalue = rset.getDouble(4);
        int srate = rset.getInt(5);
        wsdladdr = rset.getString(6);
        jtab.setValueAt(""+(row+1),row,0);
        jtab.setValueAt(wsdladdr,row,1);
        jtab.setValueAt("User - "+userid,row,2);
        rtvalue = Tools.round(rtvalue, 3);
        jtab.setValueAt(""+rtvalue,row,3);
        tpvalue = Tools.round(tpvalue, 3);
        jtab.setValueAt(""+tpvalue,row,4);
        jtab.setValueAt(""+srate,row,5);
        row++; 
      }
      rset.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : HistoryLogs - assignData : "+e);
    }
  }
}
