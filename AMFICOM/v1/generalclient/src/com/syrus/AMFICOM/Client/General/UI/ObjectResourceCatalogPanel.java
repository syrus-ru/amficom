/*
 * $Id: ObjectResourceCatalogPanel.java,v 1.7 2004/09/25 19:28:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.*;
import oracle.jdeveloper.layout.XYConstraints;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/25 19:28:29 $
 * @module generalclient_v1
 */
public class ObjectResourceCatalogPanel extends JPanel implements OperationListener
{
	private static final boolean DEBUG = true;

	/**
	 * @deprecated Use {@link #getContext()} instead.
	 */
	public ApplicationContext aContext;

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
	private ObjectResourceTablePane listPane = new ObjectResourceTablePane();
	private ObjectResourcePropertiesPane propPane = new GeneralPanel();
	private ObjectResourceFilterPane filterPane = new ObjectResourceFilterPane();

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

	private ObjectResourceDisplayModel dmod;
	private List dataSet = new LinkedList();
	private Class orclass;

	private boolean sendEvent = false;

	public ObjectResourceCatalogPanel(ApplicationContext aContext)
	{
		jbInit();
		Environment.getDispatcher().register(this, ContextChangeEvent.type);
		setContext(aContext);
	}

	public ObjectResourceCatalogPanel(ApplicationContext aContext, ObjectResourceDisplayModel dmod, Class orclass)
	{
		this(aContext);
		setDisplayModel(dmod);
		setObjectResourceClass(orclass);
	}

	public ObjectResourceCatalogPanel(ApplicationContext aContext, ObjectResourceDisplayModel dmod, Class orclass, List dataSet)
	{
		this(aContext, dmod, orclass);
		setContents(dataSet);
	}

	public void setContents(List dataSet)
	{
		if (dmod == null)
			dmod = new StubDisplayModel();
		if (dataSet == null)
			dataSet = new LinkedList();

		this.dataSet = dataSet;
		List ds = dataSet;
		if (this.filterPane.getFilter() != null)
			ds = this.filterPane.getFilter().filter(ds);
		listPane.initialize(dmod, ds);
	}

	public void setDisplayModel(ObjectResourceDisplayModel dmod)
	{
		if (dmod == null)
			dmod = new StubDisplayModel();
		if (dataSet == null)
			dataSet = new LinkedList();

		this.dmod = dmod;
		listPane.initialize(dmod, dataSet);
	}

	public void setButtonPanelVisible(boolean bool)
	{
		buttonsPanel.setVisible(bool);
	}

	public void setActionModel(ObjectResourceCatalogActionModel orcam)
	{
		if (orcam == null)
			buttonsPanel.setVisible(false);
		
		buttonsPanel.setVisible(orcam.panel);
		buttonsPanel.removeAll();
		buttonAdd.setVisible(orcam.add_button);
		buttonCancel.setVisible(orcam.cancel_button);
		buttonDelete.setVisible(orcam.remove_button);
		buttonProperties.setVisible(orcam.props_button);
		buttonSave.setVisible(orcam.save_button);

		if (orcam.add_button)
			buttonsPanel.add(buttonAdd);
		if (orcam.save_button)
			buttonsPanel.add(buttonSave);
		if (orcam.remove_button)
			buttonsPanel.add(buttonDelete);
		if (orcam.props_button)
			buttonsPanel.add(buttonProperties);
		if (orcam.cancel_button)
			buttonsPanel.add(buttonCancel);
	}
	
