package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTableModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class ObjectResourceTableEditor
		extends DefaultCellEditor
		implements TableCellEditor, CellEditorListener
{
	String edited_id;
	PropertyEditor editor;
	ObjectResourceTableModel tableModel;

	public ObjectResourceTableEditor()
	{
		super(new JTextField());
	}

	public ObjectResourceTableEditor(ObjectResourceTableModel tableModel)
	{
		super(new JTextField());
		this.tableModel = tableModel;
		this.addCellEditorListener(this);
	}

	public void setModel(ObjectResourceTableModel mytableModel)
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
		ObjectResource or = (ObjectResource )value;
		ObjectResourceModel mod = or.getModel();

		column = table.convertColumnIndexToModel(column);
		
		String col_id = tableModel.getColumnByNumber(column);
		String text = mod.getColumnValue(col_id);

		editor = tableModel.getDisplayModel().getColumnEditor(or, col_id);
		if(editor == null)
			editor = new TextFieldEditor(text);
/*			
			component = super.getTableCellEditorComponent(
					table,
					text,
					isSelected,
					row,
					column);
*/					

//		System.out.println("editor for " + text);

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
