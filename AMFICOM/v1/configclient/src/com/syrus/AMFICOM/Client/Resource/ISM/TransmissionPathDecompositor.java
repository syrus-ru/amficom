package com.syrus.AMFICOM.Client.Resource.ISM;

import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.LengthContainer;

import java.util.Hashtable;
import java.util.Vector;

public class TransmissionPathDecompositor
{
	LengthContainer []lengthContainer;

	TransmissionPath sp = null;
	Vector ports = new Vector();

	public TransmissionPathDecompositor()
	{
	}

	public TransmissionPathDecompositor(TransmissionPath sp)
	{
		setPath(sp);
	}

	public void setPath(TransmissionPath sp)
	{
		this.sp = sp;
		setTraceData(sp);
		ports = sp.sortPorts();
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
	public Object getEquipmentByOpticalDistance(double opticalDistance)
	{
		Object o = null;
		boolean left_nearest = true;
		double dist = 0.;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(dist + lengthContainer[i].opticalLength < opticalDistance)
				dist += lengthContainer[i].opticalLength;
			else
			{
				o = lengthContainer[i].obj;
				left_nearest = (opticalDistance - dist) < (dist + lengthContainer[i].opticalLength - opticalDistance);
				break;
			}
		}

		if(o instanceof CableLink)
		{
			CableLink link = (CableLink)o;
			CablePort cp1 = (CablePort )Pool.get(CablePort.typ, link.start_port_id);
			CablePort cp2 = (CablePort )Pool.get(CablePort.typ, link.end_port_id);
			CablePort cpleft = null;
			CablePort cpright = null;
			if(ports.indexOf(cp1) < ports.indexOf(cp2))
			{
				cpleft = cp1;
				cpright = cp2;
			}
			else
			{
				cpleft = cp2;
				cpright = cp1;
			}
			if(left_nearest)
				return Pool.get("kisequipment", cpleft.equipment_id);
			else
				return Pool.get("kisequipment", cpright.equipment_id);
		}
		else 
		if(o instanceof Link)
		{
			Link link = (Link)o;
			Port cp1 = (Port )Pool.get(Port.typ, link.start_port_id);
			Port cp2 = (Port )Pool.get(Port.typ, link.end_port_id);
			Port cpleft = null;
			Port cpright = null;
			if(ports.indexOf(cp1) < ports.indexOf(cp2))
			{
				cpleft = cp1;
				cpright = cp2;
			}
			else
			{
				cpleft = cp2;
				cpright = cp1;
			}
			if(left_nearest)
				return Pool.get("kisequipment", cpleft.equipment_id);
			else
				return Pool.get("kisequipment", cpright.equipment_id);
		}
		return null;
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
    if(o instanceof CableLink)
    {
      CableLink link = (CableLink)o;
	  CablePort cp1 = (CablePort )Pool.get(CablePort.typ, link.start_port_id);
	  CablePort cp2 = (CablePort )Pool.get(CablePort.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp1 : cp2;
//    return Pool.get(CablePort.typ, link.start_port_id);
    }
    else if(o instanceof Link)
    {
      Link link = (Link)o;
	  Port cp1 = (Port )Pool.get(Port.typ, link.start_port_id);
	  Port cp2 = (Port )Pool.get(Port.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp1 : cp2;
//      return Pool.get(Port.typ, link.start_port_id);
    }
    return null;
  }

//-----------------------------------------------------------------------------
  public double getSourcePortDistanceByOpticalDistance(double opticalDistance)
  {
    double dist = 0.;
    for(int i = 0; i < lengthContainer.length; i++)
    {
      if(dist + lengthContainer[i].opticalLength > opticalDistance)
      {
          return opticalDistance - dist;
      }
      dist += lengthContainer[i].opticalLength;
    }
    return opticalDistance - dist;
  }

//-----------------------------------------------------------------------------
  public double getTargetPortDistanceByOpticalDistance(double opticalDistance)
  {
    double dist = 0.;
    for(int i = 0; i < lengthContainer.length; i++)
    {
      dist += lengthContainer[i].opticalLength;
      if(dist >= opticalDistance)
      {
          return dist - opticalDistance;
      }
    }
    return 0.0;
  }


//-----------------------------------------------------------------------------
  public Object getSourcePortByPhysicalDistance(double physicalDistance)
  {
    Object o = getLinkByPhysicalDistance(physicalDistance);
    if(o instanceof CableLink)
    {
      CableLink link = (CableLink)o;
	  CablePort cp1 = (CablePort )Pool.get(CablePort.typ, link.start_port_id);
	  CablePort cp2 = (CablePort )Pool.get(CablePort.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp1 : cp2;
//      return Pool.get(CablePort.typ, link.start_port_id);
    }
    else if(o instanceof Link)
    {
      Link link = (Link)o;
	  Port cp1 = (Port )Pool.get(Port.typ, link.start_port_id);
	  Port cp2 = (Port )Pool.get(Port.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp1 : cp2;
//      return Pool.get(Port.typ, link.start_port_id);
    }
    return null;
  }


//-----------------------------------------------------------------------------
  public Object getTargetPortByOpticalDistance(double opticalDistance)
  {
    Object o = getLinkByOpticalDistance(opticalDistance);
    if(o instanceof CableLink)
    {
      CableLink link = (CableLink)o;
	  CablePort cp1 = (CablePort )Pool.get(CablePort.typ, link.start_port_id);
	  CablePort cp2 = (CablePort )Pool.get(CablePort.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp2 : cp1;
//      return Pool.get(CablePort.typ, link.end_port_id);
    }
    else if(o instanceof Link)
    {
      Link link = (Link)o;
	  Port cp1 = (Port )Pool.get(Port.typ, link.start_port_id);
	  Port cp2 = (Port )Pool.get(Port.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp2 : cp1;
//      return Pool.get(Port.typ, link.end_port_id);
    }
    return null;
  }


//-----------------------------------------------------------------------------
  public Object getTargetPortByPhysicalDistance(double physicalDistance)
  {
    Object o = getLinkByPhysicalDistance(physicalDistance);
    if(o instanceof CableLink)
    {
      CableLink link = (CableLink)o;
	  CablePort cp1 = (CablePort )Pool.get(CablePort.typ, link.start_port_id);
	  CablePort cp2 = (CablePort )Pool.get(CablePort.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp2 : cp1;
//      return Pool.get(CablePort.typ, link.end_port_id);
    }
    else if(o instanceof Link)
    {
      Link link = (Link)o;
	  Port cp1 = (Port )Pool.get(Port.typ, link.start_port_id);
	  Port cp2 = (Port )Pool.get(Port.typ, link.end_port_id);
      return (ports.indexOf(cp1) < ports.indexOf(cp2)) ? cp2 : cp1;
//      return Pool.get(Port.typ, link.end_port_id);
    }
    return null;
  }



//-----------------------------------------------------------------------------
	protected boolean setTraceData(TransmissionPath sp)
	{
		if(sp == null)
			return false;

		Hashtable ht;
		double length;
		Vector vec = new Vector();

		TransmissionPathElement []pe =
				(TransmissionPathElement[])sp.links.toArray(new TransmissionPathElement[sp.links.size()]);

		TransmissionPathElement []tmp = new TransmissionPathElement[pe.length];

		for(int i = 0; i < pe.length; i++)
		{
			tmp[pe[i].n] = pe[i];
		}
		pe = tmp;

		for(int i = 0; i < pe.length; i++) // count through all of the Path Elements
		{
			if(pe[i].is_cable) // CABLE LINK
			{
				CableLink cableLink =
						(CableLink)Pool.get(CableLink.typ, pe[i].link_id);
				if(cableLink == null)
				{
					System.out.println("cablelink " + pe[i].link_id + " is null! PathElement index " + pe[i].n);
//					System.out.println("Something wrong... - CableLink == null");
					return false;
				}

				LengthContainer lc = new LengthContainer(
						cableLink,
						cableLink.physical_length,
						cableLink.optical_length, 
						CableLink.typ);

				vec.add(lc);
			}
			else               // simple link
			{
				Link link =
						(Link)Pool.get(Link.typ, pe[i].link_id);
				if(link == null)
				{
					System.out.println("link " + pe[i].link_id + " is null! PathElement index " + pe[i].n);
					return false;
				}
				LengthContainer lc = new LengthContainer(
						link,
						link.physical_length,
						link.optical_length, 
						Link.typ);
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
//		System.out.println("Data is set...");
		return true;
	}
}



