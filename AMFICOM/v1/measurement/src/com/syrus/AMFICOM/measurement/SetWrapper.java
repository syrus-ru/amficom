/*
 * $Id: SetWrapper.java,v 1.1 2005/01/31 11:27:32 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/31 11:27:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class SetWrapper implements Wrapper {

	public static final String	COLUMN_SORT						= "sort";
	public static final String	COLUMN_DESCRIPTION				= "description";
	public static final String	LINK_COLUMN_SET_ID				= "set_id";
	public static final String	LINK_COLUMN_ME_ID				= "monitored_element_id";

	public static final String	LINK_COLUMN_SET_PARAMETERS		= "set_parameters";

	public static final String	LINK_COLUMN_SET_PARAMETER_ID	= "set_parameter_id";

	public static final String	LINK_COLUMN_TYPE_ID				= "type_id";
	public static final String	LINK_COLUMN_VALUE				= "value";

	private static SetWrapper	instance;

	private List				keys;

	private SetWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_SORT, COLUMN_DESCRIPTION, LINK_COLUMN_ME_ID,
				LINK_COLUMN_SET_PARAMETERS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SetWrapper getInstance() {
		if (instance == null)
			instance = new SetWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof Set) {
			Set set = (Set) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return set.getId();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(set.getCreated().getTime());
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return set.getCreatorId();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(set.getModified().getTime());
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return set.getModifierId();
			if (key.equals(COLUMN_SORT))
				return Integer.toString(set.getSort().value());
			if (key.equals(COLUMN_DESCRIPTION))
				return set.getDescription();
			if (key.equals(LINK_COLUMN_ME_ID))
				return set.getMonitoredElementIds();
			if (key.equals(LINK_COLUMN_SET_PARAMETERS)) {
				SetParameter[] parameters = set.getParameters();
				Map values = new HashMap();
				for (int i = 0; i < parameters.length; i++) {
					values.put(LINK_COLUMN_SET_PARAMETER_ID + i, parameters[i].getId());
					values.put(LINK_COLUMN_TYPE_ID + i, parameters[i].getType());

					byte[] bs = parameters[i].getValue();
					StringBuffer buffer = new StringBuffer();
					for (int j = 0; j < bs.length; j++) {
						String s = Integer.toString(bs[j] & 0xFF, 16);
						buffer.append((s.length() == 1 ? "0": "") + s);
					}
					values.put(LINK_COLUMN_VALUE + i, buffer.toString());

				}
				return values;
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Set) {
			Set set = (Set) object;
			if (key.equals(COLUMN_SORT))
				set.setSort(Integer.parseInt((String) value));
			else if (key.equals(COLUMN_DESCRIPTION))
				set.setDescription((String) value);
			else if (key.equals(LINK_COLUMN_ME_ID)) {
				List meIdStr = (List) value;
				List meIds = new ArrayList(meIdStr.size());
				for (Iterator it = meIdStr.iterator(); it.hasNext();)
					meIds.add(new Identifier((String) it.next()));
				try {
					set.setMonitoredElementIds(ConfigurationStorableObjectPool.getStorableObjects(meIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("SetWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(LINK_COLUMN_SET_PARAMETERS)) {
				Map setParameterMap = (Map) value;
				/* there is 3*N keys for N SetParameter */
				SetParameter[] setParameters = new SetParameter[setParameterMap.size() / 3];
				for (int i = 0; i < setParameters.length; i++) {
					try {
						Identifier parameterId = new Identifier((String) setParameterMap
								.get(LINK_COLUMN_SET_PARAMETER_ID + i));
						ParameterType parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(
							new Identifier((String) setParameterMap.get(LINK_COLUMN_TYPE_ID + i)), true);
						
						String valueStr = (String)setParameterMap.get(LINK_COLUMN_VALUE + i);
						byte[] setParameterValue =  new byte[valueStr.length() / 2];
						for (int j = 0; j < setParameterValue.length; j++) 
							setParameterValue[j] = (byte) ((char)Integer.parseInt(valueStr.substring(2*j, 2*(j+1)), 16));
						setParameters[i] = new SetParameter(parameterId, parameterType, setParameterValue);
					} catch (ApplicationException e) {
						Log.errorMessage("SetWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
				}
				set.setParameters(setParameters);
			}
		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(LINK_COLUMN_ME_ID))
			return List.class;
		if (key.equals(LINK_COLUMN_SET_PARAMETERS))
			return Map.class;
		return String.class;
	}

}
