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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ObjectResourceCatalogPanel extends JPanel
		implements OperationListener
{
	public ApplicationContext aContext;
	Dispatcher dispatcher = new Dispatcher();

	ObjectResourceTablePane listPane = new ObjectResourceTablePane();
	PropertiesPanel propPane = new GeneralTabbedPane();
	ObjectResourceFilterPane filterPane = new ObjectResourceFilterPane();

	ObjectResourceDisplayModel dmod;
	DataSet dataSet;
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

		setContext(aContext);
	}

	public ObjectResourceCatalogPanel(ApplicationContext aContext, ObjectResourceDisplayModel dmod, Class orclass)
	{
		this(aContext);
		setDisplayModel(dmod);
		setObjectResourceClass(orclass);
	}

	public ObjectResourceCatalogPanel(ApplicationContext aContext, ObjectResourceDisplayModel dmod, Class orclass, DataSet dataSet)
	{
		this(aContext, dmod, orclass);
		setContents(dataSet);
	}

	public void setContents(DataSet dataSet)
	{
		if(dmod == null)
			dmod = new StubDisplayModel();
		if(dataSet == null)
			dataSet = new DataSet();

		this.dataSet = dataSet;
		listPane.initialize(dmod, dataSet);
	}

	public void setDisplayModel(ObjectResourceDisplayModel dmod)
	{
		if(dmod == null)
			dmod = new StubDisplayModel();
		if(dataSet == null)
			dataSet = new DataSet();

		this.dmod = dmod;
		listPane.initialize(dmod, dataSet);
	}

	public void setObjectResourceClass(Class orclass)
	{
		PropertiesPanel pane;
		ObjectResourceDisplayModel dmod;
		ObjectResourceFilter fil;

		this.orclass = orclass;
		try
		{
			java.lang.reflect.Method propMethod = orclass.getMethod("getPropertyPane", new Class [0]);
			pane = (PropertiesPanel )propMethod.invoke(orclass, new Object [0]);
		}
		catch(IllegalAccessException iae)
		{
			System.out.println("??? ???? ?? ???????? ???????? ??????");
			pane = new GeneralPanel();
		}
		catch(Exception e)
		{
			System.out.println("?????");
			pane = new GeneralPanel();
		}

		try
		{
			java.lang.reflect.Method dmodMethod = orclass.getMethod("getDefaultDisplayModel", new Class [0]);
			dmod = (ObjectResourceDisplayModel )dmodMethod.invoke(orclass, new Object [0]);
		}
		catch(IllegalAccessException iae)
		{
			System.out.println("??? ???? ?? ???????? ???????? ??????");
			dmod = new StubDisplayModel(new String [] {"name"}, new String [] {"Название"} );
		}
		catch(Exception e)
		{
			System.out.println("?????");
			dmod = new StubDisplayModel(new String [] {"name"}, new String [] {"Название"} );
		}

		try
		{
			java.lang.reflect.Method filMethod = orclass.getMethod("getFilter", new Class [0]);
			fil = (ObjectResourceFilter )filMethod.invoke(orclass, new Object [0]);
		}
		catch(IllegalAccessException iae)
		{
			System.out.println("??? ???? ?? ???????? ???????? ??????");
			fil = null;
		}
		catch(Exception e)
		{
			System.out.println("?????");
			fil = null;
		}

		setProp(pane);
		setDisplayModel(dmod);
		setFilter(fil);
	}

	private void jbInit() throws Exception
	{
		buttonAdd.setText("Новый");
		buttonAdd.setSize(new Dimension (50, 8));
		buttonAdd.setFocusable(false);
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
		buttonCancel.setSize(new Dimension (50, 8));
		buttonCancel.setFocusable(false);
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
		buttonDelete.setSize(new Dimension (50, 8));
		buttonDelete.setFocusable(false);
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
		buttonProperties.setSize(new Dimension (50, 8));
		buttonProperties.setFocusable(false);
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
		buttonSave.setSize(new Dimension (50, 8));
		buttonSave.setFocusable(false);
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

		jTabbedPane.addTab( "", new TextIcon(listPane.getName(), jTabbedPane), listPane);
		propScrollPane = new JScrollPane((JComponent)propPane);
		jTabbedPane.addTab( "", new TextIcon(((JComponent)propPane).getName(), jTabbedPane), propScrollPane);
		jTabbedPane.addTab( "", new TextIcon(filterPane.getName(), jTabbedPane), filterPane);

		listPane.getViewport().setBackground(SystemColor.window);
		jTabbedPane.setBackground(SystemColor.control);

		jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
		jTabbedPane.setEnabledAt(1, false);
		jTabbedPane.setDisabledIconAt(2, new TextIcon(filterPane.getName(), jTabbedPane, false));
//		jTabbedPane.setEnabledAt(2, false);

		jTabbedPane.addChangeListener(new ObjectResourceCatalogPanel_jTabbedPane_changeAdapter(this));
		jTabbedPane.addComponentListener(new ObjectResourceCatalogPanel_jTabbedPane_componentAdapter(this));

		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
		buttonsPanel.setLayout(new XYLayout());
		buttonsPanel.setPreferredSize(new Dimension(575, 27));
		buttonsPanel.add(buttonAdd, new XYConstraints(10, 0, -1, -1));
		buttonsPanel.add(buttonSave, new XYConstraints(60, 0, -1, -1));
		buttonsPanel.add(buttonDelete, new XYConstraints(125, 0, -1, -1));
		buttonsPanel.add(buttonProperties, new XYConstraints(180, 0, -1, -1));
		buttonsPanel.add(buttonCancel, new XYConstraints(240, 0, -1, -1));

		buttonCancel.setEnabled(false);
		buttonSave.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonProperties.setEnabled(false);

		jPanel.setLayout(new BorderLayout());
		jPanel.setPreferredSize(new Dimension(575, 350));
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
			dispatcher.register(this, TreeDataSelectionEvent.typ);
		}
		propPane.setContext(aContext);
	}

	public void setFilter(ObjectResourceFilter filter)
	{
		filterPane.setFilter(filter);
		if(filter == null)
			jTabbedPane.setEnabledAt(2, false);
		else
			jTabbedPane.setEnabledAt(2, true);
	}

	public void setProp(PropertiesPanel propPane)
	{
		System.out.println("set prop");
		propScrollPane.getViewport().remove((JComponent)this.propPane);
		this.propPane = propPane;
		propScrollPane.getViewport().add((JComponent)propPane);
		propPane.setContext(aContext);
	}

	public void loadListValues(ObjectResourceDisplayModel dmod, DataSet dataSet)
	{
		if(dmod == null)
			dmod = new StubDisplayModel();
		if(dataSet == null)
			dataSet = new DataSet();

		this.dmod = dmod;
		this.dataSet = dataSet;
		listPane.initialize(dmod, dataSet);
	}

	void buttonProperties_actionPerformed(ActionEvent e)
	{
		System.out.println("props");
		ObjectResource or  = (ObjectResource)listPane.getSelectedObject();
		if(or != null)
		{
			if(!propPane.setObjectResource(or))
				return;
				jTabbedPane.setSelectedComponent(propScrollPane);
		}
	}

	void buttonDelete_actionPerformed(ActionEvent e)
	{
		ObjectResource or = (ObjectResource)listPane.getSelectedObject();

		if(!propPane.delete())
			return;

		dataSet.remove(or);
		listPane.tableModel.fireTableDataChanged();

		buttonCancel.setEnabled(true);
		buttonSave.setEnabled(true);
		jTabbedPane.setSelectedComponent(listPane);
		if (dataSet.size() == 0)
		{
			jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
			jTabbedPane.setEnabledAt(1, false);
			buttonProperties.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
		else
		{
			listPane.setSelected(dataSet.elements().nextElement());
		}
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		System.out.println("cancel");
	}

	void buttonAdd_actionPerformed(ActionEvent e)
	{
		System.out.println("add");

		if(!propPane.create())
			return;

		ObjectResource res = propPane.getObjectResource();
		dataSet.add(res);

		dispatcher.notify(new TreeListSelectionEvent(res.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
//		dispatcher.notify(new OperationEvent(res.getTyp(), 0, "treelistrefreshevent"));

		listPane.tableModel.fireTableDataChanged();
		listPane.setSelected(res);
		jTabbedPane.setSelectedComponent(propScrollPane);
		buttonSave.setEnabled(true);
	}

	void buttonSave_actionPerformed(ActionEvent e)
	{
		System.out.println("save");

		if(!propPane.save())
			return;

		buttonSave.setEnabled(false);
		buttonCancel.setEnabled(false);
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
			propPane.setObjectResource((ObjectResource)obj);

//		System.out.println("List value " + e.getFirstIndex() + " to " + e.getLastIndex());

		jTabbedPane.setIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, true));
		jTabbedPane.setEnabledAt(1, true);
		buttonProperties.setEnabled(true);
		buttonDelete.setEnabled(true);

//		System.out.println("ORCatalogPanel notify " + dispatcher + " with event \"listselectionevent\"");
		send_event = true;
		dispatcher.notify(new TreeListSelectionEvent(obj));
		send_event = false;
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(TreeDataSelectionEvent.typ))
		{
			if (send_event)
				return;

			System.out.println("ORCatalogPanel received treedataselectionevent event");

			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)oe;

			DataSet data = tdse.getDataSet();
			int n = tdse.getSelectionNumber();
			Class cl = tdse.getDataClass();

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
					ObjectResource res = data.get(n);

					if (dataSet.indexOf(res) == -1)
					{
						setContents(data);
						setObjectResourceClass(cl);
					}
					listPane.setSelected(res);
				}
				else
				{
					setContents(data);
					setObjectResourceClass(cl);
					jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
					jTabbedPane.setEnabledAt(1, false);
					jTabbedPane.setSelectedComponent(listPane);
					buttonProperties.setEnabled(false);
					buttonDelete.setEnabled(false);
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

