package com.syrus.AMFICOM.scheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;

public final class SchemeController implements ObjectResourceController
{
	public static final String COLUMN_LABEL = "label";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_SYMBOL_ID = "symbol_id";
	public static final String COLUMN_SITE_NODE_ID = "site_node_id";
	public static final String COLUMN_SCHEME_LINK_IDS = "link_ids";
	public static final String COLUMN_SCHEME_CABLE_LINK_IDS = "cable_link_ids";
	public static final String COLUMN_SCHEME_ELEMENT_IDS = "element_ids";
	public static final String COLUMN_MONITORING_SOLUTION_ID = "monitoring_solution_id";
	public static final String COLUMN_SCHEME_CELL_ID = "scheme_cell_id";
	public static final String COLUMN_UGO_CELL_ID = "ugo_cell_id";
	public static final String COLUMN_HEIGHT = "height";
	public static final String COLUMN_WIDTH = "width";
	public static final String COLUMN_MAP_ID = "map_id";
	public static final String COLUMN_DOMAIN_ID = "domain_id";

	private static SchemeController instance;

	private List keys;

	private SchemeController()
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
				COLUMN_TYPE,
				COLUMN_SYMBOL_ID,
				COLUMN_KIS_ID,
				COLUMN_SITE_NODE_ID,
				COLUMN_SCHEME_CELL_ID,
				COLUMN_UGO_CELL_ID,
				COLUMN_WIDTH,
				COLUMN_HEIGHT,
				COLUMN_SCHEME_LINK_IDS,
				COLUMN_SCHEME_CABLE_LINK_IDS,
				COLUMN_SCHEME_ELEMENT_IDS,
				COLUMN_MONITORING_SOLUTION_ID,
				COLUMN_DOMAIN_ID,
				COLUMN_MAP_ID
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeController getInstance()
	{
		if (instance == null)
			instance = new SchemeController();
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
		if (object instanceof Scheme)
		{
			Scheme scheme = (Scheme)object;
			if (key.equals(COLUMN_ID))
				result = scheme.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(scheme.getCreated());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = scheme.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(scheme.getModified());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = scheme.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = scheme.description();
			else if (key.equals(COLUMN_NAME))
				result = scheme.name();
			else if (key.equals(COLUMN_LABEL))
				result = scheme.label();
			else if (key.equals(COLUMN_TYPE))
				result = Integer.toString(scheme.type().value());
			else if (key.equals(COLUMN_SYMBOL_ID))
				result = scheme.symbolImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_WIDTH))
				result = Integer.toString(scheme.width());
			else if (key.equals(COLUMN_HEIGHT))
				result = Integer.toString(scheme.height());
			else if (key.equals(COLUMN_SCHEME_ELEMENT_IDS)) {
				SchemeElement[] elements = scheme.schemeElements();
				List res = new ArrayList(elements.length);
				for (int i = 0; i < elements.length; i++) {
					res.add(elements[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_SCHEME_LINK_IDS)) {
				SchemeLink[] links = scheme.schemeLinks();
				List res = new ArrayList(links.length);
				for (int i = 0; i < links.length; i++) {
					res.add(links[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_SCHEME_CABLE_LINK_IDS)) {
				SchemeCableLink[] links = scheme.schemeCableLinks();
				List res = new ArrayList(links.length);
				for (int i = 0; i < links.length; i++) {
					res.add(links[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_MONITORING_SOLUTION_ID))
				result = scheme.schemeMonitoringSolution().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_CELL_ID))
				result = scheme.schemeCellImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_UGO_CELL_ID))
				result = scheme.ugoCellImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_DOMAIN_ID))
				result = scheme.domainImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_MAP_ID))
				result = scheme.mapImpl().getId().getIdentifierString();
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
