package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

import java.awt.BorderLayout;
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

public class NewJMapContextDialog extends JDialog
{
  public PropertiesPanel mainPanel = new ISMMapContextPane();
  JPanel buttonPanel = new JPanel();
  JButton acceptButton = new JButton();
  JButton cancelButton = new JButton();
  JButton helpButton = new JButton();

  MapContext mapContext;

  boolean accept = false;
	BorderLayout borderLayout1 = new BorderLayout();
	private FlowLayout flowLayout1 = new FlowLayout();

  public NewJMapContextDialog(Frame frame, String title, boolean modal, MapContext myMapContext)
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
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(borderLayout1);
    buttonPanel.setLayout(flowLayout1);

    acceptButton.setText("Принять");
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
    buttonPanel.add(helpButton, null);
  }

  public boolean ifAccept()
  {
   return accept;
  }

  void acceptButton_actionPerformed(ActionEvent e)
  {
/*
    mapContext.id = idTextField.getText();
    mapContext.name = nameTextField.getText();
    mapContext.description = descriptionTextPane.getText();
    mapContext.user_id = ownerTextField.getText();
    mapContext.domain_id = domainTextField.getText();
*/
    accept = true;
	mainPanel.modify();
    this.dispose();
  }

  void cancelButton_actionPerformed(ActionEvent e)
  {
    accept = false;
    this.dispose();
  }
}