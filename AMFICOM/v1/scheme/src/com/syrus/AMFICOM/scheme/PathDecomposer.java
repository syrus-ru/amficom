/*
 * $Id: PathDecomposer.java,v 1.7 2005/03/29 15:59:03 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.scheme.corba.PathElementKind;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/03/29 15:59:03 $
 * @todo Move all code to SchemePath.
 * @module scheme_v1
 */
public final class PathDecomposer {
	private SchemePath schemePath;

	public PathDecomposer(final SchemePath schemePath) {
		setSchemePath(schemePath);
	}

	private void setSchemePath(final SchemePath schemePath) {
		this.schemePath = schemePath;
	}

	public double getOpticalDistance(final double physicalDistance)
	{
		double opticalDistance = .0;
		double d = .0;
		for (int i = 0; i < this.schemePath.getPathElementsAsArray().length; i++) {
			final double physicalLength = SchemeUtils.getPhysicalLength(this.schemePath.getPathElementsAsArray()[i]);
			if (d + physicalLength < physicalDistance) {
				d += physicalLength;
				opticalDistance += SchemeUtils.getOpticalLength(this.schemePath.getPathElementsAsArray()[i]);
			} else {
				opticalDistance += (physicalDistance - d) * SchemeUtils.getKu(this.schemePath.getPathElementsAsArray()[i]);
				break;
			}
		}
		return opticalDistance;
	}

	public double getPhysicalDistance(final double opticalDistance) {
		double physicalDistance = .0;
		double d = .0;
		for (final Iterator pathElementIterator = this.schemePath.getPathElements().iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
			final double opticalLength = SchemeUtils.getOpticalLength(pathElement);
			if (d + opticalLength < opticalDistance) {
				d += opticalLength;
				physicalDistance += SchemeUtils.getPhysicalLength(pathElement);
			} else {
				physicalDistance += (opticalDistance - d) / SchemeUtils.getKu(pathElement);
				break;
			}
		}
		return physicalDistance;
	}

	/**
	 * @param pathElement
	 * @todo Make formal parameter final: parameters shouldn't be reassigned.
	 */
	public PathElement getPreviousNode(PathElement pathElement) {
		if (pathElement.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
			return pathElement;

		final List pathElements = new LinkedList();
		pathElements.addAll(this.schemePath.getPathElements());
		int index = pathElements.indexOf(pathElement);
		if (index != -1) {
			for (ListIterator it = pathElements.listIterator(index); it.hasPrevious();) {
				pathElement = (PathElement) it.previous();
				if (pathElement.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
					return pathElement;
			}
		}
		return null;
	}

	/**
	 * @param pathElement
	 * @todo Make formal parameter final: parameters shouldn't be reassigned.
	 */
	public PathElement getNextNode(PathElement pathElement) {
		if (pathElement.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
			return pathElement;

		final List pathElements = new LinkedList();
		pathElements.addAll(this.schemePath.getPathElements());
		int index = pathElements.indexOf(pathElement);
		if (index != -1) {
			for (ListIterator it = pathElements.listIterator(index); it.hasNext();) {
				pathElement = (PathElement) it.next();
				if (pathElement.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
					return pathElement;
			}
		}
		return null;
	}
}
