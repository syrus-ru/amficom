package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;

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
		if (sp.links.size() == 0)
			return;

		setOpticalLength(
				(PathElement)sp.links.get(0),
				(PathElement)sp.links.get(sp.links.size() - 1),
				newLength);
	}

	public void setOpticalLength(PathElement startPE, PathElement endPE, double newLength)
	{
		int index = sp.links.indexOf(startPE);
		if (index == -1)
			return;

		double oldLength = 0;
		for (ListIterator it = sp.links.listIterator(index); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			oldLength += pe.getOpticalLength();
			if (pe.equals(endPE))
				break;
		}
		if (oldLength == 0)
			return;

		double k = newLength / oldLength;
		if (Math.abs(k - 1) < 0.001)
			return;
		for (ListIterator it = sp.links.listIterator(index); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			pe.setOpticalLength(pe.getOpticalLength() * k);
			if (pe.equals(endPE))
				break;
		}
	}

	public PathElement getPathElementByOpticalDistance(double opticalDistance)
	{
		if (sp.links.size() == 0)
			return null;

		double d = 0;
		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			d += pe.getOpticalLength();
			if(d >= opticalDistance)
				return pe;
		}
		return (PathElement)sp.links.listIterator(sp.links.size()).previous();
	}

	public PathElement getPathElementByPhysicalDistance(double physicalDistance)
	{
		if (sp.links.size() == 0)
			return null;

		double d = 0;
		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			d += pe.getPhysicalLength();
			if(d >= physicalDistance)
				return pe;
		}
		return (PathElement)sp.links.listIterator(sp.links.size()).previous();
	}

	public double getPhysicalDistanceByOptical(double opticalDistance)
	{
		double optd = 0;
		double physd = 0;
		for (Iterator it = sp.links.iterator(); it.hasNext(); ) {
			PathElement pe = (PathElement)it.next();
			optd += pe.getOpticalLength();
			if (optd >= opticalDistance) {
				double d = opticalDistance - (optd - pe.getOpticalLength());
				physd += d / pe.getKu();
				break;
			}
			else
				physd += pe.getPhysicalLength();
		}
		return physd;
	}

	public double getOpticalDistanceByPhysical(double physicalDistance)
	{
		double optd = 0;
		double physd = 0;
		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			physd += pe.getPhysicalLength();
			if(physd >= physicalDistance)
			{
				double d = physicalDistance - (physd - pe.getPhysicalLength());
				optd += d * pe.getKu();
				break;
			}
			else
				optd += pe.getOpticalLength();
		}
		return optd;
	}

	public double[] getOpticalDistanceFromStart(PathElement pathElement)
	{
		if (sp.links.size() == 0)
			return null;

		double tmp = 0;

		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.equals(pathElement))
			{
				double d[] = {tmp, tmp + pe.getOpticalLength()};
				return d;
			}
			tmp += pe.getOpticalLength();
		}
		return null;
	}

	public double[] getPhysicalDistanceFromStart(PathElement pathElement)
	{
		if (sp.links.size() == 0)
			return null;

		double tmp = 0;

		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.equals(pathElement))
			{
				double d[] = {tmp, tmp + pe.getPhysicalLength()};
				return d;
			}
			tmp += pe.getPhysicalLength();
		}
		return null;
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
		int index = sp.links.indexOf(pe);
		if (index > 0)
			return true;
		return false;
	}

	public PathElement getPreviousPathElement(PathElement pe)
	{
		int index = sp.links.indexOf(pe);
		if (index > 0)
			return (PathElement)sp.links.get(index - 1);
		return null;
	}

	public boolean hasNextPathElement(PathElement pe)
	{
		int index = sp.links.indexOf(pe);
		if (index != -1 && index < sp.links.size() - 2)
			return true;
		return false;
	}

	public PathElement getNextPathElement(PathElement pe)
	{
		int index = sp.links.indexOf(pe);
		if (index != -1 && index < sp.links.size() - 2)
			return (PathElement)sp.links.get(index + 1);
		return null;
	}

	public PathElement getPreviousNode(PathElement pe, boolean mustHaveOpticalPort)
	{
		if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
			return pe;

		int index = sp.links.indexOf(pe);
		if (index != -1)
		{
			for (ListIterator it = sp.links.listIterator(index); it.hasPrevious();)
			{
				pe = (PathElement)it.previous();
				if (mustHaveOpticalPort)
				{
					if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
						return pe;
				}
				else
				{
					if (pe.getType() == PathElement.SCHEME_ELEMENT)
						return pe;
				}
			}
		}
		return null;
	}

	public PathElement getNextNode(PathElement pe, boolean mustHaveOpticalPort)
	{
		if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
			return pe;

		int index = sp.links.indexOf(pe);
		if (index != -1)
		{
			for (ListIterator it = sp.links.listIterator(index); it.hasNext();)
			{
				pe = (PathElement)it.next();
				if (mustHaveOpticalPort)
				{
					if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
						return pe;
				}
				else
				{
					if (pe.getType() == PathElement.SCHEME_ELEMENT)
						return pe;
				}
			}
		}
		return null;
	}

	private boolean hasOpticalPort(PathElement pe)
	{
		ObjectResource port = pe.getSourcePort();
		if (port instanceof SchemePort)
		{
			PortType ptype = (PortType) Pool.get(PortType.typ, ((SchemePort)port).portTypeId);
			if (ptype.pClass.equals("optical"))
				return true;
		}
		port = pe.getTargetPort();
		if (port instanceof SchemePort)
		{
			PortType ptype = (PortType) Pool.get(PortType.typ, ((SchemePort)port).portTypeId);
			if (ptype.pClass.equals("optical"))
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
