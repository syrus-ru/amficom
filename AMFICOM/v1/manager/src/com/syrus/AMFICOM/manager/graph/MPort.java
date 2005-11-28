/*-
 * $Id: MPort.java,v 1.2 2005/11/28 14:47:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.manager.beans.AbstractBean;

/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MPort extends DefaultGraphCell implements Port {

	/** Edges that are connected to the port */
	protected Set<Edge> edges;
	
	protected List<Port> targets;
	protected List<Port> sources;

	/** Reference to the anchor of this port */
	protected Port anchor;

	private boolean	cacheOn;

	/**
	 * Constructs an empty port.
	 */
	public MPort() {
		this(null, null);
	}

	/**
	 * Constructs a vertex that holds a reference to the specified user object.
	 *
	 * @param userObject reference to the user object
	 */
	public MPort(Object userObject) {
		this(userObject, null);
	}

	/**
	 * Constructs a vertex that holds a reference to the specified user object
	 * and a reference to the specified anchor.
	 *
	 * @param userObject reference to the user object
	 * @param anchor to a a graphcell that constitutes the anchor
	 */
	public MPort(Object userObject, Port anchor) {
		super(userObject);
		this.setAllowsChildren(false);
		
		this.edges = new HashSet<Edge>();
		this.targets = new ArrayList<Port>();
		this.sources = new ArrayList<Port>();
		
		this.anchor = anchor;
	}

	
	/**
	 * Returns an iterator of the edges connected
	 * to the port.
	 */
	public Iterator edges() {
		return this.edges.iterator();
	}
	
	/**
	 * Adds <code>edge</code> to the list of ports.
	 */
	public boolean addEdge(Object oEdge) {
//		try {
//			throw new Exception("MPort.addEdge | " + this);
//		} catch (final Exception e) {
//			e.printStackTrace();
//		}		

		
		Edge edge = (Edge) oEdge;
		
		if (edge == null) {
			return false;
		}

		this.cacheOn = false;
		
		return this.edges.add(edge);
	}

	/**
	 * Removes <code>edge</code> from the list of ports.
	 */
	public boolean removeEdge(final Object edge) {
		if (edge == null) {
			return false;
		}		
		this.cacheOn = false;		
		return this.edges.remove(edge);
	}

	/**
	 * Returns the anchor of this port.
	 */
	public Set getEdges() {
		return new HashSet(this.edges);
	}

	/**
	 * Sets the anchor of this port.
	 */
	public void setEdges(Set edges) {		
		this.edges.clear();
		this.edges.addAll(edges);
		this.cacheOn = false;
		
		
	}

	/**
	 * Returns the anchor of this port.
	 */
	public Port getAnchor() {
		return this.anchor;
	}

	/**
	 * Sets the anchor of this port.
	 */
	public void setAnchor(Port port) {
		this.anchor = port;
	}

	/**
	 * Create a clone of the cell. The cloning of the
	 * user object is deferred to the cloneUserObject()
	 * method.
	 *
	 * @return Object  a clone of this object.
	 */
	@Override
	public Object clone() {
		MPort c = (MPort) super.clone();
		c.edges = new HashSet<Edge>();
		return c;
	}
	
	public void updateCache() {
		this.sources.clear();
		this.targets.clear();
		for(Edge edge : this.edges) {
			Port source = (Port) edge.getSource();
			Port target = (Port) edge.getTarget();
			if (source == this) {
				this.targets.add(target);
				continue;
			}
			if (target == this) {
				this.sources.add(source);
			}
		}
		this.cacheOn = true;
	}
	
	/**
	 * @return Returns the sources.
	 */
	public final List<Port> getSources() {
		if (!this.cacheOn) {
			this.updateCache();
		}
		return this.sources;
	}
	
	/**
	 * @return Returns the targets.
	 */
	public final List<Port> getTargets() {
		if (!this.cacheOn) {
			this.updateCache();
		}
		return this.targets;
	}
	
	public AbstractBean getBean() {
		if (super.userObject instanceof AbstractBean) {
			return (AbstractBean)super.userObject;
		}
		return null;
	}

	@Override
	public AbstractBean getUserObject() {
		return this.getBean();
	}
}
