/*-
 * $Id: SchemeObjectsFactory.java,v 1.58 2006/04/07 13:53:02 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
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
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
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
 * @author $Author: arseniy $
 * @version $Revision: 1.58 $, $Date: 2006/04/07 13:53:02 $
 * @module schemeclient
 */

public class SchemeObjectsFactory {
	private static int counter = 1;
	private static int schemeCounter = 1;
	private static final String EMPTY = "";  //$NON-NLS-1$
	
	private static SchemeProtoElement stubProtoElement;
	private static SchemeProtoGroup stubProtoGroup;
	private static String stubName = "should not see me";
	
	// caching of types of creating objects
	static Identifier cachedCableLinkTypeId = null;
	static Identifier cachedLinkTypeId = null;
	static Identifier cachedCablePortTypeId = null;
	static Identifier cachedPortTypeId = null;
	
	private static ApplicationContext aContext;
	
	// TODO add objects here when deleted and flush on exit
//	Set<Identifier> deletedObjects = new HashSet<Identifier>();

	/**
	 * if not initialyzed return null
	 * @return stubProtoElement
	 */
	public static SchemeProtoElement getStubProtoElement() {
		return stubProtoElement;
	}
	
	/**
	 * if not initialyzed return null
	 * @return stubProtoGroup 
	 */
	public static SchemeProtoGroup getStubProtoGroup() {
		return stubProtoGroup;
	}
	
	/**
	 * initialyze stubProtoElement and return it
	 * @return stubProtoElement
	 */
	public static SchemeProtoElement getStubProtoElementInitialyzed() {
		if (stubProtoElement == null) {
			Identifier userId = LoginManager.getUserId();
			try {
				stubProtoGroup = SchemeProtoGroup.createInstance(userId, stubName);
				stubProtoElement = SchemeProtoElement.createInstance(userId, stubName, stubProtoGroup);
			} catch (CreateObjectException e) {
				Log.errorMessage(e);
			}
		}
		return stubProtoElement;
	}
	
	/**
	 * initialyze stubProtoGroup and return it
	 * @return stubProtoGroup 
	 */
	public static SchemeProtoGroup getStubProtoGroupInitialyzed() {
		if (stubProtoGroup == null) {
			Identifier userId = LoginManager.getUserId();
			try {
				stubProtoGroup = SchemeProtoGroup.createInstance(userId, stubName);
				stubProtoElement = SchemeProtoElement.createInstance(userId, stubName, stubProtoGroup);
			} catch (CreateObjectException e) {
				Log.errorMessage(e);
			}
		}
		return stubProtoGroup;
	}
	
	public static void init(final ApplicationContext aContext1) {
		aContext = aContext1;
		aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, 
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						ObjectSelectedEvent ose = (ObjectSelectedEvent)evt;
						if (ose.isSelected(ObjectSelectedEvent.CABLELINK_TYPE)) {
							cachedCableLinkTypeId = ((Identifiable)ose.getSelectedObject()).getId();	
						} else if (ose.isSelected(ObjectSelectedEvent.LINK_TYPE)) {
							cachedLinkTypeId = ((Identifiable)ose.getSelectedObject()).getId();	
						} else if (ose.isSelected(ObjectSelectedEvent.CABLEPORT_TYPE)) {
							cachedCablePortTypeId = ((Identifiable)ose.getSelectedObject()).getId();	
						} else if (ose.isSelected(ObjectSelectedEvent.PORT_TYPE)) {
							cachedPortTypeId = ((Identifiable)ose.getSelectedObject()).getId();	
						}
					}
		});
		
		aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, 
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						SchemeEvent ev = (SchemeEvent)evt;
						if (ev.isType(SchemeEvent.UPDATE_OBJECT)) {
							Identifier id = ev.getIdentifier();
							try {
								if (id.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE) {
									SchemeCableLink link = (SchemeCableLink)ev.getStorableObject();
									cachedCableLinkTypeId = link.getAbstractLinkTypeId();
								} else if (id.getMajor() == ObjectEntities.SCHEMELINK_CODE) {
									SchemeLink link = (SchemeLink)ev.getStorableObject();
									cachedLinkTypeId = link.getAbstractLinkType().getId();
								} else if (id.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
									SchemeCablePort port = (SchemeCablePort)ev.getStorableObject();
									cachedCablePortTypeId = port.getPortType().getId();
								} else if (id.getMajor() == ObjectEntities.SCHEMEPORT_CODE) {
									SchemePort port = (SchemePort)ev.getStorableObject();
									cachedPortTypeId = port.getPortType().getId();
								}
							} catch (ApplicationException e) {
								Log.errorMessage(e);
							}
						}
					}
				}
		);
	}
	
	public static CharacteristicType createCharacteristicType(String name, IdlCharacteristicTypeSort sort) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CharacteristicType type = CharacteristicType.createInstance(userId, name, EMPTY, name, DataType.STRING, CharacteristicTypeSort.valueOf(sort));
		type.setCodename(type.getId().getIdentifierString());
		return type;
	}
	
	public static ProtoEquipment createProtoEquipment(String name, EquipmentType type) throws CreateObjectException {
		ProtoEquipment protoEqt = ProtoEquipment.createInstance(LoginManager.getUserId(), type.getId(), name, EMPTY, EMPTY, EMPTY);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, protoEqt.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return protoEqt;
	}
	
