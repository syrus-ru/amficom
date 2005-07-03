package com.syrus.AMFICOM.scheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class CableChannelingItemController extends ObjectResourceController
{
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
				COLUMN_ID,
				COLUMN_CREATED,
				COLUMN_CREATOR_ID,
				COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID,
				COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
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
			if (key.equals(COLUMN_ID))
				result = item.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(item.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = item.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(item.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = item.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_SEQUENTIAL_NUMBER))
				result = Integer.toString(item.getSequentialNumber());
			else if (key.equals(COLUMN_START_SITE_NODE_ID))
				result = item.getStartSiteNode().getId().getIdentifierString();
			else if (key.equals(COLUMN_END_SITE_NODE_ID))
				result = item.getEndSiteNode().getId().getIdentifierString();
			else if (key.equals(COLUMN_START_SPARE))
				result = Double.toString(item.getStartSpare());
			else if (key.equals(COLUMN_END_SPARE))
				result = Double.toString(item.getEndSpare());
			else if (key.equals(COLUMN_ROW_X))
				result = Integer.toString(item.getRowX());
			else if (key.equals(COLUMN_PLACE_Y))
				result = Integer.toString(item.getPlaceY());
			else if (key.equals(COLUMN_PHYSICAL_LINK_ID))
				result = item.getPhysicalLink().getId().getIdentifierString();
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
