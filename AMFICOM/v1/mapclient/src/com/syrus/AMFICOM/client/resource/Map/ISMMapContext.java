package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import com.ofx.geometry.*;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Configure.Map.Strategy.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.ISMMapContextPane;
import com.syrus.AMFICOM.Client.Configure.Map.UI.Display.*;

//Данный класс используется для описания содержимого карты (её элементов и свойств)


public class ISMMapContext extends MapContext
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "ismmapcontext";

	public String ISM_id = "";
	public String ISM_name = "";
	public String ISM_codename = "";
	public String ISM_user_id = "";
	public String ISM_description = "";
	public String ISM_domain_id = "";

	public long ISM_created = 0;
	public String ISM_created_by = "";
	public long ISM_modified = 0;
	public String ISM_modified_by = "";

	public String ISM_map_id = "";

	public ISMMapContext_Transferable transferable;

	public ISMMapContext(LogicalNetLayer logical)
	{
		super(logical);
		transferable = new ISMMapContext_Transferable();
	}

	public ISMMapContext()
	{
		super();
		transferable = new ISMMapContext_Transferable();
	}
/*
	public ISMMapContext(ISMMapContext_Transferable transferable)
	{
		super();
		this.transferable = transferable;
		setLocalFromTransferable();
	}
*/
	public ISMMapContext(ISMMapContext_Transferable transferable, MapContext_Transferable map_transferable)
	{
		super(map_transferable);
		this.transferable = transferable;
		setLocalFromTransferable();
	}


	public void setLocalFromTransferable()
	{
		int l;
		int i;
		int count;

		super.setLocalFromTransferable();
		if(transferable == null)
			return;

		ISM_id = transferable.id;
		ISM_name = transferable.name;
		ISM_codename = transferable.codename;
		ISM_user_id = transferable.user_id;
        ISM_domain_id = transferable.domain_id;
        ISM_created = transferable.created;
        ISM_modified = transferable.modified;
        ISM_modified_by = transferable.modified_by;
        ISM_created_by = transferable.created_by;
        ISM_map_id = transferable.map_id;
		ISM_description = transferable.description;

		count = transferable.node_ids.length;
		for(i = 0; i < count; i++)
			node_ids.add(transferable.node_ids[i]);

		count = transferable.nodeLink_ids.length;
		for(i = 0; i < count; i++)
			nodelink_ids.add(transferable.nodeLink_ids[i]);

		count = transferable.physicalLink_ids.length;
		for(i = 0; i < count; i++)
			link_ids.add(transferable.physicalLink_ids[i]);

		count = transferable.kis_ids.length;
		kis_ids = new Vector(count);
		for(i = 0; i < count; i++)
			kis_ids.add(transferable.kis_ids[i]);

		count = transferable.path_ids.length;
		path_ids = new Vector(count);
		for(i = 0; i < count; i++)
			path_ids.add(transferable.path_ids[i]);

	}

	public void setNetMapContext(MapContext mc)
	{
//		mc.setTransferableFromLocal();
//		super.transferable = (MapContext_Transferable )mc.getTransferable();
//		super.setLocalFromTransferable();
//		super.updateLocalFromTransferable();

		mc.logicalNetLayer = this.logicalNetLayer;

		super.changed = mc.changed;
		super.codename = mc.codename;
		super.created = mc.created;
		super.created_by = mc.created_by;
		super.curMapElement = mc.curMapElement;
		super.description = mc.description;
		super.domain_id = mc.domain_id;
		super.equipment_ids = mc.equipment_ids;
		super.id = mc.id;
		super.kis_ids = mc.kis_ids;
		super.latitude = mc.latitude;
		super.link_ids = mc.link_ids;
		super.linkState = mc.linkState;
		super.logicalNetLayer = mc.logicalNetLayer;
		super.longitude = mc.longitude;
		super.markers = mc.markers;
		super.modified = mc.modified;
		super.modified_by = mc.modified_by;
		super.name = mc.name;
		super.node_ids = mc.node_ids;
		super.nodelink_ids = mc.nodelink_ids;
		super.nodeLinks = mc.nodeLinks;
		super.nodes = mc.nodes;
		super.path_ids = mc.path_ids;
		super.physicalLinks = mc.physicalLinks;
		super.transferable = mc.transferable;
		super.transmissionPath = mc.transmissionPath;
		super.user_id = mc.user_id;
	}

	public void setNetMapContextFromTransferable(MapContext mc)
	{
		super.transferable = (MapContext_Transferable)mc.getTransferable();
		setLocalFromTransferable();
		updateLocalFromTransferable();
	}

	public void setTransferableFromLocal()
	{
		int l;
		int i;
		int count;
		ObjectResource os;

		transferable.id = ISM_id;
		transferable.name = ISM_name;
		transferable.codename = ISM_codename;
		transferable.user_id = ISM_user_id;
	    transferable.domain_id = ISM_domain_id;
		transferable.description = ISM_description;
        transferable.modified = System.currentTimeMillis();
		transferable.modified_by = ISM_user_id;
		transferable.created_by = ISM_user_id;
		transferable.map_id = ISM_map_id;

		node_ids = new Vector();
		kis_ids = new Vector();
		count = nodes.size();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)nodes.get(i);
			if(os.getTyp().equals("mapnodeelement"))
			{
				MapPhysicalNodeElement node = (MapPhysicalNodeElement)os;
				if(node.ism_map_id.equals(ISM_id))
					node_ids.add(os.getId());
			}
			if(os.getTyp().equals("mapkiselement"))
				kis_ids.add(os.getId());
		}
		transferable.node_ids = new String[node_ids.size()];
		node_ids.copyInto(transferable.node_ids);
		transferable.kis_ids = new String[kis_ids.size()];
		kis_ids.copyInto(transferable.kis_ids);

		count = nodeLinks.size();
		nodelink_ids = new Vector();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)nodeLinks.get(i);
			MapNodeLinkElement nodelink = (MapNodeLinkElement)os;
			if(nodelink.ism_map_id.equals(ISM_id))
				nodelink_ids.add(os.getId());
		}
		transferable.nodeLink_ids = new String[nodelink_ids.size()];
		nodelink_ids.copyInto(transferable.nodeLink_ids);

		count = physicalLinks.size();
		link_ids = new Vector();
	    for(i = 0; i < count; i++)
		{
			os = (ObjectResource)physicalLinks.get(i);
			MapPhysicalLinkElement link = (MapPhysicalLinkElement)os;
			if(link.ism_map_id.equals(ISM_id))
				link_ids.add(os.getId());
		}
		transferable.physicalLink_ids = new String[link_ids.size()];
		link_ids.copyInto(transferable.physicalLink_ids);

		count = transmissionPath.size();
		path_ids = new Vector();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource)transmissionPath.get(i);
			path_ids.add(os.getId());
		}
		transferable.path_ids = new String[path_ids.size()];
		path_ids.copyInto(transferable.path_ids);
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return ISM_name;
	}

	public String getId()
	{
		return ISM_id;
	}

	public long getModified()
	{
		return ISM_modified;
	}

	public void updateLocalFromTransferable()
	{
//		super.setLocalFromTransferable();
		super.updateLocalFromTransferable();

		int l;
		int i;

		l = kis_ids.size();
		for(i = 0; i < l; i++)
			nodes.add(Pool.get("mapkiselement", (String)kis_ids.get(i)));
/*
		l = node_ids.size();
		for(i = 0; i < l; i++)
			nodes.add(Pool.get("mapnodeelement", (String )node_ids.get(i)));

		l = nodelink_ids.size();
		for(i = 0; i < l; i++)
			nodeLinks.add(Pool.get("mapnodelinkelement", (String )nodelink_ids.get(i)));

		l = link_ids.size();
		for(i = 0; i < l; i++)
			physicalLinks.add(Pool.get("maplinkelement", (String )link_ids.get(i)));
*/
		l = path_ids.size();
		transmissionPath = new Vector();
		for(i = 0; i < l; i++)
			transmissionPath.add(Pool.get("mappathelement", (String)path_ids.get(i)));
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new ISMMapContextModel(this);
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new ISMMapContextPane();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new ISMMapContextDisplayModel();
	}
	
	public Enumeration getChildren(String key)
	{
		Vector vec = new Vector();
		if(key.equals("mappathelement"))
		{
			return transmissionPath.elements();
		}
		if(key.equals("mapkiselement"))
		{
			ObjectResource os;
			for(Enumeration enum = nodes.elements(); enum.hasMoreElements();)
			{
				os = (ObjectResource)enum.nextElement();
				if(os.getTyp().equals(key))
					vec.add(os);
			}
		}
		return vec.elements();
	}

	public Class getChildClass(String key)
    {
		if(key.equals("mappathelement"))
			return MapTransmissionPathElement.class;
		if(key.equals("mapkiselement"))
	        return MapKISNodeElement.class;
        return ObjectResource.class;
    }
		
	public Enumeration getChildTypes()
    {
		Vector vec = new Vector();
        vec.add("mappathelement");
        vec.add("mapkiselement");
		return vec.elements();
    }
		

	public void createFromPool( LogicalNetLayer logical)
	{
		super.createFromPool(logical);

		transferable = new ISMMapContext_Transferable();
	}

}


