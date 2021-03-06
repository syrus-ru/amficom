/*-
* $Id: TableBeanUI.java,v 1.9 2005/12/22 14:23:30 bob Exp $
*
* Copyright ? 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;

import com.syrus.AMFICOM.client.UI.StubLabelCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.Bean;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.9 $, $Date: 2005/12/22 14:23:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class TableBeanUI<T extends Bean> extends AbstractBeanUI<T> {

	protected WrapperedPropertyTableModel<T> model; 
	protected WrapperedPropertyTable<T>	table;

	protected JPanel panel;		
	protected PropertyChangeListener	listener;
	protected T	bean;
	
	private boolean editable;
	
	public static final String ENTER_ICON = 
		"com.syrus.AMFICOM.manager.resources.action.enter";
	
	public TableBeanUI(final ManagerMainFrame managerMainFrame,
			final Wrapper<T> wrapper,
			final String[] keys,
			final String resourceKeySuffix) {
		super(managerMainFrame, resourceKeySuffix);
		this.createTable(wrapper, keys);
	}

	@Override
	public JPanel getPropertyPanel(final T bean) {
//		final long time0 = System.currentTimeMillis();
		if (this.bean != bean) {
			this.bean = bean;
			this.editable = bean.isEditable();
			this.model.setObject(bean);			
		}
		bean.addPropertyChangeListener(this.listener);
//		final long time1 = System.currentTimeMillis();
//		assert Log.debugMessage("It takes " + (time1 - time0) + " ms for " + bean, Log.DEBUGLEVEL03);
		return this.panel;
	}
	
	@Override
	public void disposePropertyPanel() {
//		assert Log.debugMessage(this.bean, Log.DEBUGLEVEL03);
		this.bean.removePropertyChangeListener(this.listener);
	}
	
	private final void createTable(final Wrapper<T> wrapper,
	                               final String[] keys) {
 		if (this.table == null) { 			
 			this.model = new WrapperedPropertyTableModel<T>(wrapper, null, keys) {

 				@Override
 				public boolean isCellEditable(	int rowIndex,
 												int columnIndex) {
 					return editable;
 				}
 			};
 			
 			this.table = new WrapperedPropertyTable<T>(this.model);
 			
 			this.table.setDefaultTableCellRenderer(new UPTableCellRenderer(StubLabelCellRenderer.getInstance()));
 	
 			this.listener = new PropertyChangeListener() {
 				public void propertyChange(final PropertyChangeEvent evt) {
 					final int rowIndex = model.getRowIndex(evt.getPropertyName());
 					model.fireTableRowsUpdated(rowIndex, rowIndex);
 				}
 			};			
 			
 			this.panel = new JPanel(new GridBagLayout());
 			GridBagConstraints gbc = new GridBagConstraints();
 			gbc.fill = GridBagConstraints.BOTH;
 			gbc.gridwidth = GridBagConstraints.REMAINDER;
 			gbc.weightx = 1.0;
 			gbc.weighty = 0.0;
 			
 			gbc.gridheight = GridBagConstraints.RELATIVE;
 			final JTableHeader tableHeader = table.getTableHeader();
 			this.panel.add(tableHeader, gbc);
 			gbc.weighty = 1.0;
 			gbc.gridheight = GridBagConstraints.REMAINDER;

 			this.panel.add(new JScrollPane(this.table), gbc);
		
 		}
 	}
	
}

