package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import com.jgraph.graph.*;

public class DefaultLink extends DefaultEdge
{
	private static final long serialVersionUID = 01L;
	protected String scheme_link_id = "";
	protected String scheme_path_id = "";
	protected Point[] routed;
	protected transient Object _source, _target;
	protected transient Object source, target;

//	private transient boolean first_time = true;

//	private transient EdgeView _edge;
//	private transient Object object;

	LinkRouting routing = new LinkRouting();

	public DefaultLink(Object userObject)
	{
		super(userObject);
	}

	public DefaultLink(Object userObject, boolean allowCholdren)
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

	public DefaultEdge.Routing getRouting()
	{
		return routing;
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_link_id);
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
	public class LinkRouting implements DefaultEdge.Routing
	{
		private static final long serialVersionUID = 01L;

		public void route(EdgeView edge, java.util.List points)
		{
			DefaultLink cell = (DefaultLink)edge.getCell();
			SchemeGraph graph = (SchemeGraph)edge.getGraph();

			if (!getSchemeLinkId().equals(cell.getSchemeLinkId()))
				setSchemeLinkId(cell.getSchemeLinkId());
			if (!getSchemePathId().equals(cell.getSchemePathId()))
				setSchemePathId(cell.getSchemePathId());

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
//					!((PortCell)((DefaultPort)source).getParent()).getSchemePortId().equals(
//					((PortCell)((DefaultPort)_source).getParent()).getSchemePortId())))
			{
				_source = source;
				cell._source = cell.source;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, true);
				if (((DefaultPort)source).getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this, (PortCell)((DefaultPort)source).getParent(), true);
			}
			if (source == null && _source != null)
			{
				_source = source;
				cell._source = cell.source;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, true);
			}

			if (target != null && !target.equals(_target))
//					(_target == null ||
//					!((PortCell)((DefaultPort)target).getParent()).getSchemePortId().equals(
//					((PortCell)((DefaultPort)_target).getParent()).getSchemePortId())))
			{
				_target = target;
				cell._target = cell.target;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, false);
				if (((DefaultPort)target).getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this, (PortCell)((DefaultPort)target).getParent(), false);
			}
			if (target == null && _target != null)
			{
				_target = target;
				cell._target = cell.target;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, false);
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
					routed[0] = graph.snap(new Point(bounds.x + width, bounds.y + bounds.height));
					routed[1] = graph.snap(new Point(bounds.x + width, bounds.y + bounds.height + height));
					routed[2] = graph.snap(new Point(bounds.x + 2 * width, bounds.y + bounds.height + height));
					routed[3] = graph.snap(new Point(bounds.x + 2 * width, bounds.y + bounds.height));
				}
				else
				{
					boolean bendable = GraphConstants.isBendable(edge.getAllAttributes());
					if (!bendable || routed == null)
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
//						first_time = false;
					}
	/*				else
					{
						Point _label = GraphConstants.getLabelPosition(getAttributes());
						Point p0, pe;
						if (points.get(0) instanceof PortView)
							p0 = ((PortView)points.get(0)).getLocation(edge);
						else
							p0 = (Point)points.get(0);
						if (points.get(points.size() - 1) instanceof PortView)
							pe = ((PortView)points.get(points.size() - 1)).getLocation(edge);
						else
							pe = (Point)points.get(points.size() - 1);
						double dx = 1, dy = 1;
						if (pe.x - routed[1].x != 0)
							dx = (routed[0].x - p0.x) / (pe.x - routed[1].x);
						if (pe.y - routed[1].y != 0)
							dy = (routed[0].y - p0.y) / (pe.y - routed[1].y);
						Point label = new Point ((int)(_label.x * dx),
																		 (int)(_label.y * dy));
					//	GraphConstants.setLabelPosition(getAttributes(), label);
					}*/
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

	public SchemeLink getSchemeLink()
	{
		return (SchemeLink)Pool.get(SchemeLink.typ, scheme_link_id);
	}

	public String getSchemeLinkId()
	{
		return scheme_link_id;
	}

	public void setSchemeLinkId(String id)
	{
		scheme_link_id = id;
	}

	public SchemePath getSchemePath()
	{
		return (SchemePath)Pool.get(SchemePath.typ, scheme_path_id);
	}

	public String getSchemePathId()
	{
		return scheme_path_id;
	}

	public void setSchemePathId(String id)
	{
		scheme_path_id = id;
	}
}