	public void setObjectResourceClass(Class orclass)
	{
		ObjectResourcePropertiesPane pane = new GeneralPanel();
		ObjectResourceDisplayModel dmod = new StubDisplayModel(new String [] {"name"}, new String [] {"Название"} );
		ObjectResourceFilter fil = null;
		ObjectResourceSorter sor = null;

		this.orclass = orclass;
		if (orclass != null && ObjectResource.class.isAssignableFrom(orclass))
		{
			try
			{
				final String methodName1 = "getPropertyPaneClassName";
				try
				{
					Class clazz = Class.forName((String) (orclass.getMethod(methodName1, new Class[0]).invoke(orclass, new Object[0])));
					final String methodName2 = "getInstance";
					try
					{
						pane = (ObjectResourcePropertiesPane) (clazz.getMethod(methodName2, new Class[0]).invoke(clazz, new Object[0]));
					}
					catch (NoSuchMethodException nsme)
					{
						if (DEBUG)
							System.err.println("WARNING: " + clazz.getName() + '.' + methodName2 + "() not found.");
					}
				}
				catch (NoSuchMethodException nsme)
				{
					if (DEBUG)
						System.err.println("WARNING: " + orclass.getName() + '.' + methodName1 + "() not found.");
				}
			}
			catch (InvocationTargetException ite)
			{
				ite.printStackTrace();
				ite.getTargetException().printStackTrace();
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}

			try
			{
				java.lang.reflect.Method dmodMethod = orclass.getMethod("getDefaultDisplayModel", new Class [0]);
				dmod = (ObjectResourceDisplayModel )dmodMethod.invoke(orclass, new Object [0]);
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("getDefaultDisplayModel method not found in class " + orclass.getName());
				dmod = new StubDisplayModel(new String [] {"name"}, new String [] {"Название"} );
			}
			catch(Exception e)
			{
				System.out.println("Метод getDefaultDisplayModel не определен для " + orclass.getName());
				dmod = new StubDisplayModel(new String [] {"name"}, new String [] {"Название"} );
			}

			try
			{
				java.lang.reflect.Method filMethod = orclass.getMethod("getFilter", new Class [0]);
				fil = (ObjectResourceFilter)filMethod.invoke(orclass, new Object[0]);
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("getFilter method not found in class " + orclass.getName());
				fil = null;
			}
			catch(Exception e)
			{
				System.out.println("Метод getFilter не определен для " + orclass.getName());
				fil = null;
			}

			try
			{
				java.lang.reflect.Method sorMethod = orclass.getMethod("getSorter", new Class [0]);
				sor = (ObjectResourceSorter )sorMethod.invoke(orclass, new Object [0]);
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("getSorter method not found in class " + orclass.getName());
				sor = null;
			}
			catch(Exception e)
			{
				System.out.println("Метод getSorter не определен для " + orclass.getName());
				sor = null;
			}
		}

		setProp(pane);
		setDisplayModel(dmod);
		setFilter(fil);
		setSorter(sor);
	}

