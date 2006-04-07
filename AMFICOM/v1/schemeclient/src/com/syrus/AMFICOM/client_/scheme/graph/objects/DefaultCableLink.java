/*
 * $Id: DefaultCableLink.java,v 1.25 2006/02/09 15:24:54 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.25 $, $Date: 2006/02/09 15:24:54 $
 * @module schemeclient
 */

public class DefaultCableLink extends DefaultEdge implements IdentifiableCell {
	private static final long serialVersionUID = 3618420414867519024L;

	private Identifier schemeCablelinkId;

	protected Point[] _routed;
	protected Point[] routed;
	protected Point _from;
	protected Point _to;
	protected transient DefaultPort _source, _target;
	protected transient DefaultPort source1, target1;
	private LinkRouting routing = new LinkRouting();

	public static DefaultCableLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map<Object, Map> viewMap,
			ConnectionSet cs, Identifier linkId, boolean allowUnconnected) throws CreateObjectException {

		// we must connect cable to free CablePortCells
		CablePortCell sourceCablePortCell = null;
		CablePortCell targetCablePortCell = null;

		if (firstPort != null) {
			Object o = ((DefaultPort) firstPort.getCell()).getParent();
			if (o instanceof CablePortCell)
				sourceCablePortCell = (CablePortCell) o;
		}
		if (port != null) {
			Object o = ((DefaultPort) port.getCell()).getParent();
			if (o instanceof CablePortCell)
				targetCablePortCell = (CablePortCell) o;
		}
		
		if (!allowUnconnected && (sourceCablePortCell == null || targetCablePortCell == null)) {
			throw new CreateObjectException("Cable must be connected to cable ports : " + linkId.getIdentifierString());
		}
		if (sourceCablePortCell != null) {
			SchemeCableLink cl1 = sourceCablePortCell.getSchemeCablePort().getAbstractSchemeLink();
			if (cl1 != null && !cl1.getId().equals(linkId)) {
				throw new CreateObjectException("Other cable (" + cl1.getId() + ") already connected to port; instead of " + linkId.getIdentifierString());
			}
		}
		if (targetCablePortCell != null) {
			SchemeCableLink cl2 = targetCablePortCell.getSchemeCablePort().getAbstractSchemeLink();
			if (cl2 != null && !cl2.getId().equals(linkId)) {
				throw new CreateObjectException("Other cable (" + cl2.getId() + ") already connected to port; instead of " + linkId.getIdentifierString());
			}
		}
		DefaultCableLink cell = new DefaultCableLink(userObject);
		cell.setSchemeCableLinkId(linkId);

		int u = GraphConstants.PERCENT;
		Map map = new HashMap();
		GraphConstants.setRouting(map, cell.getRouting());
		GraphConstants.setLineWidth(map, 2.49f);
		GraphConstants.setLabelPosition(map, new Point((int)(u * 0.4), (int)(u * 0.49)));
		
		ArrayList<Point> list = new ArrayList<Point>();
		list.add(p);
		list.add(p2);

		GraphConstants.setPoints(map, list);
		GraphConstants.setBendable(map, true);
		viewMap.put(cell, map);
		
		if (sourceCablePortCell != null)
			cs.connect(cell, firstPort.getCell(), true);
		if (targetCablePortCell != null)
			cs.connect(cell, port.getCell(), false);
		
		return cell;
	}

	private DefaultCableLink(Object userObject) {
		super(userObject);
	}

	@Override
	public void setSource(Object port) {
		super.setSource(port);
		this.source1 = (DefaultPort)port;
	}

	@Override
	public void setTarget(Object port) {
		super.setTarget(port);
		this.target1 = (DefaultPort)port;
	}

	public Edge.Routing getRouting() {
		return this.routing;
	}

	public class LinkRouting implements Edge.Routing {
		private static final long serialVersionUID = 3618420414867519024L;

		public void route(EdgeView edge, java.util.List points) {
			DefaultCableLink cell = (DefaultCableLink) edge.getCell();
			SchemeGraph graph = (SchemeGraph) edge.getGraph();

			if (getSchemeCableLinkId() != null && !getSchemeCableLinkId().equals(cell.getSchemeCableLinkId()))
				setSchemeCableLinkId(cell.getSchemeCableLinkId());

			if (cell.source1 == null) {
				cell.source1 = (DefaultPort)cell.source;
			}
			if (cell.target1 == null) {
				cell.target1 = (DefaultPort)cell.target;
			}
			
			if (cell.source1 != cell._source) {
				DefaultCableLink.this.source1 = cell.source1;
				DefaultCableLink.this._source = cell._source;
			}
			if (cell.target1 != cell._target) {
				DefaultCableLink.this.target1 = cell.target1;
				DefaultCableLink.this._target = cell._target;
			}

			if (DefaultCableLink.this.source1 != null && !DefaultCableLink.this.source1.equals(DefaultCableLink.this._source)) {
				if (DefaultCableLink.this._source != null) {
					if (DefaultCableLink.this._source.getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._source.getParent(), true);
				}
				
				DefaultCableLink.this._source = DefaultCableLink.this.source1;
				cell._source = cell.source1;

				if (DefaultCableLink.this.source1.getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) DefaultCableLink.this.source1.getParent(), true);
			}
			if (DefaultCableLink.this.source1 == null && DefaultCableLink.this._source != null) {
				if (DefaultCableLink.this._source.getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._source.getParent(), true);
				
				DefaultCableLink.this._source = DefaultCableLink.this.source1;
				cell._source = cell.source1;
			}

			if (DefaultCableLink.this.target1 != null && !DefaultCableLink.this.target1.equals(DefaultCableLink.this._target)) {
				if (DefaultCableLink.this._target != null) {
					if (DefaultCableLink.this._target.getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._target.getParent(), false);
				}
				
				DefaultCableLink.this._target = DefaultCableLink.this.target1;
				cell._target = cell.target1;

				if (DefaultCableLink.this.target1.getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) DefaultCableLink.this.target1.getParent(), false);
			}
			if (DefaultCableLink.this.target1 == null && DefaultCableLink.this._target != null) {
				if (DefaultCableLink.this._target.getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._target.getParent(), false);
				
				DefaultCableLink.this._target = DefaultCableLink.this.target1;
				cell._target = cell.target1;
			}

			Point from = edge.getPoint(0);
			if (edge.getSource() != null) {
				from = edge.getSource().getLocation(null);
			}
			if (DefaultCableLink.this._from == null) {
				DefaultCableLink.this._from = new Point();
			}
			Point to = edge.getPoint(points.size() - 1);
			if (edge.getTarget() != null) {
				to = edge.getTarget().getLocation(null);
			}
			if (DefaultCableLink.this._to == null) {
				DefaultCableLink.this._to = new Point();
			}
			if (from != null && to != null) {
				// Handle self references
				if (edge.getSource() == edge.getTarget() && edge.getSource() != null) {
					Rectangle bounds = edge.getSource().getParentView().getBounds();
					int height = edge.getGraph().getGridSize();
					int width = (int) (bounds.getWidth() / 3);
					DefaultCableLink.this.routed = new Point[4];
					DefaultCableLink.this.routed[0] = new Point(bounds.x + width, bounds.y + bounds.height);
					DefaultCableLink.this.routed[1] = new Point(bounds.x + width, bounds.y + bounds.height
							+ height);
					DefaultCableLink.this.routed[2] = new Point(bounds.x + 2 * width, bounds.y + bounds.height
							+ height);
					DefaultCableLink.this.routed[3] = new Point(bounds.x + 2 * width, bounds.y + bounds.height);
				} else {
					if (DefaultCableLink.this.routed == null) {
						 DefaultCableLink.this.routed = createDefaultRouting(graph, from, to, DefaultCableLink.this.source1, DefaultCableLink.this.target1);
					} else {
						if (!SchemeActions.isIgnoreCheck()) {
						Point[] p1 = createDefaultRouting(graph, from, to, DefaultCableLink.this.source1, DefaultCableLink.this.target1);
						if (DefaultCableLink.this.routed.length != p1.length) {
							DefaultCableLink.this.routed = p1;
						}
						if (DefaultCableLink.this._routed != null
								&& DefaultCableLink.this._routed.length == DefaultCableLink.this.routed.length) {

							if ((DefaultCableLink.this._from != null && DefaultCableLink.this._to != null)
									&& (DefaultCableLink.this._from.x != from.x && DefaultCableLink.this._to.x != to.x)
									|| (DefaultCableLink.this._from.y != from.y && DefaultCableLink.this._to.y != to.y)) {
								
								int delta = from.x - DefaultCableLink.this._from.x;
								if (getSource() != null) {
									DefaultCableLink.this.routed[0].x += delta;
									if (DefaultCableLink.this.routed.length == 4) {
										DefaultCableLink.this.routed[1].x += delta;
									}
								}
								if (getTarget() != null) {
									delta = to.x - DefaultCableLink.this._to.x;
									if (DefaultCableLink.this.routed.length == 2) {
										DefaultCableLink.this.routed[1].x += delta;
									} else if (DefaultCableLink.this.routed.length == 4) {
										DefaultCableLink.this.routed[2].x += delta;
										DefaultCableLink.this.routed[3].x += delta;
									}
								}
//								if (getSource() != null && getTarget() != null) {
//									int deltay = ((from.y - DefaultCableLink.this._from.y) +
//											(to.y - DefaultCableLink.this._to.y)) / 2;
//									if (DefaultCableLink.this.routed.length == 4) {
//										DefaultCableLink.this.routed[1].y += deltay;
//										DefaultCableLink.this.routed[2].y += deltay;
//									}
//								}
								
							/*if (DefaultCableLink.this._from != null
									&& (DefaultCableLink.this._from.x != from.x || DefaultCableLink.this._from.y != from.y)) {
								// если сдвинулись одновременно начало и конец - перемещаем целиком
								if (edge.getSource() != null) {
									int delta = graph.snap((from.x - DefaultCableLink.this._from.x) / 2);
									DefaultCableLink.this.routed[0].x += delta;
									DefaultCableLink.this.routed[1].x += delta;
									if (DefaultCableLink.this.routed.length == 4) {
										delta = graph.snap(from.y - DefaultCableLink.this._from.y);
										DefaultCableLink.this.routed[1].y += delta;
										DefaultCableLink.this.routed[2].y += delta;
									}
								}
							} else if (DefaultCableLink.this._from != null
									&& (DefaultCableLink.this._from.x != from.x || DefaultCableLink.this._from.y != from.y)) {
								// если сдвинулись одновременно начало и конец - перемещаем целиком
								if (edge.getTarget() != null) {
									if (DefaultCableLink.this.routed.length == 4) {
										int delta = graph.snap(to.x - DefaultCableLink.this._to.x);
										DefaultCableLink.this.routed[2].x += delta;
										DefaultCableLink.this.routed[3].x += delta;
										delta = graph.snap(to.y - DefaultCableLink.this._to.y);
										DefaultCableLink.this.routed[1].y += delta;
										DefaultCableLink.this.routed[2].y += delta;
									}
								}*/
							} else {
								int grid = edge.getGraph().getGridSize();
								Point[] p = DefaultCableLink.this.routed;
								Point[] _p = DefaultCableLink.this._routed;
								
								// двигать можно только за точку, за линию - игнорим
								if (p.length == 2) {
									// только горизонтальные перемещения доступны
									if (p[0].x != _p[0].x && p[1].x == _p[1].x) { // двигаем точку 0
										if (p[0].x >= to.x && p1[0].x < to.x) {
											p[0].x = to.x - grid;
										}
										p[1].x = p[0].x;
									} else if (p[1].x != _p[1].x && p[0].x == _p[0].x) { // двигаем точку 1
										if (p[1].x <= from.x && p1[1].x > from.x) {
											p[1].x = from.x + grid;
										}
										p[0].x = p[1].x;
//									if (p[0].x != _p[0].x && p[1].x == _p[1].x) { // двигаем точку 0
//										p[1].x = p[0].x;
//									} else if (p[1].x != _p[1].x && p[0].x == _p[0].x) { // двигаем точку 1
//										if (p[1].x <= from.x) {
//											p[1].x = from.x + grid;
//										}
//										p[0].x = p[1].x;
									} else { // сбрасываем все изменения по х
										for (int i = 0; i < p.length; i++) {
											p[i].x = _p[i].x;
										}
									}
								} else if (p.length == 4) {
									boolean changed = false;
									//	горизонтальные перемещения
									if (from.x != DefaultCableLink.this._from.x) { // двигаем начало
//										if (edge.getSource() != null || edge.getTarget() != null) {
											int delta = from.x - DefaultCableLink.this._from.x;
											p[0].x += delta;
											p[1].x = p[0].x;
											changed = true;
//										}
									}
									if (to.x != DefaultCableLink.this._to.x) { // двигаем конец
//										if (edge.getTarget() != null || edge.getSource() != null) {
											int delta = to.x - DefaultCableLink.this._to.x;
											p[3].x += delta;
											p[2].x = p[3].x;
											changed = true;
//										}
									}
									if (p[0].x != _p[0].x && p[1].x == _p[1].x) { // двигаем точку 0
										p[1].x = p[0].x;
										changed = true;
									} else if (p[1].x != _p[1].x && p[2].x == _p[2].x) { // двигаем точку 1
//										if (p[1].x <= from.x) {
//											p[1].x = from.x + grid;
//										}
										p[0].x = p[1].x;
										changed = true;
									} 
									if (p[2].x != _p[2].x && p[1].x == _p[1].x) { // двигаем точку 2
//										if (p[2].x >= to.x) {
//											p[2].x = to.x - grid;
//										}
										p[3].x = p[2].x;
										changed = true;
									} else if (p[3].x != _p[3].x && p[2].x == _p[2].x) { // двигаем точку 3
										p[2].x = p[3].x;
										changed = true;
									} 
									// сбрасываем все изменения по х
									if (!changed) {
										for (int i = 0; i < p.length; i++) {
											p[i].x = _p[i].x;
										}
									}
									
									//	вертикальные перемещения для точек 1, 2
									if (p[1].y != _p[1].y && p[2].y == _p[2].y) { // двигаем точку 1
										p[2].y = p[1].y;
									} else if (p[2].y != _p[2].y && p[1].y == _p[1].y) { // двигаем точку 1
										p[1].y = p[2].y;
									} else if (from.y != DefaultCableLink.this._from.y) { // двигаем начало
										p[0].y = from.y;
									} else if (to.x != DefaultCableLink.this._to.y) { // двигаем начало
										p[3].y = to.y;
									} else { // сбрасываем все изменения по y
										for (int i = 1; i < p.length - 1; i++) {
											p[i].y = _p[i].y;
										}
									}
								}
								
								// крайние точки обязаны находиться на одном y с концами
								if (p[0].y != from.y) {
									p[0].y = from.y;
								}
								if (p[p.length-1].y != to.y) {
									p[p.length-1].y = to.y;
								}
//								if (p[0].x <= from.x) {
//									p[0].x = from.x + grid;
//								}
//								if (p[p.length-1].x >= to.x) {
//									p[p.length-1].x = to.x - grid;
//								}
							}
						} else {
							DefaultCableLink.this._routed = new Point[DefaultCableLink.this.routed.length];
							for (int i = 0; i < DefaultCableLink.this._routed.length; i++) {
								DefaultCableLink.this._routed[i] = new Point();
							}
						}
						for (int i = 0; i < DefaultCableLink.this.routed.length; i++) {
							DefaultCableLink.this._routed[i].x = DefaultCableLink.this.routed[i].x;
							DefaultCableLink.this._routed[i].y = DefaultCableLink.this.routed[i].y;
						}
					}
					}
				}
				// Set/Add Points
				for (int i = 0; i < DefaultCableLink.this.routed.length; i++)
					if (points.size() > i + 2)
						points.set(i + 1, DefaultCableLink.this.routed[i]);
					else
						points.add(i + 1, DefaultCableLink.this.routed[i]);
				// Remove spare points
				while (points.size() > DefaultCableLink.this.routed.length + 2) {
					points.remove(points.size() - 2);
				}
			}
			DefaultCableLink.this._from.x = from.x;
			DefaultCableLink.this._from.y = from.y;
			DefaultCableLink.this._to.x = to.x;
			DefaultCableLink.this._to.y = to.y;

