package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import javax.swing.*;
//import oracle.jdeveloper.layout.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

/**
 * <p>Title: NetOptimisation</p>
 * <p>Description: Net topology optimisation</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SYRUS</p>
 * @author Vitaliy V. Shiriaev
 * @version 1.0
 */

public class DialogExit extends JDialog implements ActionListener
{
  String []key;
  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JPanel insetsPanel2 = new JPanel();
  JPanel insetsPanel3 = new JPanel();
  JButton button1 = new JButton();
  JLabel imageLabel = new JLabel();
  JLabel label1 = new JLabel();
  JLabel label2 = new JLabel();
  JLabel label3 = new JLabel();
  JLabel label4 = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  GridLayout gridLayout1 = new GridLayout();
  String product = "NetOptimisation";
  String version = "1.0";
  String copyright = "";
  String comments = "";
  JButton button2 = new JButton();
  JLabel jLabel1 = new JLabel();
  JButton button3 = new JButton();
  JLabel jLabel2 = new JLabel();
  //--------------------------------------------------------------------------------------------------------------
  public DialogExit(Frame parent, String []key)
  {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    this.key = key;
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  //--------------------------------------------------------------------------------------------------------------
  //Component initialization
  private void jbInit() throws Exception
  {
    //imageLabel.setIcon(new ImageIcon(DialogExit.class.getResource("[Your Image]")));
    this.setTitle("Закрытие редактора цен...");
    panel1.setLayout(borderLayout1);
    panel2.setLayout(borderLayout2);
    insetsPanel1.setLayout(flowLayout1);
    insetsPanel2.setLayout(flowLayout1);
    insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    gridLayout1.setRows(4);
    gridLayout1.setColumns(1);
    label1.setText("Данные были изменены.");
    label2.setText("Сохранить?");
    label3.setText(copyright);
    label4.setText(comments);
    insetsPanel3.setLayout(gridLayout1);
    insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
    button1.setText("Да ");
    button1.addActionListener(new java.awt.event.ActionListener()
    { public void actionPerformed(ActionEvent e)
      {  button1_actionPerformed(e);
      }
    });
    button1.addActionListener(this);
    button2.addActionListener(this);
    button2.setText("Отменить");
    button2.addActionListener(new java.awt.event.ActionListener()
    { public void actionPerformed(ActionEvent e)
      {  button2_actionPerformed(e);
      }
    });
    jLabel1.setText("     ");
    button3.addActionListener(this);
    button3.setText("Нет");
    button3.addActionListener(new java.awt.event.ActionListener()
    { public void actionPerformed(ActionEvent e)
      {  button3_actionPerformed(e);
      }
    });
    jLabel2.setText("     ");
    insetsPanel2.add(imageLabel, null);
    panel2.add(insetsPanel2, BorderLayout.WEST);
    this.getContentPane().add(panel1, null);
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);
    insetsPanel3.add(label4, null);
    panel2.add(insetsPanel3, BorderLayout.CENTER);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    insetsPanel1.add(button1, null);
    insetsPanel1.add(jLabel1, null);
    insetsPanel1.add(button3, null);
    insetsPanel1.add(jLabel2, null);
    insetsPanel1.add(button2, null);
    panel1.add(panel2, BorderLayout.NORTH);
    setResizable(true);
  }
  //--------------------------------------------------------------------------------------------------------------
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e)
  { if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {  cancel();
    }
    super.processWindowEvent(e);
  }
  //--------------------------------------------------------------------------------------------------------------
  //Close the dialog
  void cancel()
  {  dispose();
  }
  //--------------------------------------------------------------------------------------------------------------
  //Close the dialog on a button event
  public void actionPerformed(ActionEvent e)
  { if (e.getSource() == button2)
    {  cancel();
    }
  }
  //--------------------------------------------------------------------------------------------------------------
  void button1_actionPerformed(ActionEvent e)
  { this.key[0] = "yes";
    cancel();
  }
  //--------------------------------------------------------------------------------------------------------------
  void button3_actionPerformed(ActionEvent e)
  { this.key[0] = "no";
    cancel();
  }
  //--------------------------------------------------------------------------------------------------------------
  void button2_actionPerformed(ActionEvent e)
  { this.key[0] = "cancel";
    cancel();
  }
  //--------------------------------------------------------------------------------------------------------------
}