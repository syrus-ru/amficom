package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTableModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class ObjectResourcePropertyTableEditor
		extends AbstractCellEditor
		implements TableCellEditor, CellEditorListener
{
	String edited_id;
	PropertyEditor editor;
	ObjectResourcePropertyTableModel tableModel;

	public ObjectResourcePropertyTableEditor()
	{
		super();
	}
	
	public ObjectResourcePropertyTableEditor(ObjectResourcePropertyTableModel mytableModel)
	{
		super();
		this.tableModel = mytableModel;
		this.addCellEditorListener(this);
	}

	public void setModel(ObjectResourcePropertyTableModel mytableModel)
	{
		this.tableModel = mytableModel;
	}

    public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column)
	{
		String text = "";
		edited_id = (String)value;
		column = table.convertColumnIndexToModel(column);
		if(column == 0)
		{
			text = tableModel.getDisplayModel().getColumnName((String)value);
//			System.out.println("edit " + (String )value + " as " + text);
			editor = new TextFieldEditor(text);
		}
		else
		{
			editor = tableModel.getDisplayModel().getColumnEditor(null, (String)value);
//			System.out.println("edit " + (String )value + " with " + editor);
			if(editor == null)
			{
				text = tableModel.getContents().getModel().getPropertyValue((String)value);
				editor = new TextFieldEditor(text);
			}
		}

		try 
		{
			JComboBox jcb = (JComboBox )editor;
			jcb.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fireEditingStopped();
					}
				});
		} catch (Exception ex) 
		{
//			ex.printStackTrace();
		} finally 
		{
		}
		
		return (Component)editor;
	}

	public Object getCellEditorValue()
	{
		return editor.getSelected();
	}

	public void editingStopped(ChangeEvent e)
	{
	}

	public void editingCanceled(ChangeEvent e)
	{
	}
}
