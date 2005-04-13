/*
 * $Id: ObjectResourcePropertiesDialog.java,v 1.10 2005/04/13 21:40:46 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/04/13 21:40:46 $
 * @module generalclient_v1
 */
public class ObjectResourcePropertiesDialog extends JDialog
{
	protected ObjectResourcePropertiesPane mainPane = null;
	protected JPanel buttonPanel = new JPanel();
	protected JButton acceptButton = new JButton();
	protected JButton cancelButton = new JButton();
	protected JButton helpButton = new JButton();

	protected ObjectResource or;

	protected boolean accept = false;
	protected BorderLayout borderLayout1 = new BorderLayout();
	protected  FlowLayout flowLayout1 = new FlowLayout();

	public ObjectResourcePropertiesDialog(
			JFrame frame, 
			String title, 
			boolean modal, 
			ObjectResource or, 
			ObjectResourcePropertiesPane pane)
	{
		super(frame, title, modal);

		this.or = or;
		mainPane = pane;
		if(mainPane != null)
			mainPane.setObjectResource(or);

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
		this.setSize(new Dimension(590, 400));
		if(mainPane != null)
			((JComponent )mainPane).setPreferredSize(new Dimension(590, 400));
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(borderLayout1);
		buttonPanel.setLayout(flowLayout1);

		acceptButton.setText(LangModel.getString("Ok"));
		acceptButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				accept();
			}
		});
		cancelButton.setText(LangModel.getString("Cancel"));
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancel();
			}
		});
		helpButton.setText(LangModel.getString("Help"));
		
		flowLayout1.setAlignment(2);

		if(mainPane != null)
			getContentPane().add((JComponent )mainPane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(acceptButton, null);
		buttonPanel.add(cancelButton, null);
//    	buttonPanel.add(helpButton, null);
	}

	public boolean ifAccept()
	{
		return accept;
	}

	void accept()
	{
		if(mainPane == null || mainPane.modify())
		{
			accept = true;
			this.dispose();
		}
		else
		{
			new MessageBox(LangModel.getString("NotAllFieldsValid")).show();
		}
	}

	void cancel()
	{
		if(mainPane != null)
		{
			mainPane.cancel();
		}
		accept = false;
		this.dispose();
	}
}
