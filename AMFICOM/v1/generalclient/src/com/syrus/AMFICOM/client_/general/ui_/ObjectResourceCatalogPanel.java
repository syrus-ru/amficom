/*
 * $Id: ObjectResourceCatalogPanel.java,v 1.6 2005/02/21 14:23:07 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.general.CharacteristicTypeController;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import oracle.jdeveloper.layout.XYConstraints;
import java.lang.reflect.InvocationTargetException;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/02/21 14:23:07 $
 * @module generalclient_v1
 */
public class ObjectResourceCatalogPanel extends JPanel implements OperationListener
{
	private static final boolean DEBUG = true;

	/**
	 * @deprecated Use {@link #getContext()} instead.
	 */
	private ApplicationContext aContext;
	private Dispatcher dispatcher = new Dispatcher();

	private JButton filterokButton = new JButton();
	private JButton filterclearButton = new JButton();
	private JButton buttonAdd = new JButton();
	private JButton buttonDelete = new JButton();
	private JButton buttonProperties = new JButton();
	private JButton buttonSave = new JButton();
	private JPanel filterContainingPanel = new JPanel();
	private JPanel filterButtonPanel = new JPanel();
	private JPanel buttonsPanel = new JPanel();
	private JPanel jPanel = new JPanel();
	private JScrollPane propScrollPane;
	private JTabbedPane jTabbedPane = new JTabbedPane();
	private ObjectResourceTable table;
	private ObjectResourcePropertiesPane propPane = new GeneralPanel();
	private ObjectResourceFilterPane filterPane = new ObjectResourceFilterPane();
	private TextIcon enabledPropsIcon = new TextIcon(LangModel.getString("Properties"), jTabbedPane, true);
	private TextIcon disabledPropsIcon = new TextIcon(LangModel.getString("Properties"), jTabbedPane, false);

	ObjectResourceController controller;

	/**
	 * This button has no functionality.
	 */
	private JButton filtercancelButton = new JButton()
	{
		{
			super.setEnabled(false);
		}

		public Color getBackground()
		{
			return Color.RED;
		}

		public void setEnabled(boolean enabled)
		{
		}
	};

	/**
	 * This button has no functionality.
	 */
	private JButton buttonCancel = new JButton()
	{
		{
			super.setEnabled(false);
		}

		public Color getBackground()
		{
			return Color.RED;
		}

		public void setEnabled(boolean enabled)
		{
		}
	};

	private List dataSet;

	private boolean sendEvent = false;

	public ObjectResourceCatalogPanel()
	{
		jbInit();
		Environment.getDispatcher().register(this, ContextChangeEvent.type);
	}

	protected void setContents(List dataSet)
	{
		if (dataSet == null)
			dataSet = new LinkedList();

		this.dataSet = dataSet;
		List ds = dataSet;
//		if (this.filterPane.getFilter() != null)
//			ds = this.filterPane.getFilter().filter(ds);
		ObjectResourceTableModel model = (ObjectResourceTableModel)table.getModel();
		model.setContents(ds);
		model.fireTableDataChanged();
	}

	private void setController(ObjectResourceController controller)
	{
//		model.controller = controller;
//		model.fireTableDataChanged();
		this.table.setModel(new ObjectResourceTableModel(controller));
	}

	public void setButtonPanelVisible(boolean bool)
	{
		buttonsPanel.setVisible(bool);
	}

