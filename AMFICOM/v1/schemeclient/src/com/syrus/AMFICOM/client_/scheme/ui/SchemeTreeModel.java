/*
 * $Id: SchemeTreeModel.java,v 1.8 2005/03/23 14:56:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/03/23 14:56:39 $
 * @module schemeclient_v1
 */

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemeController;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.SchemeKind;

public class SchemeTreeModel implements SOTreeDataModel {
	ApplicationContext aContext;

	private static SchemeKind[] schemeTypes = new SchemeKind[] { SchemeKind.NETWORK, SchemeKind.BUILDING,
			SchemeKind.CABLE_SUBNETWORK };

	public SchemeTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Color getNodeColor(SONode node) {
		return Color.BLACK;
	}

	public Icon getNodeIcon(SONode node) {
		if (node.getUserObject() instanceof String) {
			String s = (String) node.getUserObject();
			if (s.equals(Constants.SCHEME))
				return Constants.SCHEME_ICON;
			return Constants.CATALOG_ICON;
		}
		if (node.getUserObject() instanceof SchemeKind)
			return Constants.CATALOG_ICON;
		return null;
	}

	public String getNodeName(SONode node) {
		if (node.getUserObject() instanceof String) {
			String s = (String) node.getUserObject();
			if (s.equals(Constants.ROOT))
				return Constants.TEXT_ROOT;
			if (s.equals(Constants.CONFIGURATION))
				return Constants.TEXT_CONFIGURATION;
			if (s.equals(Constants.SCHEME_PROTO_GROUP))
				return Constants.TEXT_SCHEME_PROTO_GROUP;
			if (s.equals(Constants.SCHEME_TYPE))
				return Constants.TEXT_SCHEME_TYPE;
			if (s.equals(Constants.NETWORK_DIRECTORY))
				return Constants.TEXT_NETWORK_DIRECTORY;
			if (s.equals(Constants.MONITORING_DIRECTORY))
				return Constants.TEXT_MONITORING_DIRECTORY;
			if (s.equals(Constants.LINK_TYPE))
				return Constants.TEXT_LINK_TYPE;
			if (s.equals(Constants.CABLE_LINK_TYPE))
				return Constants.TEXT_CABLE_LINK_TYPE;
			if (s.equals(Constants.PORT_TYPE))
				return Constants.TEXT_PORT_TYPE;
			// if(s.equals("TransmissionPathType"))
			// return LangModelConfig.getString("menuJDirPathText");
			if (s.equals(Constants.MEASUREMENTPORT_TYPE))
				return Constants.TEXT_MEASUREMENTPORT_TYPE;
			if (s.equals(Constants.MEASUREMENT_TYPE))
				return Constants.TEXT_MEASUREMENT_TYPE;
			if (s.equals(SchemeKind.NETWORK))
				return Constants.TEXT_SCHEME_TYPE_NETWORK;
			if (s.equals(SchemeKind.BUILDING))
				return Constants.TEXT_SCHEME_TYPE_BUILDING;
			if (s.equals(SchemeKind.CABLE_SUBNETWORK))
				return Constants.TEXT_SCHEME_TYPE_CABLE;
		}
		if (node.getUserObject() instanceof SchemeKind) {
			SchemeKind type = (SchemeKind)node.getUserObject();
			switch (type.value()) {
			case SchemeKind._NETWORK:
				return Constants.TEXT_SCHEME_TYPE_NETWORK;
			case SchemeKind._CABLE_SUBNETWORK:
				return Constants.TEXT_SCHEME_TYPE_CABLE;
			case SchemeKind._BUILDING:
				return Constants.TEXT_SCHEME_TYPE_BUILDING;
			default:
				throw new UnsupportedOperationException("Unknown scheme type");
			}
		}
		if (node.getUserObject() instanceof LinkType)
			return ((LinkType) node.getUserObject()).getName();
		if (node.getUserObject() instanceof CableLinkType)
			return ((CableLinkType) node.getUserObject()).getName();
		if (node.getUserObject() instanceof PortType)
			return ((PortType) node.getUserObject()).getName();
		if (node.getUserObject() instanceof MeasurementPortType)
			return ((MeasurementPortType) node.getUserObject()).getName();
		if (node.getUserObject() instanceof MeasurementType)
			return ((MeasurementType) node.getUserObject()).getDescription();

		if (node.getUserObject() instanceof SchemeProtoGroup)
			return ((SchemeProtoGroup)node.getUserObject()).getName();
		if (node.getUserObject() instanceof Scheme)
			return ((Scheme)node.getUserObject()).getName();
		if (node.getUserObject() instanceof SchemeElement)
			return ((SchemeElement)node.getUserObject()).getName();
		if (node.getUserObject() instanceof SchemeLink)
			return ((SchemeLink)node.getUserObject()).getName();
		if (node.getUserObject() instanceof SchemeCableLink)
			return ((SchemeCableLink)node.getUserObject()).getName();
		if (node.getUserObject() instanceof SchemePath)
			return ((SchemePath)node.getUserObject()).getName();
		if (node.getUserObject() instanceof SchemePort)
			return ((SchemePort)node.getUserObject()).getName();
		if (node.getUserObject() instanceof SchemeCablePort)
			return ((SchemeCablePort)node.getUserObject()).getName();
		throw new UnsupportedOperationException("Unknown object");
	}

