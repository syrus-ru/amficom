/*-
 * $Id: SchemeObjectsFactory.java,v 1.14 2005/07/24 18:13:40 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/07/24 18:13:40 $
 * @module schemeclient_v1
 */

public class SchemeObjectsFactory {
	private static int counter = 1;
	private static int schemeCounter = 1;
	
	public static CharacteristicType createCharacteristicType(String name, CharacteristicTypeSort sort) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CharacteristicType cht = CharacteristicType.createInstance(userId, name, "", name, DataType.STRING, sort);
//		try {
//			StorableObjectPool.putStorableObject(cht);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return cht;
	}
	
	public static EquipmentType createEquipmentType(String name, String codeName) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		EquipmentType eqt = EquipmentType.createInstance(userId, codeName, "", name, "", "");
//		try {
//			StorableObjectPool.putStorableObject(eqt);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return eqt;
	}
	
	public static MeasurementType createMeasurementType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementType type = MeasurementType.createInstance(userId, codename, "", Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET);
//		try {
//			StorableObjectPool.putStorableObject(type);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return type;
	}

	public static MeasurementPortType createMeasurementPortType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPortType type = MeasurementPortType.createInstance(userId, codename, "", "");
//		try {
//			StorableObjectPool.putStorableObject(type);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return type;
	}
	
	public static PortType createPortType(String codename, PortTypeKind kind) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		PortType type = PortType.createInstance(userId, codename, "", "", PortTypeSort.PORTTYPESORT_OPTICAL, kind);
//		try {
//			StorableObjectPool.putStorableObject(type);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return type;
	}
	
	public static LinkType createLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		LinkType type = LinkType.createInstance(userId, codename, "", codename, LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", Identifier.VOID_IDENTIFIER);
//		try {
//			StorableObjectPool.putStorableObject(type);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return type;
	}
	
	public static CableLinkType createCableLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableLinkType type = CableLinkType.createInstance(userId, codename, "", codename, LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", Identifier.VOID_IDENTIFIER);
//		try {
//			StorableObjectPool.putStorableObject(type);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return type;
	}
	
	public static CableThreadType createCableThreadType(String codename, String name, LinkType linkType, CableLinkType cableLinkType) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableThreadType type = CableThreadType.createInstance(userId, codename, "", name, -1, linkType, cableLinkType);
//		try {
//			StorableObjectPool.putStorableObject(type);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return type;
	}
		
	public static Equipment createEquipment(SchemeElement schemeElement) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Equipment eq = Equipment.createInstance(userId, domainId, schemeElement.getEquipmentType(), schemeElement.getName(), schemeElement.getDescription(), schemeElement.getSymbol() == null ? Identifier.VOID_IDENTIFIER : schemeElement.getSymbol().getId(), "", "", 0, 0, "", "", "", "", "");
		schemeElement.setEquipment(eq);
		
		Identifier equipmentId = eq.getId();
		for (SchemePort sp : schemeElement.getSchemePorts()) {
			createPort(sp, equipmentId);
		}
		for (SchemeCablePort sp : schemeElement.getSchemeCablePorts()) {
			createPort(sp, equipmentId);
		}
		for (SchemeLink sl : schemeElement.getSchemeLinks()) {
			createLink(sl);
		}
		for (SchemeElement se : schemeElement.getSchemeElements()) {
			createEquipment(se);
		}
		
//		try {
//			StorableObjectPool.putStorableObject(eq);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return eq;
	}

	public static KIS createKis() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		KIS kis = KIS.createInstance(userId, domainId, "", "", "", (short)0, null, null);
//		try {
//			StorableObjectPool.putStorableObject(kis);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return kis;
	}
	
	public static Link createLink(SchemeLink schemeLink) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Link link = Link.createInstance(userId, domainId, schemeLink.getName(), schemeLink.getDescription(), schemeLink.getAbstractLinkType(), "", "", "", 0, "");
		schemeLink.setAbstractLink(link);
//		try {
//			StorableObjectPool.putStorableObject(link);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return link;
	}

	public static CableLink createCableLink(SchemeCableLink schemeLink) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		CableLink cableLink = CableLink.createInstance(userId, domainId, schemeLink.getName(), schemeLink.getDescription(), schemeLink.getAbstractLinkType(), "", "", "", 0, "");
		schemeLink.setAbstractLink(cableLink);
