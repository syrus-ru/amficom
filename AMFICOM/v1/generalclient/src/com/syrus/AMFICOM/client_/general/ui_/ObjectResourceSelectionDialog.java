/*
 * $Id: ObjectResourceSelectionDialog.java,v 1.1 2005/01/11 16:36:52 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import javax.swing.*;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/01/11 16:36:52 $
 * @module generalclient_v1
 */
public class ObjectResourceSelectionDialog extends JDialog
{
	private static Insets insets = new Insets(10, 10, 10, 10);

	private JPanel jPanel1 = new JPanel();
	private JLabel jLabel = new JLabel();

	private ObjComboBox comboBox = null;
	private ObjectResourceController controller = null;
	private ObjListModel model = null;

	private JButton buttonOk = new JButton();
	private JButton buttonCancel = new JButton();

	private int retCode = 0;
	private Object selected;
	
	public static final int RET_OK = 1;
	public static final int RET_CANCEL = 2;

	private Collection contents;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	protected ObjectResourceSelectionDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);

		controller = NamedObjectController.getInstance();
		comboBox = new ObjComboBox(controller, NamedObjectController.KEY_NAME);
		model = (ObjListModel )comboBox.getModel();

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

	public ObjectResourceSelectionDialog(Collection contents, Object or)
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

		jLabel.setPreferredSize(new Dimension(130, 20));
		comboBox.setPreferredSize(new Dimension(200, 20));
		
//		getContentPane().setLayout(new BorderLayout());
//		getContentPane().add(jPanel1, BorderLayout.CENTER);

		getContentPane().add(jLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		getContentPane().add(comboBox, ReusedGridBagConstraints.get(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

		getContentPane().add(buttonOk, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		getContentPane().add(buttonCancel, ReusedGridBagConstraints.get(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));

//		jPanel1.add(Box.createVerticalGlue(), new GridBagConstraints(1, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void setSelected(Object or)
	{
		model.setSelectedItem(or);
	}
	
	public void setContents(Collection contents)
	{
		model.removeAllElements();
		this.contents = contents;
		model.addElements(contents);
	}
	
	public Object getSelected()
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
			selected = (Object )comboBox.getSelectedItem();
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

