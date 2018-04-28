import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class MOSCProcess extends JDialog implements ActionListener
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
  String cclist[] = {"General", "Sequential", "Complimentory"};

  int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
  int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
  int row = 0,selno = 0, vmno = 0;
  MainMenu mainmenu;
  double rtvalue = 0.0, tpvalue = 0.0;
  int userid = 0;

  MOSCProcess(MainMenu mainmenu)
  {
    super(mainmenu, "Multi Objective Service Consolidation (MOSC) Process",true);
    this.mainmenu = mainmenu;
    panel = (JPanel)getContentPane();
    panel.setLayout(null);


    bfont = new Font("Times New Roman",Font.BOLD,14);
    ofont = new Font("Times New Roman",Font.PLAIN,14);
    tfont = new Font("Times New Roman",Font.BOLD + Font.ITALIC,28);

    lbltitle = new JLabel("Multi Objective Service Consolidation (MOSC) Process",JLabel.CENTER);
    lbltitle.setFont(tfont);
    lbltitle.setSize(800,50);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);

            
    jtab = new JTable(mainmenu.servcount,5);
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
    tcol.setPreferredWidth(290);
    tcol.setResizable(false);

    tcol = jtab.getColumn("C");
    tcol.setHeaderValue("User ID");
    tcol.setPreferredWidth(70);
    tcol.setResizable(false);

    tcol = jtab.getColumn("D");
    tcol.setHeaderValue("Dependency Type");
    tcol.setResizable(false);
    tcol.setPreferredWidth(160);

    tcol = jtab.getColumn("E");
    tcol.setHeaderValue("Virtual Machine (VM)");
    tcol.setPreferredWidth(140);
    tcol.setResizable(false);


    jsp.setSize(700,300);
    jsp.setLocation(50,100);
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
        int ctno = mainmenu.sclist[row];
        String ctype = cclist[ctno];
        jtab.setValueAt(ctype,row,3);
        rtvalue = Tools.round(rtvalue, 3);
        if(ctno == 0)
        {
          if(rtvalue <= 0.2)
            vmno = 1;
          else if(rtvalue <= 0.4)
            vmno = 2;
          else if(rtvalue <= 0.6)
            vmno = 3;
          else if(rtvalue <= 0.8)
            vmno = 4;
          else if(rtvalue <= 1.0)
            vmno = 5;
          else if(rtvalue <= 2.0)
            vmno = 6;
          else if(rtvalue <= 4.0)
            vmno = 7;
          else if(rtvalue <= 6.0)
            vmno = 8;
          else if(rtvalue <= 8.0)
            vmno = 9;
          else
            vmno = 10;
        }
        else if(ctno == 1)
        {
          if(tpvalue <= 0.4)
            vmno = 1;
          else if(tpvalue <= 0.6)
            vmno = 2;
          else if(tpvalue <= 0.8)
            vmno = 3;
          else if(tpvalue <= 1.0)
            vmno = 4;
          else if(tpvalue <= 5.0)
            vmno = 5;
          else if(tpvalue <= 10.0)
            vmno = 6;
          else if(tpvalue <= 20.0)
            vmno = 7;
          else if(tpvalue <= 30.0)
            vmno = 8;
          else if(tpvalue <= 50.0)
            vmno = 9;
          else
            vmno = 10;
        }
        else
        {
          int tno = Math.abs(mainmenu.rand.nextInt()) % 6;
          tno++;
          vmno = tno;
        }

        jtab.setValueAt(""+vmno, row, 4);
        row++;
      }
      rset.close();
      con.close();
    }catch(Exception e)
    {
      Screen.showMessage("Error : MOSCProcess - assignData : "+e);
    }
  }
}
