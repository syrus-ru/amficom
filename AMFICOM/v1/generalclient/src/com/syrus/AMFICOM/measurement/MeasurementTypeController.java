/*
 * $Id: MeasurementTypeController.java,v 1.3 2005/03/16 13:40:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/16 13:40:57 $
 * @module generalclient_v1
 */

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class MeasurementTypeController implements ObjectResourceController
{
	private static MeasurementTypeController instance;

	private List keys;

	private MeasurementTypeController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_NAME
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementTypeController getInstance()
	{
		if (instance == null)
			instance = new MeasurementTypeController();
		return instance;
	}

	public List getKeys()
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(COLUMN_NAME))
			name = "Название";
		else if (key.equals(COLUMN_DESCRIPTION))
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof MeasurementType)
		{
			MeasurementType type = (MeasurementType)object;
			if (key.equals(COLUMN_NAME))
				result = type.getDescription();
			else if (key.equals(COLUMN_DESCRIPTION))
					result = type.getDescription();
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
	}

	public String getKey(final int index)
	{
		return (String )this.keys.get(index);
	}

	public Object getPropertyValue(final String key)
	{
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue)
	{
	}

	public Class getPropertyClass(String key)
	{
		Class clazz = String.class;
		return clazz;
	}
	
	public static String getPropertyPaneClassName() {
		return "com.syrus.AMFICOM.Client.Configure.UI.MeasurementTypePane";
	}
	
	public static String getPropertyManagerClassName () {
		return "com.syrus.AMFICOM.client_.configuration.ui.MeasurementTypePropertiesManager";  //$NON-NLS-1$
	}
}