	public void setActionModel(ObjectResourceCatalogActionModel orcam)
	{
		if (orcam == null)
			buttonsPanel.setVisible(false);

		buttonsPanel.setVisible(orcam.PANEL);
		buttonsPanel.removeAll();
		buttonAdd.setVisible(orcam.ADD_BUTTON);
		buttonCancel.setVisible(orcam.CANCEL_BUTTON);
		buttonDelete.setVisible(orcam.REMOVE_BUTTON);
		buttonProperties.setVisible(orcam.PROPS_BUTTON);
		buttonSave.setVisible(orcam.SAVE_BUTTON);

		if (orcam.ADD_BUTTON)
			buttonsPanel.add(buttonAdd);
		if (orcam.SAVE_BUTTON)
			buttonsPanel.add(buttonSave);
		if (orcam.REMOVE_BUTTON)
			buttonsPanel.add(buttonDelete);
		if (orcam.PROPS_BUTTON)
			buttonsPanel.add(buttonProperties);
		if (orcam.CANCEL_BUTTON)
			buttonsPanel.add(buttonCancel);
	}

	public void setObjectResourceController(ObjectResourceController controller)
	{
		if (controller == null)
			return;
		this.controller = controller;
		ObjectResourcePropertiesPane pane = getPropertiesPane(controller);
		setProp(pane);
		setController(controller);
	}

	private ObjectResourcePropertiesPane getPropertiesPane(ObjectResourceController controller)
	{
		ObjectResourcePropertiesPane pane;
		try
		{
			final String methodName1 = "getPropertyPaneClassName";
			try
			{
				Class clazz = Class.forName((String) (controller.getClass().getMethod(methodName1, new Class[0]).invoke(controller.getClass(), new Object[0])));
				final String methodName2 = "getInstance";
				try
				{
					pane = (ObjectResourcePropertiesPane) (clazz.getMethod(methodName2, new Class[0]).invoke(clazz, new Object[0]));
//					pane.setContext(aContext);
				}
				catch (NoSuchMethodException nsme)
				{
					if (true)
						System.err.println("WARNING: " + clazz.getName() + '.' + methodName2 + "() not found.");
					pane = new GeneralPanel();
				}
			}
			catch (NoSuchMethodException nsme)
			{
				if (true)
					System.err.println("WARNING: " + controller.getClass().getName() + '.' + methodName1 + "() not found.");
				pane = new GeneralPanel();
			}
		}
		catch (InvocationTargetException ite)
		{
			ite.printStackTrace();
			ite.getTargetException().printStackTrace();
			pane = new GeneralPanel();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			pane = new GeneralPanel();
		}
		return pane;
	}

	private ObjectResourceFilter getFilter(Class orclass)
	{
		ObjectResourceFilter fil = null;
		try {
			java.lang.reflect.Method filMethod = orclass.getMethod("getFilter", new Class[0]);
			fil = (ObjectResourceFilter)filMethod.invoke(orclass, new Object[0]);
		}
		catch (IllegalAccessException iae) {
			System.out.println("getFilter method not found in class " + orclass.getName());
			fil = null;
		}
		catch (Exception e) {
			System.out.println("����� getFilter �� ��������� ��� " + orclass.getName());
			fil = null;
		}
		return fil;
	}



