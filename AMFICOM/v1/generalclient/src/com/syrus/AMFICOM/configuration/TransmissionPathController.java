package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class TransmissionPathController implements ObjectResourceController
{
	public static final String COLUMN_START_PORT_ID = "start_port_id";
	public static final String COLUMN_FINISH_PORT_ID= "finish_port_id";

	private static TransmissionPathController instance;

	private List keys;

	private TransmissionPathController()
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
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static TransmissionPathController getInstance()
	{
		if (instance == null)
			instance = new TransmissionPathController();
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
		if (object instanceof TransmissionPath)
		{
			TransmissionPath path = (TransmissionPath)object;
			if (key.equals(COLUMN_ID))
				result = path.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = path.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = path.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = path.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = path.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = path.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = path.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = path.getType().getId().getIdentifierString();
			else if (key.equals(COLUMN_START_PORT_ID))
				result = path.getStartPortId().getIdentifierString();
			else if (key.equals(COLUMN_FINISH_PORT_ID))
				result = path.getFinishPortId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(path.getCharacteristics().size());
				for (Iterator it = path.getCharacteristics().iterator(); it.hasNext(); ) {
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
