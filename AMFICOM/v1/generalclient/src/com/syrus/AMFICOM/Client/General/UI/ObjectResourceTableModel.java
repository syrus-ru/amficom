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
// *        Client\General\GeneralTableModel.java                         * //
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

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.Component;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ObjectResourceTableModel extends AbstractTableModel
{
    private Component parent;

	private DataSet dataSet;
	private ObjectResourceDisplayModel displayModel;

	boolean doRestrict = false;
	String domain_id = "";

	public ObjectResourceTableModel(
			ObjectResourceDisplayModel displayModel,
			DataSet dataSet)
	{
		setDisplayModel(displayModel);
		setContents(dataSet);
	}

	public ObjectResourceTableModel(ObjectResourceDisplayModel displayModel)
	{
		this(displayModel, new DataSet());
	}

	public ObjectResourceTableModel()
	{
		this(new StubDisplayModel(), new DataSet());
	}

	public void setDisplayModel(ObjectResourceDisplayModel displayModel)
	{
		this.displayModel = displayModel;
		super.fireTableDataChanged();
	}

	public ObjectResourceDisplayModel getDisplayModel()
	{
		return displayModel;
	}

	public String getColumnByNumber(int col_i)
	{
		return (String)displayModel.getColumns().get(col_i);
	}

	public void setContents(DataSet dataSet)
	{
		if(dataSet == null)
			dataSet = new DataSet();
		this.dataSet = dataSet;
		if(doRestrict)
			restrictContents();
		super.fireTableDataChanged();
	}

	public DataSet getContents()
	{
		return dataSet;
	}

	public void restrictToDomain(boolean bool)
	{
		doRestrict = bool;
		if(doRestrict)
			restrictContents();
		super.fireTableDataChanged();
	}

	public void restrictContents()
	{
		Vector vec_to_remove = new Vector();
		for(Enumeration enum = dataSet.elements(); enum.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )enum.nextElement();
			if(!or.getDomainId().equals(domain_id))
				vec_to_remove.add(or);
		}
		for(int i = 0; i < vec_to_remove.size(); i++)
			dataSet.remove((ObjectResource )vec_to_remove.get(i));
	}

	public void setDomainId(String domain_id)
	{
		this.domain_id = domain_id;
	}

	public String getDomainId()
	{
		return domain_id;
	}

	//--------------------------------------------------------------------------
	// Overrides AbstractTableModel method. Returns the number of columns in table
	//--------------------------------------------------------------------------
	public int getColumnCount()
	{
		return displayModel.getColumns().size();
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the number of rows in table
	//--------------------------------------------------------------------------
	public int getRowCount()
	{
		return dataSet.size();
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the column name for the
	// specified column
	//--------------------------------------------------------------------------
	public String getColumnName(int p_col)
	{
		String col_id = (String)displayModel.getColumns().get(p_col);
		return (String)displayModel.getColumnName(col_id);
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the value at the specified cell
	//--------------------------------------------------------------------------
	public Object getValueAt(int p_row, int p_col)
	{
		String col_id = (String)displayModel.getColumns().get(p_col);
		ObjectResource or = (ObjectResource)dataSet.get(p_row);
		return or;
//		ObjectResourceModel model = or.getModel();
//		return model.getColumnValue(col_id);
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the class for the
	// specified column
	//--------------------------------------------------------------------------
	public Class getColumnClass(int p_col)
	{
		return ObjectResource.class;
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. By default in our situation
	// all cells are non-editable
	//--------------------------------------------------------------------------
	public boolean isCellEditable(int p_row, int p_col)
	{
		String col_id = (String)displayModel.getColumns().get(p_col);
		return displayModel.isColumnEditable(col_id);
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Sets the value at the specified cell
	// to p_obj
	//--------------------------------------------------------------------------
	public void setValueAt( Object p_obj, int p_row, int p_col)
	{
		String col_id = (String)displayModel.getColumns().get(p_col);
		ObjectResource or = (ObjectResource)dataSet.get(p_row);
		ObjectResourceModel model = or.getModel();
		model.setColumnValue(col_id, p_obj);
		this.fireTableDataChanged();
	}

	public ObjectResource getObjectByIndex(int ind)
	{
//		String col_id = (String )displayModel.getColumns().get(p_col);
		return (ObjectResource)dataSet.get(ind);
	}

	public void clearTable()
	{
		dataSet = new DataSet();
		super.fireTableDataChanged();
	}

	public void moveColumn(int from, int to)
	{
		Object o = displayModel.getColumns().get(from);
		displayModel.getColumns().removeElementAt(from);
//		if(from > to)
			displayModel.getColumns().insertElementAt(o, to);
//		else
//			displayModel.getColumns().insertElementAt(o, to - 1);
	}
	
/*
  //--------------------------------------------------------------------------
  public void insertRow(Vector p_newrow)
	{
		m_data.addElement(p_newrow);
		super.fireTableDataChanged();
	}

  //--------------------------------------------------------------------------
  public void deleteRow(int p_row)
	{
		m_data.removeElementAt(p_row);
		super.fireTableDataChanged();
	}

  //--------------------------------------------------------------------------
  public Vector getRow(int p_row)
	{
		return (Vector) m_data.elementAt(p_row);
	}

  //--------------------------------------------------------------------------
  public void updateRow(Vector p_updatedRow, int p_row)
	{
		m_data.setElementAt(p_updatedRow, p_row);
		super.fireTableDataChanged();
	}
*/
	//--------------------------------------------------------------------------
	//Clears the table data
	//--------------------------------------------------------------------------
}

