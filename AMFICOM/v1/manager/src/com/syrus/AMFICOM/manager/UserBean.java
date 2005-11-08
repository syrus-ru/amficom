/*-
 * $Id: UserBean.java,v 1.25 2005/11/08 12:07:58 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.client.event.Dispatcher;
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
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.25 $, $Date: 2005/11/08 12:07:58 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBean extends Bean implements WorkstationItem {

	final SortedSet<Role>	roles;
	
	final Map<String, String> propertyName;
	
	private SystemUser	user;

	private Identifier	domainId;
	private Map<Module, PermissionAttributes>	permissionAttributesMap;

	UserBean(final SortedSet<Role> names) {
		this.roles = names;
		
		this.propertyName = new HashMap<String, String>();
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
		assert Log.debugMessage("UserBean.dispose | " + this.identifier, Log.DEBUGLEVEL09);		
		StorableObjectPool.delete(this.user.getCharacteristics(false));
		StorableObjectPool.delete(this.identifier);		
		
		final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
		for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
			Bean portBean = 
				(Bean) graphRoutines.getBean(layoutItem);
			portBean.dispose();
		}

		super.disposeLayoutItem();
	}
	
	@Override
	protected void setIdentifier(Identifier storableObject) 
	throws ApplicationException {
		super.setIdentifier(storableObject);
		this.user = StorableObjectPool.getStorableObject(this.identifier, true);
	}

//	@Override
//	public void updateEdgeAttributes(	DefaultEdge edge,
//										MPort port) {
//		AttributeMap attributes = edge.getAttributes();
//		GraphConstants.setLineWidth(attributes, 10.0f);
//		GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
//		GraphConstants.setLineColor(attributes, Color.LIGHT_GRAY);
//		GraphConstants.setForeground(attributes, Color.BLACK);
//	}

	private void refreshRoles() throws ApplicationException {
		Log.debugMessage(Log.DEBUGLEVEL09);
		final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
		for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
			assert Log.debugMessage(layoutItem.getName() +", " + layoutItem.getLayoutName() , Log.DEBUGLEVEL09);
			final Bean bean = 
				(Bean) graphRoutines.getBean(layoutItem);
			assert Log.debugMessage(bean, Log.DEBUGLEVEL09);
			if (bean instanceof PermissionBean) {
				PermissionBean permissionBean = (PermissionBean) bean;
				permissionBean.updateRolePermissions();
			}
		}
	}
	
	public void addRole(final Role role) throws ApplicationException {
		this.user.addRole(role);
		this.refreshRoles();
	}
	
	public void removeRole(final Role role) throws ApplicationException {
		this.user.removeRole(role);
		this.refreshRoles();
	}
	
	public boolean containsRole(final Role role) {
		final Set<Identifier> roleIds = this.user.getRoleIds();
		return roleIds.contains(role.getId());
	}
	
	private Characteristic findCharacteristic(final String codename) throws ApplicationException {
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
			final Characteristic characteristic = 
				Characteristic.createInstance(LoginManager.getUserId(), 
					characteristicType, 
					codename, 
					codename, 
					"", 
					this.user, 
					true, 
					true);
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
			final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
			final Dispatcher dispatcher = managerModel.getDispatcher();
			dispatcher.firePropertyChange(
				new PropertyChangeEvent(this, ObjectEntities.SYSTEMUSER, null, this));
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.NAME, oldValue, name));
		}
	}

	
	public final String getLogin() {
		return this.user.getLogin();
	}

	public final void setLogin(final String login) {
		
		// only latin chars, digits and underscore allow
		final String pattern = "[-A-Za-z0-9_]+";
		if (!login.matches(pattern)) {
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.LOGIN, login, this.user.getLogin()));
			return;
		}
		
		String login2 = this.user.getLogin();
		if (login2 != login &&
				(login2 != null && !login2.equals(login) ||
				!login.equals(login2))) {
			String oldValue = login2;
			this.user.setLogin(login);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.LOGIN, oldValue, login));
		}
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

	
	public final void setEmail(final String email) throws ApplicationException {		
		if (email.trim().length() > 0 && !email.matches("[-a-z0-9_.]+@([-a-z0-9]+\\.)+[a-z]{2,3}")) {
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.USER_EMAIL, email, this.getEmail()));
			return;
		}
		
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
			new TypicalCondition(this.id, 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.LAYOUT_ITEM_CODE, 
				LayoutItemWrapper.COLUMN_LAYOUT_NAME);

		final Set<LayoutItem> beanLayoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				typicalCondition, 
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
			final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
			for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
				assert Log.debugMessage("UserBean.setDomainId | 1 " + layoutItem.getName() 
					+ ", " + layoutItem.getLayoutName(), 
				Log.DEBUGLEVEL09);
				Log.debugMessage("UserBean.setDomainId | 1 "
					+ layoutItem.getId() + ", "
					+ layoutItem.getName() 
					+ ", layoutName:" 
					+ layoutItem.getLayoutName()
					+ ", this.codeName:" + this.id, 
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
						(UserItem) graphRoutines.getBean(layoutItem);
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
				permissionAttributes = domain.getPermissionAttributes(this.identifier, module);
				this.permissionAttributesMap.put(module, permissionAttributes);
			}
			return permissionAttributes;
		}
		return null;
	}
	
	public final SystemUser getUser() {
		return this.user;
	}
	
	public final SortedSet<Role> getRoles() {
		return this.roles;
	}

	@Override
	public String getCodename() {
		return ObjectEntities.SYSTEMUSER;
	}	
}
