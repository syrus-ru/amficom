//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\Application\ElementCatalogDialog.java        * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI;

import java.util.LinkedList;
import java.util.List;
import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.lang.reflect.InvocationTargetException;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane;

public class ObjectResourceCatalogPanel extends JPanel
		implements OperationListener
{
	public ApplicationContext aContext;
	Dispatcher dispatcher = new Dispatcher();

	ObjectResourceTablePane listPane = new ObjectResourceTablePane();
	PropertiesPanel propPane = new GeneralPanel();//new GeneralTabbedPane();
	ObjectResourceFilterPane filterPane = new ObjectResourceFilterPane();
	JPanel filterContainingPanel = new JPanel();

	private JPanel filterButtonPanel = new JPanel();
	
	private JButton filterokButton = new JButton();
	private JButton filtercancelButton = new JButton();
	private JButton filterclearButton = new JButton();

	ObjectResourceDisplayModel dmod;
	List dataSet = new LinkedList();
	Class orclass;

	JPanel jPanel = new JPanel();
	JTabbedPane jTabbedPane = new JTabbedPane();
	JScrollPane propScrollPane;
	JPanel buttonsPanel = new JPanel();
	JButton buttonAdd = new JButton();
	JButton buttonCancel = new JButton();
	JButton buttonDelete = new JButton();
	JButton buttonProperties = new JButton();
	JButton buttonSave = new JButton();

	boolean send_event = false;

	public ObjectResourceCatalogPanel(ApplicationContext aContext)
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Environment.the_dispatcher.register(this, ContextChangeEvent.type);
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
		if(dmod == null)
			dmod = new StubDisplayModel();
		if(dataSet == null)
			dataSet = new LinkedList();

		this.dataSet = dataSet;
		List ds = dataSet;
		if(this.filterPane.getFilter() != null)
			ds = this.filterPane.getFilter().filter(ds);
		listPane.initialize(dmod, ds);
	}

	public void setDisplayModel(ObjectResourceDisplayModel dmod)
	{
		if(dmod == null)
			dmod = new StubDisplayModel();
		if(dataSet == null)
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
		if(orcam == null)
			buttonsPanel.setVisible(false);
		
		buttonsPanel.setVisible(orcam.panel);
		buttonsPanel.removeAll();
		buttonAdd.setVisible(orcam.add_button);
		buttonCancel.setVisible(orcam.cancel_button);
		buttonDelete.setVisible(orcam.remove_button);
		buttonProperties.setVisible(orcam.props_button);
		buttonSave.setVisible(orcam.save_button);

		if(orcam.add_button)
			buttonsPanel.add(buttonAdd);
		if(orcam.save_button)
			buttonsPanel.add(buttonSave);
		if(orcam.remove_button)
			buttonsPanel.add(buttonDelete);
		if(orcam.props_button)
			buttonsPanel.add(buttonProperties);
		if(orcam.cancel_button)
			buttonsPanel.add(buttonCancel);
	}
	
	public void setObjectResourceClass(Class orclass)
	{
		PropertiesPanel pane = new GeneralPanel();
		ObjectResourceDisplayModel dmod = new StubDisplayModel(new String [] {"name"}, new String [] {"Название"} );;
		ObjectResourceFilter fil = null;
		ObjectResourceSorter sor = null;

		this.orclass = orclass;
		if(	orclass != null &&
			ObjectResource.class.isAssignableFrom(orclass))
		{
			try
			{
				java.lang.reflect.Method propMethod = orclass.getMethod("getPropertyPane", new Class [0]);
				pane = (PropertiesPanel )propMethod.invoke(orclass, new Object [0]);
			}
			catch(NoSuchMethodException nsme)
			{
				System.out.println("Метод getPropertyPane не определен для " + orclass.getName());
				nsme.printStackTrace();
				pane = new GeneralPanel();
			}
			catch(SecurityException se)
			{
				System.out.println("Доступ к методу getPropertyPane закрыт для " + orclass.getName());
				se.printStackTrace();
				pane = new GeneralPanel();
			}
			catch(NullPointerException npe)
			{
				System.out.println("NullPointerException для " + orclass.getName());
				npe.printStackTrace();
				pane = new GeneralPanel();
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("getPropertyPane method  - IllegalAccessException in class " + orclass.getName());
				iae.printStackTrace();
				pane = new GeneralPanel();
			}
			catch(InvocationTargetException ite)
			{
				System.out.println("Метод getPropertyPane throws exception in " + orclass.getName());
				ite.printStackTrace();
				System.out.println("Target exception is:");
				ite.getTargetException().printStackTrace();
				pane = new GeneralPanel();
			}
			catch(ExceptionInInitializerError eiie)
			{
				System.out.println("init getPropertyPane fails in " + orclass.getName());
				eiie.printStackTrace();
				pane = new GeneralPanel();
			}
			catch(Exception e)
			{
				System.out.println("Exception getPropertyPane in " + orclass.getName());
				e.printStackTrace();
				pane = new GeneralPanel();
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

	private void jbInit() throws Exception
	{
		buttonAdd.setText("Новый");
//		buttonAdd.setSize(new Dimension (50, 8));
//		buttonAdd.setFocusable(false);
		buttonAdd.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonAdd.setBorderPainted(false);

		buttonAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonAdd_actionPerformed(e);
			}
		});
		buttonCancel.setText("Отменить");
//		buttonCancel.setSize(new Dimension (50, 8));
//		buttonCancel.setFocusable(false);
		buttonCancel.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonCancel.setBorderPainted(false);
		buttonCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonCancel_actionPerformed(e);
			}
		});
		buttonDelete.setText("Удалить");