//		try {
//			StorableObjectPool.putStorableObject(cableLink);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return cableLink;
	}

	private static Port createPort(AbstractSchemePort sp, Identifier equipmentId) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Port port = Port.createInstance(userId, sp.getPortType(), sp.getName(), equipmentId);
		sp.setPort(port);
//		try {
//			StorableObjectPool.putStorableObject(port);
//		} catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return port;
	}
	
	public static SchemeImageResource createSchemeImageResource() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeImageResource ir = SchemeImageResource.createInstance(userId);
//		try {
//			StorableObjectPool.putStorableObject(ir);
//		} catch (ApplicationException e) {
//			 Log.debugException(e, Level.SEVERE);
//		}
		return ir;
	}
	
	public static BitmapImageResource createBitmapImageResource(String codename, byte[] image) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		BitmapImageResource ir = BitmapImageResource.createInstance(userId, codename, image);
//		try {
//			StorableObjectPool.putStorableObject(ir);
//		} catch (ApplicationException e) {
//			 Log.debugException(e, Level.SEVERE);
//		}
		return ir;
	}

	public static MeasurementPort createMeasurementPort(MeasurementPortType type, SchemePort port) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPort measurementPort = MeasurementPort.createInstance(userId, type, port.getName(), port.getDescription(), port.getParentSchemeDevice().getParentSchemeElement().getKis().getId(), port.getPort().getId());
//		try {
//			StorableObjectPool.putStorableObject(measurementPort);
//		} 
//		catch (IllegalObjectEntityException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return measurementPort;
	}

	public static SchemeProtoGroup createSchemeProtoGroup() throws CreateObjectException {
		SchemeProtoGroup group = SchemeProtoGroup.createInstance(LoginManager.getUserId(), "Группа (" + counter + ")");
		counter++;
//		try {
//			StorableObjectPool.putStorableObject(group);
//		} 
//		catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		return group;
	}
	
	public static SchemeProtoElement createSchemeProtoElement() throws CreateObjectException {
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
		EquipmentType eqt = null;
		try {
			Set<EquipmentType> eqTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (!eqTypes.isEmpty())
				eqt = eqTypes.iterator().next();
		} catch (ApplicationException e2) {
			throw new CreateObjectException(e2);
		}
		if (eqt == null) {
			String error = "No equipment types found. Create one at least."; //$NON-NLS-1$
			Log.debugMessage(error, Level.WARNING); 
			throw new CreateObjectException(error);
		}
		
		SchemeProtoElement protoElement = SchemeProtoElement.createInstance(LoginManager.getUserId(), "Компонент (" + counter + ")" );
		protoElement.setEquipmentType(eqt);
		counter++;
//		try {
//			StorableObjectPool.putStorableObject(protoElement);
//		} 
//		catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		return protoElement;
	}

	public static SchemeElement createSchemeElement(Scheme parentScheme, SchemeProtoElement proto) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), proto.getName(), proto.getDescription(), proto.getLabel(), proto.getEquipmentType(), null, null, null, proto.getSymbol(), proto.getUgoCell(), proto.getSchemeCell(), parentScheme);

		
		//FIXME детей сделать
		
//		try {
//			StorableObjectPool.putStorableObject(schemeElement);
//		} 
//		catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		return schemeElement;
	}
	
	public static SchemeElement createSchemeElement(Scheme parentScheme) throws CreateObjectException {
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
		EquipmentType eqt = null;
		try {
			Set<EquipmentType> eqTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (!eqTypes.isEmpty())
				eqt = eqTypes.iterator().next();
		} catch (ApplicationException e2) {
			throw new CreateObjectException(e2);
		}
		if (eqt == null) {
			String error = "No equipment types found. Create one at least."; //$NON-NLS-1$
			Log.debugMessage(error, Level.WARNING); 
			throw new CreateObjectException(error);
		}
		
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), "Компонент (" + counter + ")", parentScheme);
		schemeElement.setEquipmentType(eqt);
		counter++;
