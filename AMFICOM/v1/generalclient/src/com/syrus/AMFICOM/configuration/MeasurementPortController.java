package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class MeasurementPortController implements ObjectResourceController
{
	public static final String COLUMN_KIS_ID = "kis_id";
	public static final String COLUMN_PORT_ID = "port_id";

	private static MeasurementPortController instance;

	private List keys;

	private MeasurementPortController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_ID,
				COLUMN_CREATED,
				COLUMN_CREATOR_ID,
				COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_TYPE_ID,
				COLUMN_KIS_ID,
				COLUMN_PORT_ID,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementPortController getInstance()
	{
		if (instance == null)
			instance = new MeasurementPortController();
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
		if (key.equals(COLUMN_DESCRIPTION))
			name = "Описание";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof MeasurementPort)
		{
			MeasurementPort port = (MeasurementPort)object;
			if (key.equals(COLUMN_ID))
				result = port.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = port.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = port.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = port.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = port.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = port.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = port.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = port.getType().getId().getIdentifierString();
			else if (key.equals(COLUMN_KIS_ID))
				result = port.getKISId().getIdentifierString();
			else if (key.equals(COLUMN_PORT_ID))
				result = port.getPortId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(port.getCharacteristics().size());
				for (Iterator it = port.getCharacteristics().iterator(); it.hasNext(); ) {
					Characteristic ch = (Characteristic)it.next();
					res.add(ch.getId().getIdentifierString());
				}
				result = res;
			}
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
}
