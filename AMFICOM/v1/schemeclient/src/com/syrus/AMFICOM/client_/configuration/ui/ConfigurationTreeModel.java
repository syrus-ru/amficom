/*-
 * $Id: ConfigurationTreeModel.java,v 1.10 2005/09/01 13:39:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;

/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2005/09/01 13:39:18 $
 * @module schemeclient
 */

public class ConfigurationTreeModel implements ChildrenFactory, VisualManagerFactory {
	ApplicationContext aContext;
	private PopulatableIconedNode root;
	
	public ConfigurationTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public static final Object getRootObject() {
		return SchemeResourceKeys.CONFIGURATION;
	}
	
	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, SchemeResourceKeys.CONFIGURATION, LangModelScheme.getString(SchemeResourceKeys.CONFIGURATION),
					UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return this.root;
	}
	
	@SuppressWarnings("unqualified-field-access")
	public VisualManager getVisualManager(Item node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String)object;
			if (s.equals(SchemeResourceKeys.LINK_TYPE))
				return LinkTypePropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.CABLE_LINK_TYPE))
				return CableLinkTypePropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.PORT_TYPE))
				return PortTypePropertiesManager.getInstance(this.aContext, PortTypeKind.PORT_KIND_SIMPLE);
			if (s.equals(SchemeResourceKeys.CABLE_PORT_TYPE))
				return PortTypePropertiesManager.getInstance(this.aContext, PortTypeKind.PORT_KIND_CABLE);
			if (s.equals(SchemeResourceKeys.EQUIPMENT_TYPE))
				return EquipmentTypePropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.MEASUREMENT_PORT_TYPES))
				return MeasurementPortTypePropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.MEASUREMENT_TYPES))
				return MeasurementTypePropertiesManager.getInstance(aContext);
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
			return PortTypePropertiesManager.getInstance(aContext, ((PortType)object).getKind());
		if (object instanceof MeasurementPortType)
			return MeasurementPortTypePropertiesManager.getInstance(aContext);
		if (object instanceof MeasurementType)
			return MeasurementTypePropertiesManager.getInstance(aContext);
		if (object instanceof IdlKind)
				return null;
		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}
	

	public void populate(Item node) {
		Collection<Object> contents = CommonUIUtilities.getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.CONFIGURATION)) {
				createConfiguration(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.NETWORK_DIRECTORY)) {
				createNetworkDirectory(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.MONITORING_DIRECTORY)) {
				createMonitoringDirectory(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.LINK_TYPE)) {
				createLinkTypes(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.CABLE_LINK_TYPE)) {
				createCableLinkTypes(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.PORT_TYPE)) {
				createPortTypes(node, contents, PortTypeKind.PORT_KIND_SIMPLE);
			}
			else if (s.equals(SchemeResourceKeys.CABLE_PORT_TYPE)) {
				createPortTypes(node, contents, PortTypeKind.PORT_KIND_CABLE);
			}
			else if (s.equals(SchemeResourceKeys.EQUIPMENT_TYPE)) {
				createEquipmentTypes(node, contents);
			}
			// else if (s.equals("TransmissionPathType")) {
			// try {
			// EquivalentCondition condition = new
			// EquivalentCondition(ObjectEntities.TRANSPATH_TYPE_CODE);
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
			else if (s.equals(SchemeResourceKeys.MEASUREMENT_PORT_TYPES)) {
				createMeasurementPortTypes(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.MEASUREMENT_TYPES)) {
				createMeasurementTypes(node, contents);
			} 
		}
	}
	
	private void createConfiguration(Item node, Collection contents) {
		if (!contents.contains(SchemeResourceKeys.NETWORK_DIRECTORY))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.NETWORK_DIRECTORY, LangModelScheme.getString(SchemeResourceKeys.NETWORK_DIRECTORY), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.MONITORING_DIRECTORY))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.MONITORING_DIRECTORY, LangModelScheme.getString(SchemeResourceKeys.MONITORING_DIRECTORY), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
	}
	
	private void createNetworkDirectory(Item node, Collection contents) {
		if (!contents.contains(SchemeResourceKeys.LINK_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.LINK_TYPE, LangModelScheme.getString(SchemeResourceKeys.LINK_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.CABLE_LINK_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.CABLE_LINK_TYPE, LangModelScheme.getString(SchemeResourceKeys.CABLE_LINK_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.PORT_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.PORT_TYPE, LangModelScheme.getString(SchemeResourceKeys.PORT_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.CABLE_PORT_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.CABLE_PORT_TYPE, LangModelScheme.getString(SchemeResourceKeys.CABLE_PORT_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.EQUIPMENT_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.EQUIPMENT_TYPE, LangModelScheme.getString(SchemeResourceKeys.EQUIPMENT_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
	}
	
	private void createMonitoringDirectory(Item node, Collection contents) {
		if (!contents.contains(SchemeResourceKeys.MEASUREMENT_TYPES))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.MEASUREMENT_TYPES, LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_TYPES), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.MEASUREMENT_PORT_TYPES))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.MEASUREMENT_PORT_TYPES, LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_PORT_TYPES), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		// vec.add(new PopulatableIconedNode(this, "TransmissionPathType",
		// LangModelConfig.getString("menuJDirPathText"), true));
	}
	
	private void createLinkTypes(Item node, Collection contents) {
		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
			Collection<StorableObject> linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		
			Collection<Object> toAdd = CommonUIUtilities.getObjectsToAdd(linkTypes, contents);
			Collection<Item> toRemove = CommonUIUtilities.getItemsToRemove(linkTypes, node.getChildren());
			for (Item child : toRemove) {
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
	
	private void createCableLinkTypes(Item node, Collection contents) {
		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
			Collection<StorableObject> linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			
			Collection toAdd = CommonUIUtilities.getObjectsToAdd(linkTypes, contents);
			Collection<Item> toRemove = CommonUIUtilities.getItemsToRemove(linkTypes, node.getChildren());
			for (Item child : toRemove) {
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


	private void createPortTypes(Item node, Collection contents, PortTypeKind kind) {
		try {
			TypicalCondition condition = new TypicalCondition(kind.value(), 0, OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE, PortTypeWrapper.COLUMN_KIND);

			Collection<StorableObject> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			
			Collection toAdd = CommonUIUtilities.getObjectsToAdd(portTypes, contents);
			Collection<Item> toRemove = CommonUIUtilities.getItemsToRemove(portTypes, node.getChildren());
			for (Item child : toRemove) {
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
	
	private void createEquipmentTypes(Item node, Collection contents) {
		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
			Collection<StorableObject> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			
			Collection toAdd = CommonUIUtilities.getObjectsToAdd(equipmentTypes, contents);
			Collection<Item> toRemove = CommonUIUtilities.getItemsToRemove(equipmentTypes, node.getChildren());
			for (Item child : toRemove) {
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
	
	private void createMeasurementPortTypes(Item node, Collection contents) {
		try {
			EquivalentCondition condition = new EquivalentCondition(
					ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
			Collection<StorableObject> mpTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			
			Collection toAdd = CommonUIUtilities.getObjectsToAdd(mpTypes, contents);
			Collection<Item> toRemove = CommonUIUtilities.getItemsToRemove(mpTypes, node.getChildren());
			for (Item child : toRemove) {
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
	
	private void createMeasurementTypes(Item node, Collection contents) {
		if (contents.isEmpty()) {
			for (MeasurementType type : MeasurementType.values()) {
				if (!type.equals(MeasurementType.UNKNOWN)) {
					node.addChild(new PopulatableIconedNode(this, type, type.getDescription(), false));
				}
			}
		}
	}
}
