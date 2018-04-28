import java.io.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class KMClusters extends JDialog implements ActionListener
{
  JLabel lbltitle,lblccount;
  JComboBox jcbccount;
  JScrollPane jsp;
  JTable jtab;
  TableColumn tcol;
  JPanel panel;
  Font afont,tfont,ofont;
  WindowListener wlist;
  JButton btgo,btlist,btback;
  MainMenu mainmenu;
  KMProcess kmprocess;
  String cname = "",str ="";
  int rcount = 0;

  String cnos[] ={"2","3","4","5"};
  int selno = 0, kv = 0, row = 0;

  KMClusters(MainMenu mainmenu)
  {
    super(mainmenu, "Clustering Process with HiKM Algorithm",true);
    this.mainmenu = mainmenu;
    panel = (JPanel)getContentPane();
    panel.setLayout(null);

    Font tfont = new Font("Times New Roman",Font.PLAIN,14);
    Font ofont = new Font("Times New Roman",Font.BOLD,14);
    Font bfont = new Font("Times New Roman",Font.BOLD + Font.ITALIC,28);
    Font afont = new Font("MS Sans Serif",Font.BOLD,18);

    lbltitle = new JLabel("Clustering Process with HiKM Algorithm",JLabel.CENTER);
    lbltitle.setFont(bfont);
    lbltitle.setSize(800,50);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);


    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

    lblccount = new JLabel("Cluster Count :");
    lblccount.setSize(200,30);
    lblccount.setLocation(200,100);
    lblccount.setFont(ofont);
    panel.add(lblccount);

    jcbccount = new JComboBox(cnos);
    jcbccount.setSize(100,30);
    jcbccount.setLocation(350,100);
    jcbccount.setFont(tfont);
    panel.add(jcbccount);

    jtab = new JTable(2000,3);
    jsp = new JScrollPane(jtab,v,h);
    jtab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jtab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtab.setRowHeight(20);
    jtab.setFont(tfont);

    tcol = jtab.getColumn("A");
    tcol.setHeaderValue("S.No");
    tcol.setPreferredWidth(50);
    tcol.setResizable(false);

    tcol = jtab.getColumn("B");
    tcol.setHeaderValue("Cluster Name");
    tcol.setPreferredWidth(150);
    tcol.setResizable(false);

    tcol = jtab.getColumn("C");
    tcol.setHeaderValue("Records");
    tcol.setPreferredWidth(200);
    tcol.setResizable(false);

    jsp.setSize(400,300);
    jsp.setLocation(200,150);
    jsp.setVisible(true);
    panel.add(jsp);

    btgo = new JButton("Go");
    btgo.setMnemonic('G');
    btgo.setSize(100,30);
    btgo.setLocation(200,470);
    btgo.setFont(ofont);
    btgo.addActionListener(this);
    panel.add(btgo);

    btlist = new JButton("List");
    btlist.setMnemonic('L');
    btlist.setSize(100,30);
    btlist.setLocation(350,470);
    btlist.setFont(ofont);
    btlist.addActionListener(this);
    panel.add(btlist);

    btback = new JButton("Back");
    btback.setMnemonic('B');
    btback.setSize(100,30);
    btback.setLocation(500,470);
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

    setSize(800,600);
    setVisible(true);
  }
  public void actionPerformed(ActionEvent ae)
  {
    if(ae.getSource() == btgo)
    {
       selno = jcbccount.getSelectedIndex();
       if(selno == -1)
       {
         Screen.showMessage("Select Any Type Of Cluster :");
         return;
       }
//      btgo.setEnabled(false);
      selno = jcbccount.getSelectedIndex();
      kv = selno + 2;
      mainmenu.getLogCount();
      kmprocess = new KMProcess(this,kv);
   }
    if(ae.getSource() == btlist)
    {
      selno = jtab.getSelectedRow();
      if(selno == -1 || selno >= kv)
      {
        Screen.showMessage("Select a  Cluster from the list:");
        return;
      }
      cname = (String) jtab.getValueAt(selno,1);
      mainmenu.cname = cname; 
      str = (String) jtab.getValueAt(selno,2);
      rcount = Integer.parseInt(str);
      mainmenu.rcount = rcount;

      new KMResults(mainmenu,this);
    }
    if(ae.getSource() == btback)
    {
      dispose();
    }
  }
}
