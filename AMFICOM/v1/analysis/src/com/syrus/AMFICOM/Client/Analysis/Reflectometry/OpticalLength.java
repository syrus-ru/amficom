package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.util.ArrayList;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.LengthContainer;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class OpticalLength extends SchemePathDecompositor
{
	LengthContainer []lengthContainer;

	SchemePath sp = null;

	public OpticalLength()
	{
	}

	public OpticalLength(SchemePath sp)
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
	public double getKu(double optical_distance)
	{
		double d = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d + lengthContainer[i].opticalLength < optical_distance)
				d += lengthContainer[i].opticalLength;
			else
				return lengthContainer[i].Ku;
		}
		return 1.0;
	}

//-----------------------------------------------------------------------------
	public Object getLengthContainerObject(double optical_distance)
	{
		double d = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d + lengthContainer[i].opticalLength < optical_distance)
				d += lengthContainer[i].opticalLength;
			else
				return lengthContainer[i].obj;
		}
		return null;
	}

//-----------------------------------------------------------------------------
	public double getPhysicalDistance(double optical_distance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d + lengthContainer[i].opticalLength < optical_distance)
			{
				d += lengthContainer[i].opticalLength;
				d2 += lengthContainer[i].physicalLength;
			}
			else
			{
				double diff = optical_distance - d;
				d2 += diff / lengthContainer[i].Ku;
				break;
			}
		}
		return d2;
	}

//-----------------------------------------------------------------------------
	public double getOpticalDistance(double physical_distance)
	{
		double d = 0.0;
		double d2 = 0.0;
		for(int i = 0; i < lengthContainer.length; i++)
		{
			if(d2 + lengthContainer[i].physicalLength < physical_distance)
			{
				d2 += lengthContainer[i].physicalLength;
				d += lengthContainer[i].opticalLength;
			}
			else
			{
				double diff = physical_distance - d2;
				d += diff * lengthContainer[i].Ku;
				break;
			}
		}
		return d;
	}

//-----------------------------------------------------------------------------
	protected boolean setTraceData(SchemePath sp)
	{
		if(sp == null)
			return false;

		Characteristic c;
		ElementAttribute ea;
		double length;
		ArrayList vec = new ArrayList();

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
				String thread_id = pe[i].thread_id;
				SchemeCableThread []cableThreads =
					(SchemeCableThread [])schemeCableLink.cable_threads.toArray
						(new SchemeCableThread[schemeCableLink.cable_threads.size()]);

				SchemeCableThread cableThread = null;
				for(int j = 0; j < cableThreads.length; j++)
				{
					if(cableThreads[j].id.equalsIgnoreCase(thread_id))
					{
						cableThread = cableThreads[j];
						break;
					}
				}

				LengthContainer lc = new LengthContainer(
						cableThread,
						schemeCableLink.getPhysicalLength(),
						schemeCableLink.getOpticalLength());
				vec.add(lc);
			}
			else               // simple link
			{
				SchemeLink schemeLink =
						(SchemeLink)Pool.get(SchemeLink.typ, pe[i].link_id);
				if(schemeLink == null)
				{
					System.out.println("Something is wrong...");
					return false;
				}
				LengthContainer lc = new LengthContainer(
						schemeLink,
						schemeLink.getPhysicalLength(),
						schemeLink.getOpticalLength());
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



