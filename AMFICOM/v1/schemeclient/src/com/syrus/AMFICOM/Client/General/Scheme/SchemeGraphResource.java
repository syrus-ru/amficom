package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import com.syrus.AMFICOM.scheme.corba.*;

public class SchemeGraphResource
{
//	ApplicationContext aContext;
//	Dispatcher dispatcher;
	SchemeGraph graph;
	Scheme scheme;
	SchemeElement schemeelement;
	private SchemePath currentPath;

	public SchemeGraphResource(SchemeGraph graph)
	{
		this.graph = graph;
//		this.aContext = aContext;
	}

	void setCurrentPath(SchemePath path)
	{
		this.currentPath = path;
	}

	SchemePath getCurrentPath()
	{
		return currentPath;
	}

//	public void init_module()
//	{
//		dispatcher = aContext.getDispatcher();
//	}

	public Object[] getPathElements(SchemePath path)
	{
		Object[] cells = graph.getAll();
		ArrayList new_cells = new ArrayList();
		PathElement[] pes = path.links();
		ArrayList links = new ArrayList(pes.length);
		for (int i = 0; i < pes.length; i++)
			links.add(pes[i].abstractSchemeElement());

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultCableLink)
			{
				DefaultCableLink cable = (DefaultCableLink) cells[i];
				if (links.contains(cable.getSchemeCableLink()))
					new_cells.add(cable);
			}
			else if (cells[i] instanceof DefaultLink)
			{
				DefaultLink link = (DefaultLink) cells[i];
				if (links.contains(link.getSchemeLink()))
					new_cells.add(link);
			}
			else if (cells[i] instanceof DeviceGroup)
			{
				DeviceGroup group = (DeviceGroup)cells[i];
				if (links.contains(group.getSchemeElement()))
					new_cells.add(group);
			}
		}
		return new_cells.toArray();
	}
}