/*
 * $Id: BooleanRenderer.java,v 1.1 2004/10/07 11:31:08 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/07 11:31:08 $
 * @author $Author: bob $
 * @module module
 */
public class BooleanRenderer extends JCheckBox implements TableCellRenderer {

	private static final long	serialVersionUID	= 5430388338276405309L;
	private static BooleanRenderer	instance;

	private BooleanRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public static BooleanRenderer getInstance() {
		if (instance == null){
			instance = new BooleanRenderer();
		}
		return instance;
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
