package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class LinkController implements ObjectResourceController
{
	public static final String COLUMN_SORT = "sort";
	public static final String COLUMN_SUPPLIER = "supplier";
	public static final String COLUMN_SUPPLIER_CODE = "supplier_code";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_MARK = "mark";

	private static LinkController instance;

	private List keys;

	private LinkController()
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
				COLUMN_SORT,
				COLUMN_SUPPLIER,
				COLUMN_SUPPLIER_CODE,
				COLUMN_COLOR,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static LinkController getInstance()
	{
		if (instance == null)
			instance = new LinkController();
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
		if (object instanceof Link)
		{
			Link link = (Link)object;
			if (key.equals(COLUMN_ID))
				result = link.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = link.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = link.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = link.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = link.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				result = link.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = link.getType().getId().getIdentifierString();
			else if (key.equals(COLUMN_SORT))
				result = Integer.toString(link.getSort().value());
			else if (key.equals(COLUMN_SUPPLIER))
				result = link.getSupplier();
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				result = link.getSupplierCode();
			else if (key.equals(COLUMN_COLOR))
				result = Integer.toString(link.getColor());
			else if (key.equals(COLUMN_MARK))
				result = link.getMark();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(link.getCharacteristics().size());
				for (Iterator it = link.getCharacteristics().iterator(); it.hasNext(); ) {
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
