/**
 * $Id: ObjectResourceChooserDialog.java,v 1.2 2004/08/05 12:07:03 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTablePane;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.jdeveloper.layout.XYLayout;

/**
 * Класс $RCSfile: ObjectResourceChooserDialog.java,v $ используется для отображения окна со списком объектов с тем,
 * чтобы пользователь мог выбрать один из них. Статус действия пользователя
 * (выбрал объект или отменил действия) определяется методом getReturnCode(). 
 * Выбранный объект получается методом getReturnObject().
 * В окне выбора объекта можно включить функцию удаления выбранного объекта.
 * Для этого следует переопределить метод remove(ObjectResource obj). Для того,
 * чтобы включить эту возможность, необходимо вызвать метод 
 * setCanDelete(boolean bool)
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/08/05 12:07:03 $
 * @module
 * @author $Author: bass $
 * @see
 */
public class ObjectResourceChooserDialog extends JDialog 
{
	static public final int RET_OK = 1;
	static public final int RET_CANCEL = 2;

	protected JPanel topPanel = new JPanel();
	protected XYLayout xYLayout1 = new XYLayout();
	protected JButton buttonHelp = new JButton();
	protected JButton buttonCancel = new JButton();
	protected JButton buttonOpen = new JButton();
	protected JButton buttonDelete = new JButton();

	protected ObjectResourceTablePane listPane = new ObjectResourceTablePane();

	protected ObjectResource retObject;
	protected int retCode = 2;
	
	protected JPanel eastPanel = new JPanel();
	protected JPanel westPanel = new JPanel();
	protected JPanel bottomPanel = new JPanel();
	protected BorderLayout borderLayout1 = new BorderLayout();
	protected BorderLayout borderLayout2 = new BorderLayout();
	protected FlowLayout flowLayout2 = new FlowLayout();
	protected FlowLayout flowLayout3 = new FlowLayout();
	protected BorderLayout borderLayout3 = new BorderLayout();

	protected DataSourceInterface dataSource = null;
	
	protected boolean canDelete = false;

	public ObjectResourceChooserDialog(DataSourceInterface dataSource, String typ)
	{
		this(Environment.getActiveWindow(), LangModel.getString("node" + typ), false);
		this.dataSource = dataSource;
	}

	protected ObjectResourceChooserDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setContents(ObjectResourceDisplayModel odm, DataSet dataSet)
	{
		listPane.initialize(odm, dataSet);
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

		topPanel.add(listPane, BorderLayout.CENTER);

		buttonDelete.setVisible(false);

		listPane.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener()
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
	
	public ObjectResource getReturnObject()
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
		return (ObjectResourceTableModel )listPane.getTable().getModel();
	}

	protected void buttonOpen_actionPerformed(ActionEvent e)
	{
		retObject = (ObjectResource )listPane.getSelectedObject();
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

	protected boolean delete(ObjectResource obj)
	{
/*
		if(obj instanceof MapContext)
		{
			dataSource.RemoveMap(obj.getId());
			Pool.remove(MapContext.typ, obj.getId());
		}
		else
		if(obj instanceof Scheme)
		{
			dataSource.RemoveScheme(obj.getId());
			Pool.remove(Scheme.typ, obj.getId());
		}
*/
		return false;
	}

	protected void buttonDelete_actionPerformed(ActionEvent e)
	{
		if(!canDelete)
			return;

		ObjectResource obj = (ObjectResource )listPane.getSelectedObject();
		if(obj == null)
			return;

		if(delete(obj))
		{
			listPane.getContents().remove(obj);
			listPane.updateUI();
			buttonOpen.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
	}

	protected void listPane_valueChanged(ListSelectionEvent e)
	{
//		if (e.getValueIsAdjusting())
//			return;

		ObjectResource or = (ObjectResource )listPane.getSelectedObject();
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