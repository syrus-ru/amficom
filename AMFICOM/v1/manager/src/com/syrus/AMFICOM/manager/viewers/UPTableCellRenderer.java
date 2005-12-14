/*-
* $Id: UPTableCellRenderer.java,v 1.1 2005/12/14 15:08:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 * @version $Revision: 1.1 $, $Date: 2005/12/14 15:08:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
class UPTableCellRenderer implements TableCellRenderer {		
		
	private final TableCellRenderer	tableCellRenderer;

	public UPTableCellRenderer(final TableCellRenderer tableCellRenderer) {
		this.tableCellRenderer = tableCellRenderer;
	}
		
	public Component getTableCellRendererComponent(	JTable table,
													Object value,
													boolean isSelected,
													boolean hasFocus,
													int row,
													int column) {
		final Component component = 
			this.tableCellRenderer.getTableCellRendererComponent(table, 
				value, 
				isSelected, 
				hasFocus,
				row, 
				column);
		final boolean cellEditable = table.getModel().isCellEditable(row, 1);
		component.setEnabled(cellEditable);
		return component;
	}
}
