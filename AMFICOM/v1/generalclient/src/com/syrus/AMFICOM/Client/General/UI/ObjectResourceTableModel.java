/*
 * $Id: ObjectResourceTableModel.java,v 1.4 2004/09/25 20:00:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.Component;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/25 20:00:39 $
 * @module generalclient_v1
 */
public class ObjectResourceTableModel extends AbstractTableModel
{
    private Component parent;

	private List dataSet;
	private ObjectResourceDisplayModel displayModel;

	boolean doRestrict = false;
	String domain_id = "";

	public ObjectResourceTableModel(
			ObjectResourceDisplayModel displayModel,
			List dataSet)
	{
		setDisplayModel(displayModel);
		setContents(dataSet);
	}

	public ObjectResourceTableModel(ObjectResourceDisplayModel displayModel)
	{
		this(displayModel, new LinkedList());
	}

	public ObjectResourceTableModel()
	{
		this(new StubDisplayModel(), new LinkedList());
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

	public void setContents(List dataSet)
	{
		if(dataSet == null)
			dataSet = new LinkedList();
		this.dataSet = dataSet;
		if(doRestrict)
			restrictContents();
		super.fireTableDataChanged();
	}

	public List getContents()
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
		List vec_to_remove = new LinkedList();
		for(Iterator it = dataSet.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			if(!or.getDomainId().equals(domain_id))
				vec_to_remove.add(or);
		}
		for(Iterator it = vec_to_remove.iterator(); it.hasNext();)
			dataSet.remove((ObjectResource )it.next());
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
		String col_id = (String )displayModel.getColumns().get(p_col);
		ObjectResource or = (ObjectResource )dataSet.get(p_row);
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
		String col_id = (String )displayModel.getColumns().get(p_col);
		return displayModel.isColumnEditable(col_id);
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Sets the value at the specified cell
	// to p_obj
	//--------------------------------------------------------------------------
	public void setValueAt( Object p_obj, int p_row, int p_col)
	{
		String col_id = (String )displayModel.getColumns().get(p_col);
		ObjectResource or = (ObjectResource )dataSet.get(p_row);
		ObjectResourceModel model = or.getModel();
		model.setColumnValue(col_id, p_obj);
		this.fireTableDataChanged();
	}

	public ObjectResource getObjectByIndex(int ind)
	{
//		String col_id = (String )displayModel.getColumns().get(p_col);
		return (ObjectResource )dataSet.get(ind);
	}

	public void clearTable()
	{
		dataSet = new LinkedList();
		super.fireTableDataChanged();
	}

	public void moveColumn(int from, int to)
	{
		Object o = displayModel.getColumns().get(from);
		displayModel.getColumns().remove(from);
//		if(from > to)
			displayModel.getColumns().add(to, o);
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
