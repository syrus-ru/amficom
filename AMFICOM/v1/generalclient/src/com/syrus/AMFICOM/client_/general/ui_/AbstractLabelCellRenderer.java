package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Abstract class for JLabel and simple Component (witch extends JLabel)
 * rendering at JTable
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/11 14:20:07 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public abstract class AbstractLabelCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Weight of color blending using alpha-channel
	 */
	public static final double	ALPHA		= 0.3;
	/**
	 * 1.0 - ALPHA constant. see {@link #ALPHA}
	 */
	public static final double	ONE_MINUS_ALPHA	= 1.0 - ALPHA;

	private Color			unselectedForeground;

	private Component		component;

	public AbstractLabelCellRenderer() {
		setOpaque(true);
	}

	/**
	 * abstract method to custom rendering objectResource with aid of
	 * objectResourceController
	 * 
	 * @param table
	 * @param object
	 * @param controller
	 *                see {@link ObjectResourceController}
	 * @param key
	 *                see {@link ObjectResourceController#getKeys()}
	 */
	protected abstract void customRendering(JTable table,
						Object object,
						ObjectResourceController controller,
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
		this.component = this;
		if (value instanceof String) {
			setText((value == null) ? "" : value.toString());
		} else if (value instanceof Component) {
			this.component = (Component) value;
		} else if (value instanceof Boolean){
			BooleanRenderer renderer = BooleanRenderer.getInstance();
			return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
		} else if (value instanceof Color){
			ColorCellRenderer renderer = ColorCellRenderer.getInstance();
			return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
		}

		Object obj = null;
		String colId = null;
		int mColIndex = table.convertColumnIndexToModel(vColIndex);
		super.setBackground(table.getBackground());
		
		Object tableModel = table.getModel();
		if (tableModel instanceof ObjectResourceTableModel){
			ObjectResourceTableModel model = (ObjectResourceTableModel)tableModel; 
			obj = model.getObject(rowIndex);
			colId = model.controller.getKey(mColIndex);
			customRendering(table, obj, model.controller, colId);
		} else if (tableModel instanceof ObjPropertyTableModel){
			ObjPropertyTableModel model = (ObjPropertyTableModel)tableModel; 
			obj = model.getObject();
			colId = model.controller.getKey(mColIndex);
			customRendering(table, obj, model.controller, colId);
		}



		Color color = super.getBackground();

		if (isSelected) {
			this.component.setForeground((this.unselectedForeground != null) ? this.unselectedForeground
					: table.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			this.component.setFont(font);
			Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			this.component.setBackground(new Color((int) (c.getRed() * ONE_MINUS_ALPHA + ALPHA
					* color.getRed()) % 256, (int) (c.getGreen() * ONE_MINUS_ALPHA + ALPHA
					* color.getGreen()) % 256, (int) (c.getBlue() * ONE_MINUS_ALPHA + ALPHA
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

}