	private void jbInit()
	{
		ObjectResourceTableModel model = new ObjectResourceTableModel(
			 CharacteristicTypeController.getInstance());
		table = new ObjectResourceTable(model);

		buttonAdd.setText("�����");
		buttonAdd.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonAdd.setBorderPainted(false);
		buttonAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!propPane.create())
					return;
				Object res = propPane.getObject();
				dataSet.add(res);
				((ObjectResourceTableModel)table.getModel()).fireTableDataChanged();
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
				dispatcher.notify(new TreeListSelectionEvent(res, TreeListSelectionEvent.SELECT_EVENT));
				sendEvent = false;
				int selected = ((ObjectResourceTableModel)table.getModel()).getIndexOfObject(res);
				table.getSelectionModel().setSelectionInterval(selected, selected);
				jTabbedPane.setSelectedComponent(propScrollPane);
				buttonCancel.setEnabled(true);
				buttonSave.setEnabled(true);
			}
		});
		buttonCancel.setText("��������");
		buttonCancel.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonCancel.setBorderPainted(false);
		buttonDelete.setText("�������");
		buttonDelete.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonDelete.setBorderPainted(false);
		buttonDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;

				ObjectResourceTableModel model = (ObjectResourceTableModel)table.getModel();
				Object or = model.getObject(sel);
				if (!propPane.delete())
					return;
				dataSet.remove(or);
				model.fireTableDataChanged();
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent(or, TreeListSelectionEvent.REFRESH_EVENT));
				sendEvent = false;
				buttonCancel.setEnabled(true);
				buttonSave.setEnabled(true);
				jTabbedPane.setSelectedComponent(table);
				if (dataSet.size() == 0)
				{
					enablePropsPane(false);
				}
				else
				{
					Object obj = dataSet.iterator().next();
					int selected = model.getIndexOfObject(obj);
					table.getSelectionModel().setSelectionInterval(selected, selected);
					sendEvent = true;
					dispatcher.notify(new TreeListSelectionEvent(obj, TreeListSelectionEvent.SELECT_EVENT));
					sendEvent = false;
				}
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent(or, TreeListSelectionEvent.REFRESH_EVENT));
				sendEvent = false;
			}
		});
		buttonProperties.setText("��������");
		buttonProperties.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonProperties.setBorderPainted(false);
		buttonProperties.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				ObjectResourceTableModel model = (ObjectResourceTableModel)table.getModel();
				Object or = model.getObject(sel);

				try {
					propPane.setObject(or);
					jTabbedPane.setSelectedComponent(propScrollPane);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		buttonSave.setText("���������");
		buttonSave.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonSave.setBorderPainted(false);
		buttonSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!propPane.save())
					return;
				ObjectResourceTableModel model = (ObjectResourceTableModel)table.getModel();
				model.fireTableDataChanged();
				Object or = propPane.getObject();
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
				dispatcher.notify(new TreeListSelectionEvent(or, TreeListSelectionEvent.SELECT_EVENT));
				sendEvent = false;
			}
		});
		jTabbedPane.setTabPlacement(JTabbedPane.LEFT);

		filterokButton.setText("���������");
		filterokButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ObjectResourceCatalogPanel.this.setContents(dataSet);
			}
		});
		filtercancelButton.setText("��������");
		filterclearButton.setText("��������");
		filterclearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				/**
				 * @todo In the future, clear filter criteria in
				 *       in some more elegant way.
				 */
				ObjectResourceFilter objectResourceFilter = ObjectResourceCatalogPanel.this.filterPane.getFilter();
				objectResourceFilter.clearCriteria();
				ObjectResourceCatalogPanel.this.filterPane.setFilter(objectResourceFilter);
				ObjectResourceCatalogPanel.this.setContents(dataSet);
			}
		});
		filterContainingPanel.add(filterPane, BorderLayout.CENTER);
		filterButtonPanel.add(filterokButton, null);
		filterButtonPanel.add(filtercancelButton, null);
		filterButtonPanel.add(filterclearButton, null);
		filterContainingPanel.add(filterButtonPanel, BorderLayout.SOUTH);

		propScrollPane = new JScrollPane((JComponent)propPane);

		jTabbedPane.addTab("", new TextIcon("������", jTabbedPane), table);
		jTabbedPane.addTab("", new TextIcon("��������", jTabbedPane), propScrollPane);
		jTabbedPane.addTab("", new TextIcon("������", jTabbedPane), filterContainingPanel);

		table.setBackground(SystemColor.window);
		jTabbedPane.setBackground(SystemColor.control);

		jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
		jTabbedPane.setEnabledAt(1, false);
		jTabbedPane.setDisabledIconAt(2, new TextIcon(filterPane.getName(), jTabbedPane, false));

		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		fl.setHgap(1);
		fl.setVgap(0);
		buttonsPanel.setLayout(fl);
		buttonsPanel.setPreferredSize(new Dimension(575, 24));
		buttonsPanel.add(buttonAdd, new XYConstraints(10, 0, -1, 20));
		buttonsPanel.add(buttonSave, new XYConstraints(60, 0, -1, 20));
		buttonsPanel.add(buttonDelete, new XYConstraints(125, 0, -1, 20));
		buttonsPanel.add(buttonProperties, new XYConstraints(180, 0, -1, 20));
		buttonsPanel.add(buttonCancel, new XYConstraints(240, 0, -1, 20));

		buttonCancel.setEnabled(false);
		buttonSave.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonProperties.setEnabled(false);

		jPanel.setLayout(new BorderLayout());
		jPanel.add(buttonsPanel, BorderLayout.NORTH);
		jPanel.add(jTabbedPane, BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(jPanel, BorderLayout.CENTER);

		table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting())
					return;

				updateSelection();
			}
		});
	}

	private void updateSelection()
	{
		int selected = table.getSelectedRow();
		if (selected != -1) {
			ObjectResourceTableModel model = (ObjectResourceTableModel)table.getModel();
			Object obj = model.getObject(selected);
			propPane.setObject(obj);
			enablePropsPane(true);
			sendEvent = true;
			dispatcher.notify(new TreeListSelectionEvent(obj, TreeListSelectionEvent.SELECT_EVENT));
			sendEvent = false;
		}
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if (aContext != null)
		{
			this.dispatcher = aContext.getDispatcher();
			dispatcher.register(this, TreeDataSelectionEvent.type);
		}
		propPane.setContext(aContext);
		filterPane.setContext(aContext);
	}

	public void setFilter(ObjectResourceFilter filter)
	{
		filterPane.setFilter(filter);
		if (filter == null)
			jTabbedPane.setEnabledAt(2, false);
		else
			jTabbedPane.setEnabledAt(2, true);
	}

	private void setProp(ObjectResourcePropertiesPane propPane)
	{
		propScrollPane.getViewport().remove((JComponent)this.propPane);
		this.propPane = propPane;
		propScrollPane.getViewport().add((JComponent)propPane);
		propPane.setContext(aContext);
	}

