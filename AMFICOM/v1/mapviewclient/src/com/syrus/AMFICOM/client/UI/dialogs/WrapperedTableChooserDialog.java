/*
 * $Id: WrapperedTableChooserDialog.java,v 1.1 2005/06/08 13:44:06 krupenn Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.util.Wrapper;

/**
 * Класс используется для отображения окна со списком объектов с тем,
 * чтобы пользователь мог выбрать один из них. Статус действия пользователя
 * (выбрал объект или отменил действия) определяется методом getReturnCode(). 
 * Выбранный объект получается методом getReturnObject().
 * В окне выбора объекта можно включить функцию удаления выбранного объекта.
 * Для этого следует переопределить метод remove(ObjectResource obj). Для того,
 * чтобы включить эту возможность, необходимо вызвать метод 
 * setCanDelete(boolean bool)
 *
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
 * @module commonclient_v1
 */
public class WrapperedTableChooserDialog extends JDialog {
	static public final int RET_OK = 1;
	static public final int RET_CANCEL = 2;

	protected JPanel topPanel = new JPanel();
	protected JButton buttonHelp = new JButton();
	protected JButton buttonCancel = new JButton();
	protected JButton buttonOpen = new JButton();
	protected JButton buttonDelete = new JButton();

	protected WrapperedTable table;
	protected WrapperedTableModel model;
	protected Wrapper controller;
	protected JScrollPane scrollPane = new JScrollPane();

	protected Object retObject;
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

	public WrapperedTableChooserDialog(String title, Wrapper controller, String[] keys) {
		super(Environment.getActiveWindow(), title, true);

		this.controller = controller;
		this.model = new WrapperedTableModel(controller, keys);
		this.table = new WrapperedTable(this.model);

		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

		setCanDelete(false);
	}

	public void setContents(List list) {
		this.model.setValues(list);
		this.buttonOpen.setEnabled(false);
		this.buttonDelete.setEnabled(false);
	}

	public void setContents(Collection collection) {
		this.model.setValues(collection);
		this.buttonOpen.setEnabled(false);
		this.buttonDelete.setEnabled(false);
	}

	protected void jbInit() throws Exception {
		this.setResizable(false);
		this.setSize(new Dimension(550, 320));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation(
				(screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		this.getContentPane().setLayout(this.borderLayout2);
		this.topPanel.setLayout(this.borderLayout3);
		this.topPanel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		this.buttonHelp.setText(LangModelGeneral.getString("Help"));
		this.buttonHelp.setEnabled(false);
		this.buttonCancel.setText(LangModelGeneral.getString("Cancel"));
		this.buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonCancel_actionPerformed(e);
			}
		});
		this.buttonOpen.setText(LangModelGeneral.getString("Ok"));
		this.buttonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonOpen_actionPerformed(e);
			}
		});
		this.buttonDelete.setText(LangModelGeneral.getString("Remove"));
		this.buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonDelete_actionPerformed(e);
			}
		});
		this.eastPanel.setLayout(this.flowLayout3);
		this.westPanel.setLayout(this.flowLayout2);
		this.bottomPanel.setLayout(this.borderLayout1);
		this.flowLayout3.setAlignment(2);
		this.eastPanel.add(this.buttonOpen, null);
		this.eastPanel.add(this.buttonCancel, null);
		this.eastPanel.add(this.buttonHelp, null);
		this.westPanel.add(this.buttonDelete, null);
		this.bottomPanel.add(this.westPanel, BorderLayout.WEST);
		this.bottomPanel.add(this.eastPanel, BorderLayout.CENTER);
		this.getContentPane().add(this.bottomPanel, BorderLayout.SOUTH);
		this.getContentPane().add(this.topPanel, BorderLayout.CENTER);

		this.table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		this.scrollPane.getViewport().add(this.table);
		this.scrollPane.setWheelScrollingEnabled(true);
		this.scrollPane.getViewport().setBackground(SystemColor.window);
		this.table.setBackground(SystemColor.window);

		this.topPanel.add(this.scrollPane, BorderLayout.CENTER);

		this.table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						listPane_valueChanged(e);
					}
				});
	}

	public int getReturnCode() {
		return this.retCode;
	}

	public Object getReturnObject() {
		return this.retObject;
	}

	public void setCanDelete(boolean bool) {
		this.canDelete = bool;
		this.buttonDelete.setVisible(this.canDelete);
	}

	public WrapperedTableModel getTableModel() {
		return (WrapperedTableModel )this.table.getModel();
	}

	protected void buttonOpen_actionPerformed(ActionEvent e) {
		this.retObject = getTableModel().getObject(this.table.getSelectedRow());
		if(this.retObject == null)
			return;

		this.retCode = RET_OK;
		this.dispose();
	}

	protected void buttonCancel_actionPerformed(ActionEvent e) {
		this.retCode = RET_CANCEL;
		this.dispose();
	}

	protected boolean delete(Object obj) {
		return false;
	}

	protected void buttonDelete_actionPerformed(ActionEvent e) {
		if(!this.canDelete)
			return;

		Object obj = getTableModel().getObject(this.table.getSelectedRow());
		if(obj == null)
			return;

		if(delete(obj)) {
			getTableModel().getValues().remove(obj);
			getTableModel().fireTableDataChanged();
			this.buttonOpen.setEnabled(false);
			this.buttonDelete.setEnabled(false);
		}
	}

	protected void listPane_valueChanged(ListSelectionEvent e) {
		Object or = getTableModel().getObject(this.table.getSelectedRow());
		if(or != null) {
			this.buttonOpen.setEnabled(true);
			this.buttonDelete.setEnabled(true);
		}
		else {
			this.buttonOpen.setEnabled(false);
			this.buttonDelete.setEnabled(false);
		}
	}

}
