package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.util.Hashtable;
import java.util.Vector;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
//import com.syrus.AMFICOM.Client.Resource.ISM.LengthContainer;

public class SchemePathDecompositor
{
	LengthContainer []lengthContainer;

	SchemePath sp = null;

	public SchemePathDecompositor()
	{
	}

	public SchemePathDecompositor(SchemePath sp)
	{
		setSchemePath(sp);
	}

	public void setSchemePath(SchemePath sp)
	{
		this.sp = sp;
		setTraceData(sp);
	}

//-----------------------------------------------------------------------------
	public LengthContainer[] getLengthContainers()
	{
		return lengthContainer;
	}

//-----------------------------------------------------------------------------
	public double getKu(double opticalDistance)
	{
		double d = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d + lengthContainer[i].opticalLength < opticalDistance)
				d += lengthContainer[i].opticalLength;
			else
				return lengthContainer[i].Ku;
		}
		return 1.0;
	}

//-----------------------------------------------------------------------------
	public Object getLengthContainerObject(double opticalDistance)
	{
		double d = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d + lengthContainer[i].opticalLength < opticalDistance)
				d += lengthContainer[i].opticalLength;
			else
				return lengthContainer[i].obj;
		}
		return null;
	}

//-----------------------------------------------------------------------------
	public double getPhysicalDistance(double opticalDistance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d + lengthContainer[i].opticalLength < opticalDistance)
			{
				d += lengthContainer[i].opticalLength;
				d2 += lengthContainer[i].physicalLength;
			}
			else
			{
				double diff = opticalDistance - d;
				d2 += diff / lengthContainer[i].Ku;
				break;
			}
		}
		return d2;
	}

//-----------------------------------------------------------------------------
	public double getOpticalDistance(double physicalDistance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d2 + lengthContainer[i].physicalLength < physicalDistance)
			{
				d2 += lengthContainer[i].physicalLength;
				d += lengthContainer[i].opticalLength;
			}
			else
			{
				double diff = physicalDistance - d2;
				d += diff * lengthContainer[i].Ku;
				break;
			}
		}
		return d;
	}


//-----------------------------------------------------------------------------
	public Object getLinkByOpticalDistance(double opticalDistance)
	{
		double dist = 0.;
		for(int i=0; i<lengthContainer.length; i++)
		{
			dist += lengthContainer[i].opticalLength;
			if(dist>opticalDistance)
			{
					return lengthContainer[i].obj;
			}
		}
		return null;
	}


//-----------------------------------------------------------------------------
	public Object getLinkByPhysicalDistance(double physicalDistance)
	{
		double dist = 0.;
		for(int i=0; i<lengthContainer.length; i++)
		{
			dist += lengthContainer[i].physicalLength;
			if(dist>physicalDistance)
			{
					return lengthContainer[i].obj;
			}
		}
		return null;
	}


//-----------------------------------------------------------------------------
	public Object getSourcePortByOpticalDistance(double opticalDistance)
	{
		Object o = getLinkByOpticalDistance(opticalDistance);
		if(o instanceof SchemeCableLink)
		{
			SchemeCableLink link = (SchemeCableLink)o;
			return Pool.get(SchemeCablePort.typ, link.source_port_id);
		}
		else if(o instanceof SchemeLink)
		{
			SchemeLink link = (SchemeLink)o;
			return Pool.get(SchemePort.typ, link.source_port_id);
		}
		return null;
	}


//-----------------------------------------------------------------------------
	public Object getSourcePortByPhysicalDistance(double physicalDistance)
	{
		Object o = getLinkByPhysicalDistance(physicalDistance);
		if(o instanceof SchemeCableLink)
		{
			SchemeCableLink link = (SchemeCableLink)o;
			return Pool.get(SchemeCablePort.typ, link.source_port_id);
		}
		else if(o instanceof SchemeLink)
		{
			SchemeLink link = (SchemeLink)o;
			return Pool.get(SchemePort.typ, link.source_port_id);
		}
		return null;
	}


//-----------------------------------------------------------------------------
	public Object getTargetPortByOpticalDistance(double opticalDistance)
	{
		Object o = getLinkByOpticalDistance(opticalDistance);
		if(o instanceof SchemeCableLink)
		{
			SchemeCableLink link = (SchemeCableLink)o;
			return Pool.get(SchemeCablePort.typ, link.target_port_id);
		}
		else if(o instanceof SchemeLink)
		{
			SchemeLink link = (SchemeLink)o;
			return Pool.get(SchemePort.typ, link.target_port_id);
		}
		return null;
	}


//-----------------------------------------------------------------------------
	public Object getTargetPortByPhysicalDistance(double physicalDistance)
	{
		Object o = getLinkByPhysicalDistance(physicalDistance);
		if(o instanceof SchemeCableLink)
		{
			SchemeCableLink link = (SchemeCableLink)o;
			return Pool.get(SchemeCablePort.typ, link.target_port_id);
		}
		else if(o instanceof SchemeLink)
		{
			SchemeLink link = (SchemeLink)o;
			return Pool.get(SchemePort.typ, link.target_port_id);
		}
		return null;
	}



//-----------------------------------------------------------------------------
	protected boolean setTraceData(SchemePath sp)
	{
		if(sp == null)
			return false;

		Hashtable ht;
		Characteristic c;
		ElementAttribute ea;
		double length;
		Vector vec = new Vector();

		PathElement []pe =
				(PathElement[])sp.links.toArray(new PathElement[sp.links.size()]);
		PathElement []tmp = new PathElement[pe.length];

		for(int i = 0; i < pe.length; i++)
		{
			tmp[pe[i].n] = pe[i];
		}
		pe = tmp;

		for(int i = 0; i < pe.length; i++) // count through all of the Path Elements
		{
			if(pe[i].is_cable) // CABLE LINK
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe[i].link_id);
				if(schemeCableLink == null)
				{
					System.out.println("Something wrong... - schemeCableLink == null");
					return false;
				}

				LengthContainer lc = new LengthContainer(
						schemeCableLink,
						schemeCableLink.getPhysicalLength(),
						schemeCableLink.getOpticalLength(), SchemeCableLink.typ);

				vec.add(lc);
			}
			else               // simple link
			{
				SchemeLink schemeLink =
						(SchemeLink)Pool.get(SchemeLink.typ, pe[i].link_id);
				if(schemeLink == null)
				{
					System.out.println("scheme link " + pe[i].link_id + " is null! PathElement index " + pe[i].n);
					return false;
				}
				LengthContainer lc = new LengthContainer(
						schemeLink,
						schemeLink.getPhysicalLength(),
						schemeLink.getOpticalLength(), SchemeLink.typ);
				vec.add(lc);
			}
		} // ending of the PathElement counter

		LengthContainer []lc = (LengthContainer [])vec.toArray
				(new LengthContainer [vec.size()]);

		for(int i = 0; i < lc.length; i++)
		{
			if(lc[i].opticalLength < 0.)
				lc[i].opticalLength = 0.;
		}

		this.lengthContainer = lc;
		System.out.println("Data is set...");
		return true;
	}
}



