package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.SolutionCompact;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;

public class RISDSchemeDataSource
		extends RISDMapDataSource
		implements DataSourceInterface
{
	protected RISDSchemeDataSource()
	{
	}

	public RISDSchemeDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadSchemeProto()
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadSchemeProto:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		SchemeProtoElementSeq_TransferableHolder peh = new SchemeProtoElementSeq_TransferableHolder();
		SchemeProtoElement_Transferable protos[];
		ProtoElement proto;

		List loaded_objects = new ArrayList();
		ObjectResource or;

		try
		{
//			ecode = ((RISDSessionInfo)getSession()).ci.server.GetStatedSchemeProtoElements(((RISDSessionInfo)getSession()).accessIdentity, ih, peh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetSchemeProto! status = " + ecode);
			return;
		}

		List imvec = new ArrayList(peh.value.length);
		for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbolId);
		String[] imids = (String[])imvec.toArray(new String[imvec.size()]);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
			for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
			}
*/
		protos = peh.value;
		count = protos.length;
		System.out.println("...Done! " + count + " proto element(s) fetched");
			for (i = 0; i < count; i++)
		{
			proto = new ProtoElement(protos[i]);
			Pool.put(ProtoElement.typ, proto.getId(), proto);
			loaded_objects.add(proto);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadSchemeProto(Vector ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadSchemeProto:");

		int i;
		int ecode = 0;
		int count;
		String[] id_s = (String[])ids.toArray(new String[ids.size()]);

		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		SchemeProtoElementSeq_TransferableHolder peh = new SchemeProtoElementSeq_TransferableHolder();
		SchemeProtoElement_Transferable protos[];
		ProtoElement proto;

		List loaded_objects = new ArrayList();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.GetStatedSchemeProtoElements(((RISDSessionInfo)getSession()).accessIdentity, id_s, ih, peh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadSchemeProto! status = " + ecode);
			return;
		}

		List imvec = new ArrayList(peh.value.length);
		for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbolId);
		String[] imids = (String[])imvec.toArray(new String[imvec.size()]);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
			for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
			}
*/
		protos = peh.value;
		count = protos.length;
		System.out.println("...Done! " + count + " proto element(s) fetched");
			for (i = 0; i < count; i++)
		{
			proto = new ProtoElement(protos[i]);
			Pool.put(ProtoElement.typ, proto.getId(), proto);
			loaded_objects.add(proto);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveSchemeProtos(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;
		ImageResource_Transferable []images;
		List image_vec = new ArrayList();

		SchemeProtoElement_Transferable pes[] = new SchemeProtoElement_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			ProtoElement proto = (ProtoElement )Pool.get(ProtoElement.typ, ids[i]);
			if(proto.symbolId != null && !(proto.symbolId.equals("")))
				image_vec.add(ImageCatalogue.get(proto.symbolId));
			proto.setTransferableFromLocal();
			pes[i] = (SchemeProtoElement_Transferable )proto.getTransferable();
		}

		int count = image_vec.size();
		int i = 0;
		images = new ImageResource_Transferable[image_vec.size()];
		for(Iterator it = image_vec.iterator(); it.hasNext();)
		{
			ObjectResource os = (ObjectResource )it.next();
			os.setTransferableFromLocal();
			images[i++] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.SaveSchemeProtoElements(((RISDSessionInfo)getSession()).accessIdentity, images, pes);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveSchemeProtos! status = " + ecode);
			return;
		}
	}

	public void RemoveSchemeProtos(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;
		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.RemoveSchemeProtoElements(((RISDSessionInfo)getSession()).accessIdentity, ids);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveSchemeProtos! status = " + ecode);
			return;
		}
	}

	public void LoadSchemes()
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadSchemes:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		SchemeSeq_TransferableHolder sh = new SchemeSeq_TransferableHolder();
		Scheme_Transferable schemes[];
		Scheme scheme;

		List loaded_objects = new ArrayList();
		ObjectResource or;

		try
		{
//			ecode = ((RISDSessionInfo)getSession()).ci.server.GetSchemes(((RISDSessionInfo)getSession()).accessIdentity, ih, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting schemes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetSchemes! status = " + ecode);
			return;
		}

		List imvec = new ArrayList(ih.value.length + sh.value.length);
		for (i = 0; i < ih.value.length; i++)
			imvec.add(ih.value[i].id);
		for (i = 0; i < sh.value.length; i++)
			imvec.add(sh.value[i].symbolId);
		String[] imids = (String[])imvec.toArray(new String[imvec.size()]);
		new DataSourceImage(this).LoadImages(imids);