	public ObjectResourceController getNodeController(SONode node) {
		if (node.getUserObject() instanceof String) {
			String s = (String) node.getUserObject();
			if (s.equals(Constants.SCHEME))
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
				return null;
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
			if (s.equals(Constants.LINK_TYPE))
				return LinkTypeController.getInstance();
			if (s.equals(Constants.CABLE_LINK_TYPE))
				return CableLinkTypeController.getInstance();
			if (s.equals(Constants.PORT_TYPE))
				return PortTypeController.getInstance();
			// if(s.equals("TransmissionPathType"))
			// return TransmissionPathTypeController.getInstance();
			if (s.equals(Constants.MEASUREMENTPORT_TYPE))
				return MeasurementPortTypeController.getInstance();
			if (s.equals(Constants.MEASUREMENT_TYPE))
				return MeasurementTypeController.getInstance();
			if (s.equals(Constants.SCHEME_TYPE))
				return null;
			return null;
		}
		if (node.getUserObject() instanceof SchemeKind)
			return SchemeController.getInstance();
		if (node.getUserObject() instanceof LinkType)
			return LinkTypeController.getInstance();
		if (node.getUserObject() instanceof CableLinkType)
			return CableLinkTypeController.getInstance();
		if (node.getUserObject() instanceof PortType)
			return PortTypeController.getInstance();
		if (node.getUserObject() instanceof TransmissionPathType)
			return TransmissionPathTypeController.getInstance();
		if (node.getUserObject() instanceof MeasurementPortType)
			return MeasurementPortTypeController.getInstance();
		if (node.getUserObject() instanceof MeasurementType)
			return MeasurementTypeController.getInstance();

		if (node.getUserObject() instanceof SchemeProtoGroup) {
			if (!((SchemeProtoGroup) node.getUserObject()).getSchemeProtoGroups().isEmpty())
				return null;
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
			else
				return null;
			/**
			 * @todo write SchemeProtoElementController return
			 *       SchemeProtoElementController.getInstance();
			 */
		}
		if (node.getUserObject() instanceof Scheme)
			return SchemeController.getInstance();
		if (node.getUserObject() instanceof SchemeElement)
			return SchemeElementController.getInstance();
		throw new UnsupportedOperationException("Unknown object");
	}

