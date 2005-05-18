/*
 * $Id: ObjectResourceChooserDialog.java,v 1.11 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.StorableObject;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;

/**
 * ����� $RCSfile: ObjectResourceChooserDialog.java,v $ ������������ ��� ����������� ���� �� ������� �������� � ���,
 * ����� ������������ ��� ������� ���� �� ���. ������ �������� ������������
 * (������ ������ ��� ������� ��������) ������������ ������� getReturnCode().
 * ��������� ������ ���������� ������� getReturnObject().
 * � ���� ������ ������� ����� �������� ������� �������� ���������� �������.
 * ��� ����� ������� �������������� ����� remove(StorableObject obj). ��� ����,
 * ����� �������� ��� �����������, ���������� ������� �����
 * setCanDelete(boolean bool)
 *
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/05/18 14:01:19 $
 * @module generalclient_v1
 */
public class ObjectResourceChooserDialog extends JDialog
{
	static public final int RET_OK = 1;
	static public final int RET_CANCEL = 2;

	protected JPanel topPanel = new JPanel();
	protected JButton buttonHelp = new JButton();
	protected JButton buttonCancel = new JButton();
	protected JButton buttonOpen = new JButton();
	protected JButton buttonDelete = new JButton();

	protected ObjectResourceTable table;
	protected ObjectResourceTableModel model;
	protected ObjectResourceController controller;
	protected JScrollPane scrollPane = new JScrollPane();

	protected StorableObject retObject;
	protected int retCode = 2;
	
	protected JPanel eastPanel = new JPanel();
	protected JPanel westPanel = new JPanel();
	protected JPanel bottomPanel = new JPanel();
	protected BorderLayout borderLayout1 = new BorderLayout();
	protected BorderLayout borderLayout2 = new BorderLayout();
	protected FlowLayout flowLayout2 = new FlowLayout();
	protected FlowLayout flowLayout3 = new FlowLayout();
	protected BorderLayout borderLayout3 = new BorderLayout();

	protected boolean canDelete = false;

	public ObjectResourceChooserDialog(ObjectResourceController controller, String typ)
	{
		super(Environment.getActiveWindow(), LangModel.getString("node" + typ), true);

		this.controller = controller;
		model = new ObjectResourceTableModel(controller);
		table = new ObjectResourceTable(model);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		setCanDelete(false);
	}

	public void setContents(List list)
	{
		model.setContents(list);
		buttonOpen.setEnabled(false);
		buttonDelete.setEnabled(false);
	}
	
	protected void jbInit() throws Exception
	{
		this.setResizable(false);
		this.setSize(new Dimension(550, 320));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation(
				(screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		this.getContentPane().setLayout(borderLayout2);
		topPanel.setLayout(borderLayout3);
		topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		buttonHelp.setText(LangModel.getString("Help"));
		buttonHelp.setEnabled(false);
		buttonCancel.setText(LangModel.getString("Cancel"));
		buttonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonCancel_actionPerformed(e);
				}
			});
		buttonOpen.setText(LangModel.getString("Ok"));
		buttonOpen.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonOpen_actionPerformed(e);
				}
			});
		buttonDelete.setText(LangModel.getString("Remove"));
		buttonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonDelete_actionPerformed(e);
				}
			});
		eastPanel.setLayout(flowLayout3);
		westPanel.setLayout(flowLayout2);
		bottomPanel.setLayout(borderLayout1);
		flowLayout3.setAlignment(2);
		eastPanel.add(buttonOpen, null);
		eastPanel.add(buttonCancel, null);
		eastPanel.add(buttonHelp, null);
		westPanel.add(buttonDelete, null);
		bottomPanel.add(westPanel, BorderLayout.WEST);
		bottomPanel.add(eastPanel, BorderLayout.CENTER);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.getContentPane().add(topPanel, BorderLayout.CENTER);

		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.getViewport().add(table);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);

		topPanel.add(scrollPane, BorderLayout.CENTER);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					listPane_valueChanged(e);
				}
			});
	}

	public int getReturnCode()
	{
		return retCode;
	}
	
	public StorableObject getReturnObject()
	{
		return retObject;
	}
	
	public void setCanDelete(boolean bool)
	{
		canDelete = bool;
		buttonDelete.setVisible(canDelete);
	}
	
	public ObjectResourceTableModel getTableModel()
	{
		return (ObjectResourceTableModel )table.getModel();
	}

	protected void buttonOpen_actionPerformed(ActionEvent e)
	{
		retObject = (StorableObject )getTableModel().getObject(table.getSelectedRow());
		if(retObject == null)
			return;

		retCode = RET_OK;
		this.dispose();
	}

	protected void buttonCancel_actionPerformed(ActionEvent e)
	{
		retCode = RET_CANCEL;
		this.dispose();
	}

	protected boolean delete(StorableObject obj)
	{
/*
		if(obj instanceof MapContext)
		{
			dataSource.RemoveMap(obj.getId());
			Pool.remove(MapContext.class.getName(), obj.getId());
		}
		else
		if(obj instanceof Scheme)
		{
			dataSource.RemoveScheme(obj.getId());
			Pool.remove(Scheme.class.getName(), obj.getId());
		}
*/
		return false;
	}

	protected void buttonDelete_actionPerformed(ActionEvent e)
	{
		if(!canDelete)
			return;

		StorableObject obj = (StorableObject )getTableModel().getObject(table.getSelectedRow());
		if(obj == null)
			return;

		if(delete(obj))
		{
			getTableModel().getContents().remove(obj);
			getTableModel().fireTableDataChanged();
			buttonOpen.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
	}

	protected void listPane_valueChanged(ListSelectionEvent e)
	{
//		if (e.getValueIsAdjusting())
//			return;

		StorableObject or = (StorableObject )getTableModel().getObject(table.getSelectedRow());
		if (or != null)
		{
			buttonOpen.setEnabled(true);
			buttonDelete.setEnabled(true);
		}
		else
		{
			buttonOpen.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
	}

}
