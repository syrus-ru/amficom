package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class SchemeLinkController extends ObjectResourceController
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
				result = link.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(link.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = link.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(link.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = link.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = link.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = link.getName();
			else if (key.equals(COLUMN_LINK_ID))
				result = link.getLink().getId().getIdentifierString();
			else if (key.equals(COLUMN_TYPE_ID))
				result = link.getLinkType().getId().getIdentifierString();
			else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID))
				result = link.getSourceSchemePort().getId().getIdentifierString();
			else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID))
				result = link.getTargetSchemePort().getId().getIdentifierString();
			else if (key.equals(COLUMN_SITE_NODE_ID))
				result = link.getSiteNode().getId().getIdentifierString();
			else if (key.equals(COLUMN_OPTICAL_LENGTH))
				result = Double.toString(link.getOpticalLength());
			else if (key.equals(COLUMN_PHYSICAL_LENGTH))
				result = Double.toString(link.getPhysicalLength());
			else if (key.equals(COLUMN_SCHEME_ID))
				result = link.getParentScheme().getId().getIdentifierString();
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
