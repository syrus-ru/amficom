package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class ObjectResourcePropertiesDialog extends JDialog
{
	protected PropertiesPanel mainPanel = null;
	protected JPanel buttonPanel = new JPanel();
	protected JButton acceptButton = new JButton();
	protected JButton cancelButton = new JButton();
	protected JButton helpButton = new JButton();

	protected ObjectResource or;

	protected boolean accept = false;
	protected BorderLayout borderLayout1 = new BorderLayout();
	protected  FlowLayout flowLayout1 = new FlowLayout();

	public ObjectResourcePropertiesDialog(JFrame frame, String title, boolean modal, ObjectResource or, PropertiesPanel panel)
	{
		super(frame, title, modal);

		this.or = or;
		mainPanel = panel;
		if(mainPanel != null)
			mainPanel.setObjectResource(or);

		try
		{
			jbInit();
			pack();
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
		if(mainPanel != null)
			mainPanel.setPreferredSize(new Dimension(590, 300));
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(borderLayout1);
		buttonPanel.setLayout(flowLayout1);

		acceptButton.setText(LangModel.getString("Ok"));
		acceptButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				acceptButton_actionPerformed(e);
			}
		});
		cancelButton.setText(LangModel.getString("Cancel"));
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		helpButton.setText(LangModel.getString("Help"));
		
		flowLayout1.setAlignment(2);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(acceptButton, null);
		buttonPanel.add(cancelButton, null);
//    	buttonPanel.add(helpButton, null);
	}

	public boolean ifAccept()
	{
		return accept;
	}

	void acceptButton_actionPerformed(ActionEvent e)
	{
		if(mainPanel == null || mainPanel.modify())
		{
			accept = true;
			this.dispose();
		}
		else
		{
			new MessageBox(LangModel.getString("NotAllFieldsValid")).show();
		}
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		accept = false;
		this.dispose();
	}
}