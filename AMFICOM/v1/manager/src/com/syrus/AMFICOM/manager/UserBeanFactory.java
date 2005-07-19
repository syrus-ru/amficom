/*-
* $Id: UserBeanFactory.java,v 1.6 2005/07/19 09:49:00 bob Exp $
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;



/**
 * @version $Revision: 1.6 $, $Date: 2005/07/19 09:49:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends AbstractBeanFactory {

	private static UserBeanFactory instance;
	
	private Validator validator;
	
	private List<String> names;

	private PropertyChangeListener	listener;

	WrapperedPropertyTable	table;

	private JPanel	panel;
	
	private UserBeanFactory() {
		super("Entity.User", 
			"Entity.User", 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
		
		this.names = new ArrayList<String>();
		this.names.add(LangModelManager.getString("Entity.User.Subscriber"));
		this.names.add(null);
		this.names.add(LangModelManager.getString("Entity.User.SystemAdministator"));
		this.names.add(LangModelManager.getString("Entity.User.MediaMonitoringAdministator"));
		this.names.add(LangModelManager.getString("Entity.User.Analyst"));
		this.names.add(LangModelManager.getString("Entity.User.Operator"));
		this.names.add(null);
		this.names.add(LangModelManager.getString("Entity.User.Planner"));
		this.names.add(LangModelManager.getString("Entity.User.Specialist"));
		this.names.add(null);
	}
	
	public static final UserBeanFactory getInstance() {
		if (instance == null) {
			synchronized (UserBeanFactory.class) {
				if (instance == null) {
					instance = new UserBeanFactory();
				}				
			}
		}
		return instance;
	}

	
	@Override
	public AbstractBean createBean() {
		UserBean bean = new UserBean(this.names);
		bean.setName("User" + (++super.count));
		bean.setCodeName("User");
		bean.setValidator(this.getValidator());
		
		if (this.table == null) {
			final UserBeanWrapper wrapper = UserBeanWrapper.getInstance();
	
			this.table = 
				new WrapperedPropertyTable(wrapper, 
					bean, 
					new String[] { UserBeanWrapper.KEY_FULL_NAME, 
						UserBeanWrapper.KEY_USER_NATURE}
				);
			this.table.setDefaultTableCellRenderer();			
	
			this.listener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					WrapperedPropertyTableModel model = (WrapperedPropertyTableModel)UserBeanFactory.this.table.getModel();
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

			System.out.println("UserBeanFactory.createBean() | create table ");
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
					System.out.println("UserBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getName() 
						+ " -> " 
						+ targetBean.getName());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().equals("User") &&
						targetBean.getCodeName().equals("ARM");
				}
			};
		}
		return this.validator;
	}
	
}

