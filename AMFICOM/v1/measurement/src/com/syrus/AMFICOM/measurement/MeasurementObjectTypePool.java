/*
 * $Id: MeasurementObjectTypePool.java,v 1.4 2004/07/27 15:52:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/27 15:52:26 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementObjectTypePool {
	private static Map identifierObjectTypes;
	private static Map codenameObjectTypes;

	private MeasurementObjectTypePool() {
	}
	
	public static void init(StorableObjectType[] objectTypes) {
		identifierObjectTypes = new Hashtable(objectTypes.length);
		codenameObjectTypes = new Hashtable(objectTypes.length);

		for (int i = 0; i < objectTypes.length; i++)  {
			addIdentifierObjectType(objectTypes[i]);
			addCodenameObjectType(objectTypes[i]);
		}
	}

	public static void init(List objectTypes) {
		identifierObjectTypes = new Hashtable(objectTypes.size());
		codenameObjectTypes = new Hashtable(objectTypes.size());

		StorableObjectType objectType;
		for (Iterator iterator = objectTypes.iterator(); iterator.hasNext();) {
			objectType = (StorableObjectType)iterator.next();
			addIdentifierObjectType(objectType);
			addCodenameObjectType(objectType);
		}
	}
	
	public static StorableObjectType getObjectType(Identifier objectTypeId) {
		return (StorableObjectType)identifierObjectTypes.get(objectTypeId);
	}
	
	public static StorableObjectType getObjectType(String objectTypeCodename) {
		return (StorableObjectType)codenameObjectTypes.get(objectTypeCodename);
	}
	
	private static void addIdentifierObjectType(StorableObjectType objectType) {
		Identifier objectTypeId = objectType.getId();
		if (! identifierObjectTypes.containsKey(objectTypeId))
			identifierObjectTypes.put(objectTypeId, objectType);
		else
			Log.errorMessage("MeasurementObjectTypePool.addIdentifierObjectType | object type of id '" + objectTypeId.toString() + "' already added");
	}
	
	private static void addCodenameObjectType(StorableObjectType objectType) {
		String objectTypeCodename = objectType.getCodename();
		if (! codenameObjectTypes.containsKey(objectTypeCodename))
			codenameObjectTypes.put(objectTypeCodename, objectType);
		else
			Log.errorMessage("MeasurementObjectTypePool.addCodenameObjectType | object type of codename '" + objectTypeCodename + "' already added");
	}
}
