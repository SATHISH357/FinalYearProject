import java.io.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class KMResults extends JDialog implements ActionListener
{
  JLabel lbltitle,lblcname,lblrecords,lblcname1,lblrecords1;
  JTextField txtpid;
  JScrollPane jsp;
  JTable jtab;
  TableColumn tcol;
  JPanel panel;
  Font afont,tfont,ofont;
  WindowListener wlist;
  JButton btback;
  MainMenu mainmenu;
  int ival = 0;
  double dval = 0.0;
  KMProcess kmprocess;
  KMClusters kmclusters;
  int row = 0,selno = 0;

  KMResults(MainMenu mainmenu,KMClusters kmclusters)
  {

    super(mainmenu,"Cluster Results",true);
    this.mainmenu = mainmenu;
    this.kmprocess = kmprocess;
    this.kmclusters = kmclusters;
    this.kmprocess = kmclusters.kmprocess;
    this.selno = kmclusters.selno;
    panel =(JPanel)getContentPane();
    panel.setLayout(null);

    Font tfont = new Font("Times New Roman",Font.PLAIN,14);
    Font ofont = new Font("Times New Roman",Font.BOLD,14);
    Font bfont = new Font("Times New Roman",Font.BOLD + Font.ITALIC,28);
    Font afont = new Font("MS Sans Serif",Font.BOLD,18);

    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

    lbltitle = new JLabel("Cluster Results",JLabel.CENTER);
    lbltitle.setFont(bfont);
    lbltitle.setSize(800,50);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);

    lblcname = new JLabel("Cluster Name :");
    lblcname.setSize(130,30);
    lblcname.setLocation(100,100);
    lblcname.setFont(ofont);
    panel.add(lblcname);

    lblcname1 = new JLabel(mainmenu.cname);
    lblcname1.setSize(100,30);
    lblcname1.setLocation(250,100);
    lblcname1.setFont(tfont);
    panel.add(lblcname1);

    lblrecords = new JLabel("Records :");
    lblrecords.setSize(100,30);
    lblrecords.setLocation(390,100);
    lblrecords.setFont(ofont);
    panel.add(lblrecords);

    lblrecords1 = new JLabel(""+mainmenu.rcount);
    lblrecords1.setSize(100,30);
    lblrecords1.setLocation(520,100);
    lblrecords1.setFont(tfont);
    panel.add(lblrecords1);


    jtab = new JTable(5000,7);
    jsp = new JScrollPane(jtab,v,h);
    jtab.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jtab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtab.setRowHeight(20);


    tcol = jtab.getColumn("A");
    tcol.setHeaderValue("S.No");
    tcol.setPreferredWidth(50);
    tcol.setResizable(false);

    tcol = jtab.getColumn("B");
    tcol.setHeaderValue("WSDL Address");
    tcol.setPreferredWidth(250);
    tcol.setResizable(false);

    tcol = jtab.getColumn("C");
    tcol.setHeaderValue("I.No");
    tcol.setPreferredWidth(40);
    tcol.setResizable(false);

    tcol = jtab.getColumn("D");
    tcol.setHeaderValue("User ID");
    tcol.setPreferredWidth(70);
    tcol.setResizable(false);

    tcol = jtab.getColumn("E");
    tcol.setHeaderValue("CPU Response Time (s)");
    tcol.setPreferredWidth(140);
    tcol.setResizable(false);

    tcol = jtab.getColumn("F");
    tcol.setHeaderValue("I/O Throughput (KBS)");
    tcol.setPreferredWidth(140);
    tcol.setResizable(false);

    tcol = jtab.getColumn("G");
    tcol.setHeaderValue("Records");
    tcol.setPreferredWidth(50);
    tcol.setResizable(false);

    jsp.setSize(750,250);
    jsp.setLocation(25,150);
    jsp.setVisible(true);
    panel.add(jsp);

                          
    btback = new JButton("Back");
    btback.setMnemonic('B');
    btback.setSize(100,30);
    btback.setLocation(350,450);
    btback.setFont(ofont);
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
    try
    {
      for(int i=0;i<kmprocess.clvect[selno].size();i++)
      {
        Data data = (Data) kmprocess.clvect[selno].get(i);
        jtab.setValueAt(""+(i+1),i,0);
        jtab.setValueAt(""+data.wsdladdr,i,1);
        ival = (int) data.ldval[1];
        jtab.setValueAt(""+ival, i, 2);
        ival = (int) data.ldval[2];
        jtab.setValueAt("User - "+ival, i, 3);
        dval = data.ldval[3];
        dval = Tools.round(dval, 3);
        jtab.setValueAt(""+dval, i, 4);
        dval = data.ldval[4];
        dval = Tools.round(dval, 3);
        jtab.setValueAt(""+dval, i, 5);
        ival = (int) data.ldval[5];
        jtab.setValueAt(""+ival, i, 6);
      }
    }catch(Exception e)
    {
      Screen.showMessage("Error : KMResults - assignData : "+e);
    }
  }
}
