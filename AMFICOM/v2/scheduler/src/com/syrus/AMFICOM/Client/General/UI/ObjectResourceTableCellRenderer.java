/*
 * LabelCellRenderer.java
 * Created on 02.08.2004 10:13:20
 * 
 */
package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * @author Vladimir Dolzhenko
 */
public abstract class ObjectResourceTableCellRenderer extends JLabel implements TableCellRenderer {

	public static final double	ALPHA			= 0.3;
	private static final double	ONE_MINUS_ALPHA	= 1.0 - ALPHA;

	//protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	//		 We need a place to store the color the JLabel should be returned
	// to after its foreground and background colors have been set
	// to the selection background color.
	// These ivars will be made protected when their names are finalized.
	private Color				unselectedForeground;

	//private Color unselectedBackground;

	/**
	 * Creates a default table cell renderer.
	 */
	public ObjectResourceTableCellRenderer() {
		setOpaque(true);
		//setBorder(noFocusBorder);
	}

	protected abstract void customRendering(JTable table,
											ObjectResource objectResource,
											boolean isSelected,
											boolean hasFocus,
											int rowIndex,
											int vColIndex);

	// This method is called each time a cell in a column
	// using this renderer needs to be rendered.
	public Component getTableCellRendererComponent(	JTable table,
													Object value,
													boolean isSelected,
													boolean hasFocus,
													int rowIndex,
													int vColIndex) {
		// 'value' is value contained in the cell located at
		// (rowIndex, vColIndex)
		//
		ObjResTableModel model = (ObjResTableModel) table.getModel();
		ObjectResourceTableRow line = (ObjectResourceTableRow) model.getRow(rowIndex);

		ObjectResource objectResource = line.getObjectResource();

		customRendering(table, objectResource, isSelected, hasFocus, rowIndex, vColIndex);
		Color color = super.getBackground();

		if (isSelected) {
			super
					.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
							.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			setFont(font);
			Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			super.setBackground(new Color((int) (c.getRed() * ONE_MINUS_ALPHA + ALPHA * color.getRed()) % 256, (int) (c
					.getGreen()
					* ONE_MINUS_ALPHA + ALPHA * color.getGreen()) % 256, (int) (c.getBlue() * ONE_MINUS_ALPHA + ALPHA
					* color.getBlue()) % 256));
		} else {
			super
					.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
							.getForeground());
			setFont(table.getFont());
			super.setBackground(color);
		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); //$NON-NLS-1$
			if (table.isCellEditable(rowIndex, vColIndex)) {
				super.setForeground(UIManager.getColor("Table.focusCellForeground")); //$NON-NLS-1$
				super.setBackground(UIManager.getColor("Table.focusCellBackground")); //$NON-NLS-1$
			}
		} else {
			//setBorder(noFocusBorder);
		}

		setValue(value);

		return this;
	}

	/**
	 * Overrides <code>JComponent.setBackground</code> to assign the
	 * unselected-background color to the specified color.
	 * 
	 * @param c
	 *            set the background color to this value
	 */
	public void setBackground(Color c) {
		super.setBackground(c);
		//unselectedBackground = c;
	}

	/**
	 * Overrides <code>JComponent.setForeground</code> to assign the
	 * unselected-foreground color to the specified color.
	 * 
	 * @param c
	 *            set the foreground color to this value
	 */
	public void setForeground(Color c) {
		super.setForeground(c);
		this.unselectedForeground = c;
	}

	protected void setValue(Object value) {
		setText((value == null) ? "" : value.toString()); //$NON-NLS-1$
	}	

}