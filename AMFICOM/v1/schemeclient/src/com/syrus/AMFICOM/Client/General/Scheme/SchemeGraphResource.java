package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import com.jgraph.graph.*;
import com.jgraph.pad.*;
import com.jgraph.pad.GPGraph.*;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeGraphResource
{
//	ApplicationContext aContext;
//	Dispatcher dispatcher;
	SchemeGraph graph;
	Scheme scheme;
	SchemeElement schemeelement;
	public SchemePath currentPath;

	public SchemeGraphResource(SchemeGraph graph)
	{
		this.graph = graph;
//		this.aContext = aContext;
	}

//	public void init_module()
//	{
//		dispatcher = aContext.getDispatcher();
//	}

	public Object[] getPathElements(SchemePath path)
	{
		Object[] cells = graph.getAll();
		ArrayList new_cells = new ArrayList();
		ArrayList links = new ArrayList(path.links.size());
		for (Iterator it = path.links.iterator(); it.hasNext(); )
			links.add( ( (PathElement) it.next()).link_id);

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultCableLink)
			{
				DefaultCableLink cable = (DefaultCableLink) cells[i];
				if (links.contains(cable.getSchemeCableLinkId()))
					new_cells.add(cable);
			}
			else if (cells[i] instanceof DefaultLink)
			{
				DefaultLink link = (DefaultLink) cells[i];
				if (links.contains(link.getSchemeLinkId()))
					new_cells.add(link);
			}
		}
		return new_cells.toArray();
	}

	public Object[] getPathElements(TransmissionPath path)
	{
		Object[] cells = graph.getAll();
		List new_cells = new ArrayList();
		List links = new ArrayList(path.links.size());
		for (Iterator it = path.links.iterator(); it.hasNext();)
		{
			TransmissionPathElement tpe = (TransmissionPathElement)it.next();
			links.add(tpe.link_id);
		}

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultCableLink)
			{
				DefaultCableLink cable = (DefaultCableLink) cells[i];
				if (!cable.getSchemeCableLinkId().equals(""))
					if (links.contains(cable.getSchemeCableLink().cable_link_id))
						new_cells.add(cable);
			}
			else if (cells[i] instanceof DefaultLink)
			{
				DefaultLink link = (DefaultLink) cells[i];
				if (!link.getSchemeLinkId().equals(""))
					if (links.contains(link.getSchemeLink().link_id))
						new_cells.add(link);
			}
		}
		return new_cells.toArray();
	}


}