//		buttonDelete.setSize(new Dimension (50, 8));
//		buttonDelete.setFocusable(false);
		buttonDelete.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonDelete.setBorderPainted(false);
		buttonDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonDelete_actionPerformed(e);
			}
		});
		buttonProperties.setText("Свойства");
//		buttonProperties.setSize(new Dimension (50, 8));
//		buttonProperties.setFocusable(false);
		buttonProperties.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonProperties.setBorderPainted(false);
		buttonProperties.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonProperties_actionPerformed(e);
			}
		});

		buttonSave.setText("Сохранить");
//		buttonSave.setSize(new Dimension (50, 8));
//		buttonSave.setFocusable(false);
		buttonSave.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonSave.setBorderPainted(false);
		buttonSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonSave_actionPerformed(e);
			}
		});
		jTabbedPane.setTabPlacement(JTabbedPane.LEFT);


		filterokButton.setText("Применить");
		filterokButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					filterokButton_actionPerformed(e);
				}
			});
		filtercancelButton.setText("Отменить");
		filtercancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					filtercancelButton_actionPerformed(e);
				}
			});
		filterclearButton.setText("Очистить");
		filterclearButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					filterclearButton_actionPerformed(e);
				}
			});
		filterContainingPanel.add(filterPane, BorderLayout.CENTER);
		filterButtonPanel.add(filterokButton, null);
		filterButtonPanel.add(filtercancelButton, null);
		filterButtonPanel.add(filterclearButton, null);
		filterContainingPanel.add(filterButtonPanel, BorderLayout.SOUTH);

		propScrollPane = new JScrollPane((JComponent)propPane);

		jTabbedPane.addTab( "", new TextIcon(listPane.getName(), jTabbedPane), listPane);
		jTabbedPane.addTab( "", new TextIcon(((JComponent)propPane).getName(), jTabbedPane), propScrollPane);
		jTabbedPane.addTab( "", new TextIcon(filterPane.getName(), jTabbedPane), filterContainingPanel);//filterPane);

		listPane.getViewport().setBackground(SystemColor.window);
		jTabbedPane.setBackground(SystemColor.control);

		jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
		jTabbedPane.setEnabledAt(1, false);
		jTabbedPane.setDisabledIconAt(2, new TextIcon(filterPane.getName(), jTabbedPane, false));
//		jTabbedPane.setEnabledAt(2, false);

		jTabbedPane.addChangeListener(new ObjectResourceCatalogPanel_jTabbedPane_changeAdapter(this));
		jTabbedPane.addComponentListener(new ObjectResourceCatalogPanel_jTabbedPane_componentAdapter(this));

