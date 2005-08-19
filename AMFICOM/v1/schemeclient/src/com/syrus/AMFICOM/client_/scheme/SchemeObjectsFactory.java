/*-
 * $Id: SchemeObjectsFactory.java,v 1.22 2005/08/19 15:41:34 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.jgraph.graph.DefaultGraphCell;
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
import com.syrus.AMFICOM.configuration.EquipmentTypeCodename;
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
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.22 $, $Date: 2005/08/19 15:41:34 $
 * @module schemeclient
 */

public class SchemeObjectsFactory {
	private static int counter = 1;
	private static int schemeCounter = 1;
	private static final String EMPTY = "";  //$NON-NLS-1$
	
	public static CharacteristicType createCharacteristicType(String name, CharacteristicTypeSort sort) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CharacteristicType cht = CharacteristicType.createInstance(userId, name, EMPTY, name, DataType.STRING, sort);
		return cht;
	}
	
	public static EquipmentType createEquipmentType(String name, EquipmentTypeCodename codeName) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		EquipmentType eqt = EquipmentType.createInstance(userId, codeName.stringValue(), EMPTY, name, EMPTY, EMPTY);
		return eqt;
	}
	
	public static MeasurementType createMeasurementType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementType type = MeasurementType.createInstance(userId, codename, EMPTY, Collections.<Identifier>emptySet(), Collections.<Identifier>emptySet(), Collections.<Identifier>emptySet());
		return type;
	}

	public static MeasurementPortType createMeasurementPortType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPortType type = MeasurementPortType.createInstance(userId, codename, EMPTY, EMPTY);
		return type;
	}
	
	public static PortType createPortType(String codename, PortTypeKind kind) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		PortType type = PortType.createInstance(userId, codename, EMPTY, EMPTY, PortTypeSort.PORTTYPESORT_OPTICAL, kind);
		return type;
	}
	
	public static LinkType createLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		LinkType type = LinkType.createInstance(userId, codename, EMPTY, codename, LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, EMPTY, EMPTY, Identifier.VOID_IDENTIFIER);
		return type;
	}
	
	public static CableLinkType createCableLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableLinkType type = CableLinkType.createInstance(userId, codename, EMPTY, codename, LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, EMPTY, EMPTY, Identifier.VOID_IDENTIFIER);
		return type;
	}
	
	public static CableThreadType createCableThreadType(String codename, String name, LinkType linkType, CableLinkType cableLinkType) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableThreadType type = CableThreadType.createInstance(userId, codename, EMPTY, name, -1, linkType, cableLinkType);
		return type;
	}
		
	public static Equipment createEquipment(SchemeElement schemeElement) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Equipment eq = Equipment.createInstance(userId, domainId, schemeElement.getEquipmentType(), schemeElement.getName(), schemeElement.getDescription(), schemeElement.getSymbol() == null ? Identifier.VOID_IDENTIFIER : schemeElement.getSymbol().getId(), EMPTY, EMPTY, 0, 0, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
		schemeElement.setEquipment(eq);
		
		Identifier equipmentId = eq.getId();
		for (SchemePort sp : schemeElement.getSchemePortsRecursively()) {
			createPort(sp, equipmentId);
		}
		for (SchemeCablePort sp : schemeElement.getSchemeCablePortsRecursively()) {
			createPort(sp, equipmentId);
		}
		for (SchemeLink sl : schemeElement.getSchemeLinks()) {
			createLink(sl);
		}
		for (SchemeElement se : schemeElement.getSchemeElements()) {
			createEquipment(se);
		}
		return eq;
	}

	public static KIS createKis() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		KIS kis = KIS.createInstance(userId, domainId, EMPTY, EMPTY, EMPTY, (short)0, null, null);
		return kis;
	}
	
	public static Link createLink(SchemeLink schemeLink) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Link link = Link.createInstance(userId, domainId, schemeLink.getName(), schemeLink.getDescription(), schemeLink.getAbstractLinkType(), EMPTY, EMPTY, EMPTY, 0, EMPTY);
		schemeLink.setAbstractLink(link);
		return link;
	}

	public static CableLink createCableLink(SchemeCableLink schemeLink) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		CableLink cableLink = CableLink.createInstance(userId, domainId, schemeLink.getName(), schemeLink.getDescription(), schemeLink.getAbstractLinkType(), EMPTY, EMPTY, EMPTY, 0, EMPTY);
		schemeLink.setAbstractLink(cableLink);
		return cableLink;
	}

	private static Port createPort(AbstractSchemePort sp, Identifier equipmentId) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Port port = Port.createInstance(userId, sp.getPortType(), sp.getName(), equipmentId);
		sp.setPort(port);
		return port;
	}
	
	public static SchemeImageResource createSchemeImageResource() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeImageResource ir = SchemeImageResource.createInstance(userId);
		return ir;
	}
	
	public static BitmapImageResource createBitmapImageResource(String codename, byte[] image) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		BitmapImageResource ir = BitmapImageResource.createInstance(userId, codename, image);
		return ir;
	}

	public static MeasurementPort createMeasurementPort(MeasurementPortType type, SchemePort port) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPort measurementPort = MeasurementPort.createInstance(userId, type, port.getName(), port.getDescription(), port.getParentSchemeDevice().getParentSchemeElement().getKis().getId(), port.getPort().getId());
		return measurementPort;
	}

	public static SchemeProtoGroup createSchemeProtoGroup() throws CreateObjectException {
		SchemeProtoGroup group = SchemeProtoGroup.createInstance(
				LoginManager.getUserId(), 
				LangModelScheme.getString("Title.group") + " (" + counter + ")");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		counter++;
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
		
		SchemeProtoElement protoElement = SchemeProtoElement.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString("Title.component") + " (" + counter + ")" );  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		protoElement.setEquipmentType(eqt);
		counter++;
		return protoElement;
	}

	public static SchemeElement createSchemeElement(Scheme parentScheme, SchemeProtoElement proto) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), proto, parentScheme);
		return schemeElement;
	}
	
	public static SchemeElement createSchemeElement(SchemeElement parentSchemeElement, SchemeProtoElement proto) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), proto, parentSchemeElement);
		return schemeElement;
	}
	
	public static SchemeElement createSchemeElement(Scheme parentScheme, Scheme scheme) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), scheme, parentScheme);
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
		
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(),
				LangModelScheme.getString("Title.component") + " (" + counter + ")",   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				parentScheme);
		schemeElement.setEquipmentType(eqt);
		counter++;
		return schemeElement;
	}
	
	public static Scheme createScheme() throws CreateObjectException {
		Scheme scheme = Scheme.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString(SchemeResourceKeys.NEW_SCHEME) + (schemeCounter == 1 ? EMPTY : " (" + schemeCounter + ")"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				IdlKind.NETWORK, LoginManager.getDomainId());
		schemeCounter++;
		return scheme;
	}

	public static SchemeDevice createSchemeDevice(String name) throws CreateObjectException {
		SchemeDevice schemeDevice = SchemeDevice.createInstance(LoginManager.getUserId(), name);
		return schemeDevice;
	}
	
	public static SchemePort createSchemePort(String name, IdlDirectionType directionType, SchemeDevice parent) throws CreateObjectException {
		SchemePort schemePort = SchemePort.createInstance(LoginManager.getUserId(), name, directionType, parent);
		return schemePort;
	}
	
	public static SchemeCablePort createSchemeCablePort(String name, IdlDirectionType directionType, SchemeDevice parent) throws CreateObjectException {
		SchemeCablePort schemePort = SchemeCablePort.createInstance(LoginManager.getUserId(), name, directionType, parent);
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
	
	public static SchemeLink createSchemeLink(String name, Scheme scheme) throws CreateObjectException {
		assert scheme != null;
		Identifier userId = LoginManager.getUserId();
		SchemeLink schemeLink = SchemeLink.createInstance(userId, name, scheme);
		return schemeLink;
	}
	
	public static SchemeCableLink createSchemeCableLink(String name, Scheme parent) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeCableLink schemeLink = SchemeCableLink.createInstance(userId, name, parent);
		return schemeLink;
	}
	
	public static SchemeMonitoringSolution createSchemeMonitoringSolution(Scheme scheme) throws CreateObjectException {
		SchemeMonitoringSolution solution = SchemeMonitoringSolution.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString("Title.solution"),   //$NON-NLS-1$
				scheme);
		return solution;
	}
	
	public static PathElement createPathElement(SchemePath path, final Identifier id) throws CreateObjectException {
		if (id.getMajor() == ObjectEntities.SCHEMELINK_CODE) {
			SchemeLink link;
			try {
				link = StorableObjectPool.getStorableObject(id, false);
			} catch (ApplicationException e) {
				throw new CreateObjectException(e);
			}
			return PathElement.createInstance(LoginManager.getUserId(), path, link);
		} else if (id.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
			PathElement pe;
		}
		
		
		throw new UnsupportedOperationException("Unknown id " + id);
	}
	
	public static SchemePath createSchemePath(SchemeMonitoringSolution solution) throws CreateObjectException {
		SchemePath path = SchemePath.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString("Title.path") + " (" + counter + ")",   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				solution);
		return path;
	}
	
	public static void assignClonedIds(Map<DefaultGraphCell, DefaultGraphCell> clonedCells, Map<Identifier, Identifier> clonedIds) {
		for (DefaultGraphCell clonedCell : clonedCells.values()) {
			if (clonedCell instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)clonedCell;
				Identifier id = dev.getElementId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				if (newId.getMajor() == ObjectEntities.SCHEMEPROTOELEMENT_CODE)
					dev.setProtoElementId(newId);
				else if (newId.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE)
					dev.setSchemeElementId(newId);
				else
					assert false : "Unknown object identifier " + newId; //$NON-NLS-1$
			} else if (clonedCell instanceof DeviceCell) {
				DeviceCell cell = (DeviceCell)clonedCell;
				Identifier id = cell.getSchemeDeviceId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				cell.setSchemeDeviceId(newId);
			} else if (clonedCell instanceof PortCell) {
				PortCell cell = (PortCell)clonedCell;
				Identifier id = cell.getSchemePortId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				cell.setSchemePortId(newId);
			} else if (clonedCell instanceof CablePortCell) {
				CablePortCell cell = (CablePortCell)clonedCell;
				Identifier id = cell.getSchemeCablePortId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				cell.setSchemeCablePortId(newId);
			} else if (clonedCell instanceof DefaultCableLink) {
				DefaultCableLink cell = (DefaultCableLink)clonedCell;
				Identifier id = cell.getSchemeCableLinkId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				cell.setSchemeCableLinkId(newId);
			} else if (clonedCell instanceof DefaultLink) {
				DefaultLink cell = (DefaultLink)clonedCell;
				Identifier id = cell.getSchemeLinkId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				cell.setSchemeLinkId(newId);
			} else if (clonedCell instanceof BlockPortCell) {
				BlockPortCell cell = (BlockPortCell)clonedCell;
				Identifier id = cell.getAbstractSchemePortId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINEST);
					newId = id;
				}
				cell.setAbstractSchemePortId(newId);
			}
		}
	}
}
