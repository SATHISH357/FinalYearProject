import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class AnalysisGraph extends JDialog  implements ActionListener
{
    JLabel lblimage;

    WindowListener wlist;
    Icon ic;
    JPanel panel;
    JButton btchart1,btchart2,btchart3,btback;
    MainMenu mainmenu;

    AnalysisGraph(MainMenu mainmenu)
    {
      super(mainmenu,"Performance Analysis",true);
      this.mainmenu = mainmenu;
      panel = (JPanel)getContentPane();
      panel.setLayout(null);

      ic = new ImageIcon("chart1.jpg");
      lblimage = new JLabel(ic, JLabel.CENTER);
      lblimage.setSize(800,400);
      lblimage.setLocation(0,0);
      lblimage.setVisible(true);
      panel.add(lblimage);


      btchart1 = new JButton("Response Time");
      btchart1.setMnemonic('R');
      btchart1.setSize(150,30);
      btchart1.setLocation(75,450);
      btchart1.setVisible(true);
      btchart1.addActionListener(this);
      panel.add(btchart1);

      btchart2 = new JButton("Throughput");
      btchart2.setMnemonic('T');
      btchart2.setSize(100,30);
      btchart2.setLocation(275,450);
      btchart2.setVisible(true);
      btchart2.addActionListener(this);
      panel.add(btchart2);

      btchart3 = new JButton("Failure Rate");
      btchart3.setMnemonic('F');
      btchart3.setSize(150,30);
      btchart3.setLocation(425,450);
      btchart3.setVisible(true);
      btchart3.addActionListener(this);
      panel.add(btchart3);

      btback = new JButton("Back");
      btback.setMnemonic('B');
      btback.setSize(100,30);
      btback.setLocation(625,450);
      btback.setVisible(true);
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

     setLocation(0,0);
     setSize(800,600);
     setVisible(true);
    }
  public void actionPerformed(ActionEvent ae)
  {
    if(ae.getSource() == btchart1)
    {
      ic = new ImageIcon("chart1.jpg");
      lblimage.setIcon(ic);
    }
    if(ae.getSource() == btchart2)
    {
      ic = new ImageIcon("chart2.jpg");
      lblimage.setIcon(ic);
    }
    if(ae.getSource() == btchart3)
    {
      ic = new ImageIcon("chart3.jpg");
      lblimage.setIcon(ic);
    }
    if(ae.getSource() == btback)
    {
      dispose();
    }
  }
}