	private void jbInit()
	{
		buttonAdd.setText("Новый");
		buttonAdd.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonAdd.setBorderPainted(false);
		buttonAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!propPane.create())
					return;
				ObjectResource res = propPane.getObjectResource();
				dataSet.add(res);
				listPane.tableModel.fireTableDataChanged();
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent(res.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
				dispatcher.notify(new TreeListSelectionEvent(res, TreeListSelectionEvent.SELECT_EVENT));
				sendEvent = false;
				listPane.setSelected(res);
				jTabbedPane.setSelectedComponent(propScrollPane);
				buttonCancel.setEnabled(true);
				buttonSave.setEnabled(true);
			}
		});
		buttonCancel.setText("Отменить");
		buttonCancel.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonCancel.setBorderPainted(false);
		buttonDelete.setText("Удалить");
		buttonDelete.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonDelete.setBorderPainted(false);
		buttonDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ObjectResource or = (ObjectResource)listPane.getSelectedObject();
				if (!propPane.delete())
					return;
				dataSet.remove(or);
				listPane.tableModel.fireTableDataChanged();
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent(or.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
				sendEvent = false;
				buttonCancel.setEnabled(true);
				buttonSave.setEnabled(true);
				jTabbedPane.setSelectedComponent(listPane);
				if (dataSet.size() == 0)
				{
					jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
					jTabbedPane.setEnabledAt(1, false);
					buttonProperties.setEnabled(false);
					buttonCancel.setEnabled(false);
					buttonSave.setEnabled(false);
					buttonDelete.setEnabled(false);
				}
				else
				{
					ObjectResource obj = (ObjectResource )dataSet.iterator().next();
					listPane.setSelected(obj);
					sendEvent = true;
					dispatcher.notify(new TreeListSelectionEvent(obj, TreeListSelectionEvent.SELECT_EVENT));
					sendEvent = false;
				}
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent(or.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
				sendEvent = false;
			}
		});
		buttonProperties.setText("Свойства");
		buttonProperties.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonProperties.setBorderPainted(false);
		buttonProperties.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ObjectResource or  = (ObjectResource)listPane.getSelectedObject();
				if (or != null)
				{
					try
					{
						propPane.setObjectResource(or);
						jTabbedPane.setSelectedComponent(propScrollPane);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					} 
				}
			}
		});

		buttonSave.setText("Сохранить");
		buttonSave.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonSave.setBorderPainted(false);
		buttonSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!propPane.save())
					return;
				listPane.updateUI();
				ObjectResource or = propPane.getObjectResource();
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
				dispatcher.notify(new TreeListSelectionEvent(or, TreeListSelectionEvent.SELECT_EVENT));
				sendEvent = false;
			}
		});
		jTabbedPane.setTabPlacement(JTabbedPane.LEFT);


		filterokButton.setText("Применить");
		filterokButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ObjectResourceCatalogPanel.this.setContents(dataSet);
			}
		});
		filtercancelButton.setText("Отменить");
		filterclearButton.setText("Очистить");
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

		jTabbedPane.addTab("", new TextIcon(listPane.getName(), jTabbedPane), listPane);
		jTabbedPane.addTab("", new TextIcon(((JComponent)propPane).getName(), jTabbedPane), propScrollPane);
		jTabbedPane.addTab("", new TextIcon(filterPane.getName(), jTabbedPane), filterContainingPanel);

		listPane.getViewport().setBackground(SystemColor.window);
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

		listPane.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting())
					return;
				Object obj = (Object) listPane.getSelectedObject();
				if (obj == null)
					return;
				if (obj instanceof ObjectResource)
					propPane.setObjectResource((ObjectResource) obj);
				jTabbedPane.setIconAt(1, new TextIcon(LangModel.getString("Properties"), jTabbedPane, true));
				jTabbedPane.setEnabledAt(1, true);
				buttonProperties.setEnabled(true);
				buttonCancel.setEnabled(true);
				buttonSave.setEnabled(true);
				buttonDelete.setEnabled(true);
				sendEvent = true;
				dispatcher.notify(new TreeListSelectionEvent(obj, TreeListSelectionEvent.SELECT_EVENT));
				sendEvent = false;
			}
		});
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

	public void setSorter(ObjectResourceSorter sorter)
	{
		listPane.setSorter(sorter);
	}

	public ObjectResourceSorter getSorter()
	{
		return listPane.getSorter();
	}

	public void setProp(ObjectResourcePropertiesPane propPane)
	{
		propScrollPane.getViewport().remove((JComponent)this.propPane);
		this.propPane = propPane;
		propScrollPane.getViewport().add((JComponent)propPane);
		propPane.setContext(aContext);
	}

	public void loadListValues(ObjectResourceDisplayModel dmod, List dataSet)
	{
		if (dmod == null)
			dmod = new StubDisplayModel();
		if (dataSet == null)
			dataSet = new LinkedList();

		this.dmod = dmod;
		this.dataSet = dataSet;
		listPane.initialize(dmod, dataSet);
	}

	public void setListState()
	{
		jTabbedPane.setSelectedComponent(listPane);
		listPane.setSelected(null);

		jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
		jTabbedPane.setEnabledAt(1, false);
		buttonCancel.setEnabled(false);
		buttonProperties.setEnabled(false);
		buttonSave.setEnabled(false);
		buttonDelete.setEnabled(false);
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
			ObjectResourceCatalogActionModel orcam = (ObjectResourceCatalogActionModel )tdse.getParam();

			if (n != -1)
			{
				ObjectResource res = (ObjectResource )data.get(n);

				if (dataSet.indexOf(res) == -1)
				{
					setContents(data);
					setObjectResourceClass(cl);
					setActionModel(orcam);
				}
				listPane.setSelected(res);
			}
			else
			{
				setContents(data);
				setObjectResourceClass(cl);
				setActionModel(orcam);
				jTabbedPane.setDisabledIconAt(1, new TextIcon(LangModel.getString("Properties"), jTabbedPane, false));
				jTabbedPane.setEnabledAt(1, false);
				jTabbedPane.setSelectedComponent(listPane);
				buttonProperties.setEnabled(false);
				buttonCancel.setEnabled(false);
				buttonSave.setEnabled(false);
				buttonDelete.setEnabled(false);
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
