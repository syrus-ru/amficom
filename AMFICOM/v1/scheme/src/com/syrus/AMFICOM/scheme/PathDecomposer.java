/*
 * $Id: PathDecomposer.java,v 1.4 2005/03/25 18:00:37 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.scheme.corba.PathElementType;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/25 18:00:37 $
 * @todo Move to corba subpackage.
 * @module scheme_v1
 */
public final class PathDecomposer {
	private SchemePath schemePath;

	public PathDecomposer(final SchemePath sp) {
		setSchemePath(sp);
	}

	private void setSchemePath(final SchemePath schemePath) {
		this.schemePath = schemePath;
	}

	public void setTotalOpticalLength(final double newLength) {
		if (this.schemePath.links().length == 0)
			return;

		setOpticalLength(
				this.schemePath.links()[0],
				this.schemePath.links()[this.schemePath.links().length - 1],
				newLength);
	}

	public void setOpticalLength(final PathElement startPE, final PathElement endPE, final double newLength) {
		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(startPE);
		if (index == -1)
			return;

		double oldLength = 0;
		for (ListIterator it = links.listIterator(index); it.hasNext();) {
			PathElement pe = (PathElement)it.next();
			oldLength += SchemeUtils.getOpticalLength(pe);
			if (pe.equals(endPE))
				break;
		}
		if (oldLength == 0)
			return;

		double k = newLength / oldLength;
		if (Math.abs(k - 1) < 0.001)
			return;
		for (ListIterator it = links.listIterator(index); it.hasNext();) {
			PathElement pe = (PathElement)it.next();
			SchemeUtils.setOpticalLength(pe, SchemeUtils.getOpticalLength(pe) * k);
			if (pe.equals(endPE))
				break;
		}
	}

	public PathElement getPathElementByOpticalDistance(final double opticalDistance) {
		if (this.schemePath.links().length == 0)
			return null;

		List links = Arrays.asList(this.schemePath.links());

		double d = 0;
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			d += SchemeUtils.getOpticalLength(pe);
			if(d >= opticalDistance)
				return pe;
		}
		return (PathElement)links.listIterator(links.size()).previous();
	}

	public PathElement getPathElementByPhysicalDistance(final double physicalDistance) {
		if (this.schemePath.links().length == 0)
			return null;

		List links = Arrays.asList(this.schemePath.links());
		double d = 0;
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			d += SchemeUtils.getPhysicalLength(pe);
			if(d >= physicalDistance)
				return pe;
		}
		return (PathElement)links.listIterator(links.size()).previous();
	}

	public double[] getOpticalDistanceFromStart(final PathElement pathElement) {
		if (this.schemePath.links().length == 0)
			return new double[0];

		double tmp = 0;
		List links = Arrays.asList(this.schemePath.links());

		for (final Iterator it = links.iterator(); it.hasNext();) {
			PathElement pe = (PathElement)it.next();
			if (pe.equals(pathElement))
				return new double[]{tmp, tmp + SchemeUtils.getOpticalLength(pe)};
			tmp += SchemeUtils.getOpticalLength(pe);
		}
		return new double[0];
	}

	/**
	 * @param pathElement
	 * @deprecated This method is never used and hence is a candidate for
	 *             removal.
	 */
	public double[] getPhysicalDistanceFromStart(final PathElement pathElement) {
		if (this.schemePath.links().length == 0)
			return new double[0];

		double tmp = 0;
		List links = Arrays.asList(this.schemePath.links());

		for (final Iterator it = links.iterator(); it.hasNext();) {
			PathElement pe = (PathElement)it.next();
			if (pe.equals(pathElement))
				return new double[]{tmp, tmp + SchemeUtils.getPhysicalLength(pe)};
			tmp += SchemeUtils.getPhysicalLength(pe);
		}
		return new double[0];
	}

	public double getOpticalDistance(final double physicalDistance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for (int i = 0; i < this.schemePath.links().length; i++) {
			double pl = SchemeUtils.getPhysicalLength(this.schemePath.links()[i]);
			if (d2 + pl < physicalDistance) {
				d2 += pl;
				d += SchemeUtils.getOpticalLength(this.schemePath.links()[i]);
			}
			else {
				double diff = physicalDistance - d2;
				d += diff * SchemeUtils.getKu(this.schemePath.links()[i]);
				break;
			}
		}
		return d;
	}

	public double getPhysicalDistance(final double opticalDistance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for (int i = 0; i < this.schemePath.links().length; i++) {
			double ol = SchemeUtils.getOpticalLength(this.schemePath.links()[i]);
			if (d + ol < opticalDistance) {
				d += ol;
				d2 += SchemeUtils.getPhysicalLength(this.schemePath.links()[i]);
			}
			else {
				double diff = opticalDistance - d;
				d2 += diff / SchemeUtils.getKu(this.schemePath.links()[i]);
				break;
			}
		}
		return d2;
	}

	public boolean hasPreviousPathElement(final PathElement pe)
	{
		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(pe);
		if (index > 0)
			return true;
		return false;
	}

	public PathElement getPreviousPathElement(final PathElement pe)
	{
		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(pe);
		if (index > 0)
			return (PathElement)links.get(index - 1);
		return null;
	}

	public boolean hasNextPathElement(final PathElement pe)
	{
		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(pe);
		if (index != -1 && index < links.size() - 2)
			return true;
		return false;
	}

	public PathElement getNextPathElement(final PathElement pe)
	{
		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(pe);
		if (index != -1 && index < links.size() - 2)
			return (PathElement)links.get(index + 1);
		return null;
	}

	/**
	 * @param pathElement
	 * @todo Make formal parameter final: parameters shouldn't be reassigned.
	 */
	public PathElement getPreviousNode(PathElement pathElement)
	{
		if (pathElement.getPathElementType() == PathElementType.SCHEME_ELEMENT && hasOpticalPort(pathElement))
			return pathElement;

		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(pathElement);
		if (index != -1)
		{
			for (ListIterator it = links.listIterator(index); it.hasPrevious();)
			{
				pathElement = (PathElement)it.previous();
				if (pathElement.getPathElementType() == PathElementType.SCHEME_ELEMENT && hasOpticalPort(pathElement))
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
		if (pathElement.getPathElementType() == PathElementType.SCHEME_ELEMENT && hasOpticalPort(pathElement))
			return pathElement;

		List links = Arrays.asList(this.schemePath.links());
		int index = links.indexOf(pathElement);
		if (index != -1) {
			for (ListIterator it = links.listIterator(index); it.hasNext();) {
				pathElement = (PathElement)it.next();
				if (pathElement.getPathElementType() == PathElementType.SCHEME_ELEMENT && hasOpticalPort(pathElement))
					return pathElement;
			}
		}
		return null;
	}

	private boolean hasOpticalPort(final PathElement pathElement)
	{
		AbstractSchemePort port = pathElement.getStartAbstractSchemePort();
		if (port instanceof SchemePort) {
			PortType ptype = ((SchemePort)port).getPortType();
			if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_OPTICAL))
				return true;
		}
		port = pathElement.getEndAbstractSchemePort();
		if (port instanceof SchemePort) {
			PortType ptype = ((SchemePort)port).getPortType();
			if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_OPTICAL))
				return true;
		}
		return false;
	}
}
