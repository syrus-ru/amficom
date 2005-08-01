/*-
 * $Id: MPort.java,v 1.3 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.Port;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/01 11:32:03 $
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
		Edge edge = (Edge) oEdge;
		
		if (edge == null) {
			return false;
		}
		
		Port source = (Port) edge.getSource();
		Port target = (Port) edge.getTarget();
		
//		System.out.println("MPort.addEdge()| source:" + source + ", this:" + this);		
//		System.out.println("MPort.addEdge()| target:" + target + ", this:" + this);
		
		if ((this == source || source == null) && !this.targets.contains(target)) {
//			System.out.println("MPort.addEdge() | add target:" + target + " to " + this);
			this.targets.add(target);
		}
		
		if ((this == target || target == null) && !this.sources.contains(source)) {
//			System.out.println("MPort.addEdge() | add source:" + source + " to " + this);
			this.sources.add(source);
		}
		
		return this.edges.add(edge);
	}

	/**
	 * Removes <code>edge</code> from the list of ports.
	 */
	public boolean removeEdge(Object oEdge) {
		Edge edge = (Edge) oEdge;
		
		if (edge == null) {
			return false;
		}
		
		Port source = (Port) edge.getSource();
		Port target = (Port) edge.getTarget();
		
		if (this == source) {
//			System.out.println("MPort.removeEdge() | remove target:" + target + " from " + this);
			this.targets.remove(target);
		}
		
		if (this == target) {
//			System.out.println("MPort.removeEdge() | remove source:" + source + " from " + this);
			this.sources.remove(source);
		}
		
		return this.edges.remove(edge);
	}

	/**
	 * Returns the anchor of this port.
	 */
	public Set getEdges() {
		return new HashSet(edges);
	}

	/**
	 * Sets the anchor of this port.
	 */
	public void setEdges(Set edges) {
		
		this.edges.clear();
		this.sources.clear();
		this.targets.clear();
		
		if (edges != null) {
			for(Object oEdge: edges) {
				this.addEdge(oEdge);
			}
		}
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
	public Object clone() {
		MPort c = (MPort) super.clone();
		c.edges = new HashSet<Edge>();
		return c;
	}
	
	/**
	 * @return Returns the sources.
	 */
	public final List<Port> getSources() {
		return this.sources;
	}
	
	/**
	 * @return Returns the targets.
	 */
	public final List<Port> getTargets() {
		return this.targets;
	}
	
	public AbstractBean getBean() {
		if (super.userObject instanceof AbstractBean) {
			return (AbstractBean)super.userObject;
		}
		return null;
	}

}
