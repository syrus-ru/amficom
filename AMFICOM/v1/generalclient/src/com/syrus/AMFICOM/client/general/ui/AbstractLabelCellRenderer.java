package com.syrus.AMFICOM.client.general.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public abstract class AbstractLabelCellRenderer extends JLabel implements TableCellRenderer {

	public static final double	ALPHA		= 0.3;
	public static final double	ONE_MINUS_ALPHA	= 1.0 - ALPHA;

	private Color			unselectedForeground;

	private Component		component;

	public AbstractLabelCellRenderer() {
		setOpaque(true);
	}

	protected abstract void customRendering(JTable table, ObjectResource objectResource, String key);

	// This method is called each time a cell in a column
	// using this renderer needs to be rendered.
	public Component getTableCellRendererComponent(	JTable table,
							Object value,
							boolean isSelected,
							boolean hasFocus,
							int rowIndex,
							int vColIndex) {
		this.component = this;
		if (value instanceof String) {
			setValue(value);
		} else if (value instanceof Component) {
			this.component = (Component) value;
		}

		ObjectResourceTableModel model = (ObjectResourceTableModel) table.getModel();

		ObjectResource objectResource = model.getObjectResource(rowIndex);

		int mColIndex = table.convertColumnIndexToModel(vColIndex);
		String colId = model.controller.getKey(mColIndex);

		super.setBackground(table.getBackground());
		customRendering(table, objectResource, colId);
		Color color = super.getBackground();

		if (isSelected) {
			this.component.setForeground((this.unselectedForeground != null) ? this.unselectedForeground
					: table.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			this.component.setFont(font);
			Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			this.component.setBackground(new Color(
								(int) (c.getRed() * ONE_MINUS_ALPHA + ALPHA
										* color.getRed()) % 256, (int) (c
										.getGreen()
										* ONE_MINUS_ALPHA + ALPHA
										* color.getGreen()) % 256, (int) (c
										.getBlue()
										* ONE_MINUS_ALPHA + ALPHA
										* color.getBlue()) % 256));

		} else {
			this.component.setForeground((this.unselectedForeground != null) ? this.unselectedForeground
					: table.getForeground());
			this.component.setFont(table.getFont());
			this.component.setBackground(color);
		}

		if (hasFocus) {
			//setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			// //$NON-NLS-1$
			if (table.isCellEditable(rowIndex, vColIndex)) {
				this.component.setForeground(UIManager.getColor("Table.focusCellForeground")); //$NON-NLS-1$
				//component.setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		} else {
			//setBorder(noFocusBorder);
		}

		return this.component;
	}

	public void setBackground(Color c) {
		super.setBackground(c);
	}

	public void setForeground(Color c) {
		super.setForeground(c);
		this.unselectedForeground = c;
	}

	protected void setValue(Object value) {
		setText((value == null) ? "" : value.toString()); //$NON-NLS-1$
	}

}