package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Map.MapViewSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapView_Transferable;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.RISDSchemeDataSource;

import com.syrus.AMFICOM.Client.Resource.MapView.MapPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.ArrayList;
import java.util.Vector;

public class RISDMapViewDataSource
		extends RISDSchemeDataSource
		implements DataSourceInterface
{
	protected RISDMapViewDataSource()
	{
	}

	public RISDMapViewDataSource(SessionInterface si)
	{
		super(si);
	}
/*
	public void LoadMapViewProtoElements()
	{
		if(this.getSession() == null)
			return;
		if(!this.getSession().isOpened())
			return;

		System.out.println("LoadProtoElements:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		MapPathProtoElementSeq_TransferableHolder ph = new MapPathProtoElementSeq_TransferableHolder();
		MapPathProtoElement_Transferable paths[];
		MapPathProtoElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetMapViewProtoElements(si.accessIdentity, ih, ph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting map proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetProtoElements! status = " + ecode);
			return;
		}

		Vector imvec = new Vector();
	    for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " map proto path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapPathProtoElement(paths[i]);
			Pool.put("mappathproto", path.getId(), path);
//			Pool.putName("mappathproto", path.getId(), path.getName());
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}
*/
/*
	public void LoadMapViewProtoElements(String[] pids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadProtoElements:");

		int i;
		int ecode = 0;
		int count;
		MapPathProtoElementSeq_TransferableHolder ph = new MapPathProtoElementSeq_TransferableHolder();
		MapPathProtoElement_Transferable paths[];
		MapPathProtoElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.GetStatedMapViewProtoElements(
					((RISDSessionInfo )getSession()).accessIdentity, 
					pids, 
					ph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting map proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetProtoElements! status = " + ecode);
			return;
		}

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " map proto path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapPathProtoElement(paths[i]);
			Pool.put(MapPathProtoElement.typ, path.getId(), path);
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveMapViewProtoElements(String[] path_ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;

		Vector image_vec = new Vector();
		MapPathProtoElement_Transferable mpes[] = new MapPathProtoElement_Transferable[path_ids.length];
		for(int i = 0; i < path_ids.length; i++)
		{
			MapPathProtoElement mpe = (MapPathProtoElement )Pool.get(MapPathProtoElement.typ, path_ids[i]);
			mpe.setTransferableFromLocal();
			mpes[i] = (MapPathProtoElement_Transferable )mpe.getTransferable();
		}

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.SaveMapViewProtoElements(
					((RISDSessionInfo )getSession()).accessIdentity, 
					mpes);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving LinkTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveLinkTypes! status = " + ecode);
			return;
		}
	}
	
	public void RemoveMapViewProtoElements(String[] path_ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;
		String []gids = new String[0];
		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.RemoveMapViewProtoElements(
					((RISDSessionInfo )getSession()).accessIdentity, 
					path_ids);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing MapProtos: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveMapProtoElements! status = " + ecode);
			return;
		}
	}
*/	
	public void LoadMapViews(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadMapViews: ");

		int i;
		int ecode = 0;
		int count;
		MapViewSeq_TransferableHolder mh = new MapViewSeq_TransferableHolder();
		MapView_Transferable mvs[];
		MapView mv;

//		MapPathElementSeq_TransferableHolder ph = new MapPathElementSeq_TransferableHolder();
//		MapPathElement_Transferable paths[];
//		MapPathElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.GetStatedMapViews(
					((RISDSessionInfo )getSession()).accessIdentity, 
					ids,
					mh); 
//					ph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting maps: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetMaps! status = " + ecode);
			return;
		}

//		paths = ph.value;
//		count = paths.length;
//		System.out.println("...Done! " + count + " path(s) fetched");
//	    for (i = 0; i < count; i++)
//		{
//			path = new MapPathElement(paths[i]);
//			Pool.put(MapPathElement.typ, path.getId(), path);
//			loaded_objects.add(path);
//	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveMapViews(String[] mv_ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("SaveMap:");

		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		ArrayList path_vec = new ArrayList();

		MapView_Transferable maps[];
//		MapPathElement_Transferable paths[];

		MapView mv;
		MapPathElement path;

		maps = new MapView_Transferable[mv_ids.length];
		for(i = 0; i < mv_ids.length; i++)
		{
			mv = (MapView )Pool.get(MapView.typ, mv_ids[i]);
			mv.setTransferableFromLocal();
			maps[i] = (MapView_Transferable )mv.getTransferable();

//			for(Iterator it = mv.getPaths().iterator(); it.hasNext();)
//			{
//				os = (ObjectResource )it.next();
//				os.setTransferableFromLocal();
//				path_vec.add(os.getTransferable());
//			}

		}

//		paths = (MapPathElement_Transferable [])
//			node_vec.toArray(new MapPathElement_Transferable[0]);
	
		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.SaveMapViews(
					((RISDSessionInfo )getSession()).accessIdentity,
					maps);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving map: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveMap! status = " + ecode);
			return;
		}
	}

	public void RemoveMapView(String mv_id)
	{
		MapView mv = (MapView )Pool.get(MapView.typ, mv_id);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("RemoveMap:");

		int ecode = 0;

		String[] maps = new String[1];
		maps[0] = mv.getId();
		String[] leer = new String[0];

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.RemoveMapViews(
					((RISDSessionInfo )getSession()).accessIdentity,
					maps);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing map: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveMap! status = " + ecode);
			return;
		}
	}
/*
	public void RemoveFromMapView(String mv_id)
	{
		MapView mv = (MapView )Pool.get(MapView.typ, mv_id);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("RemoveFromMapView:");
	
		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		ArrayList path_vec = new ArrayList();
		List vec;

		String[] maps = new String[0];
		String[] paths;

		MapPathElement path;

//		vec = mv.getRemovedElements();
//		count = vec.size();
//		for(i = 0; i < count; i++)
//		{
//			os = (ObjectResource )vec.get(i);
//			if(os.getTyp().equals(MapPathElement.typ))
//				path_vec.add(os.getId());
//		}

		paths = path_vec.copyInto(new String[path_vec.size()]);

		try
		{
			ecode = si.ci.server.RemoveMapViews(
					si.accessIdentity,
					maps,
					paths);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing from map: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveFromMap! status = " + ecode);
			return;
		}
	}
*/

}