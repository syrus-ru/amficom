/*
 * $Id: ImageResource.java,v 1.4 2004/09/27 16:08:06 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import java.awt.*;
import java.io.*;
import java.util.Hashtable;
import javax.swing.ImageIcon;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 16:08:06 $
 * @module generalclient_v1
 */
public class ImageResource extends StubResource
		implements Serializable
{
	private static final long serialVersionUID = 01L;
	public ImageResource_Transferable transferable;

	String id;
	String name;
	Image image;
	String source_string;
	Object source;
	boolean loaded = false;
	static final public String typ = "imageresource";	// тип ресурса объекта, представляет собой

//	MediaTracker m_tracker;
		protected final static Component component = new Component() {};
		protected final static MediaTracker m_tracker = new MediaTracker(component);

	public ImageResource()
	{
	}

	public ImageResource(ImageResource_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public ImageResource(String id, String name, String filename)
	{
		this.id = id;
		this.name = name;
		this.source_string = "file";
		this.source = filename;

			ImageIcon myImageIcon = new ImageIcon(filename);
		image = myImageIcon.getImage();

//		image = Toolkit.getDefaultToolkit().getImage(filename);
		loaded = true;

		transferable = new ImageResource_Transferable();
	}

	public ImageResource(
			String id,
			String name,
			byte []image_bytes)
	{
		this.id = id;
		this.name = name;
		this.source_string = "bytes";
		this.source = image_bytes;
		image = Toolkit.getDefaultToolkit().createImage(image_bytes);
		loaded = true;

		transferable = new ImageResource_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		source_string = transferable.source_string;
		if(source_string.equals("bytes"))
			this.source = transferable.image;
		else
		if(source_string.equals("file"))
			this.source = transferable.filename;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.source_string = source_string;
		transferable.filename = "";
		transferable.image = new byte[0];
		if(source_string.equals("bytes"))
			transferable.image = (byte [])this.source;
		else
		if(source_string.equals("file"))
			transferable.filename = (String )this.source;
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public Hashtable getColumns()
	{
		return new Hashtable();
	}

	public int getColumnSize(String col_id)
	{
		return 100;
	}

	public String getValue(String col_id)
	{
		return "leer";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	void loadResource()
	{
		if(source_string.equals("bytes"))
			image = Toolkit.getDefaultToolkit().createImage((byte[])source);
		else
		if(source_string.equals("file"))
		{
//		    ImageIcon myImageIcon = new ImageIcon((String)source);
//			image = myImageIcon.getImage();
			image = Toolkit.getDefaultToolkit().getImage((String)source);
		}
		m_tracker.addImage(image, 0);
		try { m_tracker.waitForID(0); } catch(Exception e) {}
		loaded = true;
	}

	public Image getImage(Component comp)
	{
		if(comp == null)
			return getImage();
		if(!loaded)
			loadResource();
//		m_tracker = new MediaTracker(comp);
//		m_tracker.addImage(image,0);
//		try{ m_tracker.waitForID(0); } catch(Exception e){}
		return image;
	}

	public static Image preparedImage(Component comp, Image img)
	{
		if(comp == null)
			return img;
		MediaTracker m_tracker = new MediaTracker(comp);
		m_tracker.addImage(img,0);
		try{ m_tracker.waitForID(0); } catch(Exception e){}
		return img;
	}

	public Image getImage()
	{
		if(!loaded)
			loadResource();
		return image;
	}

	public void free()
	{
		image.flush();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(source_string);
		out.writeObject(source);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		source_string = (String )in.readObject();
		source = in.readObject();

		transferable = new ImageResource_Transferable();
	}
}
