/*-
 * $Id: UserBean.java,v 1.22 2005/10/13 15:28:14 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2005/10/13 15:28:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBean extends Bean implements  WorkstationItem {

	final SortedSet<Role>	roles;
	
	final Map<String, String> propertyName;
	
	final Map<Identifier, JTable> domainPermissionTable;
	
	JPanel switchPanel;

	private SystemUser	user;

	private Identifier	domainId;
	private Map<Module, PermissionAttributes>	permissionAttributesMap;

	
	UserBean(final SortedSet<Role> names) {
		this.roles = names;
		
		this.propertyName = new HashMap<String, String>();
		this.domainPermissionTable = new HashMap<Identifier, JTable>();	
		this.permissionAttributesMap = new HashMap<Module, PermissionAttributes>();
		
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void dispose() throws ApplicationException {
		assert Log.debugMessage("UserBean.dispose | " 
				+ Identifier.createIdentifiers(this.user.getCharacteristics(false)),
			Log.DEBUGLEVEL09);
		assert Log.debugMessage("UserBean.dispose | " + this.id, Log.DEBUGLEVEL09);		
		StorableObjectPool.delete(this.user.getCharacteristics(false));
		StorableObjectPool.delete(this.id);		
		
		for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
			Bean portBean = 
				(Bean) this.graphText.getCell(layoutItem);
			portBean.dispose();
		}

		super.disposeLayoutItem();
	}
	
	@Override
	protected void setId(Identifier storableObject) {
		super.setId(storableObject);
		try {
			this.user = StorableObjectPool.getStorableObject(this.id, true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateEdgeAttributes(	DefaultEdge edge,
										MPort port) {
		AttributeMap attributes = edge.getAttributes();
		GraphConstants.setLineWidth(attributes, 10.0f);
		GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setLineColor(attributes, Color.LIGHT_GRAY);
		GraphConstants.setForeground(attributes, Color.BLACK);
	}

	@Override
	public JPopupMenu getMenu(final Object cell) {

		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();
			
			final Set<Identifier> roleIds = this.user.getRoleIds();
			
			for(final Role role : this.roles) {

				
				final Action action = new AbstractAction(role.getDescription()){
					
					public void actionPerformed(final ActionEvent e) {
						final JCheckBoxMenuItem checkBoxMenuItem = 
							(JCheckBoxMenuItem) e.getSource();
						
						if (checkBoxMenuItem.isSelected()) {
							user.addRole(role);
						} else {
							user.removeRole(role);
						}
						
					}
				};
				
				final JCheckBoxMenuItem checkBoxMenuItem =
					new JCheckBoxMenuItem(action);
				checkBoxMenuItem.setSelected(roleIds.contains(role));
				
				popupMenu.add(checkBoxMenuItem);
			}

			//			popupMenu.add(new AbstractAction(I18N
//					.getString("Manager.Entity.User.new")
//					+ "...") {
//
//				public void actionPerformed(ActionEvent e) {
//					String string = JOptionPane.showInputDialog(null,
//						I18N.getString("Manager.Dialog.Add.User"),
//						I18N.getString("Manager.Entity.User.new"),
//						JOptionPane.OK_CANCEL_OPTION);
//					if (string == null) { return; }
//					try {
//						UserBean.this.roles.add(string);
//						UserBean.this.setNature(string);
//						AttributeMap attributeMap = new AttributeMap();
//						GraphConstants.setValue(attributeMap, string);
//						Map viewMap = new Hashtable();
//						viewMap.put(cell, attributeMap);
//						JGraph graph = UserBean.this.graphText.getGraph();
//						graph.getModel().edit(viewMap, null, null, null);
//						graph.getSelectionModel().setSelectionCell(cell);
//					} catch (ApplicationException e1) {
//						e1.printStackTrace();
//						JOptionPane.showMessageDialog(UserBean.this.graphText.getGraph(), 
//							e1.getMessage(), 
//							I18N.getString("Manager.Error"),
//							JOptionPane.ERROR_MESSAGE);
//					}
//				}
//			});
			
			popupMenu.addSeparator();
			
			popupMenu.add(new AbstractAction(I18N.getString("Manager.Dialog.GotoUserPermissions")) {

				public void actionPerformed(ActionEvent e) {					
					UserBean.this.graphText.setPerspective(new SystemUserPerpective(UserBean.this.graphText, UserBean.this, cell));
				}
			});
			
			return popupMenu;
		}

		return null;
	}

	
	private Characteristic findCharacteristic(String codename) throws ApplicationException {
		TypicalCondition typicalCondition = new TypicalCondition(codename, 
			OperationSort.OPERATION_EQUALS, 
			ObjectEntities.CHARACTERISTIC_TYPE_CODE,
			StorableObjectWrapper.COLUMN_CODENAME);
		Set<StorableObject> storableObjectsByCondition = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
		
		if (!storableObjectsByCondition.isEmpty()) {
			CharacteristicType characteristicType = (CharacteristicType) storableObjectsByCondition.iterator().next();
			Identifier characteristicTypeId = characteristicType.getId();
			for(Characteristic characteristic : this.user.getCharacteristics(false)) {
				if (characteristic.getType().getId().equals(characteristicTypeId)) {
					return characteristic;
				}
			}			
			Characteristic characteristic = Characteristic.createInstance(LoginManager.getUserId(), characteristicType, codename, codename, "", this.user, true, true);
			return characteristic;
		}
		
		throw new ObjectNotFoundException("Characteristic type with codename '" + codename + "' not found");

	}
	
	private String getCharacteriscticValue(final String codename) 
	throws ApplicationException {
		String value = this.propertyName.get(codename);
		if (value == null) {
			Characteristic characteristic = this.findCharacteristic(codename);		
			value = characteristic.getValue();
			this.propertyName.put(codename, value);
		}
		return value;
	}
	
	private void setCharacteriscticValue(final String codename, 
	                                     final String value,
	                                     final String key) 
	throws ApplicationException {
		String value2 = this.getCharacteriscticValue(codename);
		if (value2 != value &&
				(value2 != null && !value2.equals(value) ||
				!value.equals(value2))) {
			String oldValue = value2;
			Characteristic characteristic = this.findCharacteristic(codename);
			characteristic.setValue(value);
			this.propertyName.put(codename, value);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, key, oldValue, value));
		}
	}
	
	protected final String getFullName() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_FULLNAME);		
	}

	protected final void setFullName(final String fullName) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_FULLNAME, 
			fullName, 
			UserBeanWrapper.FULL_NAME);
	}

	@Override
	public final String getName() {
		return this.user.getName();
	}

	@Override
	public final void setName(final String name) {
		String name2 = this.user.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			String oldValue = name2;
			this.user.setName(name);
			this.graphText.getDispatcher().firePropertyChange(
				new PropertyChangeEvent(this, ObjectEntities.SYSTEMUSER, null, this));
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.NAME, oldValue, name));
		}
	}

	
	public final String getLogin() {
		return this.user.getLogin();
	}

	public final void setLogin(final String login) {
		String login2 = this.user.getLogin();
		if (login2 != login &&
				(login2 != null && !login2.equals(login) ||
				!login.equals(login2))) {
			String oldValue = login2;
			this.user.setLogin(login);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.LOGIN, oldValue, login));
		}
	}
	
	protected final String getNature() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_NATURE);
	}

	protected final void setNature(final String nature) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_NATURE, 
			nature,
			UserBeanWrapper.USER_NATURE);
	}

	public final String getBuilding() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_BUILDING);
	}

	
	public final void setBuilding(String building) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_BUILDING, 
			building,
			UserBeanWrapper.USER_BUILDING);
	}	
	
	public final String getCellular() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_CELLULAR);
	}

	
	public final void setCellular(String cellular) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_CELLULAR, 
			cellular,
			UserBeanWrapper.USER_CELLULAR);

	}

	
	public final String getCity() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_CITY);
	}

	
	public final void setCity(String city) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_CITY, 
			city,
			UserBeanWrapper.USER_CITY);
	}

	
	public final String getCompany() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_COMPANY);
	}

	
	public final void setCompany(String company) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_COMPANY, 
			company,
			UserBeanWrapper.USER_COMPANY);
	}

	
	public final String getDepartement() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_DEPARTEMENT);
	}

	
	public final void setDepartement(String departement) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_DEPARTEMENT, 
			departement,
			UserBeanWrapper.USER_DEPARTEMENT);
	}

	
	public final String getEmail() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_EMAIL);
	}

	
	public final void setEmail(String email) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_EMAIL, 
			email,
			UserBeanWrapper.USER_EMAIL);
	}

	
	public final String getPhone() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_PHONE);
	}

	
	public final void setPhone(String phone) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_PHONE, 
			phone,
			UserBeanWrapper.USER_PHONE);
	}

	
	public final String getPosition() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_POSITION);
	}

	
	public final void setPosition(String position) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_POSITION, 
			position,
			UserBeanWrapper.USER_POSITION);
	}

	
	public final String getRoomNo() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_ROOM_NO);
	}

	
	public final void setRoomNo(String roomNo) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_ROOM_NO, 
			roomNo,
			UserBeanWrapper.USER_ROOM_NO);
	}

	
	public final String getStreet() throws ApplicationException {
		return this.getCharacteriscticValue(CharacteristicTypeCodenames.USER_STREET);
	}

	
	public final void setStreet(String street) throws ApplicationException {
		this.setCharacteriscticValue(CharacteristicTypeCodenames.USER_STREET, 
			street,
			UserBeanWrapper.USER_STREET);
	}

	private Set<LayoutItem> getBeanChildrenLayoutItems() 
	throws ApplicationException{
		final TypicalCondition typicalCondition = 
			new TypicalCondition(this.codeName, 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.LAYOUT_ITEM_CODE, 
				LayoutItemWrapper.COLUMN_LAYOUT_NAME);

		final Set<LayoutItem> beanLayoutItems = StorableObjectPool.getStorableObjectsByCondition(
			typicalCondition, 
			true, 
			true);
		
		final LinkedIdsCondition linkedIdsCondition = 
			new LinkedIdsCondition(Identifier.createIdentifiers(beanLayoutItems),
				ObjectEntities.LAYOUT_ITEM_CODE);
		
		return StorableObjectPool.getStorableObjectsByCondition(
			linkedIdsCondition, 
			true, 
			true);
	}
	
	public void setDomainId(final Identifier oldDomainId,
	                        final Identifier newDomainId) {
		assert Log.debugMessage("UserBean.setDomainId | was:" + oldDomainId
				+ ", now:" + newDomainId, 
			Log.DEBUGLEVEL09);
		try {			
			for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
				assert Log.debugMessage("UserBean.setDomainId | 1 " + layoutItem.getName() 
					+ ", " + layoutItem.getLayoutName(), 
				Log.DEBUGLEVEL09);
				Log.debugMessage("UserBean.setDomainId | 1 "
					+ layoutItem.getId() + ", "
					+ layoutItem.getName() 
					+ ", layoutName:" 
					+ layoutItem.getLayoutName()
					+ ", this.codeName:" + this.codeName, 
				Log.DEBUGLEVEL09);		

				if (layoutItem.getLayoutName().startsWith(ObjectEntities.SYSTEMUSER)) {
					final String layoutName = !newDomainId.isVoid() ? 
							newDomainId.getIdentifierString() : 
							ObjectEntities.SYSTEMUSER;
					Log.debugMessage("UserBean.setDomainId | "
						+ layoutItem.getId() + ", "
						+ layoutItem.getName() 
						+ ", layoutName:" 
						+ layoutName, 
					Log.DEBUGLEVEL09);		
					layoutItem.setLayoutName(layoutName);
					UserItem portBean = 
						(UserItem) this.graphText.getCell(layoutItem);
					portBean.setDomainId(oldDomainId, newDomainId);
				}					
			}
		} catch (final ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected PermissionAttributes getPermissionAttributes(final Module module) throws ApplicationException {
		final Perspective perspective = this.graphText.getPerspective();
		// XXX think and refactor
		if (perspective instanceof DomainPerpective) {
			DomainPerpective domainPerpective = (DomainPerpective) perspective;
			Identifier domainId1 = domainPerpective.getDomainId();
			PermissionAttributes permissionAttributes = this.permissionAttributesMap.get(module);
			if (permissionAttributes == null ||  !domainId1.equals(this.domainId)) {				
				this.domainId = domainId1;
				final Domain domain = 
					(Domain) StorableObjectPool.getStorableObject(domainPerpective.getDomainId(), true);
				permissionAttributes = domain.getPermissionAttributes(this.id, module);
				this.permissionAttributesMap.put(module, permissionAttributes);
			}
			return permissionAttributes;
		}
		return null;
	}

}
