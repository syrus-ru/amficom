/*
 * $Id: ObjectResourceTablePane.java,v 1.11 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;

import java.awt.SystemColor;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/05/13 19:05:47 $
 * @module generalclient_v1
 */
public class ObjectResourceTablePane extends JScrollPane
{
	public ObjectResourceTableModel tableModel;
	ObjectResourceTableRenderer renderer;
	ObjectResourceTableEditor editor;
	ObjectResourceSorter sorter = null;
	JTable jTable = new JTable();
	JTableHeader jTableHeader;

	MouseListener ml_header;

	String sort_type = "";
	int sort_dir = 1;

	public ObjectResourceTablePane()
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

		ml_header = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			this_header_mousePressed (e);
			}
		};
		jTableHeader = jTable.getTableHeader();

//		jTable.setSelectionMode(
//				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

//		jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
		this.getViewport().add(jTable, null);
		this.setWheelScrollingEnabled(true);

//		this.setPreferredSize(new Dimension(510, 410));

		this.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
	}

	void tableInit()
	{
		if(tableModel == null)
			tableModel = new ObjectResourceTableModel();
//		if(renderer == null)
			renderer = new ObjectResourceTableRenderer(tableModel);
//		if(editor == null)
			editor = new ObjectResourceTableEditor(tableModel);

		jTable.setModel(tableModel);
		jTable.setDefaultRenderer(StorableObject.class, renderer);
		jTable.setDefaultEditor(StorableObject.class, editor);

//		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel ctm = jTable.getColumnModel();
		for(int i = tableModel.getColumnCount() - 1; i >= 0; i--)
		{
			TableColumn tc = ctm.getColumn(i);
			String col_id = tableModel.getColumnByNumber(i);
			tc.setPreferredWidth(tableModel.getDisplayModel().getColumnSize(col_id));
//			System.out.println("set col " + col_id + " wd " + tableModel.getDisplayModel().getColumnSize(col_id));
		}

		jTable.doLayout();
	}

	public void setRenderer(ObjectResourceTableRenderer renderer)
	{
		this.renderer = renderer;
		renderer.setModel(tableModel);
		jTable.setDefaultRenderer(StorableObject.class, renderer);
	}

	public void setEditor(ObjectResourceTableEditor editor)
	{
		this.editor = editor;
		editor.setModel(tableModel);
		jTable.setDefaultEditor(StorableObject.class, editor);
	}

	public void initialize(ObjectResourceDisplayModel displayModel, List dataSet)
	{
		tableModel = new ObjectResourceTableModel(displayModel, dataSet);
		tableInit();
//		tableModel.setDisplayModel(displayModel);
//		tableModel.setContents(list);
	}

	public void setSorter(ObjectResourceSorter sorter)
	{
		if(sorter != null)
			jTableHeader.removeMouseListener(ml_header);
		this.sorter = sorter;
		if(sorter != null)
			jTableHeader.addMouseListener(ml_header);
	}

	public ObjectResourceSorter getSorter()
	{
		return sorter;
	}

	public void setContents(List dataSet)
	{
		tableModel.setContents(dataSet);
	}

	public List getContents()
	{
		return tableModel.getContents();
	}

	public void resortContents()
	{
		if(sorter == null)
			return;
		List ds = getContents();
		sorter.setDataSet(ds);
		ds = sorter.sort(sort_type, sort_dir);
		setContents(ds);
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
	public Object getObjectAt(int i)
	{
	 return tableModel.getObjectByIndex(i);
	}

	public Object getSelectedObject()
	{
		int ind = jTable.getSelectedRow();
		if(ind == -1)
		return null;
		return tableModel.getObjectByIndex(ind);
	}

	public int setSelected(Object o)
	{
		List dataSet = tableModel.getContents();
		int i = dataSet.indexOf(o);
		if(i >= 0 && i < dataSet.size())
		{
			jTable.setRowSelectionInterval(i, i);
//			System.out.println("Selected component " + i);
		}
		else
		{
			if(dataSet.size() > 0)
				jTable.removeRowSelectionInterval(0, dataSet.size() - 1);
		}
		return i;
	}

	public void deselect(Object o)
	{
		List dataSet = tableModel.getContents();
		int i = dataSet.indexOf(o);
		if(i >= 0 && i < dataSet.size())
		{
			jTable.removeRowSelectionInterval(i, i);
//			System.out.println("Deelected component " + i);
		}
	}

	public void addListSelectionListener(ListSelectionListener parent)
	{
		jTable.getSelectionModel().addListSelectionListener(parent);
	}

	public JTable getTable()
	{
		return jTable;
	}

	void this_header_mousePressed(MouseEvent e)
	{
		JTableHeader jth = (JTableHeader )e.getSource();
		TableColumn tc = jth.getDraggedColumn();
		if (tc != null)
		{
			String str = (String )tc.getHeaderValue();

			int index = jTable.getColumnModel().getColumnIndex(tc.getIdentifier());
			index = jTable.convertColumnIndexToModel(index);
			sort_type = tableModel.getColumnByNumber(index);

			for (int i = 0; i < jTable.getColumnCount(); i++)
			{
				String sss = (String )jTable.getColumnModel().getColumn(i).getHeaderValue();
				if (jTable.getColumnModel().getColumn(i).equals(tc))
				{
					if (str.substring(0,1).equals(">"))
					{
						tc.setHeaderValue("< " + str.substring(2));
						sort_dir = 0;
						resortContents();
					}
					else if (str.substring(0,1).equals("<"))
					{
						tc.setHeaderValue("> " + str.substring(2));
						sort_dir = 1;
						resortContents();
					}
					else
					{
						sort_dir = 1;
						tc.setHeaderValue("> " + str);
						resortContents();
					}
				}
				else if (sss.substring(0,1).equals(">") || sss.substring(0,1).equals("<"))
				{
					jTable.getColumnModel().getColumn(i).setHeaderValue(sss.substring(2));
				}
			}
		}
	}
}
