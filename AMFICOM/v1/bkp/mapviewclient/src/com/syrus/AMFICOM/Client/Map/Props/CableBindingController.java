/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.syrus.AMFICOM.mapview.CablePathBinding;

public final class CableBindingController implements ObjectResourceController 
{
	public static final String KEY_START_NODE = "startnode";
	public static final String KEY_START_SPARE = "startspare";
	public static final String KEY_LINK = "link";
	public static final String KEY_END_SPARE = "endspare";
	public static final String KEY_END_NODE = "endnode";

	private static CableBindingController instance;

	private List keys;
	
	CablePath cablePath;
	Map map;

	private CableBindingController() 
	{
		// empty private constructor
		String[] keysArray = new String[] { 
				KEY_START_NODE, 
				KEY_START_SPARE, 
				KEY_LINK, 
				KEY_END_SPARE, 
				KEY_END_NODE };
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableBindingController getInstance() 
	{
		if (instance == null)
			instance = new CableBindingController();
		return instance;
	}
	
	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(KEY_START_NODE))
			name = LangModelMap.getString("StartNode");
		else
		if (key.equals(KEY_START_SPARE))
			name = LangModelMap.getString("StartSpare");
		else
		if (key.equals(KEY_LINK))
			name = LangModelMap.getString("tunnel");
		else
		if (key.equals(KEY_END_SPARE))
			name = LangModelMap.getString("EndSpare");
		else
		if (key.equals(KEY_END_NODE))
			name = LangModelMap.getString("EndNode");
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof PhysicalLink) 
		{
			PhysicalLink link = (PhysicalLink)object;
			CableChannelingItem cci = (CableChannelingItem )cablePath.getBinding().get(link);
			if (key.equals(KEY_START_NODE))
			{
//				result = link.getStartNode().getName();
				AbstractNode mne = cci.startSiteNodeImpl();
				result = (mne == null) ? "" : mne.getName();
			}
			else
			if (key.equals(KEY_START_SPARE))
			{
				result = (link instanceof UnboundLink) ? "" : String.valueOf(cci.startSpare());
			}
			else
			if (key.equals(KEY_LINK))
			{
				result = (link instanceof UnboundLink) ? "" : link.getName();
//				MapPhysicalLinkElement mle = (MapPhysicalLinkElement )map.getPhysicalLink(cci.physicalLinkId);
//				result = (mle == null) ? "" : mle.getName();
			}
			else
			if (key.equals(KEY_END_SPARE))
			{
				result = (link instanceof UnboundLink) ? "" : String.valueOf(cci.endSpare());
			}
			else
			if (key.equals(KEY_END_NODE))
			{
//				result = link.getEndNode().getName();
				AbstractNode mne = cci.endSiteNodeImpl();
				result = (mne == null) ? "" : mne.getName();
			}
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		boolean editable = false;
		if (key.equals(KEY_START_SPARE)
			|| key.equals(KEY_END_SPARE))
		{
			editable = true;
		}
		return editable;
	}

	public void setValue(Object object, final String key, final Object value)
	{
		if (object instanceof PhysicalLink) 
		{
			PhysicalLink link = (PhysicalLink)object;
			CableChannelingItem cci = (CableChannelingItem )cablePath.getBinding().get(link);
			if (key.equals(KEY_START_SPARE))
			{
				if(cci.physicalLink() != null)
					cci.startSpare(Double.parseDouble((String )value));
			}
			else
			if (key.equals(KEY_END_SPARE))
			{
				if(cci.physicalLink() != null)
					cci.endSpare(Double.parseDouble((String )value));
			}
		}
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

	public void setCablePath(CablePath cablePath)
	{
		this.cablePath = cablePath;
		this.map = cablePath.getMap();
	}

	public CablePath getCablePath()
	{
		return cablePath;
	}
}