/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
			for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
			}
*/
		schemes = sh.value;
		count = schemes.length;
		System.out.println("...Done! " + count + " scheme(s) fetched");
			for (i = 0; i < count; i++)
		{
			scheme = new Scheme(schemes[i]);
			Pool.put(Scheme.typ, scheme.getId(), scheme);
			loaded_objects.add(scheme);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadSchemes(Vector ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadSchemes:");

		String[] id_s = (String[])ids.toArray(new String[ids.size()]);
		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		SchemeSeq_TransferableHolder sh = new SchemeSeq_TransferableHolder();
		Scheme_Transferable schemes[];
		Scheme scheme;

		List loaded_objects = new ArrayList();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.GetStatedSchemes(((RISDSessionInfo)getSession()).accessIdentity, id_s, ih, sh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting schemes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetSchemes! status = " + ecode);
			return;
		}

		List imvec = new ArrayList(ih.value.length + sh.value.length);
		for (i = 0; i < ih.value.length; i++)
			imvec.add(ih.value[i].id);
		for (i = 0; i < sh.value.length; i++)
			imvec.add(sh.value[i].symbolId);
		String[] imids = (String[])imvec.toArray(new String[imvec.size()]);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
			for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
			}
*/
		schemes = sh.value;
		count = schemes.length;
		System.out.println("...Done! " + count + " scheme(s) fetched");
			for (i = 0; i < count; i++)
		{
			scheme = new Scheme(schemes[i]);
			Pool.put(Scheme.typ, scheme.getId(), scheme);
			loaded_objects.add(scheme);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveScheme(String mcId)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;
		ImageResource_Transferable []images = new ImageResource_Transferable[0];

		Scheme scheme = (Scheme )Pool.get(Scheme.typ, mcId);
		scheme.setTransferableFromLocal();
		Scheme_Transferable scheme_t = (Scheme_Transferable )scheme.getTransferable();

		Scheme_Transferable[] s_t = new Scheme_Transferable[1];
		s_t[0] = scheme_t;

		if(scheme.symbolId != null && !(scheme.symbolId.equals("")))
		{
			images = new ImageResource_Transferable[1];
			ImageResource os = ImageCatalogue.get(scheme.symbolId);
			os.setTransferableFromLocal();
			images[0] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.SaveSchemes(((RISDSessionInfo)getSession()).accessIdentity, images, s_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving schemes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveScheme! status = " + ecode);
			return;
		}
	}

	public void RemoveScheme(String mcId)
	{
		Scheme sch = (Scheme )Pool.get("scheme", mcId);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("RemoveScheme:");

		int ecode = 0;

		String[] ss = new String[1];
		ss[0] = sch.getId();
		String[] leer = new String[0];

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.RemoveSchemes(
					((RISDSessionInfo)getSession()).accessIdentity,
					ss,
					leer,
					leer,
					leer);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing scheme: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveScheme! status = " + ecode);
			return;
		}
	}

	public void RemoveFromScheme(String mcId)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;
	}
