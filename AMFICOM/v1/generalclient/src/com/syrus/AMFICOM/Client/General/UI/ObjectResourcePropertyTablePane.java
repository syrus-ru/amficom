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
// *        Client\General\Panels\GeneralListPane.java                    * //
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

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

public class ObjectResourcePropertyTablePane extends JScrollPane
{
//	XYLayout xYLayout1 = new XYLayout();
	ObjectResourcePropertyTableModel tableModel;
	ObjectResourcePropertyTableRenderer renderer;
	ObjectResourcePropertyTableEditor editor;
	JTable jTable = new JTable();

//	Hashtable columns;

//	Hashtable contents;

	public ObjectResourcePropertyTablePane()
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
	}

	private void jbInit() throws Exception
	{
		setName(LangModel.getString("labelTabbedList"));

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

//		jTable.setSelectionMode(
//				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
//		jTable.setMaximumSize(new Dimension(500, 400));
//		jTable.setMinimumSize(new Dimension(500, 400));
		this.getViewport().add(jTable, null);
		this.setWheelScrollingEnabled(true);

		this.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
//		jTable.setBackground(this.getBackground());

//		jTable1.setDefaultRenderer(String.class, new MyParamRenderer());
//		jTable1.setDefaultRenderer(Integer.class, new MyParamRenderer());

//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.setPreferredSize(new Dimension(510, 410));
	}

	void tableInit()
	{
		if(tableModel == null)
			tableModel = new ObjectResourcePropertyTableModel();
		if(renderer == null)
			renderer = new ObjectResourcePropertyTableRenderer(tableModel);
		if(editor == null)
			editor = new ObjectResourcePropertyTableEditor(tableModel);

		jTable.setModel(tableModel);
		jTable.setDefaultRenderer(Object.class, renderer);
		jTable.setDefaultEditor(Object.class, editor);

		jTable.doLayout();
	}

	public void setRenderer(ObjectResourcePropertyTableRenderer renderer)
	{
		this.renderer = renderer;
		renderer.setModel(tableModel);
		jTable.setDefaultRenderer(ObjectResource.class, renderer);
	}

	public void setEditor(ObjectResourcePropertyTableEditor editor)
	{
		this.editor = editor;
		editor.setModel(tableModel);
		jTable.setDefaultEditor(ObjectResource.class, editor);
	}

	public void initialize(Vector cols, ObjectResource or)
	{
		tableModel = new ObjectResourcePropertyTableModel(cols, or);
		tableInit();
//		tableModel.setDisplayModel(displayModel);
//		tableModel.setContents(list);
	}

	public void initialize(String [] cols, ObjectResource or)
	{
		tableModel = new ObjectResourcePropertyTableModel(cols, or);
		tableInit();
//		tableModel.setDisplayModel(displayModel);
//		tableModel.setContents(list);
	}

	public void setTableModel(ObjectResourcePropertyTableModel tableModel)
	{
		this.tableModel = tableModel;
		renderer = new ObjectResourcePropertyTableRenderer(tableModel);
		editor = new ObjectResourcePropertyTableEditor(tableModel);
		tableInit();
	}

/*
	public String getSelectedField(String field_name)
	{
		int ind = jTable.getSelectedRow();
		int i;

		for(i = jTable.getColumnCount(); i > 0; i--)
			if(jTable.getColumnName(i - 1).equals(
					(String)columns.get(field_name)))
				break;
		if(i == 0)
			return null;
		return (String)tableModel.getValueAt(ind, i - 1);
	}
*/
	public Object getSelectedObject()
	{
		int ind = jTable.getSelectedRow();
		return tableModel.getObjectByIndex(ind);
	}

	public void setSelected(Object o)
	{
		tableModel.setContents((ObjectResource)o);
//		int i = tableModel.getObjectIndex((ObjectResource )o);
/*
		DataSet list = tableModel.getContents();
		int i = list.indexOf(o);
		if(i < list.size())
			jTable.getSelectionModel().setSelectionInterval(i, i);
*/
	}

	public void addListSelectionListener(ListSelectionListener parent)
	{
		jTable.getSelectionModel().addListSelectionListener(parent);
	}

	public JTable getTable()
	{
		return jTable;
	}

}
