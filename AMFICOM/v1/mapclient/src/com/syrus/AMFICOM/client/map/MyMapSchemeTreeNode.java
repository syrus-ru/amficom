package com.syrus.AMFICOM.Client.Configure.Map;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.UI.*;

public class MyMapSchemeTreeNode extends DefaultMutableTreeNode
{
	ObjectResource or;
	String name;
	String id;

	MapSchemeElementLabel elementLabel = null;
	
	public MyMapSchemeTreeNode(ObjectResource or)
	{
		super(or.getName());
		this.or = or;
		this.name = null;
		this.id = null;

		if(or instanceof SchemeElement)
		{
			SchemeElement se = (SchemeElement )or;
			if(se.scheme_id != null && !se.scheme_id.equals(""))
			{
				MapProtoElement mpe = (MapProtoElement )Pool.get(MapProtoElement.typ, "map_scheme_proto");
				if(mpe != null)
				{
					ImageIcon ii = new ImageIcon(mpe.get_image().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					elementLabel = new MapSchemeElementLabel(ii, se);
					se.mpe = mpe;
				}
			}
			else
			{
				Hashtable ht = Pool.getHash(MapProtoElement.typ);
				if(ht != null)
				for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
				{
					MapProtoElement mpe = (MapProtoElement )enum.nextElement();
					if(mpe.pe_ids.contains(se.proto_element_id))
					{
						ImageIcon ii = new ImageIcon(mpe.get_image().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
						elementLabel = new MapSchemeElementLabel(ii, se);
						se.mpe = mpe;
						break;
					}
				}
			}
		}
		else
		if(or instanceof SchemeCableLink)
		{
			SchemeCableLink se = (SchemeCableLink )or;

			Hashtable ht = Pool.getHash(MapPhysicalLinkProtoElement.typ);
			if(ht != null)
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				MapPhysicalLinkProtoElement mplpe = (MapPhysicalLinkProtoElement )enum.nextElement();
				if(mplpe.cabletype_ids.contains(se.cable_link_type_id))
				{
//					ImageIcon ii = new ImageIcon(ImageCatalogue.get("cable").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					ImageIcon ii = new ImageIcon("images/linkmode.gif");
					elementLabel = new MapSchemeElementLabel(ii, se);
					se.mplpe = mplpe;
					break;
				}
			}
		}
		else
		if(or instanceof SchemePath)
		{
			SchemePath se = (SchemePath )or;

			Hashtable ht = Pool.getHash(MapTransmissionPathProtoElement.typ);
			if(ht != null)
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				MapTransmissionPathProtoElement mtppe = (MapTransmissionPathProtoElement )enum.nextElement();
				if(mtppe.pathtype_ids.contains(se.type_id))
				{
//					ImageIcon ii = new ImageIcon(ImageCatalogue.get("path").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					ImageIcon ii = new ImageIcon("images/pathmode.gif");
					elementLabel = new MapSchemeElementLabel(ii, se);
					se.mtppe = mtppe;
					break;
				}
			}
		}
	}

	public MyMapSchemeTreeNode(String name, String id)
	{
		super(name);
		this.name = name;
		this.id = id;
		this.or = null;
	}

	public String getName()
	{
		if(or == null)
			return name;
		return or.getName();
	}

	public Image getImage()
	{
		if(or == null)
			return null;

		if(! (or instanceof SchemeElement))
			return null;

		SchemeElement se = (SchemeElement )or;
		return se.mpe.get_image();
/*		
		for(Enumeration enum = Pool.getHash(MapProtoElement.typ).elements(); enum.hasMoreElements();)
		{
			MapProtoElement mpe = (MapProtoElement )enum.nextElement();
			if(mpe.pe_ids.contains(se.proto_element_id))
			{
				return mpe.get_image();
			}
		}
		return null;
*/
	}

}