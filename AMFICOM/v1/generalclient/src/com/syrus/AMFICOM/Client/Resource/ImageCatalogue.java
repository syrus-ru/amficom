//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ���������� ��������� ����� ���������� ��������� ����       * //
// *           (�������� ���������� ������ pmServer � ������ pmRISDImpl)  * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\ImageCatalogue.java                           * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import java.util.Enumeration;
import java.util.Hashtable;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ImageCatalogue extends Object
{
	static Hashtable hash = new Hashtable();

	static
	{
		add(
				"pc",
				new ImageResource("pc", "pc", "images/pc.gif"));
/*
		add(
				"node",
				new ImageResource("node", "node", "images/node.gif"));
		add(
				"void",
				new ImageResource("void", "void", "images/void.gif"));
		add(
				"nodelink",
				new ImageResource("nodelink", "nodelink", "images/nodelinkmode.gif"));
		add(
				"cable",
				new ImageResource("cable", "cable", "images/linkmode.gif"));
		add(
				"path",
				new ImageResource("path", "path", "images/pathmode.gif"));
*/
		add(
				"net",
				new ImageResource("net", "net", "images/net.gif"));

	}

	protected ImageCatalogue()
	{
	}
	
	protected ImageCatalogue(
			String id,
			ImageResource[] loaded_images)
	{
//		transferable.id = id;
//		transferable.loaded_images = loaded_images;
	}

	static public ImageResource get(String name)
	{
		ImageResource ir = null;
		try
		{
			ir = (ImageResource )hash.get(name);
		}
		catch(Exception e)
		{
			ir = null;
		}
		if(ir == null)
			ir = load(name);
		return ir;
	}

	static private ImageResource load(String name)
	{
		ImageResource ir = null;
		DataSourceInterface dsi = Environment.getDefaultDataSourceInterface(SessionInterface.getActiveSession());
		new DataSourceImage(dsi).LoadImages(new String[] { name });

		try
		{
			ir = (ImageResource )hash.get(name);
		}
		catch(Exception e)
		{
			ir = null;
		}

		if(ir == null)
		{
			if(name.equals("pc"))
				ir = null;
			else
			if(name.equals("net"))
				ir = get("pc");
			else
				ir = get("net");
		}
		return ir;
	}

	static public void add(String name, ImageResource ir)
	{
		hash.put(name, ir);
	}

	static public void remove(String name)
	{
		ImageResource ir = null;
		try
		{
			ir = (ImageResource )hash.get(name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		ir.free();
		hash.remove(name);
	}

	static public Enumeration getAll()
	{
		return hash.elements();
	}

	static public void removeAll()
	{
		hash.clear();
	}
}
