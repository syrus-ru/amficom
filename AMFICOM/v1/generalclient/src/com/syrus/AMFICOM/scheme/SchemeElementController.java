package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class SchemeElementController extends ObjectResourceController
{
	public static final String COLUMN_LABEL = "label";
	public static final String COLUMN_SYMBOL_ID = "symbol_id";
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String COLUMN_EQUIPMENT_TYPE_ID = "equipment_type_id";
	public static final String COLUMN_SCHEME_ID = "scheme_id";
	public static final String COLUMN_INTERNAL_SCHEME_ID = "internal_scheme_id";
	public static final String COLUMN_KIS_ID = "kis_id";
	public static final String COLUMN_SITE_NODE_ID = "site_node_id";
	public static final String COLUMN_SCHEME_LINK_IDS = "link_ids";
	public static final String COLUMN_SCHEME_DEVICE_IDS = "device_ids";
	public static final String COLUMN_SCHEME_ELEMENT_IDS = "element_ids";
	public static final String COLUMN_SCHEME_CELL_ID = "scheme_cell_id";
	public static final String COLUMN_UGO_CELL_ID = "ugo_cell_id";

	private static SchemeElementController instance;

	private List keys;

	private SchemeElementController()
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
				COLUMN_LABEL,
				COLUMN_SYMBOL_ID,
				COLUMN_EQUIPMENT_ID,
				COLUMN_EQUIPMENT_TYPE_ID,
				COLUMN_SCHEME_ID,
				COLUMN_INTERNAL_SCHEME_ID,
				COLUMN_KIS_ID,
				COLUMN_SITE_NODE_ID,
				COLUMN_SCHEME_LINK_IDS,
				COLUMN_SCHEME_DEVICE_IDS,
				COLUMN_SCHEME_ELEMENT_IDS,
				COLUMN_SCHEME_CELL_ID,
				COLUMN_UGO_CELL_ID,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeElementController getInstance()
	{
		if (instance == null)
			instance = new SchemeElementController();
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
		if (object instanceof SchemeElement)
		{
			SchemeElement element = (SchemeElement)object;
			if (key.equals(COLUMN_ID))
				result = element.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(element.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = element.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(element.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = element.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = element.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = element.getName();
			else if (key.equals(COLUMN_LABEL))
				result = element.getLabel();
			else if (key.equals(COLUMN_SYMBOL_ID))
				result = element.getSymbol().getId().getIdentifierString();
			else if (key.equals(COLUMN_EQUIPMENT_ID))
				result = element.getEquipment().getId().getIdentifierString();
			else if (key.equals(COLUMN_EQUIPMENT_TYPE_ID))
				result = element.getEquipmentType().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_ID))
				result = element.getParentScheme().getId().getIdentifierString();
			else if (key.equals(COLUMN_INTERNAL_SCHEME_ID))
				result = element.getScheme().getId().getIdentifierString();
			else if (key.equals(COLUMN_KIS_ID))
				result = element.getKis().getId().getIdentifierString();
			else if (key.equals(COLUMN_SITE_NODE_ID))
				result = element.getSiteNode().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_DEVICE_IDS)) {
				SchemeDevice[] devices = element.getSchemeDevicesAsArray();
				List res = new ArrayList(devices.length);
				for (int i = 0; i < devices.length; i++) {
					res.add(devices[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_SCHEME_ELEMENT_IDS)) {
				SchemeElement[] elements = element.getSchemeElementsAsArray();
				List res = new ArrayList(elements.length);
				for (int i = 0; i < elements.length; i++) {
					res.add(elements[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_SCHEME_LINK_IDS)) {
				SchemeLink[] links = element.getSchemeLinksAsArray();
				List res = new ArrayList(links.length);
				for (int i = 0; i < links.length; i++) {
					res.add(links[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_SCHEME_CELL_ID))
				result = element.getSchemeCell().getId().getIdentifierString();
			else if (key.equals(COLUMN_UGO_CELL_ID))
				result = element.getUgoCell().getId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(element.getCharacteristics().size());
				for (Iterator it = element.getCharacteristics().iterator(); it.hasNext(); ) {
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
