/*
 * $Id: CellEditorHelper.java,v 1.1 2004/10/07 11:31:08 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/07 11:31:08 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class CellEditorHelper extends AbstractCellEditor implements TableCellEditor {

	//
	//  Instance Variables
	//

	private static final long	serialVersionUID	= 8701520658533242067L;
	/** The Swing component being edited. */
	protected JComponent[][]	editorComponents;
	/**
	 * The delegate class which handles all methods sent from the
	 * <code>CellEditor</code>.
	 */
	protected EditorDelegate	delegate;
	/**
	 * An integer specifying the number of clicks needed to start editing.
	 * Even if <code>clickCountToStart</code> is defined as zero, it will
	 * not initiate until a click occurs.
	 */
	protected int			clickCountToStart	= 1;

	public CellEditorHelper(int rows, int columns) {
		this.editorComponents = new JComponent[rows][columns];
		setCellEditor(new JTextField(), 0, 0);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if ((i != 0) && (j != 0)) {
					
					this.editorComponents[i][j] = this.editorComponents[0][0];
				}
			}
		}
	}

	//
	//  Constructors
	//

	public void setCellEditor(final JTextField textField, final int row, final int column) {
		this.editorComponents[row][column] = textField;
		this.clickCountToStart = 2;
		this.delegate = new EditorDelegate() {

			private static final long	serialVersionUID	= -608421869632265997L;

			public void setValue(Object value) {
				textField.setText((value != null) ? value.toString() : "");
			}

			public Object getCellEditorValue() {
				return textField.getText();
			}
		};
		textField.addActionListener(this.delegate);
	}

	public void setCellEditor(final JCheckBox checkBox, final int row, final int column) {
		
		this.editorComponents[row][column] = checkBox;
		this.delegate = new EditorDelegate() {

			private static final long	serialVersionUID	= 4232887684263669900L;

			public void setValue(Object value) {
				boolean selected = false;
				if (value instanceof Boolean) {
					selected = ((Boolean) value).booleanValue();
				} else if (value instanceof String) {
					selected = value.equals("true");
				}
				checkBox.setSelected(selected);
			}

			public Object getCellEditorValue() {
				return Boolean.valueOf(checkBox.isSelected());
			}
		};
		checkBox.addActionListener(this.delegate);
	}

	/**
	 * Constructs a <code>DefaultCellEditor</code> object that uses a
	 * combo box.
	 * 
	 * @param comboBox
	 *                a <code>JComboBox</code> object
	 */
	public void setCellEditor(final JComboBox comboBox, final int row, final int column) {
		
		this.editorComponents[row][column] = comboBox;
		comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		this.delegate = new EditorDelegate() {

			private static final long	serialVersionUID	= 4979406160672187615L;

			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}

			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}

			public boolean shouldSelectCell(EventObject anEvent) {
				if (anEvent instanceof MouseEvent) {
					MouseEvent e = (MouseEvent) anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}

			public boolean stopCellEditing() {
				if (comboBox.isEditable()) {
					// Commit edited value.
					comboBox.actionPerformed(new ActionEvent(CellEditorHelper.this, 0, ""));
				}
				return super.stopCellEditing();
			}
		};
		comboBox.addActionListener(this.delegate);
	}

	/**
	 * Returns a reference to the editor component.
	 * 
	 * @return the editor <code>Component</code>
	 */
	public Component getComponent(final int row, final int column) {
		
		return this.editorComponents[row][column];
	}

	//
	//  Modifying
	//

	/**
	 * Specifies the number of clicks needed to start editing.
	 * 
	 * @param count
	 *                an int specifying the number of clicks needed to start
	 *                editing
	 * @see #getClickCountToStart
	 */
	public void setClickCountToStart(int count) {
		
		this.clickCountToStart = count;
	}

	/**
	 * Returns the number of clicks needed to start editing.
	 * 
	 * @return the number of clicks needed to start editing
	 */
	public int getClickCountToStart() {
		
		return this.clickCountToStart;
	}

	//
	//  Override the implementations of the superclass, forwarding all
	// methods
	//  from the CellEditor interface to our delegate.
	//

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#getCellEditorValue
	 */
	public Object getCellEditorValue() {
		
		return this.delegate.getCellEditorValue();
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#isCellEditable(EventObject)
	 */
	public boolean isCellEditable(EventObject anEvent) {
		
		return this.delegate.isCellEditable(anEvent);
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#shouldSelectCell(EventObject)
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		
		return this.delegate.shouldSelectCell(anEvent);
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#stopCellEditing
	 */
	public boolean stopCellEditing() {
		
		return this.delegate.stopCellEditing();
	}

	/**
	 * Forwards the message from the <code>CellEditor</code> to the
	 * <code>delegate</code>.
	 * 
	 * @see EditorDelegate#cancelCellEditing
	 */
	public void cancelCellEditing() {
		
		this.delegate.cancelCellEditing();
	}

	//
	//  Implementing the CellEditor Interface
	//
	/** Implements the <code>TableCellEditor</code> interface. */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.delegate.setValue(value);		
		return this.editorComponents[row][column];
	}

	//
	//  Protected EditorDelegate class
	//

	/**
	 * The protected <code>EditorDelegate</code> class.
	 */
	protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

		private static final long	serialVersionUID	= 8122206571291424884L;
		/** The value of this cell. */
		protected Object		value;

		/**
		 * Returns the value of this cell.
		 * 
		 * @return the value of this cell
		 */
		public Object getCellEditorValue() {
			return this.value;
		}

		/**
		 * Sets the value of this cell.
		 * 
		 * @param value
		 *                the new value of this cell
		 */
		public void setValue(Object value) {
			this.value = value;
		}

		/**
		 * Returns true if <code>anEvent</code> is <b>not </b> a
		 * <code>MouseEvent</code>. Otherwise, it returns true if the
		 * necessary number of clicks have occurred, and returns false
		 * otherwise.
		 * 
		 * @param anEvent
		 *                the event
		 * @return true if cell is ready for editing, false otherwise
		 * @see #setClickCountToStart
		 * @see #shouldSelectCell
		 */
		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent) { return ((MouseEvent) anEvent).getClickCount() >= CellEditorHelper.this.clickCountToStart; }
			return true;
		}

		/**
		 * Returns true to indicate that the editing cell may be
		 * selected.
		 * 
		 * @param anEvent
		 *                the event
		 * @return true
		 * @see #isCellEditable
		 */
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}

		/**
		 * Returns true to indicate that editing has begun.
		 * 
		 * @param anEvent
		 *                the event
		 */
		public boolean startCellEditing(EventObject anEvent) {
			return true;
		}

		/**
		 * Stops editing and returns true to indicate that editing has
		 * stopped. This method calls <code>fireEditingStopped</code>.
		 * 
		 * @return true
		 */
		public boolean stopCellEditing() {
			fireEditingStopped();
			return true;
		}

		/**
		 * Cancels editing. This method calls
		 * <code>fireEditingCanceled</code>.
		 */
		public void cancelCellEditing() {
			fireEditingCanceled();
		}

		/**
		 * When an action is performed, editing is ended.
		 * 
		 * @param e
		 *                the action event
		 * @see #stopCellEditing
		 */
		public void actionPerformed(ActionEvent e) {
			CellEditorHelper.this.stopCellEditing();
		}

		/**
		 * When an item's state changes, editing is ended.
		 * 
		 * @param e
		 *                the action event
		 * @see #stopCellEditing
		 */
		public void itemStateChanged(ItemEvent e) {
			CellEditorHelper.this.stopCellEditing();
		}
	}

} // End of class JCellEditor