//		try {
//			StorableObjectPool.putStorableObject(schemeElement);
//		} 
//		catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		return schemeElement;
	}
	
	public static Scheme createScheme() throws CreateObjectException {
		Scheme scheme = Scheme.createInstance(LoginManager.getUserId(), LangModelScheme.getString(SchemeResourceKeys.NEW_SCHEME)
				+ (schemeCounter == 1 ? "" : "(" + schemeCounter + ")"), IdlKind.NETWORK, LoginManager.getDomainId());
		schemeCounter++;
//		try {
//			StorableObjectPool.putStorableObject(scheme);
//		} 
//		catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		return scheme;
	}

	public static SchemeDevice createSchemeDevice(String name) throws CreateObjectException {
		SchemeDevice schemeDevice = SchemeDevice.createInstance(LoginManager.getUserId(), name);
//		try {
//			StorableObjectPool.putStorableObject(schemeDevice);
//		} 
//		catch (ApplicationException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return schemeDevice;
	}
	
	public static SchemePort createSchemePort(String name, IdlDirectionType directionType, SchemeDevice parent) throws CreateObjectException {
		SchemePort schemePort = SchemePort.createInstance(LoginManager.getUserId(), name, directionType, parent);
//		try {
//			StorableObjectPool.putStorableObject(schemePort);
//		} 
//		catch (ApplicationException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return schemePort;
	}
	
	public static SchemeCablePort createSchemeCablePort(String name, IdlDirectionType directionType, SchemeDevice parent) throws CreateObjectException {
		SchemeCablePort schemePort = SchemeCablePort.createInstance(LoginManager.getUserId(), name, directionType, parent);
//		try {
//			StorableObjectPool.putStorableObject(schemePort);
//		} 
//		catch (ApplicationException e) {
//			Log.debugException(e, Level.SEVERE);
//		}
		return schemePort;
	}
	
	public static SchemeLink createSchemeLink(String name) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeLink schemeLink = SchemeLink.createInstance(userId, name);
		return schemeLink;
	}
	
	public static SchemeLink createSchemeLink(String name, SchemeElement schemeElement) throws CreateObjectException {
		assert schemeElement != null;
		Identifier userId = LoginManager.getUserId();
		SchemeLink schemeLink = SchemeLink.createInstance(userId, name, schemeElement);
		return schemeLink;
	}
	
	public static SchemeCableLink createSchemeCableLink(String name, Scheme parent) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeCableLink schemeLink = SchemeCableLink.createInstance(userId, name, parent);
//		try {
//			StorableObjectPool.putStorableObject(schemeLink);
//		} 
//		catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		return schemeLink;
	}
	
	public static void assignClonedIds(Map<DefaultGraphCell, DefaultGraphCell> clonedCells, Map<Identifier, Identifier> clonedIds) {
		for (DefaultGraphCell clonedCell : clonedCells.values()) {
			if (clonedCell instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)clonedCell;
				Identifier id = dev.getElementId();
				if (dev.getType() == DeviceGroup.PROTO_ELEMENT) {
					dev.setProtoElementId(clonedIds.get(id));
				} else {
					dev.setSchemeElementId(clonedIds.get(id));
				}
			} else if (clonedCell instanceof DeviceCell) {
				DeviceCell cell = (DeviceCell)clonedCell;
				Identifier id = cell.getSchemeDeviceId();
				cell.setSchemeDeviceId(clonedIds.get(id));
			} else if (clonedCell instanceof PortCell) {
				PortCell cell = (PortCell)clonedCell;
				Identifier id = cell.getSchemePortId();
				cell.setSchemePortId(clonedIds.get(id));
			} else if (clonedCell instanceof CablePortCell) {
				CablePortCell cell = (CablePortCell)clonedCell;
				Identifier id = cell.getSchemeCablePortId();
				cell.setSchemeCablePortId(clonedIds.get(id));
			} else if (clonedCell instanceof DefaultCableLink) {
				DefaultCableLink cell = (DefaultCableLink)clonedCell;
				Identifier id = cell.getSchemeCableLinkId();
				cell.setSchemeCableLinkId(clonedIds.get(id));
			} else if (clonedCell instanceof DefaultLink) {
				DefaultLink cell = (DefaultLink)clonedCell;
				Identifier id = cell.getSchemeLinkId();
				cell.setSchemeLinkId(clonedIds.get(id));
			} else if (clonedCell instanceof BlockPortCell) {
				BlockPortCell cell = (BlockPortCell)clonedCell;
				Identifier id = cell.getAbstractSchemePortId();
				cell.setAbstractSchemePortId(clonedIds.get(id));
			}
		}
	}
}
