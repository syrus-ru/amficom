/*-
* $Id: PermissionBeanFactory.java,v 1.2 2005/10/13 15:25:36 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.2 $, $Date: 2005/10/13 15:25:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class PermissionBeanFactory extends AbstractBeanFactory {

	private Validator validator;
	
	private PropertyChangeListener	listener;
	
	private static Map<Module, PermissionBeanFactory> instanceMap;

	private final Module module;
	
	private JTable	table;

	private JPanel	panel;
	
	private PermissionBeanFactory(final ManagerMainFrame graphText, 
	                              final Module module) {
		super("com/syrus/AMFICOM/manager/resources/icons/" + module.getCodename() +".gif", 
			"com/syrus/AMFICOM/manager/resources/" + module.getCodename() +".gif");
		super.graphText = graphText;
		this.module = module;
	}
	
	@Override
	public String getShortName() {
		return this.module.getDescription();
	}
	
	@Override
	public String getName() {
		return this.module.getDescription();
	}
	
	public static final synchronized PermissionBeanFactory getInstance(final ManagerMainFrame graphText,
			final Module module) {
		if (instanceMap == null) {
			instanceMap = new HashMap<Module, PermissionBeanFactory>();				
		}
		
		PermissionBeanFactory factory = instanceMap.get(module);
		if (factory == null) {
			factory = new PermissionBeanFactory(graphText, module);
			instanceMap.put(module, factory);
		}
		return factory;
	}

	
	@Override
	public AbstractBean createBean(final Perspective perspective) 
	throws CreateObjectException, IllegalObjectEntityException {
		
		SystemUserPerpective userPerpective = (SystemUserPerpective) perspective;
		
		final PermissionAttributes permissionAttributes = 
			PermissionAttributes.createInstance(LoginManager.getUserId(),
				userPerpective.getDomainId(),
				userPerpective.getUserId(),
				this.module);
		
		return this.createBean(permissionAttributes.getId());

	}
	
	@Override
	public AbstractBean createBean(final String codename) {
		return this.createBean(new Identifier(codename));
	}
	
	protected AbstractBean createBean(final Identifier identifier) {
		
		try {
			final PermissionAttributes permissionAttributes = 
				StorableObjectPool.getStorableObject(identifier, true);
			final Module module2 = permissionAttributes.getModule();
			if (module2 != this.module) {
				final PermissionBeanFactory factory = 
					instanceMap.get(module2);
				return factory.createBean(identifier);
			}			
		} catch (final ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		final PermissionBean bean = new PermissionBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());		

		bean.setId(identifier);	
		
		bean.table = this.getTable(bean);
		
		bean.addPropertyChangeListener(this.listener);
		
		bean.setPropertyPanel(this.panel);		
		
		super.graphText.getDispatcher().firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.PERMATTR, null, bean));

		
		return bean;
	}
	
	
	private final JTable getTable(final PermissionBean bean) {
 		if (this.table == null) {
 			final PermissionTableModel model = new PermissionTableModel(bean);
 			
 			
 			final String[] columnToolTips = new String[] {
 					I18N.getString("Manager.Entity.PermissionAttibute.ActionTooltip"),
 					I18N.getString("Manager.Entity.PermissionAttibute.DenyTooltip"),
 					I18N.getString("Manager.Entity.PermissionAttibute.PermitTooltip"),
 					I18N.getString("Manager.Entity.PermissionAttibute.RoleTooltip")	
 			};

 			
 			this.table = 
 				new JTable(model) {
 				@Override
 				protected JTableHeader createDefaultTableHeader() {
 					return new JTableHeader(this.columnModel) {
 			            @Override
						public String getToolTipText(MouseEvent e) {
 			                Point p = e.getPoint();
 			                int index = this.columnModel.getColumnIndexAtX(p.x);
 			                int realIndex = 
 			                        this.columnModel.getColumn(index).getModelIndex();
 			                return columnToolTips[realIndex];
 			            }
 			        };
 				}
 			};
 	
 			this.listener = new PropertyChangeListener() {
 				public void propertyChange(PropertyChangeEvent evt) {
// 					final int rowIndex = model.getRowIndex(evt.getPropertyName());
// 					model.fireTableRowsUpdated(rowIndex, rowIndex);
 				}
 			};
 			 
 			final int[] columnsWidth = new int[model.getColumnCount()];
 			final JTableHeader tableHeader = this.table.getTableHeader();
 			{ 				
 				final TableCellRenderer headerRenderer = tableHeader.getDefaultRenderer();
 				final TableColumnModel columnModel = tableHeader.getColumnModel();
 				for (int i = 1; i < columnModel.getColumnCount(); i++) {
 					final Component component = headerRenderer.getTableCellRendererComponent(null, columnModel.getColumn(i)
 							.getHeaderValue(), false, false, 0, 0);
 					columnsWidth[i] = component.getPreferredSize().width;
 				}
 			}
 			for (int i = 0; i < model.getRowCount(); i++) {
 				for (int j = 1; j < columnsWidth.length; j++) {
 					final Rectangle cellRect = this.table.getCellRect(i, j, true);
 					int width = cellRect.width - cellRect.x;
 					if (width > columnsWidth[j])
 						columnsWidth[j] = width;
 				}
 			}

 			for (int j = 1; j < columnsWidth.length; j++) {
 				this.table.getColumnModel().getColumn(j).setMaxWidth(columnsWidth[j]);
 				this.table.getColumnModel().getColumn(j).setPreferredWidth(columnsWidth[j]);
 			}
 			
 			this.panel = new JPanel(new GridBagLayout());
 			GridBagConstraints gbc = new GridBagConstraints();
 			gbc.fill = GridBagConstraints.BOTH;
 			gbc.gridwidth = GridBagConstraints.REMAINDER;
 			gbc.weightx = 1.0;
 			gbc.weighty = 1.0;
 			this.panel.add(tableHeader, gbc);		
 			this.panel.add(new JScrollPane(this.table), gbc);
 		}
 		
 		return this.table;
 	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.PERMATTR;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("PermissionBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getName() 
						+ " -> " 
						+ targetBean.getName());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ObjectEntities.PERMATTR) &&
						targetBean.getCodeName().startsWith(ObjectEntities.SYSTEMUSER);
				}
			};
		}
		return this.validator;
	}
	
	private class PermissionTableModel extends AbstractTableModel {


		private final PermissionBean	permissionBean;
		private PermissionAttributes	permissionAttributes;
		private List<String>	keys;
		private PermissionBeanWrapper	permissionBeanWrapper;

		public PermissionTableModel(final PermissionBean permissionBean) {
			this.permissionBean = permissionBean;
			
			this.permissionAttributes = this.permissionBean.getPermissionAttributes();
			this.permissionBeanWrapper = PermissionBeanWrapper.getInstance(this.permissionAttributes.getModule());
			this.keys = this.permissionBeanWrapper.getKeys();
		}
		
		@Override
		public Class< ? > getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return String.class;
			}
			return Boolean.class;
		}
		
		public int getColumnCount() {
			return 4;
		}
		
		public int getRowCount() {
			return this.keys.size();
		}
		
		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 0:
					return I18N.getString("Manager.Entity.PermissionAttibute.Action");
			case 1:
					return I18N.getString("Manager.Entity.PermissionAttibute.Deny");
			case 2:
					return I18N.getString("Manager.Entity.PermissionAttibute.Permit");
			case 3:
					return I18N.getString("Manager.Entity.PermissionAttibute.Role");
			}
			return null;
		}
		
		public Object getValueAt(	int rowIndex,
									int columnIndex) {
			if (columnIndex == 0) {
				return this.permissionBeanWrapper.getName(this.keys.get(rowIndex));
			}
			
			PermissionCodename permissionCode = PermissionCodename.valueOf(this.keys.get(rowIndex));
			switch(columnIndex) {
			case 1:
				return Boolean.valueOf(this.permissionAttributes.isDenied(permissionCode));
			case 2:
				return Boolean.valueOf(this.permissionAttributes.isPermissionEnable(permissionCode));
			case 3:
				try {
						return this.permissionBean.getRolePermissions().get(permissionCode);
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			return null;
		}

		@Override
		public boolean isCellEditable(	int rowIndex,
										int columnIndex) {
			return columnIndex != 0 && columnIndex != 3;
		}		

		@Override
		public void setValueAt(	Object aValue,
								int rowIndex,
								int columnIndex) {
			PermissionCodename permissionCode = PermissionCodename.valueOf(this.keys.get(rowIndex));
			boolean b = ((Boolean)aValue).booleanValue();
			switch(columnIndex) {
			case 1:
				this.permissionAttributes.setDenidEnable(permissionCode, b);
				if (b) {
					this.permissionAttributes.setPermissionEnable(permissionCode, !b);
				}
				break;
			case 2:
				this.permissionAttributes.setPermissionEnable(permissionCode, b);
				if (b) {
					this.permissionAttributes.setDenidEnable(permissionCode, !b);					
				}
				break;
			default:
				break;
			}
			this.fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}
	
}

