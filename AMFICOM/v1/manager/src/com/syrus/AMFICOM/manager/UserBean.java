/*-
 * $Id: UserBean.java,v 1.7 2005/08/02 14:42:06 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/02 14:42:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBean extends Bean {

	List<String>	names;

	private SystemUser	user;
	
	UserBean(List<String> names) {
		this.names = names;
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
	public JPopupMenu getMenu(	final JGraphText graphText,
								final Object cell) {

		if (cell != null) {
			JPopupMenu popupMenu = new JPopupMenu();
			String lastName = null;
			for (final String name1 : this.names) {
				if (name1 != null) {
					popupMenu.add(new AbstractAction(name1) {

						public void actionPerformed(ActionEvent e) {
							try {
								UserBean.this.setNature(name1);
								AttributeMap attributeMap = new AttributeMap();
								GraphConstants.setValue(attributeMap, name1);
								Map viewMap = new Hashtable();
								viewMap.put(cell, attributeMap);
								graphText.getGraph().getModel().edit(viewMap, null, null, null);
							} catch (ApplicationException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(graphText.getGraph(), 
									e1.getMessage(), 
									LangModelManager.getString("Error"),
									JOptionPane.ERROR_MESSAGE);
							}
						}
					});
				} else {
					popupMenu.addSeparator();
				}

				lastName = name1;
			}

			if (lastName != null) {
				popupMenu.addSeparator();
			}

			popupMenu.add(new AbstractAction(LangModelManager
					.getString("Entity.User.new")
					+ "...") {

				public void actionPerformed(ActionEvent e) {
					String string = JOptionPane.showInputDialog(null,
						LangModelManager.getString("Dialog.Add.User"),
						LangModelManager.getString("Entity.User.new"),
						JOptionPane.OK_CANCEL_OPTION);
					if (string == null) { return; }
					try {
						UserBean.this.names.add(string);
						UserBean.this.setNature(string);
						AttributeMap attributeMap = new AttributeMap();
						GraphConstants.setValue(attributeMap, string);
						Map viewMap = new Hashtable();
						viewMap.put(cell, attributeMap);
						JGraph graph = graphText.getGraph();
						graph.getModel().edit(viewMap, null, null, null);
						graph.getSelectionModel().setSelectionCell(cell);
					} catch (ApplicationException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(graphText.getGraph(), 
							e1.getMessage(), 
							LangModelManager.getString("Error"),
							JOptionPane.ERROR_MESSAGE);
					}
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
			for(Characteristic characteristic : this.user.getCharacteristics()) {
				if (characteristic.getType().getId().equals(characteristicTypeId)) {
					return characteristic;
				}
			}			
			Characteristic characteristic = Characteristic.createInstance(LoginManager.getUserId(), characteristicType, codename, codename, "", this.user, true, true);
			StorableObjectPool.putStorableObject(characteristic);
			return characteristic;
		}
		
		throw new ObjectNotFoundException("Characteristic type with codename '" + codename + "' not found");

	}
	
	protected final String getFullName() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_FULLNAME);		
		return characteristic.getValue();
	}

	protected final void setFullName(final String fullName) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_FULLNAME);
		String fullName2 = characteristic.getValue();
		if (fullName2 != fullName &&
				(fullName2 != null && !fullName2.equals(fullName) ||
				!fullName.equals(fullName2))) {
			String oldValue = fullName2;
			characteristic.setValue(fullName);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_FULL_NAME, oldValue, fullName));
		}	
	}

	public final String getName() {
		return this.user.getName();
	}

	public final void setName(final String name) {
		String name2 = this.user.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			String oldValue = name2;
			this.user.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_NAME, oldValue, name));
		}
	}
	
	protected final String getNature() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_NATURE);		
		return characteristic.getValue();
	}

	protected final void setNature(final String nature) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_NATURE);
		String nature2 = characteristic.getValue();
		if (nature2 != nature &&
				(nature2 != null && !nature2.equals(nature) ||
				!nature.equals(nature2))) {
			String oldValue = nature2;
			characteristic.setValue(nature);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_NATURE, oldValue, nature));
		}
	}

	public final String getBuilding() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_BUILDING);		
		return characteristic.getValue();
	}

	
	public final void setBuilding(String building) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_BUILDING);
		String building2 = characteristic.getValue();
		if (building2 != building &&
				(building2 != null && !building2.equals(building) ||
				!building.equals(building2))) {
			String oldValue = building2;
			characteristic.setValue(building);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_BUILDING, oldValue, building));
		}
	}	
	
	public final String getCellular() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_CELLULAR);		
		return characteristic.getValue();
	}

	
	public final void setCellular(String cellular) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_CELLULAR);
		String cellular2 = characteristic.getValue();
		if (cellular2 != cellular &&
				(cellular2 != null && !cellular2.equals(cellular) ||
				!cellular.equals(cellular2))) {
			String oldValue = cellular2;
			characteristic.setValue(cellular);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_CELLULAR, oldValue, cellular));
		}
	}

	
	public final String getCity() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_CITY);		
		return characteristic.getValue();
	}

	
	public final void setCity(String city) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_CITY);
		String city2 = characteristic.getValue();
		if (city2 != city &&
				(city2 != null && !city2.equals(city) ||
				!city.equals(city2))) {
			String oldValue = city2;
			characteristic.setValue(city);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_CITY, oldValue, city));
		}
	}

	
	public final String getCompany() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_COMPANY);		
		return characteristic.getValue();
	}

	
	public final void setCompany(String company) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_COMPANY);
		String company2 = characteristic.getValue();
		if (company2 != company &&
				(company2 != null && !company2.equals(company) ||
				!company.equals(company2))) {
			String oldValue = company2;
			characteristic.setValue(company);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_COMPANY, oldValue, company));
		}
	}

	
	public final String getDepartement() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_DEPARTEMENT);		
		return characteristic.getValue();
	}

	
	public final void setDepartement(String departement) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_DEPARTEMENT);
		String departement2 = characteristic.getValue();
		if (departement2 != departement &&
				(departement2 != null && !departement2.equals(departement) ||
				!departement.equals(departement2))) {
			String oldValue = departement2;
			characteristic.setValue(departement);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_DEPARTEMENT, oldValue, departement));
		}
	}

	
	public final String getEmail() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_EMAIL);		
		return characteristic.getValue();
	}

	
	public final void setEmail(String email) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_EMAIL);
		String email2 = characteristic.getValue();
		if (email2 != email &&
				(email2 != null && !email2.equals(email) ||
				!email.equals(email2))) {
			String oldValue = email2;
			characteristic.setValue(email);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_EMAIL, oldValue, email));
		}
	}

	
	public final String getPhone() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_PHONE);		
		return characteristic.getValue();
	}

	
	public final void setPhone(String phone) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_PHONE);
		String phone2 = characteristic.getValue();
		if (phone2 != phone &&
				(phone2 != null && !phone2.equals(phone) ||
				!phone.equals(phone2))) {
			String oldValue = phone2;
			characteristic.setValue(phone);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_PHONE, oldValue, phone));
		}
	}

	
	public final String getPosition() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_POSITION);		
		return characteristic.getValue();
	}

	
	public final void setPosition(String position) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_POSITION);
		String position2 = characteristic.getValue();
		if (position2 != position &&
				(position2 != null && !position2.equals(position) ||
				!position.equals(position2))) {
			String oldValue = position2;
			characteristic.setValue(position);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_POSITION, oldValue, position));
		}
	}

	
	public final String getRoomNo() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_ROOM_NO);		
		return characteristic.getValue();
	}

	
	public final void setRoomNo(String roomNo) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_ROOM_NO);
		String roomNo2 = characteristic.getValue();
		if (roomNo2 != roomNo &&
				(roomNo2 != null && !roomNo2.equals(roomNo) ||
				!roomNo.equals(roomNo2))) {
			String oldValue = roomNo2;
			characteristic.setValue(roomNo);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_ROOM_NO, oldValue, roomNo));
		}
	}

	
	public final String getStreet() throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_STREET);		
		return characteristic.getValue();
	}

	
	public final void setStreet(String street) throws ApplicationException {
		Characteristic characteristic = this.findCharacteristic(CharacteristicTypeCodenames.USER_STREET);
		String street2 = characteristic.getValue();
		if (street2 != street &&
				(street2 != null && !street2.equals(street) ||
				!street.equals(street2))) {
			String oldValue = street2;
			characteristic.setValue(street);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_STREET, oldValue, street));
		}
	}

}
