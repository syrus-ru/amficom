package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;

public final class SchemeLinkController implements ObjectResourceController
{
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";
	public static final String COLUMN_LINK_ID = "link_id";
	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_SCHEME_ID = "scheme_id";
	public static final String COLUMN_SITE_NODE_ID = "site_node_id";
	public static final String COLUMN_OPTICAL_LENGTH = "optical_length";
	public static final String COLUMN_PHYSICAL_LENGTH = "physical_length";

	private static SchemeLinkController instance;

	private List keys;

	private SchemeLinkController()
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
				COLUMN_SOURCE_SCHEME_PORT_ID,
				COLUMN_TARGET_SCHEME_PORT_ID,
				COLUMN_LINK_ID,
				COLUMN_TYPE_ID,
				COLUMN_SCHEME_ID,
				COLUMN_SITE_NODE_ID,
				COLUMN_OPTICAL_LENGTH,
				COLUMN_PHYSICAL_LENGTH,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeLinkController getInstance()
	{
		if (instance == null)
			instance = new SchemeLinkController();
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
		if (object instanceof SchemeLink)
		{
			SchemeLink link = (SchemeLink)object;
			if (key.equals(COLUMN_ID))
				result = link.id().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(link.created());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = link.creatorId().identifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(link.modified());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = link.modifierId().identifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = link.description();
			else if (key.equals(COLUMN_NAME))
				result = link.name();
			else if (key.equals(COLUMN_LINK_ID))
				result = link.linkImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_TYPE_ID))
				result = link.linkTypeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID))
				result = link.sourceSchemePort().id().identifierString();
			else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID))
				result = link.targetSchemePort().id().identifierString();
			else if (key.equals(COLUMN_SITE_NODE_ID))
				result = link.siteNodeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_OPTICAL_LENGTH))
				result = Double.toString(link.opticalLength());
			else if (key.equals(COLUMN_PHYSICAL_LENGTH))
				result = Double.toString(link.physicalLength());
			else if (key.equals(COLUMN_SCHEME_ID))
				result = link.scheme().id().identifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(link.characteristics().length);
				for (Iterator it = link.characteristicsImpl().getValue().iterator(); it.hasNext(); ) {
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
