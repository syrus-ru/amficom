/*-
* $Id: TabledBeanFactory.java,v 1.1 2005/08/01 11:32:03 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class TabledBeanFactory extends AbstractBeanFactory {

	protected Validator validator;
	
	protected PropertyChangeListener	listener;

	WrapperedPropertyTable	table;

	protected JPanel	panel;

	protected TabledBeanFactory(final String nameKey, 
	                              final String shortNameKey,
	                              final String iconUrl,
	                              final String imageUrl) {
		super(nameKey, shortNameKey, iconUrl, imageUrl);
	}

	
	protected final WrapperedPropertyTable getTable(final AbstractBean bean,
	                           final Wrapper wrapper,
	                           final String[] keys) {
		if (this.table == null) {
			this.table = 
				new WrapperedPropertyTable(wrapper, 
					bean, 
					keys
				);
			this.table.setDefaultTableCellRenderer();			
	
			this.listener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					WrapperedPropertyTableModel model = (WrapperedPropertyTableModel) TabledBeanFactory.this.table.getModel();
					model.fireTableDataChanged();
				}
			};
			
			
			this.panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			this.panel.add(this.table.getTableHeader(), gbc);		
			this.panel.add(new JScrollPane(this.table), gbc);
		}
		
		return this.table;
	}
}

