/*
 * $Id: SchemeTreeModel.java,v 1.18 2005/04/27 08:47:29 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

/**
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2005/04/27 08:47:29 $
 * @module schemeclient_v1
 */

import java.util.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.configuration.ui.*;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.general.ui_.tree_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.logic.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;

public class SchemeTreeModel implements ChildrenFactory, VisualManagerFactory {
	ApplicationContext aContext;

	private static Kind[] schemeTypes = new Kind[] { Kind.NETWORK, Kind.BUILDING,
			Kind.CABLE_SUBNETWORK };
	private static String[] schemeTypeNames = new String[] {
			LangModelScheme.getString(Constants.SCHEME_TYPE_NETWORK), LangModelScheme.getString(Constants.SCHEME_TYPE_BUILDING),
			LangModelScheme.getString(Constants.SCHEME_TYPE_CABLE) };

	public SchemeTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public VisualManager getVisualManager(Item node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String)object;
			/*if (s.equals(Constants.SCHEME))
				return SchemeController.getInstance();
			if (s.equals(Constants.SCHEME_ELEMENT))
				return SchemeElementController.getInstance();
			if (s.equals(Constants.SCHEME_LINK))
				return SchemeLinkController.getInstance();
			if (s.equals(Constants.SCHEME_CABLELINK))
				return SchemeCableLinkController.getInstance();
			if (s.equals(Constants.SCHEME_PATH))
				return SchemePathController.getInstance();
			if (s.equals(Constants.SCHEME_PROTO_GROUP))
				return null;*/
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
			if (s.equals(Constants.LINK_TYPE))
				return LinkTypePropertiesManager.getInstance(aContext);
			if (s.equals(Constants.CABLE_LINK_TYPE))
				return CableLinkTypePropertiesManager.getInstance(aContext);
			if (s.equals(Constants.PORT_TYPE))
				return PortTypePropertiesManager.getInstance(aContext);
			if (s.equals(Constants.EQUIPMENT_TYPE))
				return EquipmentTypePropertiesManager.getInstance(aContext);
			if (s.equals(Constants.MEASUREMENT_PORT_TYPES))
				return MeasurementPortTypePropertiesManager.getInstance(aContext);
			if (s.equals(Constants.MEASUREMENT_TYPES))
				return MeasurementTypePropertiesManager.getInstance(aContext);
			if (s.equals(Constants.SCHEME_TYPE))
				return null;
			// for any other strings return null Manager
			return null;
		}
		if (object instanceof EquipmentType)
			return EquipmentTypePropertiesManager.getInstance(aContext);
		if (object instanceof LinkType)
			return LinkTypePropertiesManager.getInstance(aContext);
		if (object instanceof CableLinkType)
			return CableLinkTypePropertiesManager.getInstance(aContext);
		if (object instanceof PortType)
			return PortTypePropertiesManager.getInstance(aContext);
		if (object instanceof MeasurementPortType)
			return MeasurementPortTypePropertiesManager.getInstance(aContext);
		if (object instanceof MeasurementType)
			return MeasurementTypePropertiesManager.getInstance(aContext);
		if (object instanceof Kind)
				return null;
//		if (object instanceof Kind)
//			return SchemeController.getInstance();
//		if (object instanceof SchemeProtoGroup) {
//			if (!((SchemeProtoGroup) object).getSchemeProtoGroups().isEmpty())
//				return null;
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
//			else
//				return null;
			/**
			 * @todo write SchemeProtoElementController return
			 *       SchemeProtoElementController.getInstance();
			 */