//	public void loadListValues(ObjectResourceController controller, List dataSet)
//	{
//		setContents(dataSet);
//		setController(controller);
//	}

	public void setListState()
	{
		jTabbedPane.setSelectedComponent(table);
		table.getSelectionModel().clearSelection();
		enablePropsPane(false);
	}

	private void enablePropsPane(boolean b)
	{
		if (b)
			jTabbedPane.setIconAt(1, enabledPropsIcon);
		else
			jTabbedPane.setDisabledIconAt(1, disabledPropsIcon);
		jTabbedPane.setEnabledAt(1, b);
		jTabbedPane.setSelectedComponent(table);
		buttonProperties.setEnabled(b);
		buttonCancel.setEnabled(b);
		buttonSave.setEnabled(b);
		buttonDelete.setEnabled(b);
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (sendEvent)
				return;
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent) oe;

			List data = tdse.getList();
			int n = tdse.getSelectionNumber();
			Class cl = tdse.getDataClass();
			ObjectResourceController controller = (ObjectResourceController)tdse.orcc;
			ObjectResourceCatalogActionModel orcam = (ObjectResourceCatalogActionModel )tdse.getParam();

			table.getSelectionModel().clearSelection();
			if (this.controller == null || !this.controller.equals(controller))
				setObjectResourceController(controller);
			if (this.dataSet == null || !this.dataSet.equals(data))
				setContents(data);
			setActionModel(orcam);

			if (n != -1)
			{
				ObjectResourceTableModel model = (ObjectResourceTableModel)table.getModel();
				Object res = data.get(n);
				int selected = model.getIndexOfObject(res);
				if (selected != -1)
				{
					table.getSelectionModel().setSelectionInterval(selected, selected);
					updateSelection();
				}
			}
			else
			{
				enablePropsPane(false);
				jTabbedPane.setSelectedComponent(table);
			}
		}
		else if (oe.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent )oe;
			if (cce.DOMAIN_SELECTED)
				setContents(new LinkedList());
		}
	}
}
