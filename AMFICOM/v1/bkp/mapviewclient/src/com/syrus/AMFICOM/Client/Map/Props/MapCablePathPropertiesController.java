/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public final class MapCablePathPropertiesController 
		implements MapElementPropertiesController 
{

	private static MapCablePathPropertiesController instance;

	private List keys;
	
	private Map schemeCableLinks = new HashMap();
	private Map nodes = new HashMap();

	private MapCablePathPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_SCHEME_CABLE_ID,
				PROPERTY_TOPOLOGICAL_LENGTH,
				PROPERTY_OPTICAL_LENGTH,
				PROPERTY_PHYSICAL_LENGTH,
				PROPERTY_START_NODE_ID,
				PROPERTY_END_NODE_ID,
				PROPERTY_COLOR,
				PROPERTY_STYLE,
				PROPERTY_THICKNESS};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapCablePathPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapCablePathPropertiesController();
		return instance;
	}
	
	public String getKey(final int index) 
	{
		return (String )this.keys.get(index);
	}

	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = LangModelMap.getString(key);
		if(name == null)
			name = "";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		CablePath path = (CablePath)object;
		
		CableController cc = (CableController)com.syrus.AMFICOM.Client.Map.Controllers.CableController.getInstance();

		if (key.equals(PROPERTY_NAME))
		{
			result = path.getName();
		}
		else
		if (key.equals(PROPERTY_SCHEME_CABLE_ID))
		{
			// remove .getName
			result = path.getSchemeCableLink().name();
		}
		else
		if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
		{
			result = MapPropertiesManager.getDistanceFormat().format(path.getLengthLt());
		}
		else
		if (key.equals(PROPERTY_OPTICAL_LENGTH))
		{
			result = MapPropertiesManager.getDistanceFormat().format(path.getLengthLo());
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LENGTH))
		{
			result = MapPropertiesManager.getDistanceFormat().format(path.getLengthLf());
		}
		else
		if (key.equals(PROPERTY_START_NODE_ID))
		{
			// remove .getName
			result = path.getStartNode().getName();
		}
		else
		if (key.equals(PROPERTY_END_NODE_ID))
		{
			// remove .getName
			result = path.getEndNode().getName();
		}
		else
		if (key.equals(PROPERTY_COLOR))
		{
			result = cc.getColor(path);
		}
		else
		if (key.equals(PROPERTY_STYLE))
		{
			result = cc.getStyle(path);
		}
		else
		if (key.equals(PROPERTY_THICKNESS))
		{
			result = String.valueOf(cc.getLineSize(path));
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		if (key.equals(PROPERTY_NAME))
			return true;
		if (key.equals(PROPERTY_OPTICAL_LENGTH))
			return true;
		if (key.equals(PROPERTY_PHYSICAL_LENGTH))
			return true;
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
		CablePath path = (CablePath)object;

		if (key.equals(PROPERTY_NAME))
		{
			path.setName((String )value);
		}
		else
		if (key.equals(PROPERTY_OPTICAL_LENGTH))
		{
			try 
			{
				path.setLengthLo(Double.parseDouble((String )value));
			} 
			catch (NumberFormatException ex) 
			{
			} 
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LENGTH))
		{
			try
			{
				path.setLengthLf(Double.parseDouble((String )value));
			} 
			catch (NumberFormatException ex) 
			{
			} 
		}
	}

	public Object getPropertyValue(final String key) 
	{
		Object result = "";
		return result;
	}

	public Object getPropertyValue(Object object, final String key) 
	{
		Object result = "";
		if (key.equals(PROPERTY_SCHEME_CABLE_ID))
		{
			schemeCableLinks.clear();
			CablePath cpath = (CablePath)object;
			for(Iterator it = cpath.getMapView().getSchemes().iterator(); it.hasNext();)
			{
				Scheme scheme = (Scheme )it.next();
				for(Iterator it2 = SchemeUtils.getAllCableLinks(scheme).iterator(); it2.hasNext();)
				{
					SchemeCableLink scl = (SchemeCableLink )it2.next();
					schemeCableLinks.put(new ObjectResourceLabel(scl, scl.name()), scl);
				}
			}
			result = schemeCableLinks;
		}
		else
		if (key.equals(PROPERTY_START_NODE_ID)
			|| key.equals(PROPERTY_END_NODE_ID))
		{
			nodes.clear();
			CablePath cpath = (CablePath)object;
			for(Iterator it = cpath.getMap().getSiteNodes().iterator(); it.hasNext();)
			{
				AbstractNode node = (AbstractNode)it.next();
				nodes.put(new ObjectResourceLabel(node, node.getName()), node);
			}
			result = nodes;
		}
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) 
	{
	}

	public Class getPropertyClass(String key) 
	{
		Class clazz = String.class;
		if (key.equals(PROPERTY_NAME))
			clazz = String.class;
		else
		if (key.equals(PROPERTY_SCHEME_CABLE_ID))
			clazz = String.class;
//			clazz = SchemeCableLink.class;
		else
		if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
			clazz = String.class;
		else
		if (key.equals(PROPERTY_OPTICAL_LENGTH))
			clazz = String.class;
		else
		if (key.equals(PROPERTY_PHYSICAL_LENGTH))
			clazz = String.class;
		else
		if (key.equals(PROPERTY_START_NODE_ID))
			clazz = String.class;
//			clazz = MapNodeElement.class;
		else
		if (key.equals(PROPERTY_END_NODE_ID))
			clazz = String.class;
//			clazz = MapNodeElement.class;
		else
		if (key.equals(PROPERTY_COLOR))
			clazz = Color.class;
		else
		if (key.equals(PROPERTY_STYLE))
			clazz = BasicStroke.class;
		else
		if (key.equals(PROPERTY_THICKNESS))
			clazz = String.class;
		return clazz;
	}

}