//		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
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
//		jPanel.setPreferredSize(new Dimension(575, 350));
		jPanel.add(buttonsPanel, BorderLayout.NORTH);
		jPanel.add(jTabbedPane, BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(jPanel, BorderLayout.CENTER);

		listPane.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				this_valueChanged(e);
			}
		});
	}

	void jTabbedPane_componentShown(ComponentEvent e)
	{
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
		if(filter == null)
			jTabbedPane.setEnabledAt(2, false);
		else
			jTabbedPane.setEnabledAt(2, true);
	}

	private void filterclearButton_actionPerformed(ActionEvent e)
	{
		filterPane.getFilter().clearCriteria();
		filterPane.setFilter(filterPane.getFilter());
		setContents(dataSet);
	}

	private void filtercancelButton_actionPerformed(ActionEvent e)
	{
	}

	private void filterokButton_actionPerformed(ActionEvent e)
	{
//		DataSet ds = filter.filter(dataSet);
		setContents(dataSet);
	}

	public void setSorter(ObjectResourceSorter sorter)
	{
		listPane.setSorter(sorter);
	}

	public ObjectResourceSorter getSorter()
	{
		return listPane.getSorter();
	}

	public void setProp(PropertiesPanel propPane)
	{
//		System.out.println("set prop");
		propScrollPane.getViewport().remove((JComponent)this.propPane);
		this.propPane = propPane;
		propScrollPane.getViewport().add((JComponent)propPane);
		propPane.setContext(aContext);
	}

	public void loadListValues(ObjectResourceDisplayModel dmod, List dataSet)
	{
		if(dmod == null)
			dmod = new StubDisplayModel();
		if(dataSet == null)
			dataSet = new LinkedList();

		this.dmod = dmod;
		this.dataSet = dataSet;
		listPane.initialize(dmod, dataSet);
	}

	void buttonProperties_actionPerformed(ActionEvent e)
	{
//		System.out.println("props");
		ObjectResource or  = (ObjectResource)listPane.getSelectedObject();
		if(or != null)
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

	void buttonDelete_actionPerformed(ActionEvent e)
	{
		ObjectResource or = (ObjectResource)listPane.getSelectedObject();

		if(!propPane.delete())
			return;

		dataSet.remove(or);
		listPane.tableModel.fireTableDataChanged();

		send_event = true;
		dispatcher.notify(new TreeListSelectionEvent(or.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
//		dispatcher.notify(new OperationEvent(or.getTyp(), 0, "treelistrefreshevent"));
		send_event = false;
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
			send_event = true;
			dispatcher.notify(new TreeListSelectionEvent(obj, TreeListSelectionEvent.SELECT_EVENT));
			send_event = false;
		}
		send_event = true;
		dispatcher.notify(new TreeListSelectionEvent(or.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
//		dispatcher.notify(new OperationEvent(or.getTyp(), 0, "treelistrefreshevent"));
		send_event = false;
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		System.out.println("cancel");
	}

	void buttonAdd_actionPerformed(ActionEvent e)
	{
		System.out.println("add");

		if(!propPane.create())
		{
//			new MessageBox("Невозможно создать объект").show();
			return;
		}

		ObjectResource res = propPane.getObjectResource();
		dataSet.add(res);

		listPane.tableModel.fireTableDataChanged();

		send_event = true;
		dispatcher.notify(new TreeListSelectionEvent(res.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
//		dispatcher.notify(new OperationEvent(res.getTyp(), 0, "treelistrefreshevent"));
		dispatcher.notify(new TreeListSelectionEvent(res, TreeListSelectionEvent.SELECT_EVENT));
		send_event = false;

//		listPane.tableModel.fireTableDataChanged();
		listPane.setSelected(res);
		jTabbedPane.setSelectedComponent(propScrollPane);
		buttonCancel.setEnabled(true);
		buttonSave.setEnabled(true);
	}

	void buttonSave_actionPerformed(ActionEvent e)
	{
		System.out.println("save");

		if(!propPane.save())
		{
			return;
		}
		listPane.updateUI();

		ObjectResource or = propPane.getObjectResource();
		
		send_event = true;
		dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
//		dispatcher.notify(new OperationEvent("", 0, "treelistrefreshevent"));
		dispatcher.notify(new TreeListSelectionEvent(or, TreeListSelectionEvent.SELECT_EVENT));
		send_event = false;

//		buttonSave.setEnabled(false);
//		buttonCancel.setEnabled(false);
	}

	void jTabbedPane_stateChanged(ChangeEvent e)
	{
	}

	public void this_valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
			return;
		Object obj = (Object)listPane.getSelectedObject();
		if (obj == null)
			return;
		if(obj instanceof ObjectResource)
			propPane.setObjectResource((ObjectResource )obj);

//		System.out.println("List value " + e.getFirstIndex() + " to " + e.getLastIndex());

		jTabbedPane.setIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, true));
		jTabbedPane.setEnabledAt(1, true);
		buttonProperties.setEnabled(true);
		buttonCancel.setEnabled(true);
		buttonSave.setEnabled(true);
		buttonDelete.setEnabled(true);

//		System.out.println("ORCatalogPanel notify " + dispatcher + " with event \"listselectionevent\"");
		send_event = true;
		dispatcher.notify(new TreeListSelectionEvent(obj, TreeListSelectionEvent.SELECT_EVENT));
		send_event = false;
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (send_event)
				return;

//			System.out.println("ORCatalogPanel received treedataselectionevent event");

			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)oe;

			List data = tdse.getList();
			int n = tdse.getSelectionNumber();
			Class cl = tdse.getDataClass();
			ObjectResourceCatalogActionModel orcam = (ObjectResourceCatalogActionModel )tdse.getParam();

	/*    if (data.size() == 0)
				{
					setContents(data);
					jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
					jTabbedPane.setEnabledAt(1, false);
					jTabbedPane.setSelectedComponent(listPane);
					buttonProperties.setEnabled(false);
					buttonDelete.setEnabled(false);
					return;
				}
*/
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
					jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
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
				if(cce.DOMAIN_SELECTED)
				{
					setContents(new LinkedList());
				}
			}
	//		received_event = false;

	}
}

class ObjectResourceCatalogPanel_jTabbedPane_componentAdapter
		extends java.awt.event.ComponentAdapter
{
	ObjectResourceCatalogPanel adaptee;

	ObjectResourceCatalogPanel_jTabbedPane_componentAdapter(
			ObjectResourceCatalogPanel adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.jTabbedPane_componentShown(e);
	}
}

class ObjectResourceCatalogPanel_jTabbedPane_changeAdapter
		implements javax.swing.event.ChangeListener
{
	ObjectResourceCatalogPanel adaptee;

	ObjectResourceCatalogPanel_jTabbedPane_changeAdapter(
			ObjectResourceCatalogPanel adaptee)
	{
		this.adaptee = adaptee;
	}

	public void stateChanged(ChangeEvent e)
	{
		adaptee.jTabbedPane_stateChanged(e);
	}
}
