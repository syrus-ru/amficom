/*
 * $Id: AnalysisTypeWrapper.java,v 1.1 2005/01/27 11:54:26 bob Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/27 11:54:26 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class AnalysisTypeWrapper implements Wrapper {

	public static final String			COLUMN_CODENAME					= "codename";
	public static final String			COLUMN_DESCRIPTION				= "description";

	public static final String			COLUMN_PARAMETER_TYPES_IN		= "in_params";
	public static final String			COLUMN_PARAMETER_TYPES_OUT		= "out_params";
	public static final String			COLUMN_PARAMETER_TYPES_CRI		= "criteris_params";
	public static final String			COLUMN_PARAMETER_TYPES_ETALON	= "etalon_params";

	private static AnalysisTypeWrapper	instance;

	private List						keys;

	private AnalysisTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION,
				COLUMN_PARAMETER_TYPES_IN, COLUMN_PARAMETER_TYPES_OUT, COLUMN_PARAMETER_TYPES_CRI,
				COLUMN_PARAMETER_TYPES_ETALON};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static AnalysisTypeWrapper getInstance() {
		if (instance == null)
			instance = new AnalysisTypeWrapper();
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
		if (object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return analysisType.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return analysisType.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return analysisType.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return analysisType.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return analysisType.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_CODENAME))
				return analysisType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return analysisType.getDescription();
			if (key.equals(COLUMN_PARAMETER_TYPES_IN))
				return analysisType.getInParameterTypes();
			if (key.equals(COLUMN_PARAMETER_TYPES_OUT))
				return analysisType.getOutParameterTypes();
			if (key.equals(COLUMN_PARAMETER_TYPES_CRI))
				return analysisType.getCriteriaParameterTypes();
			if (key.equals(COLUMN_PARAMETER_TYPES_ETALON))
				return analysisType.getEtalonParameterTypes();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			if (key.equals(COLUMN_CODENAME))
				analysisType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				analysisType.setDescription((String) value);
			else if (key.equals(COLUMN_PARAMETER_TYPES_IN)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					analysisType.setInParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(COLUMN_PARAMETER_TYPES_OUT)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					analysisType.setOutParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(COLUMN_PARAMETER_TYPES_CRI)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					analysisType.setCriteriaParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds,
						true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(COLUMN_PARAMETER_TYPES_ETALON)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					analysisType.setEtalonParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds,
						true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
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
		if (key.equals(COLUMN_PARAMETER_TYPES_IN) || key.equals(COLUMN_PARAMETER_TYPES_OUT)
				|| key.equals(COLUMN_PARAMETER_TYPES_CRI) || key.equals(COLUMN_PARAMETER_TYPES_ETALON))
			return List.class;
		return String.class;
	}

}
