package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class SchemeDeviceController implements ObjectResourceController
{
	public static final String COLUMN_SCHEME_PORT_IDS = "scheme_port_ids";
	public static final String COLUMN_SCHEME_CABLEPORT_IDS = "scheme_cableport_ids";

	private static SchemeDeviceController instance;

	private List keys;

	private SchemeDeviceController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_ID,
				COLUMN_CREATED,
				COLUMN_CREATOR_ID,
				COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID,
				COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_SCHEME_PORT_IDS,
				COLUMN_SCHEME_CABLEPORT_IDS,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeDeviceController getInstance()
	{
		if (instance == null)
			instance = new SchemeDeviceController();
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
			name = "��������";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof SchemeDevice)
		{
			SchemeDevice device = (SchemeDevice)object;
			if (key.equals(COLUMN_ID))
				result = device.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(device.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = device.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(device.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = device.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = device.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = device.getName();
			else if (key.equals(COLUMN_SCHEME_PORT_IDS)) {
				SchemePort[] ports = device.schemePorts();
				List res = new ArrayList(ports.length);
				for (int i = 0; i < ports.length; i++) {
					res.add(ports[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_SCHEME_CABLEPORT_IDS)) {
				SchemeCablePort[] ports = device.schemeCablePorts();
				List res = new ArrayList(ports.length);
				for (int i = 0; i < ports.length; i++) {
					res.add(ports[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(device.getCharacteristics().size());
				for (Iterator it = device.getCharacteristics().iterator(); it.hasNext(); ) {
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
