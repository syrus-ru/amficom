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
		double d = 0;
		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			d += pe.getOpticalLength();
			if(d >= opticalDistance)
				return pe;
		}
		return null;
	}

	public PathElement getPathElementByPhysicalDistance(double physicalDistance)
	{
		double d = 0;
		for(Iterator it = sp.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			d += pe.getPhysicalLength();
			if(d >= physicalDistance)
				return pe;
		}
		return null;
	}

	public PathElement getPreviousPathElement(PathElement pe)
	{
		int index = sp.links.indexOf(pe);
		if (index > 0)
			return (PathElement)sp.links.get(index - 1);
		return pe;
	}

	public PathElement getNextPathElement(PathElement pe)
	{
		int index = sp.links.indexOf(pe);
		if (index != -1 && index < sp.links.size() - 2)
			return (PathElement)sp.links.get(index + 1);
		return pe;
	}

	public PathElement getPreviousNode(PathElement pe)
	{
		if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
			return pe;

		int index = sp.links.indexOf(pe);
		if (index != -1)
		{
			for (ListIterator it = sp.links.listIterator(index); it.hasPrevious();)
			{
				pe = (PathElement)it.previous();
				if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
					return pe;
			}
		}
		return pe;
	}

	public PathElement getNextNode(PathElement pe)
	{
		if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
			return pe;

		int index = sp.links.indexOf(pe);
		if (index != -1)
		{
			for (ListIterator it = sp.links.listIterator(index); it.hasNext();)
			{
				pe = (PathElement)it.next();
				if (pe.getType() == PathElement.SCHEME_ELEMENT && hasOpticalPort(pe))
					return pe;
			}
		}
		return pe;
	}

	private boolean hasOpticalPort(PathElement pe)
	{
		ObjectResource port = pe.getSourcePort();
		if (port instanceof SchemePort)
		{
			PortType ptype = (PortType) Pool.get(PortType.typ, ( (SchemePort) port).port_type_id);
			if (ptype.p_class.equals("optical"))
				return true;
		}
		port = pe.getTargetPort();
		if (port instanceof SchemePort)
		{
			PortType ptype = (PortType) Pool.get(PortType.typ, ( (SchemePort) port).port_type_id);
			if (ptype.p_class.equals("optical"))
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