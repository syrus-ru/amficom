package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import com.jgraph.graph.*;

public class DefaultCableLink extends DefaultEdge
{
	private static final long serialVersionUID = 01L;

	protected String scheme_cablelink_id = "";
	protected String scheme_path_id = "";
	protected Point[] routed;

	protected transient Object _source, _target;
	protected transient Object source, target;

	//private transient boolean first_time = true;
	//private transient EdgeView _edge;
	//private transient Object object;

	public DefaultCableLink(Object userObject)
	{
		super(userObject);
	}

	public DefaultCableLink(Object userObject, boolean allowCholdren)
	{
		super(userObject, allowCholdren);
	}

	public void setSource(Object port)
	{
		super.setSource(port);
		source = port;
	}

	public void setTarget(Object port)
	{
		super.setTarget(port);
		target = port;
	}

	public Edge.Routing getRouting()
	{
		return new LinkRouting();
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_cablelink_id);
		out.writeObject(scheme_path_id);
		out.writeObject(routed);

		HashSet set = (HashSet)Pool.get("serialized", "serialized");
		if (set == null)
		{
			set = new HashSet();
			Pool.put("serialized", "serialized", set);
		}
		if (!set.contains(_source))
		{
			set.add(_source);
			out.writeObject(_source);
		}
		if (!set.contains(_target))
		{
			set.add(_target);
			out.writeObject(_target);
		}
		if (!set.contains(source))
		{
			set.add(source);
			out.writeObject(source);
		}
		if (!set.contains(target))
		{
			set.add(target);
			out.writeObject(target);
		}
	}
*/
	public class LinkRouting implements Edge.Routing
	{
		private static final long serialVersionUID = 01L;

		public void route(EdgeView edge, java.util.List points)
		{
			DefaultCableLink cell = (DefaultCableLink)edge.getCell();
			SchemeGraph graph = (SchemeGraph)edge.getGraph();

			if (!getSchemeCableLinkId().equals(cell.getSchemeCableLinkId()))
				setSchemeCableLinkId(cell.getSchemeCableLinkId());
//			if (!getSchemePathId().equals(cell.getSchemePathId()))
//				setSchemePathId(cell.getSchemePathId());

			if (cell.source != cell._source)
			{
				source = cell.source;
				_source = cell._source;
			}
			if (cell.target != cell._target)
			{
				target = cell.target;
				_target = cell._target;
			}

			if (source != null && !source.equals(_source))
//					(_source == null ||
//					!((CablePortCell)((DefaultPort)source).getParent()).getSchemeCablePortId().equals(
//					((CablePortCell)((DefaultPort)_source).getParent()).getSchemeCablePortId())))
			{
				_source = source;
				cell._source = cell.source;

				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, true);
				if (((DefaultPort)source).getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell)((DefaultPort)source).getParent(), true);
			}
			if (source == null && _source != null)
			{
				_source = source;
				cell._source = cell.source;

				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, true);
			}

			if (target != null && !target.equals(_target))
//					(_target == null ||
//					!((CablePortCell)((DefaultPort)target).getParent()).getSchemeCablePortId().equals(
//					((CablePortCell)((DefaultPort)_target).getParent()).getSchemeCablePortId())))
			{
				_target = target;
				cell._target = cell.target;

				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, false);
				if (((DefaultPort)target).getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell)((DefaultPort)target).getParent(), false);
			}
			if (target == null && _target != null)
			{
				_target = target;
				cell._target = cell.target;

				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, false);
			}

			int n = points.size();
			Point from = edge.getPoint(0);
			if (edge.getSource() instanceof PortView)
				from = edge.getSource().getLocation(null);
			Point to = edge.getPoint(n - 1);
			if (edge.getTarget() instanceof PortView)
				to = edge.getTarget().getLocation(null);
			if (from != null && to != null)
			{
				// Handle self references
				if (edge.getSource() == edge.getTarget() && edge.getSource() != null)
				{
					Rectangle bounds = edge.getSource().getParentView().getBounds();
					int height = edge.getGraph().getGridSize();
					int width = (int) (bounds.getWidth() / 3);
					routed = new Point[4];
					routed[0] = new Point(bounds.x + width, bounds.y + bounds.height);
					routed[1] = new Point(bounds.x + width, bounds.y + bounds.height + height);
					routed[2] = new Point(bounds.x + 2 * width, bounds.y + bounds.height + height);
					routed[3] = new Point(bounds.x + 2 * width, bounds.y + bounds.height);
				}
				else
				{
					boolean bendable = GraphConstants.isBendable(edge.getAllAttributes());
					if (routed == null)//(first_time) //if (!bendable || first_time)
					{
						int dx = Math.abs(from.x - to.x);
						int dy = Math.abs(from.y - to.y);
						int x2 = from.x + ((to.x - from.x) / 2);
						int y2 = from.y + ((to.y - from.y) / 2);
						routed = new Point[4];
						routed[0] = graph.snap(new Point(x2, from.y));
						routed[1] = graph.snap(new Point(x2, from.y));
						routed[2] = graph.snap(new Point(x2, to.y));
						routed[3] = graph.snap(new Point(x2, to.y));

						//if (_edge != null && !_edge.equals(edge))
						//	first_time = false;
						//else
						//	_edge = edge;

					}
				}
				// Set/Add Points
				for (int i=0; i<routed.length; i++)
					if (points.size() > i+2)
						points.set(i+1, routed[i]);
					else
						points.add(i+1, routed[i]);
				// Remove spare points
				while (points.size() > routed.length+2)
				{
					points.remove(points.size()-2);
				}
			}
		}
	}

	public SchemeCableLink getSchemeCableLink()
	{
		return (SchemeCableLink)Pool.get(SchemeCableLink.typ, scheme_cablelink_id);
	}

	public String getSchemeCableLinkId()
	{
		return scheme_cablelink_id;
	}

	public void setSchemeCableLinkId(String id)
	{
		scheme_cablelink_id = id;
	}

//	public SchemePath getSchemePath()
//	{
//		return (SchemePath)Pool.get(SchemePath.typ, scheme_path_id);
//	}

//	public String getSchemePathId()
//	{
//		return scheme_path_id;
//	}

//	public void setSchemePathId(String id)
//	{
//		scheme_path_id = id;
//	}
}

