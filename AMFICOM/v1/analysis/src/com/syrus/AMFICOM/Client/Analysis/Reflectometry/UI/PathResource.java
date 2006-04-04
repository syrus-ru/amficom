/*-
 * $Id: PathResource.java,v 1.1 2006/04/04 08:05:05 stas Exp $
 *
 * Copyright ї 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.SOAnchorImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public final class PathResource {
	private SchemePath path;

	private PathElementContainer startPathElement;
	private PathElementContainer endPathElement;
	private PathElementContainer selectedPathElement;
	private List<PathElementContainer> activePathElements;
	private List<PathElementContainer> pathElements;
	
	public PathResource(final SchemePath path1) {
		this.activePathElements = new LinkedList<PathElementContainer>();
		this.pathElements = new LinkedList<PathElementContainer>();
		
		try {
			setPath(path1);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	public SchemePath getPath() {
		return this.path;
	}
	public void setPath(final SchemePath path1) throws ApplicationException {
		this.path = path1;
		if (this.path != null) {
			this.pathElements.clear();
			final SortedSet<PathElement> pathElements1 = this.path.getPathMembers();
			for (PathElement pathElement : pathElements1) {
				this.pathElements.add(new PathElementContainer(pathElement));
			}
		} else {
			this.pathElements = Collections.emptyList();
		}
	}
//	public void setPath(SchemePath path1, EventAnchorer anchorer) throws ApplicationException {
//		this.path = path1;
//		if (this.path != null) {
//			this.pathElements.clear();
//			final SortedSet<PathElement> pathElements1 = this.path.getPathMembers();
//			for (PathElement pathElement : pathElements1) {
//				this.pathElements.add(new PathElementContainer(pathElement, anchorer.));
//			}
//		} else {
//			this.pathElements = Collections.emptyList();
//		}
//	}
	
	
	public List<PathElementContainer> getPathMembers() {
		return this.pathElements;
	}
	
	public List<PathElementContainer> getActivePathElements() {
		return Collections.unmodifiableList(this.activePathElements);
	}
	public void clearActivePathElements() {
		this.activePathElements.clear();
	}
	public void addActivePathElement(final PathElementContainer container) {
		if (!this.activePathElements.contains(container)) {
			this.activePathElements.add(container);
		}
	}
	public PathElementContainer getFirstActivePathElement() {
		if (this.activePathElements.isEmpty()) {
			return null;
		}
		
		Iterator<PathElementContainer> it = this.activePathElements.iterator();
		PathElementContainer first = it.next();
		while (it.hasNext()) {
			PathElementContainer container = it.next();
			if (container.getPathElement().compareTo(first.getPathElement()) < 0) {
				first = container;
			}
		}
		return first;
	}
	public PathElementContainer getLastActivePathElement() {
		if (this.activePathElements.isEmpty()) {
			return null;
		}
		
		Iterator<PathElementContainer> it = this.activePathElements.iterator();
		PathElementContainer last = it.next();
		while (it.hasNext()) {
			PathElementContainer container = it.next();
			if (container.getPathElement().compareTo(last.getPathElement()) > 0) {
				last = container;
			}
		}
		return last;
	}

	public PathElementContainer getSelectedPathElement() {
		return this.selectedPathElement;
	}
	public void setSelectedPathElement(PathElementContainer selectedPathElement) {
		this.selectedPathElement = selectedPathElement;
	}
	
	public PathElementContainer getStartPathElement() {
		return this.startPathElement;
	}
	public void setStartPathElement(final PathElementContainer startPathElement1) {
		this.startPathElement = startPathElement1;
	}
	public PathElementContainer getEndPathElement() {
		return this.endPathElement;
	}
	public void setEndPathElement(final PathElementContainer endPathElement1) {
		this.endPathElement = endPathElement1;
	}

	public boolean hasPrevious(final PathElementContainer pathElementContainer) {
		return this.pathElements.indexOf(pathElementContainer) > 0;
	}
	public PathElementContainer getPrevious(final PathElementContainer pathElementContainer) {
		return this.pathElements.listIterator(this.pathElements.indexOf(pathElementContainer)).previous();
	}
	public boolean hasNext(final PathElementContainer pathElementContainer) {
		return this.pathElements.indexOf(pathElementContainer) < this.pathElements.size() - 1;
	}
	public PathElementContainer getNext(final PathElementContainer pathElementContainer) {
		return this.pathElements.listIterator(this.pathElements.indexOf(pathElementContainer) + 1).next();
	}
	public PathElementContainer getPreviousAnchored(final PathElementContainer container) {
		final ListIterator<PathElementContainer> it = 
				this.pathElements.listIterator(this.pathElements.indexOf(container));
		PathElementContainer previous = container;
		while (it.hasPrevious()) {
			previous = it.previous();
			if (previous.isAnchored()) {
				return previous;
			}
		}
		return previous;
	}
	public PathElementContainer getNextAnchored(final PathElementContainer container) {
		final ListIterator<PathElementContainer> it = 
				this.pathElements.listIterator(this.pathElements.indexOf(container));
		PathElementContainer next = container;
		while (it.hasNext()) {
			next = it.next();
			if (next.isAnchored()) {
				return next;
			}
		}
		return next;
	}
	public void updateAnchor(final PathElementContainer container, final ModelTraceAndEvents mtae, final int dockedEvent) {
		long id = container.getPathElement().getId().getIdentifierCode();
		// create link to Etalon
		// определяем привязчика
		EventAnchorer ea = Heap.obtainAnchorer();
		
		boolean anchorerUpdated = false;
		
		// отвязываем от старого события
		for (int i = 0; i < mtae.getNEvents(); i++) {
			SOAnchorImpl soAnchor = ea.getEventAnchor(i);
			if (soAnchor.getValue() == id) {
				container.setAnchored(false);
				ea.setEventAnchor(i, SOAnchorImpl.VOID_ANCHOR);
				anchorerUpdated = true;
				Log.debugMessage("Removed anchor for event " + i, Level.FINER);
				break;
			}
		}
		// привязываем к новому событию в случае, если к нему не привязан кто-то другой
		int nEvent = dockedEvent;
		if (dockedEvent != -1) {
			SOAnchorImpl soAnchor = ea.getEventAnchor(nEvent);
			if (soAnchor.isVoid()) {
				container.setAnchored(true);
				ea.setEventAnchor(nEvent, new SOAnchorImpl(id));
				anchorerUpdated = true;
				Log.debugMessage("Create new anchor for event " + nEvent, Level.FINER);
			} else {
				Log.debugMessage("Already created anchor for event " + nEvent, Level.FINER);
			}
		}
		if (anchorerUpdated) {
			Heap.notifyAnchorerChanged();
		}
	}
	
	public PathElementContainer getPathElementByOpticalDistance(final double opticalDistance) {
		if (this.pathElements.isEmpty()) {
			return null;
		}
		double opticalLength = 0;
		for (final PathElementContainer container : this.pathElements) {
			opticalLength += container.getPathElement().getOpticalLength();
			if (opticalLength >= opticalDistance) {
				return container;
			}
		}
		return this.pathElements.listIterator(this.pathElements.size()).previous();
	}
	public double[] getOpticalDistanceFromStart(final PathElementContainer pathElement) {
		double opticalDistanceFromStart = 0;
		for (final PathElementContainer container : this.pathElements) {
			if (container.equals(pathElement)) {
				return new double[]{opticalDistanceFromStart, opticalDistanceFromStart + container.getPathElement().getOpticalLength()};
			}
			opticalDistanceFromStart += container.getPathElement().getOpticalLength();
		}
		/*
		 * Never.
		 */
		return new double[2];
	}
	public void changeOpticalLength(final PathElementContainer start, final PathElementContainer end, final double increment) {
		try {
			this.path.changeOpticalLength(start.getPathElement(), end.getPathElement(), increment);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}

final class PathElementContainer {
	private PathElement pathElement;
	private boolean anchored; 
	
	public PathElementContainer(PathElement pathElement) {
		this(pathElement, false);
	}
	
	public PathElementContainer(PathElement pathElement, boolean anchored) {
		this.pathElement = pathElement;
		setAnchored(anchored);
	}

	public PathElement getPathElement() {
		return this.pathElement;
	}

	public boolean isAnchored() {
		return this.anchored;
	}

	public void setAnchored(boolean moveable) {
		this.anchored = moveable;
	}
	
	@Override
	public String toString() {
		return this.pathElement.getName() + "(" + this.pathElement.getId() + ") " + (this.anchored ? "anchored" : "not anchored"); 
	}
}
