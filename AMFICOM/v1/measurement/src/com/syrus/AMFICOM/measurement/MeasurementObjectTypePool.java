package com.syrus.AMFICOM.measurement;

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;

public class MeasurementObjectTypePool {
	private static Map identifierParameterTypes;
	private static Map identifierActionTypes;
	private static Map codenameParameterTypes;
	private static Map codenameActionTypes;

	private MeasurementObjectTypePool() {
	}
	
	public static void init(ParameterType[] parameterTypes,
													ActionType[] actionTypes) {
		identifierParameterTypes = new Hashtable(parameterTypes.length);
		identifierActionTypes = new Hashtable(actionTypes.length);
		codenameParameterTypes = new Hashtable(parameterTypes.length);
		codenameActionTypes = new Hashtable(actionTypes.length);

		for (int i = 0; i < parameterTypes.length; i++) {
			addIdentifierParameterType(parameterTypes[i]);
			addCodenameParameterType(parameterTypes[i]);
		}
		for (int i = 0; i < actionTypes.length; i++) {
			addIdentifierActionType(actionTypes[i]);
			addCodenameActionType(actionTypes[i]);
		}
	}
	
	public static void init(List parameterTypes,
													List actionTypes) {
		identifierParameterTypes = new Hashtable(parameterTypes.size());
		identifierActionTypes = new Hashtable(actionTypes.size());
		codenameParameterTypes = new Hashtable(parameterTypes.size());
		codenameActionTypes = new Hashtable(actionTypes.size());

		ParameterType parameterType;
		ActionType actionType;
		for (Iterator iterator = parameterTypes.iterator(); iterator.hasNext();) {
			parameterType = (ParameterType)iterator.next();
			addIdentifierParameterType(parameterType);
			addCodenameParameterType(parameterType);
		}
		for (Iterator iterator = actionTypes.iterator(); iterator.hasNext();) {
			actionType = (ActionType)iterator.next();
			addIdentifierActionType(actionType);
			addCodenameActionType(actionType);
		}
	}
	
	public static ParameterType getParameterType(Identifier parameterTypeId) {
		return (ParameterType)identifierParameterTypes.get(parameterTypeId);
	}
	
	public static ActionType getActionType(Identifier actionTypeId) {
		return (ActionType)identifierActionTypes.get(actionTypeId);
	}
	
	public static ParameterType getParameterType(String parameterTypeCodename) {
		return (ParameterType)codenameParameterTypes.get(parameterTypeCodename);
	}
	
	public static ActionType getActionType(String actionTypeCodename) {
		return (ActionType)codenameActionTypes.get(actionTypeCodename);
	}
	
	private static void addIdentifierParameterType(ParameterType parameterType) {
		Identifier parameterTypeId = parameterType.getId();
		if (! identifierParameterTypes.containsKey(parameterTypeId))
			identifierParameterTypes.put(parameterTypeId, parameterType);
		else
			Log.errorMessage("MeasurementObjectTypePool.addIdentifierParameterType | parameter type of id '" + parameterTypeId.toString() + "' already added");
	}
	
	private static void addIdentifierActionType(ActionType actionType) {
		Identifier actionTypeId = actionType.getId();
		if (! identifierActionTypes.containsKey(actionTypeId))
			identifierActionTypes.put(actionTypeId, actionType);
		else
			Log.errorMessage("MeasurementObjectTypePool.addIdentifierActionType | action type of id '" + actionTypeId.toString() + "' already added");
	}
	
	private static void addCodenameParameterType(ParameterType parameterType) {
		String parameterTypeCodename = parameterType.getCodename();
		if (! codenameParameterTypes.containsKey(parameterTypeCodename))
			codenameParameterTypes.put(parameterTypeCodename, parameterType);
		else
			Log.errorMessage("MeasurementObjectTypePool.addCodenameParameterType | parameter type of codename '" + parameterTypeCodename + "' already added");
	}
	
	private static void addCodenameActionType(ActionType actionType) {
		String actionTypeCodename = actionType.getCodename();
		if (! codenameActionTypes.containsKey(actionTypeCodename))
			codenameActionTypes.put(actionTypeCodename, actionType);
		else
			Log.errorMessage("MeasurementObjectTypePool.addCodenameActionType | action type of codename '" + actionTypeCodename + "' already added");
	}
}
