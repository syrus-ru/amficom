package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import oracle.aurora.jndi.sess_iiop.ServiceCtx;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.*;
import com.syrus.AMFICOM.CORBA.ISM.*;
import com.syrus.AMFICOM.CORBA.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.*;
import com.syrus.AMFICOM.Client.General.*;

public class RISDSchemeDataSource
		extends RISDDirectoryDataSource
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
		if(si == null)
			return;
		if(!si.isOpened())
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
		
		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetSchemeProtoElements(si.accessIdentity, ih, peh);
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

		Vector imvec = new Vector();
	    for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
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
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadSchemeProto:");

		int i;
		int ecode = 0;
		int count;
		String[] id_s = new String[ids.size()];
		ids.copyInto(id_s);
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		SchemeProtoElementSeq_TransferableHolder peh = new SchemeProtoElementSeq_TransferableHolder();
		SchemeProtoElement_Transferable protos[];
		ProtoElement proto;
		
		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetStatedSchemeProtoElements(si.accessIdentity, id_s, ih, peh);
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

		Vector imvec = new Vector();
	    for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
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
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		ImageResource_Transferable []images;
		Vector image_vec = new Vector();
		
		SchemeProtoElement_Transferable pes[] = new SchemeProtoElement_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			ProtoElement proto = (ProtoElement )Pool.get(ProtoElement.typ, ids[i]);
			if(proto.symbol_id != null && !(proto.symbol_id.equals("")))
				image_vec.add(ImageCatalogue.get(proto.symbol_id));
			proto.setTransferableFromLocal();
			pes[i] = (SchemeProtoElement_Transferable )proto.getTransferable();
		}

		int count = image_vec.size();
		int i = 0;
		images = new ImageResource_Transferable[image_vec.size()];
		for(Enumeration enum = image_vec.elements(); enum.hasMoreElements();)
		{
			ObjectResource os = (ObjectResource )enum.nextElement();
			os.setTransferableFromLocal();
			images[i++] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveSchemeProtoElements(si.accessIdentity, images, pes);
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
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		try
		{
			ecode = si.ci.server.RemoveSchemeProtoElements(si.accessIdentity, ids);
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
		if(si == null)
			return;
		if(!si.isOpened())
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
		
		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetSchemes(si.accessIdentity, ih, sh);
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

		Vector imvec = new Vector();
	    for (i = 0; i < ih.value.length; i++)
			imvec.add(ih.value[i].id);
	    for (i = 0; i < sh.value.length; i++)
			imvec.add(sh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
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
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadSchemes:");

		String[] id_s = new String[ids.size()];
		ids.copyInto(id_s);
		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		SchemeSeq_TransferableHolder sh = new SchemeSeq_TransferableHolder();
		Scheme_Transferable schemes[];
		Scheme scheme;
		
		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetStatedSchemes(si.accessIdentity, id_s, ih, sh);
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

		Vector imvec = new Vector();
	    for (i = 0; i < ih.value.length; i++)
			imvec.add(ih.value[i].id);
	    for (i = 0; i < sh.value.length; i++)
			imvec.add(sh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
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

	public void SaveScheme(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		ImageResource_Transferable []images = new ImageResource_Transferable[0];

		Scheme scheme = (Scheme )Pool.get(Scheme.typ, mc_id);
		scheme.setTransferableFromLocal();
		Scheme_Transferable scheme_t = (Scheme_Transferable )scheme.getTransferable();

		Scheme_Transferable[] s_t = new Scheme_Transferable[1];
		s_t[0] = scheme_t;

		if(scheme.symbol_id != null && !(scheme.symbol_id.equals("")))
		{
			images = new ImageResource_Transferable[1];
			ImageResource os = ImageCatalogue.get(scheme.symbol_id);
			os.setTransferableFromLocal();
			images[0] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveSchemes(si.accessIdentity, images, s_t);
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

	public void RemoveScheme(String mc_id)
	{
		Scheme sch = (Scheme )Pool.get("scheme", mc_id);

		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("RemoveScheme:");

		int ecode = 0;

		String[] ss = new String[1];
		ss[0] = sch.getId();
		String[] leer = new String[0];

		try
		{
			ecode = si.ci.server.RemoveSchemes(
					si.accessIdentity,
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

	public void RemoveFromScheme(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void LoadAttributeTypes()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		ElementAttributeTypeSeq_TransferableHolder ath = new ElementAttributeTypeSeq_TransferableHolder();
		ElementAttributeType_Transferable atypes[];
		ElementAttributeType atype;

		try
		{
			ecode = si.ci.server.LoadAttributeTypes(si.accessIdentity, ath);
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
			atype = new ElementAttributeType(atypes[i]);
			Pool.put(ElementAttributeType.typ, atype.getId(), atype);
	    }
	}

	public void LoadAttributeTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		ElementAttributeTypeSeq_TransferableHolder ath = new ElementAttributeTypeSeq_TransferableHolder();
		ElementAttributeType_Transferable atypes[];
		ElementAttributeType atype;

		try
		{
			ecode = si.ci.server.LoadStatedAttributeTypes(si.accessIdentity, ids, ath);
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
			atype = new ElementAttributeType(atypes[i]);
			Pool.put(ElementAttributeType.typ, atype.getId(), atype);
	    }
	}

	public void SaveSchemeOptimizeInfo(String soi_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		SchemeOptimizeInfo_Transferable soi = (SchemeOptimizeInfo_Transferable )
			Pool.get("optimized_scheme_info", soi_id);
		try
		{
			si.ci.server.saveSchemeOptimizeInfo(si.accessIdentity, soi);
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
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		SchemeOptimizeInfo_Transferable sois[];

		try
		{
			sois = si.ci.server.getSchemeOptimizeInfo(si.accessIdentity);
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
			Pool.put("optimized_scheme_info", sois[i].id, sois[i]);
	    }
	}
	
	public void RemoveSchemeOptimizeInfo(String soi_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.removeSchemeOptimizeInfo(si.accessIdentity, new String[] { soi_id });
		}
		catch (Exception ex)
		{
			System.err.print("Error removting SOI: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	public void SaveSchemeMonitoringSolutions(String sol_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		SolutionCompact sc = (SolutionCompact )Pool.get("sm_solution", sol_id);
		sc.setTransferableFromLocal();
		SchemeMonitoringSolution_Transferable sol = 
				(SchemeMonitoringSolution_Transferable )sc.getTransferable();
		try
		{
			si.ci.server.saveSchemeMonitoringSolutions(si.accessIdentity, sol);
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
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		SchemeMonitoringSolution_Transferable sols[];

		try
		{
			sols = si.ci.server.getSchemeMonitoringSolutions(si.accessIdentity);
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
	
	public void RemoveSchemeMonitoringSolution(String sol_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		try
		{
			si.ci.server.removeSchemeMonitoringSolutions(si.accessIdentity, new String[] { sol_id });
		}
		catch (Exception ex)
		{
			System.err.print("Error removting SOL: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}
}
