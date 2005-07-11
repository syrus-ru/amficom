/*-
 * $Id: SchemeObjectsFactory.java,v 1.11 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import com.syrus.AMFICOM.client.model.Environment;
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.11 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class SchemeObjectsFactory {
	private static int counter = 1;
	private static int schemeCounter = 1;
	
	public static EquipmentType createEquipmentType() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		EquipmentType eqt = EquipmentType.createInstance(userId, "", "", "", "", "");
		try {
			StorableObjectPool.putStorableObject(eqt);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return eqt;
	}
	
	public static MeasurementType createMeasurementType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementType type = MeasurementType.createInstance(userId, codename, "", Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return type;
	}

	public static MeasurementPortType createMeasurementPortType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPortType type = MeasurementPortType.createInstance(userId, codename, "", "");
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return type;
	}
	
	public static PortType createPortType(String codename, PortTypeKind kind) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		PortType type = PortType.createInstance(userId, codename, "", "", PortTypeSort.PORTTYPESORT_OPTICAL, kind);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return type;
	}
	
	public static LinkType createLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		LinkType type = LinkType.createInstance(userId, codename, "", codename, LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", Identifier.VOID_IDENTIFIER);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return type;
	}
	
	public static CableLinkType createCableLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableLinkType type = CableLinkType.createInstance(userId, codename, "", codename, LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", Identifier.VOID_IDENTIFIER);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return type;
	}
	
	public static CableThreadType createCableThreadType(String codename, String name, LinkType linkType, CableLinkType cableLinkType) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableThreadType type = CableThreadType.createInstance(userId, codename, "", name, -1, linkType, cableLinkType);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return type;
	}
		
	public static Equipment createEquipment() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Equipment eq = Equipment.createInstance(userId, domainId, null, "", "", null, "", "", 0, 0, "", "", "", "", "");
		try {
			StorableObjectPool.putStorableObject(eq);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return eq;
	}

	public static KIS createKis() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		KIS kis = KIS.createInstance(userId, domainId, "", "", "", (short)0, null, null);
		try {
			StorableObjectPool.putStorableObject(kis);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return kis;
	}
	
	public static Link createLink() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Link link = Link.createInstance(userId, domainId, "", "", null, "", "", "", 0, "");
		try {
			StorableObjectPool.putStorableObject(link);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return link;
	}

	public static CableLink createCableLink() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		CableLink cableLink = CableLink.createInstance(userId, domainId, "", "", null, "", "", "", 0, "");
		try {
			StorableObjectPool.putStorableObject(cableLink);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return cableLink;
	}

	public static Port createPort(PortType type) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Port port = Port.createInstance(userId, type, "", null);
		try {
			StorableObjectPool.putStorableObject(port);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return port;
	}
	
	public static SchemeImageResource createImageResource() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeImageResource ir = SchemeImageResource.createInstance(userId);
		try {
			StorableObjectPool.putStorableObject(ir);
		} catch (IllegalObjectEntityException e) {
			 Log.debugException(e, Level.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: ResourceStorableObjectPool.putStorableObject()");
		}
		return ir;
	}
	
	public static BitmapImageResource createBitmap(String codename, byte[] image) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		BitmapImageResource ir = BitmapImageResource.createInstance(userId, codename, image);
		try {
			StorableObjectPool.putStorableObject(ir);
		} catch (IllegalObjectEntityException e) {
			 Log.debugException(e, Level.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: ResourceStorableObjectPool.putStorableObject()");
		}
		return ir;
	}
	/*
	public static SchemeDevice createSchemeDevice(String name) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().user_id);
		SchemeDevice schemeDevice = SchemeDevice.createInstance(userId, name);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeDevice);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemeDevice;
	}*/
	/*
	public static AbstractSchemePort createSchemePort() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemePort schemePort = SchemePort.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemePort);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemePort;
	}*/
	
	public static MeasurementPort createMeasurementPort(MeasurementPortType type) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPort measurementPort = MeasurementPort.createInstance(
				userId, type, "", "", null, null);
		try {
			StorableObjectPool.putStorableObject(measurementPort);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Level.SEVERE);
		}
		return measurementPort;
	}
	/*
	public static AbstractSchemePort createSchemeCablePort() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemeCablePort schemeCablePort = SchemeCablePort.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeCablePort);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemeCablePort;
	}*/

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
		try {
			StorableObjectPool.putStorableObject(protoElement);
		} 
		catch (ApplicationException e) {
			Log.errorException(e);
		}
		return protoElement;
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
		try {
			StorableObjectPool.putStorableObject(schemeElement);
		} 
		catch (ApplicationException e) {
			Log.errorException(e);
		}
		return schemeElement;
	}
	
	public static Scheme createScheme() throws CreateObjectException {
		Scheme scheme = Scheme.createInstance(LoginManager.getUserId(), LangModelScheme.getString(SchemeResourceKeys.NEW_SCHEME)
				+ (schemeCounter == 1 ? "" : "(" + schemeCounter + ")"), Kind.NETWORK, LoginManager.getDomainId()); 
		try {
			StorableObjectPool.putStorableObject(scheme);
		} 
		catch (ApplicationException e) {
			Log.errorException(e);
		}
		return scheme;
	}
	/*
	public static SchemeElement createElement() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemeElement schemeElement = SchemeElement.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeElement);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemeElement;
	}*/
	/*
	public static SchemeLink createSchemeLink() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemeLink schemeLink = SchemeLink.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeLink);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemeLink;
	}
	
	public static SchemeCableLink createSchemeCableLink() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemeCableLink schemeCableLink = SchemeCableLink.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeCableLink);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemeCableLink;
	}*/
}
