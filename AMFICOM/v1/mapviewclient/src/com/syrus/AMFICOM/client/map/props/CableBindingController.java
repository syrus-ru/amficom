/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CableBindingController implements ObjectResourceController 
{
	public static final String KEY_START_NODE = "startnode";
	public static final String KEY_START_SPARE = "startspare";
	public static final String KEY_LINK = "link";
	public static final String KEY_END_SPARE = "endspare";
	public static final String KEY_END_NODE = "endnode";

	private static CableBindingController instance;

	private List keys;
	
	MapCablePathElement path;
//	SchemeCableLink scl;

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
			name = LangModel.getString("StartNode");
		else
		if (key.equals(KEY_START_NODE))
			name = LangModel.getString("StartSpare");
		else
		if (key.equals(KEY_START_NODE))
			name = LangModel.getString("PhysicalLink");
		else
		if (key.equals(KEY_START_NODE))
			name = LangModel.getString("EndSpare");
		else
		if (key.equals(KEY_START_NODE))
			name = LangModel.getString("EndNode");
		return name;
	}

	public Object getValue(final ObjectResource objectResource, final String key)
	{
		Object result = null;
		if (objectResource instanceof CableChannelingItem) 
		{
			CableChannelingItem cci = (CableChannelingItem )objectResource;
			if (key.equals(KEY_START_NODE))
			{
				MapNodeElement mne = (MapNodeElement )path.getMap().getNode(cci.startSiteId);
				result = mne.getName();
			}
			else
			if (key.equals(KEY_START_SPARE))
			{
				result = String.valueOf(cci.startSpare);
			}
			else
			if (key.equals(KEY_LINK))
			{
				MapLinkElement mle = (MapLinkElement )path.getMap().getPhysicalLink(cci.physicalLinkId);
				result = mle.getName();
			}
			else
			if (key.equals(KEY_END_SPARE))
			{
				result = String.valueOf(cci.endSpare);
			}
			else
			if (key.equals(KEY_END_NODE))
			{
				MapNodeElement mne = (MapNodeElement )path.getMap().getNode(cci.endSiteId);
				result = mne.getName();
			}
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(ObjectResource objectResource, final String key, final Object value)
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


	public void setPath(MapCablePathElement path)
	{
		this.path = path;
//		scl = path.getSchemeCableLink();
	}


	public MapCablePathElement getPath()
	{
		return path;
	}
}
