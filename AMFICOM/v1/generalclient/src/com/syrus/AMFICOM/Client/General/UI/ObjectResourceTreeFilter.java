package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.Enumeration;
import java.util.Vector;

public class ObjectResourceTreeFilter
{
	Vector filter;
	ObjectResource resource;

	public ObjectResourceTreeFilter(ObjectResource resource)
	{
		this (resource, new String[0]);
	}

	public ObjectResourceTreeFilter(ObjectResource resource, String filter_element)
	{
		this(resource, new String[] {filter_element});
	}

	public ObjectResourceTreeFilter(ObjectResource resource, String[] filter_elements)
	{
		this.resource = resource;
		setFilter (filter_elements);
	}

	public void setFilter (String[] filter_elements)
	{
    if (filter_elements == null)
      return;
    filter = new Vector();
    for (int i=0; i < filter_elements.length; i++)
      filter.add(filter_elements[i]);
	}

	public void addFilterElement (String filter_element)
	{
		filter.add(filter_element);
	}

	public void removeFilterElement (String filter_element)
	{
		filter.remove(filter_element);
	}

	public void removeFilter ()
	{
		filter = new Vector();
	}

	public ObjectResource getResource ()
	{
		return resource;
	}

	public String getName()
	{
    if (resource == null)
      return "unknown object resource";

		return resource.getName();
	}

	public Enumeration getChildTypes()
	{
    if (resource == null)
      return new Vector().elements();

		Enumeration types = resource.getChildTypes();
    if (filter.isEmpty())
      return types;

		Vector filtered_types = new Vector();
		while (types.hasMoreElements())
		{
      String type = (String)types.nextElement();
      if (!filter.contains(type))
        filtered_types.add(type);
		}
		return filtered_types.elements();
	}

	public Enumeration getChildren(String type_id)
	{
    if (resource == null)
      return new Vector().elements();

    return resource.getChildren(type_id);
	}
}