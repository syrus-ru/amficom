package com.syrus.AMFICOM.Client.Configure.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: Проект: Интегрированной Системы Мониторинга</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Syrus Systems</p>
 * @author Markin E. N.
 * @version 1.0
 */

public class NewMapContextDialog extends JDialog
{
  public PropertiesPanel mainPanel = new MapContextPane();
  JPanel buttonPanel = new JPanel();
  JButton acceptButton = new JButton();
  JButton cancelButton = new JButton();
  JButton helpButton = new JButton();

  MapContext mapContext;

  boolean accept = false;
	BorderLayout borderLayout1 = new BorderLayout();
	private FlowLayout flowLayout1 = new FlowLayout();

  public NewMapContextDialog(Frame frame, String title, boolean modal, MapContext myMapContext)
  {
    super(frame, title, modal);
    try
    {
      mapContext = myMapContext;
      jbInit();
      pack();
	  mainPanel.setObjectResource(mapContext);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception
  {
		this.setResizable(false);
		this.setSize(new Dimension(590, 300));
		mainPanel.setPreferredSize(new Dimension(590, 300));
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(borderLayout1);
    buttonPanel.setLayout(flowLayout1);

    acceptButton.setText("OK");
    acceptButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        acceptButton_actionPerformed(e);
      }
    });
    cancelButton.setText("Отменить");
    cancelButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        cancelButton_actionPerformed(e);
      }
    });
    helpButton.setText("Помощь");
		flowLayout1.setAlignment(2);

    getContentPane().add(mainPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(acceptButton, null);
    buttonPanel.add(cancelButton, null);
//    buttonPanel.add(helpButton, null);
  }

  public boolean ifAccept()
  {
   return accept;
  }

  void acceptButton_actionPerformed(ActionEvent e)
  {
	if(mainPanel.modify())
	{
		accept = true;
		this.dispose();
	}
	else
	{
		new MessageBox("Невозможно создать вид - не все поля заданы").show();
	}
  }

  void cancelButton_actionPerformed(ActionEvent e)
  {
    accept = false;
    this.dispose();
  }
}