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
/**
 * @author  srangan.in
 * @version 1.0
 *
 * Development Environment        :  JDeveloper 2.0
 * Name of the Application        :  genTableModel.java
 * Creation/Modification History  :
 *
 *    srangan.in       24-Dec-1998      Created
 *
 * Overview of Application        :
 *
 * This class maintains the data required for a JTable handling.
 * It can be used as the table model, by any application using JTables.
 * It encapsulates the maintenance of the JTable data, and also provides a
 * few value addition member functions:
 *    1) populateFromResultSet: takes the result set from a query and
 *       handles the population of the table data from the data query.
 *    2) insertRow: takes a vector containing a new row values, and
 *       creates a new row in the table.
 *    3) deleteRow: deletes the row specified from the displayed rows.
 *    4) getRow:    returns a vector containing the row data.
 *    5) repaintTable: Sends an event to the JTable asking it to reset the
 *       table properties at repaint.
 *    6) clearTable: Clears table data
 *
 * The data itself is maintained in a vector, and hence the table data is
 * maintained efficiently, as the vector can grow and shrink as and when the
 * table data changes.
 *
 * The constructor takes an array of columnNames, number of rows to be created
 * initially. Also it takes an array of default value object(which may be
 * heterogeneous. This helps in deciding the column type.
 *
 * If more functionality is required, like setting columns as non-editable
 * and changing cell-renderers, this class can be extended.
 */
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI;

import java.sql.*;
import java.util.*;

/** Use the following import statements with SWING version 1.1**/
import javax.swing.table.*;

public class GeneralTableModel extends AbstractTableModel
{
	public Vector m_data;                  // Hold the table data
	public String[] m_columnNames;         // Hold the column names.

  /************************************************************************
	Constructor:
	Parameters- p_columns: array of column titles.
				p_defaultv: array of default value objects, for each column
				p_rows: number of rows initially
  *************************************************************************
  */
	public GeneralTableModel(
			int p_cols)
	{
		int i;
		int j;
		m_columnNames = new String[p_cols];
		for (i = 0; i < p_cols; i++) // Empty column names
			m_columnNames[i] = "";

		m_data = new Vector();  // Instantiate Data vector
	}

	public GeneralTableModel(
			String p_columns[],
			Object p_defaultv[],
			int p_rows)
	{
		int i;
		int j;
		m_columnNames = new String[p_columns.length];
		for (i = 0; i < p_columns.length; i++) // Copy column names
			m_columnNames[i] = new String(p_columns[i]);

		m_data = new Vector();  // Instantiate Data vector

		// Instantiate data vector to default values
		for (i = 0; i < p_rows; i++)
		{
			Vector l_cols = new Vector();
			for (j = 0; j < p_columns.length; j++)
				l_cols.addElement(p_defaultv[j]);
			m_data.addElement(l_cols);
		}
	}

  /************************************************************************
   Overrides AbstractTableModel method. Returns the number of columns in table
  *************************************************************************
  */
	public int getColumnCount()
	{
		return m_columnNames.length;
	}

  /************************************************************************
   Overrides AbstractTableModel method. Returns the number of rows in table
  *************************************************************************
  */
	public int getRowCount()
	{
		return m_data.size();
	}

  /************************************************************************
   Overrides AbstractTableModel method. Returns the column name for the
   specified column
  *************************************************************************
  */
	public String getColumnName(int p_col)
	{
		return m_columnNames[p_col];
	}

  /************************************************************************
   Overrides AbstractTableModel method. Returns the value at the specified cell
  *************************************************************************
  */
	public Object getValueAt(int p_row, int p_col)
	{
		Vector l_colvector = (Vector) m_data.elementAt(p_row);
		return l_colvector.elementAt(p_col);
	}

  /************************************************************************
   Overrides AbstractTableModel method. Returns the class for the
   specified column
  *************************************************************************
  */
	public Class getColumnClass(int p_col)
	{
		return getValueAt(0, p_col).getClass();
	}

  /************************************************************************
   Overrides AbstractTableModel method. By default in our situation
   all cells are non-editable
  *************************************************************************
  */
	public boolean isCellEditable(int p_row, int p_col)
	{
		return false;
	}


  /************************************************************************
   Overrides AbstractTableModel method. Sets the value at the specified cell
   to p_obj
  *************************************************************************
  */
	public void setValueAt( Object p_obj, int p_row, int p_col)
	{
		Vector l_colvector = (Vector) m_data.elementAt(p_row);
		l_colvector.setElementAt(p_obj, p_col);
	}

  /************************************************************************
   Repopulates the table data. The table is populated with the rows returned
   by the ResultSet
  *************************************************************************
  */
	public void populateFromResultSet(ResultSet p_rset)
	{
		int i;

		m_data = new Vector();

		try
		{
			while (p_rset.next())
			{
				Vector l_cols = new Vector();
				for (i = 0; i < m_columnNames.length; i++)
					l_cols.addElement(p_rset.getObject(i + 1));
				m_data.addElement(l_cols);
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		super.fireTableDataChanged();
	}

  /************************************************************************
   Adds a new row to the table
  *************************************************************************
  */
	public void insertRow(Vector p_newrow)
	{
		m_data.addElement(p_newrow);
		super.fireTableDataChanged();
	}

  /************************************************************************
   Deletes the specified row from the table
  *************************************************************************
  */
	public void deleteRow(int p_row)
	{
		m_data.removeElementAt(p_row);
		super.fireTableDataChanged();
	}

  /************************************************************************
   Returns the values at the specified row as a vector
  *************************************************************************
  */
	public Vector getRow(int p_row)
	{
		return (Vector) m_data.elementAt(p_row);
	}

  /************************************************************************
   Updates the specified row. It replaces the row vector at the specified
   row with the new vector.
  *************************************************************************
  */
	public void updateRow(Vector p_updatedRow, int p_row)
	{
		m_data.setElementAt(p_updatedRow, p_row);
		super.fireTableDataChanged();
	}

  /************************************************************************
   Clears the table data
  *************************************************************************
  */
	public void clearTable()
	{
		m_data = new Vector();
		super.fireTableDataChanged();
	}
}