//	public static EquipmentType createEquipmentType(String name, EquipmentTypeCodename codeName) throws CreateObjectException {
//		Identifier userId = LoginManager.getUserId();
//		EquipmentType eqt = EquipmentType.createInstance(userId, codeName.stringValue(), EMPTY, name, EMPTY, EMPTY);
//		return eqt;
//	}
	
//	public static MeasurementType createMeasurementType(String codename) throws CreateObjectException {
//		Identifier userId = LoginManager.getUserId();
//		MeasurementType type = MeasurementType.createInstance(userId,
//				codename,
//				EMPTY,
//				EnumSet.noneOf(ParameterType.class),
//				EnumSet.noneOf(ParameterType.class),
//				Collections.<Identifier> emptySet());
//		return type;
//	}

	public static MeasurementPortType createMeasurementPortType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPortType type = MeasurementPortType.createInstance(userId, codename, EMPTY, codename, EnumSet.noneOf(MeasurementType.class));
		type.setCodename(type.getId().getIdentifierString());
		
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, type.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return type;
	}
	
	public static PortType createPortType(String codename, PortTypeKind kind) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		PortType type = PortType.createInstance(userId, codename, EMPTY, codename, PortTypeSort.PORTTYPESORT_OPTICAL, kind);
		type.setCodename(type.getId().getIdentifierString());
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, type.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return type;
	}
	
	public static LinkType createLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		LinkType type = LinkType.createInstance(userId, codename, EMPTY, codename, LinkTypeSort.LINKTYPESORT_OPTICAL, EMPTY, EMPTY, Identifier.VOID_IDENTIFIER);
		type.setCodename(type.getId().getIdentifierString());
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, type.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return type;
	}
	
	public static CableLinkType createCableLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableLinkType type = CableLinkType.createInstance(userId, codename, EMPTY, codename, LinkTypeSort.LINKTYPESORT_OPTICAL, EMPTY, EMPTY, Identifier.VOID_IDENTIFIER);
		type.setCodename(type.getId().getIdentifierString());
		
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, type.getId(), SchemeEvent.CREATE_OBJECT));
		}
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
		
		try {
			Equipment eq = Equipment.createInstance(userId, domainId, schemeElement.getProtoEquipment().getId(), schemeElement.getName(), schemeElement.getDescription(), schemeElement.getSymbol() == null ? Identifier.VOID_IDENTIFIER : schemeElement.getSymbol().getId(), EMPTY, EMPTY, 0, 0, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
			
			for (Characteristic c : schemeElement.getProtoEquipment().getCharacteristics(true)) {
				Characteristic cloned = c.clone();
				cloned.setParentCharacterizable(eq, false);
			}
			
			schemeElement.setEquipment(eq);
			
			Identifier equipmentId = eq.getId();
			try {
				for (SchemePort sp : schemeElement.getSchemePortsRecursively(false)) {
					createPort(sp, equipmentId);
				}
			} catch (ApplicationException e) {
				throw new CreateObjectException(e);
			}
			try {
				for (SchemeCablePort sp : schemeElement.getSchemeCablePortsRecursively(false)) {
					createPort(sp, equipmentId);
				}
			} catch (ApplicationException e) {
				throw new CreateObjectException(e);
			}
			for (SchemeLink sl : schemeElement.getSchemeLinks(false)) {
				createLink(sl);
			}
			for (SchemeElement se : schemeElement.getSchemeElements(false)) {
				createEquipment(se);
			}
			return eq;
		} catch (ApplicationException e) {
			throw new CreateObjectException(e); 
		} catch (CloneNotSupportedException e) {
			throw new CreateObjectException(e);
		}
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
		
		try {
			for (Characteristic c : schemeLink.getAbstractLinkType().getCharacteristics(true)) {
				Characteristic cloned = c.clone();
				cloned.setParentCharacterizable(link, false);
			}
		} catch (ApplicationException e) {
			throw new CreateObjectException(e); 
		} catch (CloneNotSupportedException e) {
			throw new CreateObjectException(e);
		}
		
		schemeLink.setAbstractLink(link);
		return link;
	}

	public static CableLink createCableLink(SchemeCableLink schemeLink) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		CableLink cableLink = CableLink.createInstance(userId, domainId, schemeLink.getName(), schemeLink.getDescription(), schemeLink.getAbstractLinkType(), EMPTY, EMPTY, EMPTY, 0, EMPTY);
		
		try {
			for (Characteristic c : schemeLink.getAbstractLinkType().getCharacteristics(true)) {
				Characteristic cloned = c.clone();
				cloned.setParentCharacterizable(cableLink, false);
			}
		} catch (ApplicationException e) {
			throw new CreateObjectException(e); 
		} catch (CloneNotSupportedException e) {
			throw new CreateObjectException(e);
		}
		
		schemeLink.setAbstractLink(cableLink);
		return cableLink;
	}

	public static Port createPort(AbstractSchemePort sp, Identifier equipmentId) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Port port = Port.createInstance(userId, sp.getPortType(), sp.getName(), equipmentId);
		
		try {
			for (Characteristic c : sp.getPortType().getCharacteristics(true)) {
				Characteristic cloned = c.clone();
				cloned.setParentCharacterizable(port, false);
			}
		} catch (ApplicationException e) {
			throw new CreateObjectException(e); 
		} catch (CloneNotSupportedException e) {
			throw new CreateObjectException(e);
		}
		
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

	public static MeasurementPort createMeasurementPort(MeasurementPortType type, SchemePort port, KIS kis) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPort measurementPort = MeasurementPort.createInstance(userId, type, port.getName(), port.getDescription(), kis.getId(), port.getPort().getId());
		return measurementPort;
	}

	public static SchemeProtoGroup createSchemeProtoGroup() throws CreateObjectException {
		SchemeProtoGroup group = SchemeProtoGroup.createInstance(
				LoginManager.getUserId(), 
				LangModelScheme.getString("Title.group") + " (" + counter + ")");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		counter++;
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, group.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return group;
	}
	
	public static SchemeProtoElement createSchemeProtoElement() throws CreateObjectException {
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PROTOEQUIPMENT_CODE);
		ProtoEquipment protoEq = null;
		try {
			Set<ProtoEquipment> protoEqs = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (!protoEqs.isEmpty())
				protoEq = protoEqs.iterator().next();
		} catch (ApplicationException e2) {
			throw new CreateObjectException(e2);
		}
		if (protoEq == null) {
			String error = "No equipment types found. Create one at least."; //$NON-NLS-1$
			Log.debugMessage(error, Level.WARNING); 
			throw new CreateObjectException(error);
		}
		
		Identifier userId = LoginManager.getUserId();
		SchemeProtoElement protoElement = SchemeProtoElement.createInstance(userId, 
				LangModelScheme.getString("Title.component") + " (" + counter + ")", getStubProtoElementInitialyzed());  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		protoElement.setProtoEquipment(protoEq);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, protoElement.getId(), SchemeEvent.CREATE_OBJECT));
		}
		counter++;
		return protoElement;
	}

	public static SchemeElement createSchemeElement(Scheme parentScheme, SchemeProtoElement proto) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), proto, parentScheme);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeElement.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeElement;
	}
	
	public static SchemeElement createSchemeElement(SchemeElement parentSchemeElement, SchemeProtoElement proto) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), proto, parentSchemeElement);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeElement.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeElement;
	}
	
	public static SchemeElement createSchemeElement(Scheme parentScheme, Scheme scheme) throws CreateObjectException {
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(), scheme, parentScheme);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeElement.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeElement;
	}
	
	public static SchemeElement createSchemeElement(Scheme parentScheme) throws CreateObjectException {
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PROTOEQUIPMENT_CODE);
		ProtoEquipment protoEq = null;
		try {
			Set<ProtoEquipment> protoEqs = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (!protoEqs.isEmpty())
				protoEq = protoEqs.iterator().next();
		} catch (ApplicationException e2) {
			throw new CreateObjectException(e2);
		}
		if (protoEq == null) {
			String error = "No equipment types found. Create one at least."; //$NON-NLS-1$
			Log.debugMessage(error, Level.WARNING); 
			throw new CreateObjectException(error);
		}
		
		SchemeElement schemeElement = SchemeElement.createInstance(LoginManager.getUserId(),
				LangModelScheme.getString("Title.component") + " (" + counter + ")",   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				parentScheme);
		schemeElement.setProtoEquipment(protoEq);
		counter++;
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeElement.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeElement;
	}
	
	public static Scheme createScheme() throws CreateObjectException {
		Scheme scheme = Scheme.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString(SchemeResourceKeys.NEW_SCHEME) + (schemeCounter == 1 ? EMPTY : " (" + schemeCounter + ")"),  //$NON-NLS-1$ //$NON-NLS-2$ 
				IdlKind.NETWORK, LoginManager.getDomainId());
		schemeCounter++;
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, scheme.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return scheme;
	}

	public static SchemeDevice createSchemeDevice(String name) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeDevice schemeDevice = SchemeDevice.createInstance(userId, name, getStubProtoElementInitialyzed());
		return schemeDevice;
	}
	
	public static SchemePort createSchemePort(String name, IdlDirectionType directionType, SchemeDevice parent) throws ApplicationException {
		PortType type = null;
		if (cachedPortTypeId != null) {
			type = StorableObjectPool.getStorableObject(cachedPortTypeId, true);
		}
		
		if (type == null) {
			StorableObjectCondition condition = new TypicalCondition(
					PortTypeKind._PORT_KIND_SIMPLE,
					0, OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE, PortTypeWrapper.COLUMN_KIND);
			Set types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (types.isEmpty()) {
				throw new CreateObjectException(LangModelGraph.getString("error_porttype_not_found")); 
			}
			type = (PortType)types.iterator().next();
			cachedPortTypeId = type.getId();
		}

		SchemePort schemePort = SchemePort.createInstance(LoginManager.getUserId(), name, directionType, parent);
		schemePort.setPortType(type);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemePort.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemePort;
	}
	
	public static SchemeCablePort createSchemeCablePort(String name, IdlDirectionType directionType, SchemeDevice parent) throws ApplicationException {
		PortType type = null;
		if (cachedCablePortTypeId != null) {
			type = StorableObjectPool.getStorableObject(cachedCablePortTypeId, true);
		}
		
		if (type == null) {
			StorableObjectCondition condition = new TypicalCondition(
					PortTypeKind._PORT_KIND_CABLE,
					0, OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE, PortTypeWrapper.COLUMN_KIND);
			Set types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (types.isEmpty()) {
				throw new CreateObjectException(LangModelGraph.getString("error_porttype_not_found")); 
			}
			type = (PortType)types.iterator().next();
			cachedCablePortTypeId = type.getId();
		}

		SchemeCablePort schemePort = SchemeCablePort.createInstance(LoginManager.getUserId(), name, directionType, parent);
		schemePort.setPortType(type);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemePort.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemePort;
	}
	
	public static SchemeLink createSchemeLink(String name) throws ApplicationException {
		LinkType type = null;
		if (cachedLinkTypeId != null) {
			type = StorableObjectPool.getStorableObject(cachedLinkTypeId, true);
		}
		
		if (type == null) {
			StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
			Set types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (types.isEmpty()) {
				throw new CreateObjectException(LangModelGraph.getString("error_linktype_not_found"));
			}
			type = (LinkType)types.iterator().next();
			cachedLinkTypeId = type.getId();
		}
		
		Identifier userId = LoginManager.getUserId();
		SchemeLink schemeLink = SchemeLink.createInstance(userId, name, getStubProtoElementInitialyzed());
		schemeLink.setAbstractLinkType(type);
		
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeLink.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeLink;	
	}

	/*
	 * unused 
	 *
	public static SchemeLink createSchemeLink(String name, SchemeElement schemeElement) throws CreateObjectException {
		assert schemeElement != null;
		Identifier userId = LoginManager.getUserId();
		SchemeLink schemeLink = SchemeLink.createInstance(userId, name, schemeElement);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeLink.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeLink;
	}
	
	public static SchemeLink createSchemeLink(String name, Scheme scheme) throws CreateObjectException {
		assert scheme != null;
		Identifier userId = LoginManager.getUserId();
		SchemeLink schemeLink = SchemeLink.createInstance(userId, name, scheme);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeLink.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeLink;
	}
*/
	public static SchemeCableLink createSchemeCableLink(String name, Scheme parent, Identifier typeId) throws ApplicationException {
		cachedCableLinkTypeId = typeId;
		return createSchemeCableLink(name, parent);
	}
	
	public static SchemeCableLink createSchemeCableLink(String name, Scheme parent) throws ApplicationException {
		CableLinkType type = null;
		if (cachedCableLinkTypeId != null) {
			type = StorableObjectPool.getStorableObject(cachedCableLinkTypeId, true);
		}
		
		if (type == null) {
			StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
			Set types = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (types.isEmpty()) {
				throw new CreateObjectException(LangModelGraph.getString("error_cablelinktype_not_found"));
			}
			type = (CableLinkType)types.iterator().next();
			cachedCableLinkTypeId = type.getId();
		}
		
		Identifier userId = LoginManager.getUserId();
		SchemeCableLink schemeLink = SchemeCableLink.createInstance(userId, name, parent);
		schemeLink.setAbstractLinkTypeExt(type, userId, false);
		
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, schemeLink.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return schemeLink;
	}
	
	public static SchemeMonitoringSolution createSchemeMonitoringSolution(Scheme scheme) throws CreateObjectException {
		SchemeMonitoringSolution solution = SchemeMonitoringSolution.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString("Title.solution"),   //$NON-NLS-1$
				scheme);
		return solution;
	}
	
	/*public static PathElement createPathElement(SchemePath path, final Identifier id) throws CreateObjectException {
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
	}*/
	
	public static SchemePath createSchemePath(SchemeMonitoringSolution solution) throws CreateObjectException {
		SchemePath path = SchemePath.createInstance(LoginManager.getUserId(), 
				LangModelScheme.getString("Title.path") + " (" + counter + ")",   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				solution);
		if (aContext != null) {
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(aContext, path.getId(), SchemeEvent.CREATE_OBJECT));
		}
		return path;
	}
	
	public static TransmissionPath createTransmissionPath(SchemePath path, TransmissionPathType type) throws CreateObjectException {
		SortedSet<PathElement> pathMembers;
		try {
			pathMembers = path.getPathMembers();
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
		AbstractSchemePort startPort = pathMembers.first().getEndAbstractSchemePort();
		assert startPort != null;
		AbstractSchemePort endPort = pathMembers.last().getStartAbstractSchemePort();
		assert endPort != null;
				
		TransmissionPath transmissionPath = TransmissionPath.createInstance(
				LoginManager.getUserId(), LoginManager.getDomainId(), path.getName(), EMPTY, 
				type, startPort.getId(), endPort.getId());
		return transmissionPath;
	}
	
	public static TransmissionPathType createTransmissionPathType(String name) throws CreateObjectException {
		TransmissionPathType type = TransmissionPathType.createInstance(LoginManager.getUserId(), name, EMPTY, name);
		return type;
	}
	
	public static MonitoredElement createMonitoredElement(SchemePath path, MeasurementPort mp) 
			throws CreateObjectException {
		try {
			final Set<TransmissionPathType> tpTypes = StorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.TRANSPATH_TYPE_CODE), true);
			// get any TransmissionPathType - it is not used really 
			TransmissionPathType tpType;
			if (tpTypes.isEmpty()) {
				tpType = SchemeObjectsFactory.createTransmissionPathType(mp.getType().getCodename());
			} else {
				tpType = tpTypes.iterator().next();
			}
			final TransmissionPath tpath = SchemeObjectsFactory.createTransmissionPath(path, tpType);
			// create ME
			return SchemeObjectsFactory.createMonitoredElement(
					path.getName(), mp.getId(), EMPTY, Collections.singleton(tpath.getId()));
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
	
	public static MonitoredElement createMonitoredElement(String name, Identifier measurementPortId,
			String localAddress, Set<Identifier> monitoredDomainMemberIds) throws CreateObjectException {
		MonitoredElement me = MonitoredElement.createInstance(LoginManager.getUserId(),
				LoginManager.getDomainId(), name, measurementPortId, 
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH, 
				localAddress, monitoredDomainMemberIds);
		return me;
	}
	
	public static void assignClonedIds(Map<DefaultGraphCell, DefaultGraphCell> clonedCells, Map<Identifier, Identifier> clonedIds) {
		for (DefaultGraphCell clonedCell : clonedCells.values()) {
			if (clonedCell instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)clonedCell;
				Identifier id = dev.getElementId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				if (newId.getMajor() == ObjectEntities.SCHEMEPROTOELEMENT_CODE)
					dev.setProtoElementId(newId);
				else if (newId.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE)
					dev.setSchemeElementId(newId);
				else if (newId.getMajor() == ObjectEntities.SCHEME_CODE) {
					try {
						Scheme scheme = StorableObjectPool.getStorableObject(newId, false);
						SchemeElement parent = scheme.getParentSchemeElement();
						if (parent != null) {
							dev.setSchemeElementId(parent.getId());
						} else {
							Log.debugMessage("can not insert scheme without parent se for newid " + newId + " no clone performed", Level.FINE);
						}
					} catch (ApplicationException e) {
						Log.errorMessage(e);
					}
				}
				else
					assert false : "Unknown object identifier " + newId; //$NON-NLS-1$
			} else if (clonedCell instanceof DeviceCell) {
				DeviceCell cell = (DeviceCell)clonedCell;
				Identifier id = cell.getSchemeDeviceId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				cell.setSchemeDeviceId(newId);
			} else if (clonedCell instanceof PortCell) {
				PortCell cell = (PortCell)clonedCell;
				Identifier id = cell.getSchemePortId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				cell.setSchemePortId(newId);
			} else if (clonedCell instanceof CablePortCell) {
				CablePortCell cell = (CablePortCell)clonedCell;
				Identifier id = cell.getSchemeCablePortId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				cell.setSchemeCablePortId(newId);
			} else if (clonedCell instanceof DefaultCableLink) {
				DefaultCableLink cell = (DefaultCableLink)clonedCell;
				Identifier id = cell.getSchemeCableLinkId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				cell.setSchemeCableLinkId(newId);
			} else if (clonedCell instanceof DefaultLink) {
				DefaultLink cell = (DefaultLink)clonedCell;
				Identifier id = cell.getSchemeLinkId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				cell.setSchemeLinkId(newId);
			} else if (clonedCell instanceof BlockPortCell) {
				BlockPortCell cell = (BlockPortCell)clonedCell;
				Identifier id = cell.getAbstractSchemePortId();
				Identifier newId = clonedIds.get(id);
				if (newId == null) {
					Log.debugMessage("cloned id not found for id " + id + " no clone performed", Level.FINE);
					newId = id;
				}
				cell.setAbstractSchemePortId(newId);
			}
		}
	}
}