//		}
//		if (object instanceof Scheme)
//			return SchemeController.getInstance();
//		if (object instanceof SchemeElement)
//			return SchemeElementController.getInstance();
		throw new UnsupportedOperationException("Unknown object " + object);
	}
	
	Collection getChildObjects(Item node) {
		Collection childObjects = new ArrayList(node.getChildren().size());
		for (Iterator it = node.getChildren().iterator(); it.hasNext();)
			childObjects.add(((Item)it.next()).getObject());
		return childObjects;
	}
	
	Collection getDifference(Collection minuend, Collection subtrahend) {
		Collection difference = new LinkedList();
		for (Iterator it = minuend.iterator(); it.hasNext();) {
			Object obj = it.next();
			if (!subtrahend.contains(obj))
				difference.add(obj);
		}
		return difference;
	}
	

	public void populate(Item node) {
		Collection contents = getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(Constants.ROOT)) {
				if (!contents.contains(Constants.CONFIGURATION))
					node.addChild(new PopulatableIconedNode(this, Constants.CONFIGURATION, LangModelScheme.getString(Constants.CONFIGURATION), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.SCHEME_TYPE))
					node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_TYPE, LangModelScheme.getString(Constants.SCHEME_TYPE), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.SCHEME_PROTO_GROUP))
					node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_PROTO_GROUP, LangModelScheme.getString(Constants.SCHEME_PROTO_GROUP), Constants.ICON_CATALOG));
			} 
			else if (s.equals(Constants.CONFIGURATION)) {
				if (!contents.contains(Constants.NETWORK_DIRECTORY))
					node.addChild(new PopulatableIconedNode(this, Constants.NETWORK_DIRECTORY, LangModelScheme.getString(Constants.NETWORK_DIRECTORY), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.MONITORING_DIRECTORY))
					node.addChild(new PopulatableIconedNode(this, Constants.MONITORING_DIRECTORY, LangModelScheme.getString(Constants.MONITORING_DIRECTORY), Constants.ICON_CATALOG));
			} 
			else if (s.equals(Constants.NETWORK_DIRECTORY)) {
				if (!contents.contains(Constants.LINK_TYPE))
					node.addChild(new PopulatableIconedNode(this, Constants.LINK_TYPE, LangModelScheme.getString(Constants.LINK_TYPE), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.CABLE_LINK_TYPE))
					node.addChild(new PopulatableIconedNode(this, Constants.CABLE_LINK_TYPE, LangModelScheme.getString(Constants.CABLE_LINK_TYPE), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.PORT_TYPE))
					node.addChild(new PopulatableIconedNode(this, Constants.PORT_TYPE, LangModelScheme.getString(Constants.PORT_TYPE), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.EQUIPMENT_TYPE))
					node.addChild(new PopulatableIconedNode(this, Constants.EQUIPMENT_TYPE, LangModelScheme.getString(Constants.EQUIPMENT_TYPE), Constants.ICON_CATALOG));
			} 
			else if (s.equals(Constants.MONITORING_DIRECTORY)) {
				if (!contents.contains(Constants.MEASUREMENT_TYPES))
					node.addChild(new PopulatableIconedNode(this, Constants.MEASUREMENT_TYPES, LangModelScheme.getString(Constants.MEASUREMENT_TYPES), Constants.ICON_CATALOG));
				if (!contents.contains(Constants.MEASUREMENT_PORT_TYPES))
					node.addChild(new PopulatableIconedNode(this, Constants.MEASUREMENT_PORT_TYPES, LangModelScheme.getString(Constants.MEASUREMENT_PORT_TYPES), Constants.ICON_CATALOG));
				// vec.add(new PopulatableIconedNode(this, "TransmissionPathType",
				// LangModelConfig.getString("menuJDirPathText"), true));
			} 
			else if (s.equals(Constants.SCHEME_TYPE)) {
				for (int i = 0; i < schemeTypes.length; i++) {
					if (!contents.contains(schemeTypes[i]))
						node.addChild(new PopulatableIconedNode(this, schemeTypes[i], schemeTypeNames[i], Constants.ICON_CATALOG));
				}
			} 
			else if (s.equals(Constants.LINK_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);
					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection toAdd = getDifference(linkTypes, contents);
					Collection toRemove = getDifference(contents, linkTypes);
					for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						if (toRemove.contains(child.getObject()))
							child.setParent(null);
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						LinkType type = (LinkType) it.next();
						node.addChild(new PopulatableIconedNode(this, type, false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.CABLE_LINK_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					Collection toAdd = getDifference(linkTypes, contents);
					Collection toRemove = getDifference(contents, linkTypes);
					for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						if (toRemove.contains(child.getObject()))
							child.setParent(null);
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						CableLinkType type = (CableLinkType) it.next();
						node.addChild(new PopulatableIconedNode(this, type, false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.PORT_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PORTTYPE_ENTITY_CODE);
					Collection portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection toAdd = getDifference(portTypes, contents);
					Collection toRemove = getDifference(contents, portTypes);
					for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						if (toRemove.contains(child.getObject()))
							child.setParent(null);
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						PortType type = (PortType) it.next();
						node.addChild(new PopulatableIconedNode(this, type, false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals(Constants.EQUIPMENT_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
					Collection equipmentTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					
					Collection toAdd = getDifference(equipmentTypes, contents);
					Collection toRemove = getDifference(contents, equipmentTypes);
					for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						if (toRemove.contains(child.getObject()))
							child.setParent(null);
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						EquipmentType type = (EquipmentType)it.next();
						node.addChild(new PopulatableIconedNode(this, type, false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			// else if (s.equals("TransmissionPathType")) {
			// try {
			// EquivalentCondition condition = new
			// EquivalentCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
			// Collection pathTypes =
			// ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition,
			// true);
			//
			// for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
			// TransmissionPathType type = (TransmissionPathType)it.next();
			// vec.add(new PopulatableIconedNode(this, type, false));
			// }
			// }
			// catch (ApplicationException ex) {
			// ex.printStackTrace();
			// }
			// }
			else if (s.equals(Constants.MEASUREMENT_PORT_TYPES)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					Collection mpTypes = ConfigurationStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					Collection toAdd = getDifference(mpTypes, contents);
					Collection toRemove = getDifference(contents, mpTypes);
					for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						if (toRemove.contains(child.getObject()))
							child.setParent(null);
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						MeasurementPortType type = (MeasurementPortType)it.next();
						node.addChild(new PopulatableIconedNode(this, type, false));
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.MEASUREMENT_TYPES)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					Collection measurementTypes = MeasurementStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					Collection toAdd = getDifference(measurementTypes, contents);
					Collection toRemove = getDifference(contents, measurementTypes);
					for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						if (toRemove.contains(child.getObject()))
							child.setParent(null);
					}
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						MeasurementType type = (MeasurementType)it.next();
						node.addChild(new PopulatableIconedNode(this, type, type.getDescription(), false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			/*else if (s.equals(Constants.SCHEME_PROTO_GROUP)) {
				try {
					Identifier domainId = new Identifier(((RISDSessionInfo) aContext
							.getSessionInterface()).getAccessIdentifier().domain_id);
					LinkedIdsCondition condition = new LinkedIdsCondition(domainId,
							ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
					Set groups = SchemeStorableObjectPool.getStorableObjectsByCondition(
							condition, true);

					for (Iterator it = groups.iterator(); it.hasNext();) {
						SchemeProtoGroup group = (SchemeProtoGroup) it.next();
						if (!contents.contains(group))
							node.addChild(new PopulatableIconedNode(this, group));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.SCHEME)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.getSchemeElementsAsArray().length; i++) {
					SchemeElement el = parent.getSchemeElementsAsArray()[i];
					if (el.getScheme() != null)
						ds.add(el.getScheme());
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						Scheme sch = (Scheme) it.next();
						if (!contents.contains(sch))
							node.addChild(new PopulatableIconedNode(this, sch));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_ELEMENT)) {
				Object parent = ((Item) node.getParent()).getObject();
				List ds = new LinkedList();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null)
							ds.add(element);
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					ds.addAll(Arrays.asList(el.getSchemeElementsAsArray()));
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						SchemeElement element = (SchemeElement) it.next();
						if (element.getSchemeLinksAsArray().length != 0	|| element.getSchemeElementsAsArray().length != 0) {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), true));
						}
						else {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), false));
						}
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_LINK)) {
				Object parent = ((Item) node.getParent()).getObject();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = scheme.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					for (int i = 0; i < el.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = el.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_CABLELINK)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				for (int i = 0; i < parent.getSchemeCableLinksAsArray().length; i++) {
					SchemeCableLink link = parent.getSchemeCableLinksAsArray()[i];
					if (!contents.contains(link))
						node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
				}
			} 
			else if (s.equals(Constants.SCHEME_PATH)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				for (int i = 0; i < parent.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray().length; i++) {
					SchemePath path = parent.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray()[i];
					if (!contents.contains(path))
						node.addChild(new PopulatableIconedNode(this, path, path.getName(), false));
				}
			}
		} 
		else {
			if (node.getObject() instanceof Kind) {
				Kind type = (Kind) node.getObject();
				TypicalCondition condition = new TypicalCondition(String.valueOf(type
						.value()), OperationSort.OPERATION_EQUALS,
						ObjectEntities.SCHEME_ENTITY_CODE,
						com.syrus.AMFICOM.scheme.SchemeController.COLUMN_TYPE);
				try {
					Set schemes = SchemeStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext();) {
						Scheme sc = (Scheme) it.next();
						if (!contents.contains(sc))
							node.addChild(new PopulatableIconedNode(this, sc));
					}
				} 
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}
			if (node.getObject() instanceof SchemeProtoGroup) {
				SchemeProtoGroup parent_group = (SchemeProtoGroup) node.getObject();
				for (final Iterator schemeProtoGroupIterator = parent_group.getSchemeProtoGroups().iterator(); schemeProtoGroupIterator.hasNext();) {
					final SchemeProtoGroup map_group = (SchemeProtoGroup) schemeProtoGroupIterator.next();
					ImageIcon icon;
					if (map_group.getSymbol() == null) {
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					}
					else {
						icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
								map_group.getSymbol().getImage()).getScaledInstance(16, 16,
								Image.SCALE_SMOOTH));
					}
					if (!contents.contains(map_group))
						node.addChild(new PopulatableIconedNode(this, map_group, map_group.getName(), !map_group.getSchemeProtoGroups().isEmpty() || !map_group.getSchemeProtoElements().isEmpty()));
				}
				if (parent_group.getSchemeProtoGroups().isEmpty()) {
					for (final Iterator schemeProtoElementIterator = parent_group.getSchemeProtoElements().iterator(); schemeProtoElementIterator.hasNext();) {
						final SchemeProtoElement schemeProtoElement = (SchemeProtoElement) schemeProtoElementIterator.next();
						// schemeProtoElement.parent(parent_group);
						if (! contents.contains(schemeProtoElement))
							node.addChild(new PopulatableIconedNode(this, schemeProtoElement, schemeProtoElement.getName(), false));
					}
				}
			} 
			else if (node.getObject() instanceof Scheme) {
				Scheme s = (Scheme) node.getObject();
				if (s.getSchemeElementsAsArray().length != 0) {
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getScheme() == null) {
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getScheme() != null) {
							has_schemes = true;
							break;
						}
					}
					if (has_schemes) {
						if (!contents.contains(Constants.SCHEME))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME));
					}
					if (has_elements) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_ELEMENT));
					}
				}
				if (s.getSchemeLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_LINK))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_LINK));
				}
				if (s.getSchemeCableLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_CABLELINK))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_CABLELINK));
				}
				if (s.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_PATH))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_PATH));
				}
			} 
			else if (node.getObject() instanceof SchemeElement) {
				SchemeElement schel = (SchemeElement) node.getObject();
				if (schel.getScheme() != null) {
					Scheme scheme = schel.getScheme();
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null) {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), (element.getSchemeLinksAsArray().length != 0 || element.getSchemeElementsAsArray().length != 0)));
						} 
						else {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element));
						}
					}
				} 
				else {
					if (schel.getSchemeElementsAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_ELEMENT));
					}
					if (schel.getSchemeLinksAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_LINK))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_LINK));
					}
				}
			}*/
		}
	}
}