/*
	public void LoadAttributeTypes()
	{
		if(si == null)
			return;
		if(!((RISDSessionInfo)getSession()).isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		ElementAttributeTypeSeq_TransferableHolder ath = new ElementAttributeTypeSeq_TransferableHolder();
		ElementAttributeType_Transferable atypes[];
		ElementAttributeType atype;

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.LoadAttributeTypes(((RISDSessionInfo)getSession()).accessIdentity, ath);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting attribute types: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadAttributeTypes! status = " + ecode);
			return;
		}

		atypes = ath.value;
		count = atypes.length;
		System.out.println("...Done! " + count + " attribute type(s) fetched");
			for (i = 0; i < count; i++)
		{
			atype = new com.syrus1.AMFICOM.Client.Resource.General.ElementAttributeType(atypes[i]);
			Pool.put(ElementAttributeType.typ, atype.getId(), atype);
			}
	}
*/
/*
	public void LoadAttributeTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!((RISDSessionInfo)getSession()).isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		ElementAttributeTypeSeq_TransferableHolder ath = new ElementAttributeTypeSeq_TransferableHolder();
		ElementAttributeType_Transferable atypes[];
		ElementAttributeType atype;

		try
		{
			ecode = ((RISDSessionInfo)getSession()).ci.server.LoadStatedAttributeTypes(((RISDSessionInfo)getSession()).accessIdentity, ids, ath);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting attribute types: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadAttributeTypes! status = " + ecode);
			return;
		}

		atypes = ath.value;
		count = atypes.length;
		System.out.println("...Done! " + count + " attribute type(s) fetched");
			for (i = 0; i < count; i++)
		{
			atype = new com.syrus1.AMFICOM.Client.Resource.General.ElementAttributeType(atypes[i]);
			Pool.put(ElementAttributeType.typ, atype.getId(), atype);
			}
	}
*/
	public void SaveSchemeOptimizeInfo(String soiId)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		SchemeOptimizeInfo_Transferable soi = (SchemeOptimizeInfo_Transferable )
			Pool.get("optimized_schemeInfo", soiId);
		try
		{
			((RISDSessionInfo)getSession()).ci.server.saveSchemeOptimizeInfo(((RISDSessionInfo)getSession()).accessIdentity, soi);
		}
		catch (Exception ex)
		{
			System.err.print("Error savting SOI: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void LoadSchemeOptimizeInfo()
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		SchemeOptimizeInfo_Transferable sois[];

		try
		{
			sois = ((RISDSessionInfo)getSession()).ci.server.getSchemeOptimizeInfo(((RISDSessionInfo)getSession()).accessIdentity);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting SOI: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		count = sois.length;
		System.out.println("...Done! " + count + " SOI(s) fetched");
			for (i = 0; i < count; i++)
		{
			Pool.put("optimized_schemeInfo", sois[i].id, sois[i]);
			}
	}

	public void RemoveSchemeOptimizeInfo(String soiId)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		try
		{
			((RISDSessionInfo)getSession()).ci.server.removeSchemeOptimizeInfo(((RISDSessionInfo)getSession()).accessIdentity, new String[] { soiId });
		}
		catch (Exception ex)
		{
			System.err.print("Error removting SOI: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void SaveSchemeMonitoringSolutions(String solId)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		SolutionCompact sc = (SolutionCompact )Pool.get("sm_solution", solId);
		sc.setTransferableFromLocal();
		SchemeMonitoringSolution_Transferable sol =
				(SchemeMonitoringSolution_Transferable )sc.getTransferable();
		try
		{
			((RISDSessionInfo)getSession()).ci.server.saveSchemeMonitoringSolutions(((RISDSessionInfo)getSession()).accessIdentity, sol);
		}
		catch (Exception ex)
		{
			System.err.print("Error savting SOL: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void LoadSchemeMonitoringSolutions()
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		SchemeMonitoringSolution_Transferable sols[];

		try
		{
			sols = ((RISDSessionInfo)getSession()).ci.server.getSchemeMonitoringSolutions(((RISDSessionInfo)getSession()).accessIdentity);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting SOL: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		count = sols.length;
		System.out.println("...Done! " + count + " SOL(s) fetched");
			for (i = 0; i < count; i++)
		{
			SolutionCompact sc = new SolutionCompact(sols[i]);
			Pool.put("sm_solution", sc.getId(), sc);
			}
	}

	public void RemoveSchemeMonitoringSolution(String solId)
	{
		if(getSession() == null)
			return;
		if(!((RISDSessionInfo)getSession()).isOpened())
			return;

		try
		{
			((RISDSessionInfo)getSession()).ci.server.removeSchemeMonitoringSolutions(((RISDSessionInfo)getSession()).accessIdentity, new String[] { solId });
		}
		catch (Exception ex)
		{
			System.err.print("Error removting SOL: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}
}
