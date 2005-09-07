
package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Wrapper;

/**
 * Abstract class for JLabel and simple Component (witch extends JLabel)
 * rendering at JTable
 * 
 * @version $Revision: 1.4 $, $Date: 2005/09/07 02:37:31 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public abstract class AbstractLabelCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Weight of color blending using alpha-channel
	 */
	public static final double	ALPHA			= 0.3;
	/**
	 * 1.0 - ALPHA constant. see {@link #ALPHA}
	 */
	public static final double ONE_MINUS_ALPHA = 1.0 - ALPHA;

	protected Map<Class, TableCellRenderer> renderers;

	private Color unselectedForeground;

	public AbstractLabelCellRenderer() {
		super.setOpaque(true);
		super.setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
		this.renderers = new HashMap<Class, TableCellRenderer>();
		this.renderers.put(Color.class, ColorCellRenderer.getInstance());
		this.renderers.put(Boolean.class, BooleanRenderer.getInstance());
	}

	/**
	 * add custom renderer for object of Clazz clazz
	 * 
	 * @param clazz
	 * @param cellRenderer
	 */
	public void addCustomRenderer(final Class clazz, final TableCellRenderer cellRenderer) {
		this.renderers.put(clazz, cellRenderer);
	}

	/**
	 * This method is called each time a cell in a column using this renderer
	 * needs to be rendered.
	 */
	public Component getTableCellRendererComponent(	final JTable table,
			final Object value,
			final boolean isSelected,
			final boolean hasFocus,
			final int rowIndex,
			final int vColIndex) {
		super.setBackground(table.getBackground());
		if (value instanceof JLabel) {
			final JLabel label = ((JLabel) value);
			this.setText(label.getText());
			this.setBackground(label.getBackground());
		} else {
			if (value != null) {
				final TableCellRenderer cellRenderer = this.renderers.get(value.getClass());
				if (cellRenderer != null) {
					return cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
				}
			}
			this.setText((value == null) ? "" : value.toString());
		}

		Object obj = null;
		String colId = null;
		final int mColIndex = table.convertColumnIndexToModel(vColIndex);
		

		final Object tableModel = table.getModel();
		if (tableModel instanceof WrapperedTableModel) {
			final WrapperedTableModel<?> model = (WrapperedTableModel) tableModel;
			obj = model.getObject(rowIndex);
			colId = model.keys[mColIndex];
			customRendering(table, obj, model.wrapper, colId);
		} else if (tableModel instanceof WrapperedPropertyTableModel) {
			final WrapperedPropertyTableModel<?> model = (WrapperedPropertyTableModel) tableModel;
			obj = model.getObject();
			if (mColIndex == 1) {// 'property' field
				colId = model.keys[rowIndex];
				customRendering(table, obj, model.wrapper, colId);
			}
		}

//		Color color = super.getBackground();
		final Color color = this.getBackground();

		if (isSelected) {
			this.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			this.setFont(font);
			final Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			this.setBackground(new Color((int) (c.getRed() * ONE_MINUS_ALPHA + ALPHA * color.getRed()) % 256,
					(int) (c.getGreen() * ONE_MINUS_ALPHA + ALPHA * color.getGreen()) % 256,
					(int) (c.getBlue() * ONE_MINUS_ALPHA + ALPHA * color.getBlue()) % 256));
		} else {
			this.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table.getForeground());
			this.setFont(table.getFont());
			this.setBackground(color);
		}

		if (hasFocus) {
			super.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			// //$NON-NLS-1$
			if (table.isCellEditable(rowIndex, vColIndex)) {
				this.setForeground(UIManager.getColor("Table.focusCellForeground")); //$NON-NLS-1$
				// setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		} else {
			super.setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
		}

		return this;
	}

	@Override
	public void setBackground(final Color c) {
		super.setBackground(c);
	}

	@Override
	public void setForeground(final Color c) {
		super.setForeground(c);
		this.unselectedForeground = c;
	}
	
	/**
	 * abstract method to custom rendering objectResource with aid of
	 * objectResourceController
	 * 
	 * @param table
	 * @param object
	 * @param controller
	 *            see {@link Wrapper}
	 * @param key
	 *            see {@link Wrapper#getKeys()}
	 */
	protected abstract void customRendering(final JTable table, final Object object, final Wrapper controller, final String key);

}
