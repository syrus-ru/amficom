/*
 * $Id: ObjectResourcePropertyTableModel.java,v 1.4 2004/09/27 05:55:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.Component;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 05:55:00 $
 * @module generalclient_v1
 */
public class ObjectResourcePropertyTableModel extends AbstractTableModel
{
    private Component parent;

	private List columns = new LinkedList();
	private ObjectResource or;
	private ObjectResourceDisplayModel displayModel = new StubPropertyDisplayModel();

	public ObjectResourcePropertyTableModel(
			List columns,
//			ObjectResourceDisplayModel displayModel,
			ObjectResource or)
	{
		this.columns.addAll(columns);
//		setDisplayModel(displayModel);
		setContents(or);
	}

	public ObjectResourcePropertyTableModel(
			String[] columns,
//			ObjectResourceDisplayModel displayModel,
			ObjectResource or)
	{
		for(int i = 0; i < columns.length; i++)
			this.columns.add(columns[i]);
//		setDisplayModel(displayModel);
		setContents(or);
	}

	public ObjectResourcePropertyTableModel(List columns)
	{
		this(columns, null);
	}

	public ObjectResourcePropertyTableModel(String[] columns)
	{
		this(columns, null);
	}

	public ObjectResourcePropertyTableModel()
	{
		this(new LinkedList(), null);
	}

	public void setDisplayModel(ObjectResourceDisplayModel displayModel)
	{
		this.displayModel = displayModel;
	}

	public ObjectResourceDisplayModel getDisplayModel()
	{
		return displayModel;
	}

	public String getColumnByNumber(int col_i)
	{
		return (String )displayModel.getColumns().get(col_i);
	}

	public void setContents(ObjectResource or)
	{
		this.or = or;
		displayModel = new StubPropertyDisplayModel(or);
		super.fireTableDataChanged();
	}

	public ObjectResource getContents()
	{
		return or;
	}

	//--------------------------------------------------------------------------
	// Overrides AbstractTableModel method. Returns the number of columns in table
	//--------------------------------------------------------------------------
	public int getColumnCount()
	{
		return 2;
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the number of rows in table
	//--------------------------------------------------------------------------
	public int getRowCount()
	{
//		ObjectResourceModel mod = or.getModel();
//		return mod.getColumns().size();
		return displayModel.getColumns().size();
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the column name for the
	// specified column
	//--------------------------------------------------------------------------
	public String getColumnName(int p_col)
	{
		try 
        {
            return (String)columns.get(p_col);
        } 
		catch (Exception ex) 
        {
            return String.valueOf(p_col);
        }
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the value at the specified cell
	//--------------------------------------------------------------------------
	public Object getValueAt(int p_row, int p_col)
	{
		if(or == null)
			return "";
		String col_id = (String)displayModel.getColumns().get(p_row);
//		ObjectResourceModel mod = or.getModel();
		return col_id;
//		return mod.getColumnValue(vol_id);
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Returns the class for the
	// specified column
	//--------------------------------------------------------------------------
	public Class getColumnClass(int p_col)
	{
		return Object.class;
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. By default in our situation
	// all cells are non-editable
	//--------------------------------------------------------------------------
	public boolean isCellEditable(int p_row, int p_col)
	{
		if(or == null)
			return false;
		else
		if(p_col == 0)
			return false;
		else
		{
			String col_id = (String)displayModel.getColumns().get(p_row);
			return displayModel.isColumnEditable(col_id);
//			return true;
		}
	}

	//--------------------------------------------------------------------------
	//Overrides AbstractTableModel method. Sets the value at the specified cell
	// to p_obj
	//--------------------------------------------------------------------------
	public void setValueAt( Object p_obj, int p_row, int p_col)
	{
		if(or == null)
			return;
		else
		if(p_col == 0)
			return;
		else
		{
			String col_id = (String)displayModel.getColumns().get(p_row);
			ObjectResourceModel model = or.getModel();
			model.setPropertyValue(col_id, p_obj);
			this.fireTableDataChanged();
		}
	}

	public Object getObjectByIndex(int ind)
	{
		String col_id = (String)displayModel.getColumns().get(ind);
		return getValueAt(ind, 0);
	}

	public void clearTable()
	{
		or = null;
		super.fireTableDataChanged();
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