//			GraphConstants.setPoints(edge.getAllAttributes(), points);
		}
	}
	
	Point[] createDefaultRouting(SchemeGraph graph, Point from, Point to, DefaultPort sourcePort, DefaultPort targetPort) {
		Point[] p = null;
		int grid = graph.getGridSize();

		long start = System.currentTimeMillis();
		
		if (sourcePort != null && targetPort != null) {
			SchemeCablePort startPort = ((CablePortCell)sourcePort.getParent()).getSchemeCablePort();
			SchemeCablePort endPort = ((CablePortCell)targetPort.getParent()).getSchemeCablePort();
			//	special action in case of codirection
			if (startPort != null && endPort != null) { // this may be in import
			if (startPort.getDirectionType() == endPort.getDirectionType()) {
				p = new Point[2];
				if (startPort.getDirectionType() == IdlDirectionType._IN) {
					int minx = Math.min(from.x, to.x);
					p[0] = graph.snap(new Point(minx - grid, from.y));
					p[1] = graph.snap(new Point(minx - grid, to.y));
				} else {
					int maxx = Math.max(from.x, to.x);
					p[0] = graph.snap(new Point(maxx + grid, from.y));
					p[1] = graph.snap(new Point(maxx + grid, to.y));
				}
			} else if (startPort.getDirectionType() == IdlDirectionType._IN) { //	special action in case of moving in reverse direction
				int x2 = from.x + ((to.x - from.x) / 2);
				int y2 = from.y + ((to.y - from.y) / 2);
				if (from.x - grid * 3 < to.x) {
					p = new Point[4];
					p[0] = graph.snap(new Point(from.x - grid, from.y));
					p[1] = graph.snap(new Point(from.x - grid, y2));
					p[2] = graph.snap(new Point(to.x + grid, y2));
					p[3] = graph.snap(new Point(to.x + grid, to.y));
				} else {
					p = new Point[2];
					p[0] = graph.snap(new Point(x2, from.y));
					p[1] = graph.snap(new Point(x2, to.y));
				}
			}
		}
		}

		if (p == null) {
			int x2 = from.x + ((to.x - from.x) / 2);
			int y2 = from.y + ((to.y - from.y) / 2);
			if (from.x + graph.getGridSize() * 3 <= to.x) {
				p = new Point[2];
//				p[0] = graph.snap(new Point(from.x + grid, from.y));
				p[0] = graph.snap(new Point(x2, from.y));
				p[1] = graph.snap(new Point(x2, to.y));
//				p[3] = graph.snap(new Point(to.x - grid, to.y));
			} else {
				p = new Point[4];
				p[0] = graph.snap(new Point(from.x + grid, from.y));
				p[1] = graph.snap(new Point(from.x + grid, y2));
				p[2] = graph.snap(new Point(to.x - grid, y2));
				p[3] = graph.snap(new Point(to.x - grid, to.y));
			}
		}
		return p;
	}

	public SchemeCableLink getSchemeCableLink() {
		try {
			return (SchemeCableLink) StorableObjectPool.getStorableObject(this.schemeCablelinkId, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
	}

	public Identifier getSchemeCableLinkId() {
		return this.schemeCablelinkId;
	}

	public void setSchemeCableLinkId(Identifier id) {
		this.schemeCablelinkId = id;
	}

	public Identifier getId() {
		return this.schemeCablelinkId;
	}

	public void setId(Identifier id) {
		this.schemeCablelinkId = id;
	}
}

