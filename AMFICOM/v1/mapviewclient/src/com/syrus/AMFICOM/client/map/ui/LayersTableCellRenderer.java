/**
 * $Id: LayersTableCellRenderer.java,v 1.2 2005/05/27 15:14:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class LayersTableCellRenderer extends TristateCheckBox implements TableCellRenderer {

	private static LayersTableCellRenderer	instance;

	public LayersTableCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	public static synchronized LayersTableCellRenderer getInstance() {
		if (instance == null)
			instance = new LayersTableCellRenderer();
		return instance;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

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
		if(partial && val.booleanValue()) {
			this.setState(TristateCheckBox.DONT_CARE);
		}

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
