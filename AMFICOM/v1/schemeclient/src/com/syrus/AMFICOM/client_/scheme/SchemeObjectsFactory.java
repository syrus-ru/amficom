/*-
 * $Id: SchemeObjectsFactory.java,v 1.1 2005/04/05 14:10:46 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:10:46 $
 * @module schemeclient_v1
 */

public class SchemeObjectsFactory {
	private static Identifier userId;
	private static int counter = 0;

	public static void setContext(ApplicationContext aContext) {
		if (aContext != null && aContext.getSessionInterface() != null) {
				userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().user_id);
		}
	}
	
	public static SchemeImageResource createImageResource() throws CreateObjectException {
		SchemeImageResource ir = SchemeImageResource.createInstance(userId);
		try {
			ResourceStorableObjectPool.putStorableObject(ir);
		} catch (IllegalObjectEntityException e) {
			 Log.debugException(e, Log.SEVERE);
		}
		return ir;
	}
	
	public static SchemeDevice createDevice() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemeDevice schemeDevice = SchemeDevice.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeDevice);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return schemeDevice;
	}
	
	public static AbstractSchemePort createPort() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemePort schemePort = SchemePort.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemePort);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return schemePort;
	}
	
	public static AbstractSchemePort createCablePort() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemeCablePort schemeCablePort = SchemeCablePort.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeCablePort);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return schemeCablePort;
	}
	
	public static SchemeProtoElement createProtoElement() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemeProtoElement protoElement = SchemeProtoElement.createInstance(userId, "Component" + counter++);
		try {
			SchemeStorableObjectPool.putStorableObject(protoElement);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return protoElement;
	}
	
	public static SchemeElement createElement() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemeElement schemeElement = SchemeElement.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeElement);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return schemeElement;
	}
	
	public static SchemeLink createLink() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemeLink schemeLink = SchemeLink.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeLink);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return schemeLink;
	}
	
	public static SchemeCableLink createCableLink() throws CreateObjectException {
		if (userId == null)
			throw new NullPointerException("ApplicationContext is not set");
		SchemeCableLink schemeCableLink = SchemeCableLink.createInstance(userId);
		try {
			SchemeStorableObjectPool.putStorableObject(schemeCableLink);
		} 
		catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.SEVERE);
		}
		return schemeCableLink;
	}
}
