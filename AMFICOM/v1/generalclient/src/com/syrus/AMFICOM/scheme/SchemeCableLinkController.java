
package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.Characteristic;
import java.util.*;

public final class SchemeCableLinkController extends ObjectResourceController {

	public static final String					COLUMN_SOURCE_SCHEME_PORT_ID	= "source_scheme_port_id";
	public static final String					COLUMN_TARGET_SCHEME_PORT_ID	= "target_scheme_port_id";
	public static final String					COLUMN_LINK_ID					= "link_id";
	public static final String					COLUMN_SCHEME_ID				= "scheme_id";
	public static final String					COLUMN_OPTICAL_LENGTH			= "optical_length";
	public static final String					COLUMN_PHYSICAL_LENGTH			= "physical_length";
	public static final String					COLUMN_SCHEME_CABLE_THREADS		= "scheme_cable_threads";
	public static final String					COLUMN_CABLE_CHANNELING_ITEMS	= "scheme_channeling_items";

	private static SchemeCableLinkController	instance;

	private List								keys;

	private SchemeCableLinkController() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_ID, COLUMN_CREATED, COLUMN_CREATOR_ID, COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_SOURCE_SCHEME_PORT_ID,
				COLUMN_TARGET_SCHEME_PORT_ID, COLUMN_LINK_ID, COLUMN_TYPE_ID, COLUMN_SCHEME_ID, COLUMN_OPTICAL_LENGTH,
				COLUMN_PHYSICAL_LENGTH, COLUMN_SCHEME_CABLE_THREADS, COLUMN_CABLE_CHANNELING_ITEMS,
				COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeCableLinkController getInstance() {
		if (instance == null)
			instance = new SchemeCableLinkController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(COLUMN_NAME))
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;
		if (object instanceof SchemeCableLink) {
			SchemeCableLink link = (SchemeCableLink) object;
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
				result = link.getCableLinkType().getId().getIdentifierString();
			else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID))
				result = link.getSourceSchemeCablePort().getId().getIdentifierString();
			else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID))
				result = link.getTargetSchemeCablePort().getId().getIdentifierString();
			else if (key.equals(COLUMN_OPTICAL_LENGTH))
				result = Double.toString(link.getOpticalLength());
			else if (key.equals(COLUMN_PHYSICAL_LENGTH))
				result = Double.toString(link.getPhysicalLength());
			else if (key.equals(COLUMN_SCHEME_ID))
				result = link.getParentScheme().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_CABLE_THREADS)) {
				final Set schemeCableThreads = link.getSchemeCableThreads();
				final Set schemeCableThreadIds = new HashSet(schemeCableThreads.size());
				for (final Iterator schemeCableThreadIterator = schemeCableThreads.iterator(); schemeCableThreadIterator.hasNext();)
					schemeCableThreadIds.add(((SchemeCableThread) schemeCableThreadIterator.next()).getId().getIdentifierString());
				result = schemeCableThreadIds;
			} else if (key.equals(COLUMN_CABLE_CHANNELING_ITEMS)) {
				final Set cableChannelingItems = link.getCableChannelingItems();
				final Set cableChannelingItemIds = new HashSet(cableChannelingItems.size());
				for (final Iterator cableChannelingItemIterator = cableChannelingItems.iterator(); cableChannelingItemIterator.hasNext();)
					cableChannelingItemIds.add(((CableChannelingItem) cableChannelingItemIterator.next()).getId().getIdentifierString());
				result = cableChannelingItemIds;
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(link.getCharacteristics().size());
				for (Iterator it = link.getCharacteristics().iterator(); it.hasNext();) {
					Characteristic ch = (Characteristic) it.next();
					res.add(ch.getId().getIdentifierString());
				}
				result = res;
			}
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}
}
