package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.util.*;

import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

public class PathDecompositor
{
	private SchemePath sp;

	public PathDecompositor(SchemePath sp)
	{
		setSchemePath(sp);
	}

	private void setSchemePath(SchemePath sp)
	{
		this.sp = sp;
	}

	public void setTotalOpticalLength(double newLength)
	{
		if (sp.links().length == 0)
			return;

		setOpticalLength(
				sp.links()[0],
				sp.links()[sp.links().length - 1],
				newLength);
	}

	public void setOpticalLength(PathElement startPE, PathElement endPE, double newLength)
	{
		List links = Arrays.asList(sp.links());
		int index = links.indexOf(startPE);
		if (index == -1)
			return;

		double oldLength = 0;
		for (ListIterator it = links.listIterator(index); it.hasNext();)
		{
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
		for (ListIterator it = links.listIterator(index); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			SchemeUtils.setOpticalLength(pe, SchemeUtils.getOpticalLength(pe) * k);
			if (pe.equals(endPE))
				break;
		}
	}

	public PathElement getPathElementByOpticalDistance(double opticalDistance)
	{
		if (sp.links().length == 0)
			return null;

		List links = Arrays.asList(sp.links());

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

	public PathElement getPathElementByPhysicalDistance(double physicalDistance)
	{
		if (sp.links().length == 0)
			return null;

		List links = Arrays.asList(sp.links());
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

	public double[] getOpticalDistanceFromStart(PathElement pathElement)
	{
		if (sp.links().length == 0)
			return null;

		double tmp = 0;
		List links = Arrays.asList(sp.links());

		for(Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.equals(pathElement))
			{
				double d[] = {tmp, tmp + SchemeUtils.getOpticalLength(pe)};
				return d;
			}
			tmp += SchemeUtils.getOpticalLength(pe);
		}
		return null;
	}

	public double[] getPhysicalDistanceFromStart(PathElement pathElement)
	{
		if (sp.links().length == 0)
			return null;

		double tmp = 0;
		List links = Arrays.asList(sp.links());

		for(Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.equals(pathElement))
			{
				double d[] = {tmp, tmp + SchemeUtils.getPhysicalLength(pe)};
				return d;
			}
			tmp += SchemeUtils.getPhysicalLength(pe);
		}
		return null;
	}

	public double getOpticalDistance(double physicalDistance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for (int i = 0; i < sp.links().length; i++) {
			double pl = SchemeUtils.getPhysicalLength(sp.links()[i]);
			if (d2 + pl < physicalDistance) {
				d2 += pl;
				d += SchemeUtils.getOpticalLength(sp.links()[i]);
			}
			else {
				double diff = physicalDistance - d2;
				d += diff * SchemeUtils.getKu(sp.links()[i]);
				break;
			}
		}
		return d;
	}

	public double getPhysicalDistance(double opticalDistance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for (int i = 0; i < sp.links().length; i++) {
			double ol = SchemeUtils.getOpticalLength(sp.links()[i]);
			if (d + ol < opticalDistance) {
				d += ol;
				d2 += SchemeUtils.getPhysicalLength(sp.links()[i]);
			}
			else {
				double diff = opticalDistance - d;
				d2 += diff / SchemeUtils.getKu(sp.links()[i]);
				break;
			}
		}
		return d2;
	}


/*
	public double[] getOpticalDistancesFromStart(PathElement startPE, PathElement endPE)
	{
		double d = 0;
		double[] res = new double[sp.links.indexOf(endPE) - sp.links.indexOf(startPE) + 1];
		int counter = 0;

		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.equals(endPE))
			{
				res[counter++] += pe.getOpticalLength();
				return res;
			}
			d += pe.getOpticalLength();
			if (counter > 0 || pe.equals(startPE))
				res[counter++] = d;
		}
		return res;
	}

	public double[] getPhysicalDistancesFromStart(PathElement startPE, PathElement endPE)
	{
		double d = 0;
		double[] res = new double[sp.links.indexOf(endPE) - sp.links.indexOf(startPE) + 1];
		int counter = 0;

		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.equals(endPE))
			{
				res[counter++] += pe.getPhysicalLength();
				return res;
			}
			d += pe.getPhysicalLength();
			if (counter > 0 || pe.equals(startPE))
				res[counter++] = d;
		}
		return res;
	}
*/
	public boolean hasPreviousPathElement(PathElement pe)
	{
		List links = Arrays.asList(sp.links());
		int index = links.indexOf(pe);
		if (index > 0)
			return true;
		return false;
	}

	public PathElement getPreviousPathElement(PathElement pe)
	{
		List links = Arrays.asList(sp.links());
		int index = links.indexOf(pe);
		if (index > 0)
			return (PathElement)links.get(index - 1);
		return null;
	}

	public boolean hasNextPathElement(PathElement pe)
	{
		List links = Arrays.asList(sp.links());
		int index = links.indexOf(pe);
		if (index != -1 && index < links.size() - 2)
			return true;
		return false;
	}

	public PathElement getNextPathElement(PathElement pe)
	{
		List links = Arrays.asList(sp.links());
		int index = links.indexOf(pe);
		if (index != -1 && index < links.size() - 2)
			return (PathElement)links.get(index + 1);
		return null;
	}

	public PathElement getPreviousNode(PathElement pe)
	{
		if (pe.type() == Type.SCHEME_ELEMENT && hasOpticalPort(pe))
			return pe;

		List links = Arrays.asList(sp.links());
		int index = links.indexOf(pe);
		if (index != -1)
		{
			for (ListIterator it = links.listIterator(index); it.hasPrevious();)
			{
				pe = (PathElement)it.previous();
				if (pe.type() == Type.SCHEME_ELEMENT && hasOpticalPort(pe))
					return pe;
			}
		}
		return null;
	}

	public PathElement getNextNode(PathElement pe)
	{
		if (pe.type() == Type.SCHEME_ELEMENT && hasOpticalPort(pe))
			return pe;

		List links = Arrays.asList(sp.links());
		int index = links.indexOf(pe);
		if (index != -1)
		{
			for (ListIterator it = links.listIterator(index); it.hasNext();)
			{
				pe = (PathElement)it.next();
				if (pe.type() == Type.SCHEME_ELEMENT && hasOpticalPort(pe))
					return pe;
			}
		}
		return null;
	}

	private boolean hasOpticalPort(PathElement pe)
	{
		AbstractSchemePort port = pe.startAbstractSchemePort();
		if (port instanceof SchemePort)
		{
			PortType ptype = ((SchemePort)port).portTypeImpl();
			if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_OPTICAL))
				return true;
		}
		port = pe.endAbstractSchemePort();
		if (port instanceof SchemePort)
		{
			PortType ptype = ((SchemePort)port).portTypeImpl();
			if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_OPTICAL))
				return true;
		}
		return false;
	}


/*
	public double getKu(double opticalDistance)
	{
		double d = 1;
		PathElement pe = getPathElementByOpticalDistance(opticalDistance);
		if (pe != null)
			return pe.getKu();
		return d;
	}

	public ObjectResource getSourcePortByOpticalDistance(double opticalDistance)
	{
		PathElement pe = getPathElementByPhysicalDistance(opticalDistance);
		if (pe != null)
			return pe.getSourcePort();
		return null;
	}

	public ObjectResource getSourcePortByPhysicalDistance(double physicalDistance)
	{
		PathElement pe = getPathElementByPhysicalDistance(physicalDistance);
		if (pe != null)
			return pe.getSourcePort();
		return null;
	}

	public ObjectResource getTargetPortByOpticalDistance(double opticalDistance)
	{
		PathElement pe = getPathElementByPhysicalDistance(opticalDistance);
		if (pe != null)
			return pe.getTargetPort();
		return null;
	}

	public ObjectResource getTargetPortByPhysicalDistance(double physicalDistance)
	{
		PathElement pe = getPathElementByPhysicalDistance(physicalDistance);
		if (pe != null)
			return pe.getTargetPort();
		return null;
	}
*/

}
