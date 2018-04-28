import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

class DistanceEstimate extends JDialog implements ActionListener
{
  JPanel panel;
  JLabel lbltitle,lblsname;
  JButton btestimate,btback;
  WindowListener wlist;
  JComboBox jcbsnlist;
  JTable jtab;
  String str[] = {"2","3","4","5","6","7","8","9"};
  String ctlist[] = {"Static QoS","Dynamic QoS"};
  TableColumn tcol;
  Font afont,ofont,tfont;
  JScrollPane jsp;
  String cname = "" , str1 = "", sname = "";
  int rcount = 0;

  MainMenu mainmenu;
  int kvalue = 0, ccount = 0, ctypeno = 0;
  int selno = 0, selno1 = 0;
  Random rand;
  int row = 0;
  String wsdladdr = "";
  double dweight = 0.0, distance = 0.0;

  DistanceEstimate(MainMenu mainmenu)
  {
    super(mainmenu,"Service Distance Estimation Process",true);
    this.mainmenu = mainmenu;
    rand = new Random();
    panel = (JPanel)getContentPane();
    panel.setLayout(null);

    tfont = new Font("Times New Roman",Font.PLAIN,14);
    ofont = new Font("Times New Roman",Font.BOLD,14);
    afont = new Font("Times New Roman",Font.BOLD + Font.ITALIC,28);

    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

    lbltitle = new JLabel("Service Distance Estimation Process",JLabel.CENTER);
    lbltitle.setFont(afont);
    lbltitle.setSize(800,30);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);

    lblsname = new JLabel("Service Name");
    lblsname.setSize(150,30);
    lblsname.setLocation(100,100);
    lblsname.setFont(tfont);
    panel.add(lblsname);

    jcbsnlist = new JComboBox(mainmenu.wslist);
    jcbsnlist.setSize(480,30);
    jcbsnlist.setLocation(220,100);
    jcbsnlist.setFont(tfont);
    panel.add(jcbsnlist);

    btestimate =  new JButton("Estimate");
    btestimate.setSize(100,30);
    btestimate.setLocation(275,480);
    btestimate.setFont(ofont);
    btestimate.setMnemonic('E');
    btestimate.addActionListener(this);
    panel.add(btestimate);

    btback = new JButton("Back");
    btback.setSize(100,30);
    btback.setLocation(425,480);
    btback.setFont(ofont);
    btback.setMnemonic('B');
    btback.addActionListener(this);
    panel.add(btback);

    jtab = new JTable(2010,4);
    jsp = new JScrollPane(jtab,v,h);
    jtab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jtab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtab.setRowHeight(20);

    tcol = jtab.getColumn("A");
    tcol.setHeaderValue("S. No.");
    tcol.setPreferredWidth(50);
    tcol.setResizable(false);

    tcol = jtab.getColumn("B");
    tcol.setHeaderValue("WSDL Address");
    tcol.setPreferredWidth(340);
    tcol.setResizable(false);

    tcol = jtab.getColumn("C");
    tcol.setHeaderValue("Weight");
    tcol.setPreferredWidth(120);
    tcol.setResizable(false);

    tcol = jtab.getColumn("D");
    tcol.setHeaderValue("Distance");
    tcol.setPreferredWidth(120);
    tcol.setResizable(false);

    jsp.setSize(640,250);
    jsp.setLocation(70,170);
    jsp.setVisible(true);
    panel.add(jsp);

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
    if(ae.getSource() == btestimate)
    {
      selno = jcbsnlist.getSelectedIndex();
      if(selno == -1)
      {
        Screen.showMessage("Select a Service name from the list");
        jcbsnlist.requestFocus();
        return;
      }
      sname = (String) jcbsnlist.getSelectedItem();
      assignData();
    }
   if(ae.getSource() == btback)
   {
     dispose();
   }
  }
  public void assignData()
  {
    for(int i=0;i<row;i++)
      for(int j=0;j<4;j++)
        jtab.setValueAt("",i,j);
    row = 0;
    String sql = "";
    try
    {
      Class.forName("oracle.jdbc.OracleDriver");
      String cstr = "jdbc:oracle:thin:CloudRank/CloudRank@localhost:1521:xe";
      Connection con = DriverManager.getConnection(cstr);
      sql = "select wsdladdr, dweight, abs(dweight - (select dweight from swlist2 where wsdladdr = ?)) from swlist2 where wsdladdr <> ? order by wsdladdr";
      PreparedStatement pstat = con.prepareStatement(sql);
      pstat.setString(1, sname);
      pstat.setString(2, sname);
      ResultSet rset = pstat.executeQuery();
      while(rset.next())
      {
        wsdladdr = rset.getString(1);
        dweight = rset.getDouble(2);
        dweight = Tools.round(dweight, 4);
        distance = rset.getDouble(3);
        distance = Tools.round(distance, 4);

        jtab.setValueAt(""+(row+1),row,0);
        jtab.setValueAt(wsdladdr,row,1);
        jtab.setValueAt(""+dweight,row,2);
        jtab.setValueAt(""+distance,row,3);
        row++;
        if(row == kvalue)
          break;
      }
      rset.close();
      con.close();

    }catch(Exception e)
    {
      Screen.showMessage("Error : DistanceEstimate - assignData : "+e);
    }
  }
}
