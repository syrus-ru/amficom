/*
 * $Id: ObjectResourceSelectionDialog.java,v 1.2 2004/09/27 05:53:30 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/27 05:53:30 $
 * @module generalclient_v1
 */
public class ObjectResourceSelectionDialog extends JDialog
{
	private JPanel jPanel1 = new JPanel();
	private JLabel jLabel = new JLabel();

	private ObjectResourceComboBox comboBox = new ObjectResourceComboBox();

	private JButton buttonOk = new JButton();
	private JButton buttonCancel = new JButton();

	private int retCode = 0;
	private ObjectResource selected;
	
	public static final int RET_OK = 1;
	public static final int RET_CANCEL = 2;

	private Collection contents;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	protected ObjectResourceSelectionDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		try
		{
			jbInit();
			pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ObjectResourceSelectionDialog(Collection contents)
	{
		this(Environment.getActiveWindow(), LangModel.getString("SelectElement"), false);
		setContents(contents);
	}

	public ObjectResourceSelectionDialog(Collection contents, ObjectResource or)
	{
		this(contents);
		setSelected(or);
	}

	private void jbInit() throws Exception
	{
		getContentPane().setLayout(gridBagLayout1);

		this.setResizable(false);

		jLabel.setText(LangModel.getString("Element"));

		buttonOk.setText(LangModel.getString("buttonSelect"));
		buttonOk.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonOk_actionPerformed(e);
				}
			});
		buttonCancel.setText(LangModel.getString("buttonCancel"));
		buttonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonCancel_actionPerformed(e);
				}
			});

		jLabel.setPreferredSize(new Dimension(100, 20));
//		comboBox.setPreferredSize(new Dimension(200, 20));
		
//		getContentPane().setLayout(new BorderLayout());
//		getContentPane().add(jPanel1, BorderLayout.CENTER);

		getContentPane().add(jLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		getContentPane().add(comboBox, new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		getContentPane().add(buttonOk, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		getContentPane().add(buttonCancel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));

//		jPanel1.add(Box.createVerticalGlue(), new GridBagConstraints(1, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void setSelected(ObjectResource or)
	{
		comboBox.setSelected(or);
	}
	
	public void setContents(Collection contents)
	{
		this.contents = contents;
		comboBox.setContents(contents, false);
	}
	
	public ObjectResource getSelected()
	{
		return this.selected;
	}
	
	public int getReturnCode()
	{
		return this.retCode;
	}

	public void setLabel(String text)
	{
		this.jLabel.setText(text);
	}

	private void buttonOk_actionPerformed(ActionEvent e)
	{
		try
		{
			selected = (ObjectResource )comboBox.getSelectedItem();
			retCode = RET_OK;
		}
		catch (Exception ex)
		{
			selected = null;
			retCode = RET_CANCEL;
			ex.printStackTrace();
		}
		dispose();
	}

	private void buttonCancel_actionPerformed(ActionEvent e)
	{
		retCode = RET_CANCEL;
		dispose();
	}

}