	public void updateChildNodes(SONode node) {
		if(!node.isExpanded())
			return;
		List contents = node.getChildrenUserObjects();
		
		if (node.getUserObject() instanceof String) {
			String s = (String) node.getUserObject();
			if (s.equals(Constants.ROOT)) {
				if (!contents.contains(Constants.CONFIGURATION))
					node.add(new SOCheckableNode(this, Constants.CONFIGURATION, true, true));
				if (!contents.contains(Constants.SCHEME_TYPE))
					node.add(new SOMutableNode(this, Constants.SCHEME_TYPE));
				if (!contents.contains(Constants.SCHEME_PROTO_GROUP))
					node.add(new SOMutableNode(this, Constants.SCHEME_PROTO_GROUP));
			} 
			else if (s.equals(Constants.CONFIGURATION)) {
				if (!contents.contains(Constants.NETWORK_DIRECTORY))
					node.add(new SOCheckableNode(this, Constants.NETWORK_DIRECTORY));
				if (!contents.contains(Constants.MONITORING_DIRECTORY))
					node.add(new SOMutableNode(this, Constants.MONITORING_DIRECTORY));
			} 
			else if (s.equals(Constants.NETWORK_DIRECTORY)) {
				if (!contents.contains(Constants.LINK_TYPE))
					node.add(new SOCheckableNode(this, Constants.LINK_TYPE));
				if (!contents.contains(Constants.CABLE_LINK_TYPE))
					node.add(new SOMutableNode(this, Constants.CABLE_LINK_TYPE));
				if (!contents.contains(Constants.PORT_TYPE))
					node.add(new SOMutableNode(this, Constants.PORT_TYPE));
			} 
			else if (s.equals(Constants.MONITORING_DIRECTORY)) {
				if (!contents.contains(Constants.MEASUREMENT_TYPE))
					node.add(new SOMutableNode(this, Constants.MEASUREMENT_TYPE));
				if (!contents.contains(Constants.MEASUREMENTPORT_TYPE))
					node.add(new SOMutableNode(this, Constants.MEASUREMENTPORT_TYPE));
				// vec.add(new SOMutableNode(this, "TransmissionPathType",
				// LangModelConfig.getString("menuJDirPathText"), true));
			} 
			else if (s.equals(Constants.SCHEME_TYPE)) {
				for (int i = 0; i < schemeTypes.length; i++) {
					if (!contents.contains(schemeTypes[i]))
						node.add(new SOMutableNode(this, schemeTypes[i]));
				}
			} 
			else if (s.equals(Constants.LINK_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.LINKTYPE_ENTITY_CODE);

					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = linkTypes.iterator(); it.hasNext();) {
						LinkType type = (LinkType) it.next();
						if (!contents.contains(type))
							node.add(new SOCheckableNode(this, type, false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.CABLE_LINK_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.CABLELINKTYPE_ENTITY_CODE);

					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = linkTypes.iterator(); it.hasNext();) {
						CableLinkType type = (CableLinkType) it.next();
						if (!contents.contains(type))
							node.add(new SOMutableNode(this, type, false));
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
					for (Iterator it = portTypes.iterator(); it.hasNext();) {
						PortType type = (PortType) it.next();
						if (!contents.contains(type))
							node.add(new SOMutableNode(this, type, false));
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
			// vec.add(new SOMutableNode(this, type, false));
			// }
			// }
			// catch (ApplicationException ex) {
			// ex.printStackTrace();
			// }
			// }
			else if (s.equals(Constants.MEASUREMENTPORT_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext();) {
						MeasurementPortType type = (MeasurementPortType) it.next();
						if (!contents.contains(type))
							node.add(new SOMutableNode(this, type, false));
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.MEASUREMENT_TYPE)) {
				try {
					EquivalentCondition condition = new EquivalentCondition(
							ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					Collection measurementTypes = MeasurementStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
						MeasurementType type = (MeasurementType) it.next();
						if (!contents.contains(type))
							node.add(new SOMutableNode(this, type, false));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.SCHEME_PROTO_GROUP)) {
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo) aContext
							.getSessionInterface()).getAccessIdentifier().domain_id);
					LinkedIdsCondition condition = new LinkedIdsCondition(domain_id,
							ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
					List groups = SchemeStorableObjectPool.getStorableObjectsByCondition(
							condition, true);

					for (Iterator it = groups.iterator(); it.hasNext();) {
						SchemeProtoGroup group = (SchemeProtoGroup) it.next();
						if (!contents.contains(group))
							node.add(new SOMutableNode(this, group));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.SCHEME)) {
				Scheme parent = (Scheme) ((SONode) node.getParent()).getUserObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.getSchemeElementsAsArray().length; i++) {
					SchemeElement el = parent.getSchemeElementsAsArray()[i];
					if (el.getInnerScheme() != null)
						ds.add(el.getInnerScheme());
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						Scheme sch = (Scheme) it.next();
						if (!contents.contains(sch))
							node.add(new SOMutableNode(this, sch));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_ELEMENT)) {
				Object parent = ((SONode) node.getParent()).getUserObject();
				List ds = new LinkedList();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getInnerScheme() == null)
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
								node.add(new SOMutableNode(this, element, true));
						}
						else {
							if (!contents.contains(element))
								node.add(new SOMutableNode(this, element, false));
						}
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_LINK)) {
				Object parent = ((SONode) node.getParent()).getUserObject();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = scheme.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.add(new SOMutableNode(this, link, false));
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					for (int i = 0; i < el.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = el.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.add(new SOMutableNode(this, link, false));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_CABLELINK)) {
				Scheme parent = (Scheme) ((SONode) node.getParent()).getUserObject();
				for (int i = 0; i < parent.getSchemeCableLinksAsArray().length; i++) {
					SchemeCableLink link = parent.getSchemeCableLinksAsArray()[i];
					if (!contents.contains(link))
						node.add(new SOMutableNode(this, link, false));
				}
			} 
			else if (s.equals(Constants.SCHEME_PATH)) {
				Scheme parent = (Scheme) ((SONode) node.getParent()).getUserObject();
				for (int i = 0; i < parent.getCurrentSchemeMonitoringSolution().schemePaths().length; i++) {
					SchemePath path = parent.getCurrentSchemeMonitoringSolution().schemePaths()[i];
					if (!contents.contains(path))
						node.add(new SOMutableNode(this, path, false));
				}
			}
		} 
		else {
			if (node.getUserObject() instanceof SchemeKind) {
				SchemeKind type = (SchemeKind) node.getUserObject();
				TypicalCondition condition = new TypicalCondition(String.valueOf(type
						.value()), OperationSort.OPERATION_EQUALS,
						ObjectEntities.SCHEME_ENTITY_CODE,
						com.syrus.AMFICOM.scheme.SchemeController.COLUMN_TYPE);
				try {
					List schemes = SchemeStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext();) {
						Scheme sc = (Scheme) it.next();
						if (!contents.contains(sc))
							node.add(new SOMutableNode(this, sc));
					}
				} 
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}
			if (node.getUserObject() instanceof SchemeProtoGroup) {
				SchemeProtoGroup parent_group = (SchemeProtoGroup) node.getUserObject();
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
						node.add(new SOMutableNode(this, map_group, !map_group.getSchemeProtoGroups().isEmpty() || !map_group.getSchemeProtoElements().isEmpty()));
				}
				if (parent_group.getSchemeProtoGroups().isEmpty()) {
					for (final Iterator schemeProtoElementIterator = parent_group.getSchemeProtoElements().iterator(); schemeProtoElementIterator.hasNext();) {
						final SchemeProtoElement schemeProtoElement = (SchemeProtoElement) schemeProtoElementIterator.next();
						// schemeProtoElement.parent(parent_group);
						if (! contents.contains(schemeProtoElement))
							node.add(new SOMutableNode(this, schemeProtoElement, false));
					}
				}
			} 
			else if (node.getUserObject() instanceof Scheme) {
				Scheme s = (Scheme) node.getUserObject();
				if (s.getSchemeElementsAsArray().length != 0) {
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getInnerScheme() == null) {
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getInnerScheme() != null) {
							has_schemes = true;
							break;
						}
					}
					if (has_schemes) {
						if (!contents.contains(Constants.SCHEME))
							node.add(new SOMutableNode(this, Constants.SCHEME));
					}
					if (has_elements) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.add(new SOMutableNode(this, Constants.SCHEME_ELEMENT));
					}
				}
				if (s.getSchemeLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_LINK))
						node.add(new SOMutableNode(this, Constants.SCHEME_LINK));
				}
				if (s.getSchemeCableLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_CABLELINK))
						node.add(new SOMutableNode(this, Constants.SCHEME_CABLELINK));
				}
				if (s.getCurrentSchemeMonitoringSolution().schemePaths().length != 0) {
					if (!contents.contains(Constants.SCHEME_PATH))
						node.add(new SOMutableNode(this, Constants.SCHEME_PATH));
				}
			} 
			else if (node.getUserObject() instanceof SchemeElement) {
				SchemeElement schel = (SchemeElement) node.getUserObject();
				if (schel.getInnerScheme() != null) {
					Scheme scheme = schel.getInnerScheme();
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getInnerScheme() == null) {
							if (!contents.contains(element))
								node.add(new SOMutableNode(this, element, (element.getSchemeLinksAsArray().length != 0 || element.getSchemeElementsAsArray().length != 0)));
						} 
						else {
							if (!contents.contains(element))
								node.add(new SOMutableNode(this, element));
						}
					}
				} 
				else {
					if (schel.getSchemeElementsAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.add(new SOMutableNode(this, Constants.SCHEME_ELEMENT));
					}
					if (schel.getSchemeLinksAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_LINK))
							node.add(new SOMutableNode(this, Constants.SCHEME_LINK));
					}
				}
			}
		}
	}
}
