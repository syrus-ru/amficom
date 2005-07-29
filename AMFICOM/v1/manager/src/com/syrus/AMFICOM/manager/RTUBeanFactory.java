/*-
 * $Id: RTUBeanFactory.java,v 1.5 2005/07/29 12:12:33 bob Exp $
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

import static com.syrus.AMFICOM.manager.RTUBeanWrapper.*;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/29 12:12:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class RTUBeanFactory extends AbstractBeanFactory {
	
	private static RTUBeanFactory instance;
	
	private Validator validator;
	
	private PropertyChangeListener	listener;

	WrapperedPropertyTable	table;

	private JPanel	panel;
	
	private RTUBeanFactory() {
		super("Entity.RemoteTestUnit", 
			"Entity.RemoteTestUnit.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/rtu.gif", 
			"com/syrus/AMFICOM/manager/resources/rtu.png");
	}
	
	public static final RTUBeanFactory getInstance() {
		if(instance == null) {
			synchronized (RTUBeanFactory.class) {
				if(instance == null) {
					instance = new RTUBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {
		RTUBean bean = new RTUBean();
		bean.setName("RTU" + (++super.count));
		bean.setCodeName("RTU");
		bean.setValidator(this.getValidator());
		
		if (this.table == null) {
			final RTUBeanWrapper wrapper = RTUBeanWrapper.getInstance();
	
			this.table = 
				new WrapperedPropertyTable(wrapper, 
					bean, 
					new String[] { KEY_NAME, 
							KEY_DESCRIPTION, 
							KEY_MCM_ID,
							KEY_HOSTNAME,
							KEY_PORT}
				);
			this.table.setDefaultTableCellRenderer();			
	
			this.listener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					WrapperedPropertyTableModel model = (WrapperedPropertyTableModel)RTUBeanFactory.this.table.getModel();
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
		
		bean.table = this.table;
		bean.addPropertyChangeListener(this.listener);
		
		bean.setPropertyPanel(this.panel);
		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().equals("RTU") &&
						targetBean.getCodeName().equals("Net");
				}
			};
		}
		return this.validator;
	}
	
}
