package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.*;

public final class CableChannelingItemController implements ObjectResourceController
{
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SEQUENTIAL_NUMBER = "number";
	public static final String COLUMN_START_SITE_NODE_ID = "start_site_node_id";
	public static final String COLUMN_END_SITE_NODE_ID = "end_site_node_id";
	public static final String COLUMN_START_SPARE = "start_spare";
	public static final String COLUMN_END_SPARE = "end_spare";
	public static final String COLUMN_ROW_X = "row_x";
	public static final String COLUMN_PLACE_Y = "place_y";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";

	private static CableChannelingItemController instance;

	private List keys;

	private CableChannelingItemController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				StorableObjectDatabase.COLUMN_ID,
				StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID,
				StorableObjectType.COLUMN_CODENAME,
				StorableObjectType.COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_SEQUENTIAL_NUMBER,
				COLUMN_START_SITE_NODE_ID,
				COLUMN_END_SITE_NODE_ID,
				COLUMN_START_SPARE,
				COLUMN_END_SPARE,
				COLUMN_ROW_X,
				COLUMN_PLACE_Y,
				COLUMN_PHYSICAL_LINK_ID
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableChannelingItemController getInstance()
	{
		if (instance == null)
			instance = new CableChannelingItemController();
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
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof CableChannelingItem)
		{
			CableChannelingItem item = (CableChannelingItem)object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				result = item.id().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				result = Long.toString(item.created());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				result = item.creatorId().identifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				result = Long.toString(item.modified());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				result = item.modifierId().identifierString();
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				result = item.description();
			else if (key.equals(COLUMN_NAME))
				result = item.name();
			else if (key.equals(COLUMN_SEQUENTIAL_NUMBER))
				result = Integer.toString(item.sequentialNumber());
			else if (key.equals(COLUMN_START_SITE_NODE_ID))
				result = item.startSiteNodeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_END_SITE_NODE_ID))
				result = item.endSiteNodeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_START_SPARE))
				result = Double.toString(item.startSpare());
			else if (key.equals(COLUMN_END_SPARE))
				result = Double.toString(item.endSpare());
			else if (key.equals(COLUMN_ROW_X))
				result = Integer.toString(item.rowX());
			else if (key.equals(COLUMN_PLACE_Y))
				result = Integer.toString(item.placeY());
			else if (key.equals(COLUMN_PHYSICAL_LINK_ID))
				result = item.physicalLinkImpl().getId().getIdentifierString();
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
