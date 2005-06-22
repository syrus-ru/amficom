/*-
 * $Id: SchemeObjectsFactory.java,v 1.8 2005/06/22 10:10:50 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.util.Collections;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.IdlPortPackage.PortSort;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.resource.*;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/06/22 10:10:50 $
 * @module schemeclient_v1
 */

public class SchemeObjectsFactory {
//	private static int counter = 0;
	
	public static EquipmentType createEquipmentType() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		EquipmentType eqt = EquipmentType.createInstance(userId, "", "", "", "", "");
		try {
			StorableObjectPool.putStorableObject(eqt);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return eqt;
	}
	
	public static MeasurementType createMeasurementType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementType type = MeasurementType.createInstance(userId, codename, "", Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}

	public static MeasurementPortType createMeasurementPortType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		MeasurementPortType type = MeasurementPortType.createInstance(userId, codename, "", "");
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
	
	public static PortType createPortType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		PortType type = PortType.createInstance(userId, codename, "", "", PortTypeSort.PORTTYPESORT_OPTICAL);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
	
	public static LinkType createLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		LinkType type = LinkType.createInstance(userId, codename, "", "", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", null);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
	
	public static CableLinkType createCableLinkType(String codename) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		CableLinkType type = CableLinkType.createInstance(userId, codename, "", "", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", null);
		try {
			StorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
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
			Log.debugException(e, Log.SEVERE);
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
			Log.debugException(e, Log.SEVERE);
		}
		return kis;
	}
	
	public static Link createLink(LinkSort sort) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		Link link = Link.createInstance(userId, domainId, "", "", null, "", "", "", sort, 0, "");
		try {
			StorableObjectPool.putStorableObject(link);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return link;
	}
	
	public static Port createPort(PortSort sort) throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		Port port = Port.createInstance(userId, null, "", null, sort);
		try {
			StorableObjectPool.putStorableObject(port);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return port;
	}
	
	public static SchemeImageResource createImageResource() throws CreateObjectException {
		Identifier userId = LoginManager.getUserId();
		SchemeImageResource ir = SchemeImageResource.createInstance(userId);
		try {
			StorableObjectPool.putStorableObject(ir);
		} catch (IllegalObjectEntityException e) {
			 Log.debugException(e, Log.SEVERE);
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
			 Log.debugException(e, Log.SEVERE);
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
			Log.debugException(e, Log.SEVERE);
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
	/*
	public static SchemeProtoElement createProtoElement() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemeProtoElement protoElement = SchemeProtoElement.createInstance(userId, "Component" + counter++);
		try {
			SchemeStorableObjectPool.putStorableObject(protoElement);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		} catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return protoElement;
	}*/
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
