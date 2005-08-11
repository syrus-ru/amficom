/*
 * $Id: LayersTableCellEditor.java,v 1.4 2005/08/11 12:43:32 arseniy Exp $ Copyright � 2005 Syrus Systems. Dept. of Science & Technology. Project:
 * AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/11 12:43:32 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class LayersTableCellEditor extends DefaultCellEditor {

	private static LayersTableCellEditor instance;

	public LayersTableCellEditor() {
		super(new JCheckBox());
		super.editorComponent.setOpaque(true);
		super.editorComponent.setBorder(
				UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
		((JCheckBox )super.editorComponent).setHorizontalAlignment(SwingConstants.CENTER);
	}

	public static synchronized LayersTableCellEditor getInstance() {
		if(instance == null)
			instance = new LayersTableCellEditor();
		return instance;
	}

	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int rowIndex,
			int vColIndex) {
		if(value instanceof LayerVisibility) {
			LayerVisibility lv = (LayerVisibility) value;
			if(vColIndex == 0) 
				value = lv.getVisible();
			else if(vColIndex == 2) 
				value = lv.getLabelVisible();
		}
		if(!(value instanceof Boolean))
			return null;

		if (isSelected) {
			super.editorComponent.setForeground(table.getSelectionForeground());
			super.editorComponent.setBackground(table.getSelectionBackground());
		} else {
			super.editorComponent.setForeground(table.getForeground());
			super.editorComponent.setBackground(table.getBackground());
		}
		return super.getTableCellEditorComponent(
				table,
				value,
				isSelected,
				rowIndex,
				vColIndex);
	}
}
