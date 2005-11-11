/*-
* $Id: TableBeanUI.java,v 1.3 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.manager.Bean;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/11 10:58:02 $
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
	
	public static final String ENTER_ICON = "Manager.EnterIcon";
	
	{
		final URL resource = TableBeanUI.class.getClassLoader().getResource("com/syrus/AMFICOM/manager/resources/icons/enter.gif");
		if (resource != null) {
			UIManager.put(ENTER_ICON, new ImageIcon(resource));
		}
	}

	public TableBeanUI(final ManagerMainFrame managerMainFrame,
			final Wrapper<T> wrapper,
			final String[] keys,
			final String iconUrl,
			final String imageUrl) {
		super(managerMainFrame, iconUrl, imageUrl);
		this.createTable(wrapper, keys);
	}

	public JPanel getPropertyPanel(final T bean) {
		this.bean = bean;
		this.model.setObject(bean);
		bean.addPropertyChangeListener(this.listener);
		return this.panel;
	}
	
	public void disposePropertyPanel() {
		this.bean.removePropertyChangeListener(this.listener);
	}
	
	private final void createTable(final Wrapper<T> wrapper,
	                               final String[] keys) {
 		if (this.table == null) { 			
 			this.model = new WrapperedPropertyTableModel<T>(wrapper, null, keys);
 			
 			this.table = new WrapperedPropertyTable<T>(this.model);
// 			this.table.setDefaultTableCellRenderer();			
 			this.table.setTableHeader(null);
 	
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
 			gbc.weighty = 1.0;
// 			gbc.gridheight = GridBagConstraints.RELATIVE;
// 			this.panel.add(this.table.getTableHeader(), gbc);
 			gbc.gridheight = GridBagConstraints.REMAINDER;
 			this.panel.add(new JScrollPane(this.table), gbc);
		
 		}
 	}
	
}

