/*-
 * $Id: UserBean.java,v 1.6 2005/08/01 11:32:03 bob Exp $
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

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBean extends Bean {

	List<String>	names;
	
	private String	nature;	

	private String	fullName;
	
	private String	position;
	
	private String	departement;
	
	private String	company;

	private String	roomNo;
	
	private String	city;
	
	private String	street;
	
	private String	building;
	
	private String	email;
	
	private String	phone;
	
	private String	cellular;
	
	UserBean(List<String> names) {
		this.names = names;
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
							UserBean.this.setNature(name1);
							AttributeMap attributeMap = new AttributeMap();
							GraphConstants.setValue(attributeMap, name1);
							Map viewMap = new Hashtable();
							viewMap.put(cell, attributeMap);
							graphText.getGraph().getModel().edit(viewMap, null, null, null);
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
					UserBean.this.names.add(string);
					UserBean.this.setNature(string);
					AttributeMap attributeMap = new AttributeMap();
					GraphConstants.setValue(attributeMap, string);
					Map viewMap = new Hashtable();
					viewMap.put(cell, attributeMap);
					JGraph graph = graphText.getGraph();
					graph.getModel().edit(viewMap, null, null, null);
					graph.getSelectionModel().setSelectionCell(cell);
				}
			});
			return popupMenu;
		}

		return null;
	}

	protected final String getFullName() {
		return this.fullName;
	}

	protected final void setFullName(final String fullName) {
		this.fullName = fullName;
		if (this.fullName != fullName &&
				(this.fullName != null && !this.fullName.equals(fullName) ||
				!fullName.equals(this.fullName))) {
			String oldValue = this.fullName;
			this.fullName = fullName;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_FULL_NAME, oldValue, fullName));
		}	
	}

	protected final String getNature() {
		return this.nature;
	}

	protected final void setNature(final String nature) {
		if (this.nature != nature &&
				(this.nature != null && !this.nature.equals(nature) ||
				!nature.equals(this.nature))) {
			String oldValue = this.name;
			this.nature = nature;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_NATURE, oldValue, nature));
		}		
	}

	public final String getBuilding() {
		return this.building;
	}

	
	public final void setBuilding(String building) {
		if (this.building != building &&
				(this.building != null && !this.building.equals(building) ||
				!building.equals(this.building))) {
			String oldValue = this.name;
			this.building = building;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_BUILDING, oldValue, building));
		}	
	}

	
	public final String getCellular() {
		return this.cellular;
	}

	
	public final void setCellular(String cellular) {
		if (this.cellular != cellular &&
				(this.cellular != null && !this.cellular.equals(cellular) ||
				!cellular.equals(this.cellular))) {
			String oldValue = this.name;
			this.cellular = cellular;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_CELLULAR, oldValue, cellular));
		}
	}

	
	public final String getCity() {
		return this.city;
	}

	
	public final void setCity(String city) {
		if (this.city != city &&
				(this.city != null && !this.city.equals(city) ||
				!city.equals(this.city))) {
			String oldValue = this.name;
			this.city = city;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_CITY, oldValue, city));
		}
	}

	
	public final String getCompany() {
		return this.company;
	}

	
	public final void setCompany(String company) {
		if (this.company != company &&
				(this.company != null && !this.company.equals(company) ||
				!company.equals(this.company))) {
			String oldValue = this.name;
			this.company = company;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_COMPANY, oldValue, company));
		}
	}

	
	public final String getDepartement() {
		return this.departement;
	}

	
	public final void setDepartement(String departement) {
		if (this.departement != departement &&
				(this.departement != null && !this.departement.equals(departement) ||
				!departement.equals(this.departement))) {
			String oldValue = this.name;
			this.departement = departement;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_DEPARTEMENT, oldValue, departement));
		}
	}

	
	public final String getEmail() {
		return this.email;
	}

	
	public final void setEmail(String email) {
		if (this.email != email &&
				(this.email != null && !this.email.equals(email) ||
				!email.equals(this.email))) {
			String oldValue = this.name;
			this.email = email;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_EMAIL, oldValue, email));
		}
	}

	
	public final String getPhone() {
		return this.phone;
	}

	
	public final void setPhone(String phone) {
		if (this.phone != phone &&
				(this.phone != null && !this.phone.equals(phone) ||
				!phone.equals(this.phone))) {
			String oldValue = this.name;
			this.phone = phone;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_PHONE, oldValue, phone));
		}
	}

	
	public final String getPosition() {
		return this.position;
	}

	
	public final void setPosition(String position) {
		if (this.position != position &&
				(this.position != null && !this.position.equals(position) ||
				!position.equals(this.position))) {
			String oldValue = this.name;
			this.position = position;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_POSITION, oldValue, position));
		}
	}

	
	public final String getRoomNo() {
		return this.roomNo;
	}

	
	public final void setRoomNo(String roomNo) {
		if (this.roomNo != roomNo &&
				(this.roomNo != null && !this.roomNo.equals(roomNo) ||
				!roomNo.equals(this.roomNo))) {
			String oldValue = this.name;
			this.roomNo = roomNo;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_ROOM_NO, oldValue, roomNo));
		}
	}

	
	public final String getStreet() {
		return this.street;
	}

	
	public final void setStreet(String street) {
		if (this.street != street &&
				(this.street != null && !this.street.equals(street) ||
				!street.equals(this.street))) {
			String oldValue = this.name;
			this.street = street;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_STREET, oldValue, street));
		}
	}

}
