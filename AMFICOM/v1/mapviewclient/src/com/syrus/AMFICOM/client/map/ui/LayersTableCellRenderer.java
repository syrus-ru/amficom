/**
 * $Id: LayersTableCellRenderer.java,v 1.1 2005/05/25 16:15:58 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.table.TableCellRenderer;

import com.syrus.AMFICOM.client_.general.ui_.ADefaultTableCellRenderer;

public class LayersTableCellRenderer extends ADefaultTableCellRenderer {

	private static LayersTableCellRenderer	instance;

	public LayersTableCellRenderer() {
		super();
		ADefaultTableCellRenderer.renderers.put(
				Boolean.class, 
				new UIDefaults.ProxyLazyValue(LayersTableCellRenderer.BooleanRenderer.class.getName()));
	}

	public static synchronized ADefaultTableCellRenderer getInstance() {
		if (instance == null)
			instance = new LayersTableCellRenderer();
		return instance;
	}

	public static class BooleanRenderer extends TristateCheckBox implements TableCellRenderer {

		public BooleanRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected,
														boolean hasFocus,
														int row,
														int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean) value).booleanValue()));
			return this;
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		Component component = null;
		if(value instanceof LayerVisibility) {
			LayerVisibility lv = (LayerVisibility) value;
			boolean partial = false;
			Boolean val = null;
			if(vColIndex == 0) {
				val = ((LayerVisibility)value).getVisible();
				partial = lv.isPartial();
			}
			else if(vColIndex == 2) {
				val = ((LayerVisibility)value).getLabelVisible();
				partial = lv.isLabelPartial(); 
			}
			component = super.getTableCellRendererComponent(
					table,
					val,
					isSelected,
					hasFocus,
					rowIndex,
					vColIndex);
			
			if(partial && val.booleanValue()) {
				TristateCheckBox checkBox = (TristateCheckBox ) component;
				checkBox.setState(TristateCheckBox.DONT_CARE);
			}
		}
		else
			component = super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				rowIndex,
				vColIndex);
		return component;
	}

}
