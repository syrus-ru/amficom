package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.syrus.util.Wrapper;

/**
 * Abstract class for JComboBox rendering at JTable
 * 
 * @version $Revision: 1.2 $, $Date: 2005/05/30 15:45:42 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public abstract class AbstractComboBoxCellRenderer extends JComboBox implements TableCellRenderer {

	private Color	unselectedForeground;

	/**
	 * abstract method to custom rendering objectResource with aid of
	 * objectResourceController
	 * 
	 * @param table
	 * @param object 
	 * @param objectResourceController
	 *                see {@link Wrapper}
	 * @param key
	 *                see {@link Wrapper#getKeys()}
	 */
	protected abstract void customRendering(JTable table,
						Object object,
						Wrapper objectResourceController,
						String key);

	/**
	 * This method is called each time a cell in a column using this
	 * renderer needs to be rendered.
	 */
	public Component getTableCellRendererComponent(	JTable table,
							Object value,
							boolean isSelected,
							boolean hasFocus,
							int rowIndex,
							int vColIndex) {
		// 'value' is value contained in the cell located at
		// (rowIndex, vColIndex)
		//
		WrapperedTableModel model = (WrapperedTableModel) table.getModel();
		Object object = model.getObject(rowIndex);

		int mColIndex = table.convertColumnIndexToModel(vColIndex);
		String key = model.keys[mColIndex];

		super.setBackground(table.getBackground());
		customRendering(table, object, model.wrapper, key);
		Color color = super.getBackground();

		if (isSelected) {
			super.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
					.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			setFont(font);
			Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			super
					.setBackground(new Color(
									(int) (c.getRed()
											* AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
											* color.getRed()) % 256,
									(int) (c.getGreen()
											* AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
											* color.getGreen()) % 256,
									(int) (c.getBlue()
											* AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
											* color.getBlue()) % 256));
		} else {
			super.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
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

		System.out.println(getSelectedItem());

		return this;
	}

	/**
	 * Overrides <code>JComponent.setBackground</code> to assign the
	 * unselected-background color to the specified color.
	 * 
	 * @param c
	 *                set the background color to this value
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
	 *                set the foreground color to this value
	 */
	public void setForeground(Color c) {
		super.setForeground(c);
		this.unselectedForeground = c;
	}

}
