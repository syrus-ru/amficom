/*
* $Id: MeasurementTypeWrapper.java,v 1.1 2005/01/27 15:49:23 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/27 15:49:23 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementTypeWrapper implements Wrapper {

	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";
	public static final String COLUMN_CODENAME = "codename";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String LINK_COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID = "measurement_port_type_id";

	private static MeasurementTypeWrapper	instance;

	private List						keys;

	private MeasurementTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT,
				LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementTypeWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementTypeWrapper();
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
		if (object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return measurementType.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return measurementType.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return measurementType.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return measurementType.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return measurementType.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_CODENAME))
				return measurementType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return measurementType.getDescription();
			if (key.equals(MODE_IN))
				return measurementType.getInParameterTypes();
			if (key.equals(MODE_OUT))
				return measurementType.getOutParameterTypes();
			if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
				return measurementType.getMeasurementPortTypes();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType) object;
			if (key.equals(COLUMN_CODENAME))
				measurementType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				measurementType.setDescription((String) value);
			else if (key.equals(MODE_IN)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					measurementType.setInParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(MODE_OUT)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					measurementType.setOutParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					measurementType.setMeasurementPortTypes(ConfigurationStorableObjectPool.getStorableObjects(paramTypeIds,
						true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
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
		if (key.equals(MODE_IN) || key.equals(MODE_OUT)
				|| key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
			return List.class;
		return String.class;
	}
}
