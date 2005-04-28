/*-
 * $Id: SchemeObjectsFactory.java,v 1.4 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.util.Collections;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class SchemeObjectsFactory {
	private static ApplicationContext aContext;
//	private static int counter = 0;

	public static void setContext(ApplicationContext aContext1) {
		aContext = aContext1;
	}
	
	public static EquipmentType createEquipmentType() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		EquipmentType eqt = EquipmentType.createInstance(userId, "", "", "", "", "");
		try {
			ConfigurationStorableObjectPool.putStorableObject(eqt);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return eqt;
	}
	
	public static MeasurementType createMeasurementType(String codename) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		MeasurementType type = MeasurementType.createInstance(userId, codename, "", Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET);
		try {
			MeasurementStorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}

	public static MeasurementPortType createMeasurementPortType(String codename) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		MeasurementPortType type = MeasurementPortType.createInstance(userId, codename, "", "");
		try {
			MeasurementStorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
	
	public static PortType createPortType(String codename) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		PortType type = PortType.createInstance(userId, codename, "", "", PortTypeSort.PORTTYPESORT_OPTICAL);
		try {
			ConfigurationStorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
	
	public static LinkType createLinkType(String codename) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		LinkType type = LinkType.createInstance(userId, codename, "", "", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", null);
		try {
			ConfigurationStorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
	
	public static CableLinkType createCableLinkType(String codename) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		CableLinkType type = CableLinkType.createInstance(userId, codename, "", "", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "", "", null);
		try {
			ConfigurationStorableObjectPool.putStorableObject(type);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return type;
	}
		
	public static Equipment createEquipment() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().domain_id);
		Equipment eq = Equipment.createInstance(userId, domainId, null, "", "", null, "", "", 0, 0, "", "", "", "", "");
		try {
			ConfigurationStorableObjectPool.putStorableObject(eq);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return eq;
	}

	public static KIS createKis() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().domain_id);
		KIS kis = KIS.createInstance(userId, domainId, "", "", "", (short)0, null, null);
		try {
			ConfigurationStorableObjectPool.putStorableObject(kis);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return kis;
	}
	
	public static Link createLink(LinkSort sort) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().domain_id);
		Link link = Link.createInstance(userId, domainId, "", "", null, "", "", "", sort, 0, "");
		try {
			ConfigurationStorableObjectPool.putStorableObject(link);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return link;
	}
	
	public static Port createPort(PortSort sort) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		Port port = Port.createInstance(userId, null, "", null, sort);
		try {
			ConfigurationStorableObjectPool.putStorableObject(port);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return port;
	}
	
	public static SchemeImageResource createImageResource() throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		SchemeImageResource ir = SchemeImageResource.createInstance(userId);
		try {
			ResourceStorableObjectPool.putStorableObject(ir);
		} catch (IllegalObjectEntityException e) {
			 Log.debugException(e, Log.SEVERE);
		}
		catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: ResourceStorableObjectPool.putStorableObject()");
		}
		return ir;
	}
	
	public static BitmapImageResource createBitmap(String codename, byte[] image) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		BitmapImageResource ir = BitmapImageResource.createInstance(userId, codename, image);
		try {
			ResourceStorableObjectPool.putStorableObject(ir);
		} catch (IllegalObjectEntityException e) {
			 Log.debugException(e, Log.SEVERE);
		}
		catch (UnsupportedOperationException e) {
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
		}
		catch (UnsupportedOperationException e) {
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
		}
		catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemePort;
	}*/
	
	public static MeasurementPort createMeasurementPort(MeasurementPortType type) throws CreateObjectException {
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().user_id);
		MeasurementPort measurementPort = MeasurementPort.createInstance(
				userId, type, "", "", null, null);
		try {
			SchemeStorableObjectPool.putStorableObject(measurementPort);
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
		}
		catch (UnsupportedOperationException e) {
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
		}
		catch (UnsupportedOperationException e) {
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
		}
		catch (UnsupportedOperationException e) {
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
		}
		catch (UnsupportedOperationException e) {
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
		}
		catch (UnsupportedOperationException e) {
			System.err.println("Unsupported operation: SchemeStorableObjectPool.putStorableObject()");
		}
		return schemeCableLink;
	}*/
}
