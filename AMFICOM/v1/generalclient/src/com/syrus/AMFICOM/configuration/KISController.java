package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class KISController implements ObjectResourceController
{
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String COLUMN_MCM_ID = "mcm_id";
	public static final String COLUMN_HOSTNAME = "hostname";
	public static final String COLUMN_TCP_PORT = "tcp_port";
	public static final String COLUMN_MEASUREMENT_PORT_IDS = "measurementPortIds";
	public static final String COLUMN_CHARACTERISTICS = "characteristics";

	private static KISController instance;

	private List keys;

	private KISController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				StorableObjectDatabase.COLUMN_ID,
				StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID,
				StorableObjectType.COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_EQUIPMENT_ID,
				COLUMN_MCM_ID,
				COLUMN_HOSTNAME,
				COLUMN_TCP_PORT,
				COLUMN_MEASUREMENT_PORT_IDS,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static KISController getInstance()
	{
		if (instance == null)
			instance = new KISController();
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
		if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
			name = "Описание";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof KIS)
		{
			KIS kis = (KIS)object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				result = kis.getId().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				result = kis.getCreated().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				result = kis.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				result = kis.getModified().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				result = kis.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				result = kis.getName();
			else if (key.equals(COLUMN_EQUIPMENT_ID))
				result = kis.getEquipmentId().getIdentifierString();
			else if (key.equals(COLUMN_MCM_ID))
				result = kis.getMCMId().getIdentifierString();
			else if (key.equals(COLUMN_HOSTNAME))
				result = kis.getHostName();
			else if (key.equals(COLUMN_TCP_PORT))
				result = Short.toString(kis.getTCPPort());
			else if (key.equals(COLUMN_MEASUREMENT_PORT_IDS)) {
				List res = new ArrayList(kis.getMeasurementPortIds().size());
				for (Iterator it = kis.getMeasurementPortIds().iterator(); it.hasNext(); ) {
					res.add(((Identifier)it.next()).getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(kis.getCharacteristics().size());
				for (Iterator it = kis.getCharacteristics().iterator(); it.hasNext(); ) {
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
