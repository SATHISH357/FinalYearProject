import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

class MVMMenu extends JDialog implements ActionListener
{
  JLabel lbltitle;
  JButton btdbsc,btmosc,btperformances,btback;
  WindowListener wlist;
  JPanel panel;
  Icon icon;
  MainMenu mainmenu;
  String imgtype = "  ";

  MVMMenu(MainMenu mainmenu)
  {
    super(mainmenu,"Mapping Virtual Machines (VM)",true);
    this.mainmenu = mainmenu;
    panel = (JPanel)getContentPane();
    panel.setLayout(null);
    panel.setBackground(new Color(255,255,255));

    icon = new ImageIcon("btmvmtitle.jpg");
    lbltitle = new JLabel(icon);
    lbltitle.setSize(800,100);
    lbltitle.setLocation(0,30);
    panel.add(lbltitle);

    icon = new ImageIcon("btmvmmenu.jpg");
    btdbsc = new JButton(icon);
    btdbsc.setSize(icon.getIconWidth(),icon.getIconHeight());
    btdbsc.setLocation(225,170);
    btdbsc.setBorderPainted(false);
    btdbsc.setContentAreaFilled(false);
    btdbsc.addActionListener(this);
    panel.add(btdbsc);

    icon = new ImageIcon("btmvmmenu2.jpg");
    btmosc = new JButton(icon);
    btmosc.setSize(icon.getIconWidth(),icon.getIconHeight());
    btmosc.setLocation(225,240);
    btmosc.setBorderPainted(false);
    btmosc.setContentAreaFilled(false);
    btmosc.addActionListener(this);
    panel.add(btmosc);

    icon = new ImageIcon("btmvmmenu3.jpg");
    btperformances = new JButton(icon);
    btperformances.setSize(icon.getIconWidth(),icon.getIconHeight());
    btperformances.setLocation(225,310);
    btperformances.setBorderPainted(false);
    btperformances.setContentAreaFilled(false);
    btperformances.addActionListener(this);
    panel.add(btperformances);

    icon = new ImageIcon("btmvmmenu4.jpg");
    btback = new JButton(icon);
    btback.setSize(icon.getIconWidth(),icon.getIconHeight());
    btback.setLocation(225,380);
    btback.setBorderPainted(false);
    btback.setContentAreaFilled(false);
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
    if(ae.getSource() == btdbsc)
    {
      new DBSCProcess(mainmenu);
    }
    if(ae.getSource() == btmosc)
    {
      new MOSCProcess(mainmenu);
    }
    if(ae.getSource() == btperformances)
    {
      new AnalysisGraph(mainmenu);
    }
    if(ae.getSource() == btback)
    {
      dispose();
    }                     
  }
}
