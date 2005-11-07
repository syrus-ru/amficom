/*-
* $Id: UserPermissionBeanUI.java,v 1.1 2005/11/07 15:21:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import com.syrus.AMFICOM.manager.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.PermissionBean;
import com.syrus.AMFICOM.manager.PermissionBeanFactory;
import com.syrus.AMFICOM.manager.PermissionBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/07 15:21:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserPermissionBeanUI extends AbstractBeanUI<PermissionBean> {
	
	private JTable	table;

	private JPanel	panel;
	
	private PropertyChangeListener	listener;	
	
	private PermissionTableModel model;

	private PermissionBean	bean;
	
	private Map<Module, Icon> iconCache;
	private Map<Module, Icon> imageCache;

	public UserPermissionBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame);
		this.createTable();
	}	
	
	private final void createTable() {
 		if (this.table == null) {
 			this.model = new PermissionTableModel();	
 			
 			final String[] columnToolTips = new String[] {
 					I18N.getString("Manager.Entity.PermissionAttibute.ActionTooltip"),
 					I18N.getString("Manager.Entity.PermissionAttibute.PermissionTooltip")
 			};
 			
 			this.table = 
 				new JTable(this.model) {
 				
 			    @Override
				public String getToolTipText(final MouseEvent e) {
 			    	final Point p = e.getPoint();
 			    	final int rowIndex = rowAtPoint(p);
 			        
 			        return this.getModel().getValueAt(rowIndex, 0).toString();
 			    }
 				
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
 					assert Log.debugMessage(evt.getPropertyName(), Log.DEBUGLEVEL09);
// 					final int rowIndex = model.getRowIndex(evt.getPropertyName());
// 					model.fireTableRowsUpdated(rowIndex, rowIndex);
 					model.fireTableDataChanged();
 				}
 			};
 			 
 			final JTableHeader tableHeader = this.table.getTableHeader();
 			
 			this.panel = new JPanel(new GridBagLayout());
 			GridBagConstraints gbc = new GridBagConstraints();
 			gbc.fill = GridBagConstraints.BOTH;
 			gbc.gridwidth = GridBagConstraints.REMAINDER;
 			gbc.weightx = 1.0;
 			gbc.weighty = 1.0;
 			this.panel.add(tableHeader, gbc);		
 			this.panel.add(new JScrollPane(this.table), gbc);
 		}
 	}
	
	private void updateTable(final PermissionBean bean) {		
		this.model.setPermissionBean(bean);
		final int[] columnsWidth = new int[this.model.getColumnCount()];
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
			for (int i = 0; i < this.model.getRowCount(); i++) {
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
	}

	
	@Override
	public JPanel getPropertyPanel(final PermissionBean bean) {		
		this.bean = bean;
		bean.addPropertyChangeListener(this.listener);
		this.updateTable(bean);
		return this.panel;
	}
	
	@Override
	public void disposePropertyPanel() {
		this.bean.removePropertyChangeListener(this.listener);
	}
	
	private final Icon getCachedIcon(final Module module) {
		if (this.iconCache == null) {
			this.iconCache = new HashMap<Module, Icon>();
		}
		
		Icon icon = this.iconCache.get(module);
		if (icon == null) {
			final String iconUrl = "com/syrus/AMFICOM/manager/resources/icons/" + module.getCodename() +".gif";
			final URL resource = AbstractBeanFactory.class.getClassLoader().getResource(iconUrl);
			if (resource != null) {
				icon = new ImageIcon(resource);
				this.iconCache.put(module, icon);
			} else {
				assert Log.debugMessage("PermissionBeanUI.getCachedIcon | " + iconUrl + " not found ",
					Log.DEBUGLEVEL09);
			}
		}
		return icon;
	}	
	
	private final Icon getCachedImage(final Module module) {
		if (this.imageCache == null) {
			this.imageCache = new HashMap<Module, Icon>();
		}
		
		Icon image = this.imageCache.get(module);
		if (image == null) {
			final String imageUrl = "com/syrus/AMFICOM/manager/resources/" + module.getCodename() +".gif";
			final URL resource = AbstractBeanFactory.class.getClassLoader().getResource(imageUrl);
			if (resource != null) {
				image = new ImageIcon(resource);
				this.imageCache.put(module, image);
			} else {
				assert Log.debugMessage("PermissionBeanUI.getCachedImage | " + imageUrl + " not found ",
					Log.DEBUGLEVEL09);
			}
		}
		return image;
	}

	@Override
	public Icon getIcon(AbstractBeanFactory<PermissionBean> factory) {
		return this.getCachedIcon(((PermissionBeanFactory)factory).getModule());
	}
	
	@Override
	public Icon getImage(final PermissionBean bean) {
		return this.getCachedImage(bean.getPermissionAttributes().getModule());
	}
	
	private class PermissionTableModel extends AbstractTableModel {

		private PermissionBean	permissionBean;
		private PermissionAttributes	permissionAttributes;
		private List<String>	keys;
		private PermissionBeanWrapper	permissionBeanWrapper;

		public void setPermissionBean(final PermissionBean	permissionBean) {
			this.permissionBean = permissionBean;			
			this.permissionAttributes = this.permissionBean.getPermissionAttributes();
			this.permissionBeanWrapper = 
				PermissionBeanWrapper.getInstance(this.permissionAttributes.getModule());
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
			return 2;
		}
		
		public int getRowCount() {
			return this.keys != null ? this.keys.size() : 0;
		}
		
		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 0:
				return I18N.getString("Manager.Entity.PermissionAttibute.Action");
			case 1:
				return I18N.getString("Manager.Entity.PermissionAttibute.Permission");
			}
			return null;
		}
		
		public Object getValueAt(	int rowIndex,
									int columnIndex) {
			if (columnIndex == 0) {
				return this.permissionBeanWrapper.getName(this.keys.get(rowIndex));
			}
			
			final PermissionCodename permissionCode = PermissionCodename.valueOf(this.keys.get(rowIndex));
			switch(columnIndex) {
			case 1:
				try {
					return Boolean.valueOf(!this.permissionAttributes.isDenied(permissionCode) && 
						 this.permissionBean.getRolePermission(permissionCode));
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Boolean.valueOf(this.permissionAttributes.isDenied(permissionCode));
			}
			return null;
		}

		@Override
		public boolean isCellEditable(	int rowIndex,
										int columnIndex) {
			if (columnIndex != 0) {
				final PermissionCodename permissionCode = 
					PermissionCodename.valueOf(this.keys.get(rowIndex));
				try {
					return this.permissionBean.getRolePermission(permissionCode);
				} catch (final ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}		

		@Override
		public void setValueAt(	Object aValue,
								int rowIndex,
								int columnIndex) {
			PermissionCodename permissionCode = PermissionCodename.valueOf(this.keys.get(rowIndex));
			boolean b = ((Boolean)aValue).booleanValue();
			switch(columnIndex) {
			case 1:
				this.permissionAttributes.setDenidEnable(permissionCode, !b);
				break;
			default:
				break;
			}
			this.fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}
	
}

