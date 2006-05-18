/*-
 * $$Id: LayersTableCellRenderer.java,v 1.5 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
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
			setSelected((value != null && val.booleanValue()));
			if(partial && val.booleanValue()) {
				this.setState(TristateCheckBox.DONT_CARE);
			}
		}
		else if(value instanceof Boolean) {
			setSelected((value != null && ((Boolean) value).booleanValue()));
		}

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		
		return this;
	}

}
