// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.base;

import com.ofx.geometry.*;
import com.ofx.geometry.SxGeometryCollection;
import com.ofx.geometry.SxMultiPoint;
import com.ofx.geometry.SxMultiPolygon;
import com.ofx.geometry.SxMultiPolyline;
import com.ofx.geometry.SxPoint;
import com.ofx.geometry.SxPolygon;
import com.ofx.geometry.SxPolyline;
import com.ofx.index.SxIndexable;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxSpatialObject;
import java.applet.Applet;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Vector;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

// Referenced classes of package com.ofx.base:
//			SxEnvironment, SxLogInterface

public class SxUtil
{

	public SxUtil()
	{
	}

	public static void wait(int i)
	{
		try
		{
			java.lang.Thread.currentThread();
			java.lang.Thread.sleep(i * 1000);
		}
		catch(java.lang.InterruptedException interruptedexception)
		{
			com.ofx.base.SxUtil.env();
			com.ofx.base.SxEnvironment.log().println(interruptedexception);
		}
	}

	public static java.lang.String formatReportLine(java.util.Vector vector, java.util.Vector vector1)
	{
		int i = 1;
		java.lang.String s = "                                                                                                                                       ";
		java.lang.String s1 = "";
		for(int j = 0; j < vector.size(); j++)
		{
			int k = ((java.lang.Integer)vector.elementAt(j)).intValue();
			java.lang.Object obj = " ";
			if(j < vector1.size())
				obj = vector1.elementAt(j);
			int l = k - i - 1;
			java.lang.String s2 = "";
			if(l > 0)
				s2 = s.substring(0, l);
			s1 = s1 + s2;
			s1 = s1 + obj.toString();
			i = s1.length();
		}

		return s1;
	}

	public static java.lang.String copyReplaceAll(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		int i = s.indexOf(s1, 0);
		if(i < 0)
			return new String(s);
		java.lang.String s3 = s.substring(0, i) + s2;
		int j = i + s1.length();
		while((i = s.indexOf(s1, i + s1.length())) > 0) 
		{
			s3 = s3 + s.substring(j, i) + s2;
			if(i > 0)
				j = i + s1.length();
		}
		if(j <= s.length())
			s3 = s3 + s.substring(j, s.length());
		return s3;
	}

	public static java.util.Vector substringsDelimitedBy(java.lang.String s, char c, java.lang.Character character)
	{
		java.util.Vector vector = new Vector();
		char c1 = '\0';
		if(character != null)
			c1 = character.charValue();
		java.lang.String s1 = s;
		if(c == ' ')
			s1 = s1.trim();
		Object obj = null;
		int i = s1.length();
		boolean flag = false;
		for(int k = 0; k <= i;)
		{
			while(k < i && s1.charAt(k) == ' ') 
				k++;
			int j = k;
			java.lang.String s2;
			if(k == i)
			{
				s2 = "";
				k++;
			} else
			if(c == s1.charAt(k))
			{
				s2 = "";
				k++;
			} else
			if(s1.charAt(k) == c1)
			{
				j = k + 1;
				if((k = s1.indexOf(c1, k + 1)) == -1)
					k = i;
				s2 = s1.substring(j, k);
				k = s1.indexOf(c, k);
				if(k == -1)
					k = i;
				k++;
			} else
			{
				k = s1.indexOf(c, j);
				if(k == -1)
					k = i;
				s2 = s1.substring(j, k);
				k++;
			}
			vector.addElement(s2);
		}

		return vector;
	}

	public static java.awt.Image loadImage(java.lang.String s, java.lang.Object obj)
	{
		return com.ofx.base.SxUtil.loadImage(s, obj, "/com/ofx/component/");
	}

	public static java.awt.Image loadImage(java.lang.String s, java.lang.Object obj, boolean flag)
	{
		return com.ofx.base.SxUtil.loadImage(s, obj, "/com/ofx/component/", flag);
	}

	public static java.awt.Image loadImage(java.lang.String s, java.lang.Object obj, java.lang.String s1)
	{
		return com.ofx.base.SxUtil.loadImage(s, obj, s1, false);
	}

	public static java.awt.Image loadImage(java.lang.String s, java.lang.Object obj, java.lang.String s1, boolean flag)
	{
		try
		{
			java.net.URL url = obj.getClass().getResource(s);
			java.lang.String s2 = url.getHost();
			java.awt.Image image;
			if(s2 != null)
			{
				image = java.awt.Toolkit.getDefaultToolkit().getImage(url);
			} else
			{
				java.lang.Object obj1 = url.getContent();
				image = java.awt.Toolkit.getDefaultToolkit().createImage((java.awt.image.ImageProducer)obj1);
			}
			if(flag)
			{
				java.awt.MediaTracker mediatracker = new MediaTracker(com.ofx.base.SxUtil.imageLoaderFrame);
				mediatracker.addImage(image, 0);
				try
				{
					mediatracker.waitForAll();
				}
				catch(java.lang.InterruptedException interruptedexception) { }
			}
			return image;
		}
		catch(java.lang.Exception exception)
		{
			return com.ofx.base.SxUtil.loadImageNoGetResource(s, s1, flag);
		}
	}

	public static java.awt.Image loadImageNoGetResource(java.lang.String s, java.lang.String s1, boolean flag)
	{
		java.awt.Image image = null;
		if(com.ofx.base.SxUtil.env().isSpatialFXApplet())
		{
			java.lang.String s2 = s1;
			if(s1 == null)
				s2 = "";
			java.lang.String s3 = com.ofx.base.SxUtil.env().getCodeBase().toString();
			if(s2.startsWith("/") && s3.endsWith("/"))
				s3 = s3.substring(0, s3.length() - 1);
			s3 = s3 + s2;
			if(s.startsWith("/") && s3.endsWith("/"))
				s3 = s3.substring(0, s3.length() - 1);
			s3 = s3 + s;
			try
			{
				java.net.URL url = new URL(s3);
				image = com.ofx.base.SxUtil.env().getSpatialFXApplet().getImage(url);
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxUtil.env();
				com.ofx.base.SxEnvironment.log().println("SxUtil.loadImageNoGetResource() exception: " + exception);
			}
		} else
		{
			throw new RuntimeException("Cannot load image named '" + s + "'");
		}
		if(flag && image != null)
		{
			java.awt.MediaTracker mediatracker = new MediaTracker(com.ofx.base.SxUtil.imageLoaderFrame);
			mediatracker.addImage(image, 0);
			try
			{
				mediatracker.waitForAll();
			}
			catch(java.lang.InterruptedException interruptedexception) { }
		}
		return image;
	}

	public static byte[] compress(byte abyte0[])
	{
		byte abyte1[] = null;
		try
		{
			java.io.ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			java.util.zip.DeflaterOutputStream deflateroutputstream = new DeflaterOutputStream(bytearrayoutputstream);
			deflateroutputstream.write(abyte0);
			deflateroutputstream.close();
			bytearrayoutputstream.flush();
			abyte1 = bytearrayoutputstream.toByteArray();
			bytearrayoutputstream.close();
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println("SxUtil.compress() failed with exception: " + exception);
		}
		return abyte1;
	}

	public static java.util.Vector uncompress(byte abyte0[], int i)
	{
		java.util.Vector vector = new Vector(2);
		int j = abyte0.length;
		int k = j * 10;
		int l = (int)((double)k * 0.25D);
		int j1 = k - (l + 10);
		if(i > 0)
		{
			int i1 = i;
			k = i;
			int k1 = k;
		}
		byte abyte1[] = new byte[k];
		try
		{
			java.io.ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			java.util.zip.InflaterInputStream inflaterinputstream = new InflaterInputStream(bytearrayinputstream);
			int l1 = 0;
			int i2 = 0;
			while(l1 < k) 
			{
				int j2 = inflaterinputstream.read(abyte1, i2 + l1, k - l1);
				if(j2 > 0)
					l1 = j2 + l1;
				else
					l1 = k;
			}
			inflaterinputstream.close();
			bytearrayinputstream.close();
			vector.addElement(new Integer(l1));
		}
		catch(java.lang.Exception exception)
		{
			exception.printStackTrace();
			com.ofx.base.SxEnvironment.log().println("SxUtil.uncompress() exception=" + exception);
			vector.addElement(new Integer(abyte0.length));
			vector.addElement(abyte0);
			return vector;
		}
		vector.addElement(abyte1);
		return vector;
	}

	public static byte[] binaryForObject(java.lang.Object obj, boolean flag)
	{
		byte abyte0[] = new byte[0];
		java.io.ObjectOutputStream objectoutputstream = null;
		java.io.ByteArrayOutputStream bytearrayoutputstream = null;
		java.util.zip.GZIPOutputStream gzipoutputstream = null;
		try
		{
			if(flag)
			{
				try
				{
					bytearrayoutputstream = new ByteArrayOutputStream();
					gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream);
					objectoutputstream = new ObjectOutputStream(gzipoutputstream);
					objectoutputstream.writeObject(obj);
					objectoutputstream.close();
					gzipoutputstream.close();
					abyte0 = bytearrayoutputstream.toByteArray();
				}
				catch(java.io.IOException ioexception)
				{
					try
					{
						if(objectoutputstream != null)
							objectoutputstream.close();
						if(gzipoutputstream != null)
							gzipoutputstream.close();
						bytearrayoutputstream.close();
					}
					catch(java.io.IOException ioexception1) { }
					bytearrayoutputstream = new ByteArrayOutputStream();
					objectoutputstream = new ObjectOutputStream(bytearrayoutputstream);
					objectoutputstream.writeObject(obj);
					abyte0 = bytearrayoutputstream.toByteArray();
				}
			} else
			{
				bytearrayoutputstream = new ByteArrayOutputStream();
				objectoutputstream = new ObjectOutputStream(bytearrayoutputstream);
				objectoutputstream.writeObject(obj);
				abyte0 = bytearrayoutputstream.toByteArray();
			}
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		finally
		{
			try
			{
				if(bytearrayoutputstream != null)
					bytearrayoutputstream.close();
				if(gzipoutputstream != null)
					gzipoutputstream.close();
				if(objectoutputstream != null)
					objectoutputstream.close();
			}
			catch(java.io.IOException ioexception2)
			{
				com.ofx.base.SxEnvironment.log().println(ioexception2);
			}
		}
		return abyte0;
	}

	public static java.lang.Object objectFromBinary(byte abyte0[], boolean flag)
	{
		java.lang.Object obj = null;
		if(abyte0.length > 0)
		{
			java.io.ObjectInputStream objectinputstream = null;
			java.io.ByteArrayInputStream bytearrayinputstream = null;
			try
			{
				if(flag)
				{
					java.util.zip.GZIPInputStream gzipinputstream = null;
					try
					{
						bytearrayinputstream = new ByteArrayInputStream(abyte0);
						gzipinputstream = new GZIPInputStream(bytearrayinputstream);
						objectinputstream = new ObjectInputStream(gzipinputstream);
						obj = objectinputstream.readObject();
					}
					catch(java.io.IOException ioexception)
					{
						try
						{
							if(objectinputstream != null)
								objectinputstream.close();
							if(gzipinputstream != null)
								gzipinputstream.close();
							bytearrayinputstream.close();
						}
						catch(java.io.IOException ioexception1) { }
						bytearrayinputstream = new ByteArrayInputStream(abyte0);
						objectinputstream = new ObjectInputStream(bytearrayinputstream);
						obj = objectinputstream.readObject();
					}
				} else
				{
					bytearrayinputstream = new ByteArrayInputStream(abyte0);
					objectinputstream = new ObjectInputStream(bytearrayinputstream);
					obj = objectinputstream.readObject();
				}
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log().println(exception);
			}
			finally
			{
				try
				{
					objectinputstream.close();
					bytearrayinputstream.close();
				}
				catch(java.io.IOException ioexception2)
				{
					com.ofx.base.SxEnvironment.log().println(ioexception2);
				}
			}
		}
		return obj;
	}

	public static com.ofx.index.SxIndexable createSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[], double ad1[], int i, boolean flag, 
			double ad2[][], boolean aflag[], int j, java.lang.Object obj)
	{
		Object obj1 = null;
		if(sxclass.isPointType())
			if(flag)
				return com.ofx.base.SxUtil.createMultiPointSpatialObjectFrom(sxclass, class1, s, s1, ad2, j, obj);
			else
				return com.ofx.base.SxUtil.createPointSpatialObjectFrom(sxclass, class1, s, s1, ad, ad1, obj);
		if(sxclass.isPolylineType())
			if(flag)
				return com.ofx.base.SxUtil.createMultiPolylineSpatialObjectFrom(sxclass, class1, s, s1, ad2, j, obj);
			else
				return com.ofx.base.SxUtil.createPolylineSpatialObjectFrom(sxclass, class1, s, s1, ad, ad1, i, obj);
		if(flag)
			return com.ofx.base.SxUtil.createMultiPolygonSpatialObjectFrom(sxclass, class1, s, s1, ad2, aflag, j, obj);
		else
			return com.ofx.base.SxUtil.createPolygonSpatialObjectFrom(sxclass, class1, s, s1, ad, ad1, i, obj);
	}

	public static com.ofx.index.SxIndexable createPointSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[], double ad1[], java.lang.Object obj)
	{
		java.lang.Object obj1 = null;
		com.ofx.geometry.SxPoint sxpoint = new SxPoint(new SxDoublePoint(ad[0], ad1[0]));
		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxpoint);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createPointSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxpoint);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createTextSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[], double ad1[], java.lang.Object obj)
	{
		java.lang.Object obj1 = null;
		com.ofx.geometry.SxText sxtext = new SxText(ad, ad1, s1);
		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxtext);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createTextSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxtext);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createMultiPointSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[][], int i, java.lang.Object obj)
	{
		java.lang.Object obj1 = null;
		Object obj2 = null;
		com.ofx.geometry.SxMultiPoint sxmultipoint = new SxMultiPoint();
		Object obj3 = null;
		for(int j = 0; j < i; j++)
		{
			double ad1[] = ad[j];
			com.ofx.geometry.SxPoint sxpoint = new SxPoint(new SxDoublePoint(ad1[0], ad1[1]));
			sxmultipoint.addGeometry(sxpoint);
		}

		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxmultipoint);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createMultiPointSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxmultipoint);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createNonUserDefinedPointSpatialObjectFrom(java.lang.Class class1, com.ofx.repository.SxClass sxclass, java.lang.String s, java.lang.String s1, double ad[], double ad1[])
	{
		com.ofx.index.SxIndexable sxindexable = null;
		try
		{
			sxindexable = (com.ofx.index.SxIndexable)class1.newInstance();
		}
		catch(java.lang.Exception exception)
		{
			throw new RuntimeException("SxUtil.createNonUserDefinedPointSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
		}
		sxindexable.setIdentifier(s);
		sxindexable.setLabel(s1);
		sxindexable.setClassDefinition(sxclass);
		com.ofx.geometry.SxPoint sxpoint = new SxPoint(new SxDoublePoint(ad[0], ad1[0]));
		sxindexable.setGeometry(sxpoint);
		return sxindexable;
	}

	public static com.ofx.index.SxIndexable createNonUserDefinedTextSpatialObjectFrom(java.lang.Class class1, com.ofx.repository.SxClass sxclass, java.lang.String s, java.lang.String s1, double ad[], double ad1[])
	{
		com.ofx.index.SxIndexable sxindexable = null;
		try
		{
			sxindexable = (com.ofx.index.SxIndexable)class1.newInstance();
		}
		catch(java.lang.Exception exception)
		{
			throw new RuntimeException("SxUtil.createNonUserDefinedTextSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
		}
		sxindexable.setIdentifier(s);
		sxindexable.setLabel(s1);
		sxindexable.setClassDefinition(sxclass);
		com.ofx.geometry.SxText sxtext = new SxText(ad, ad1, s1);
		sxindexable.setGeometry(sxtext);
		return sxindexable;
	}

	public static com.ofx.index.SxIndexable createPolylineSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[], double ad1[], int i, java.lang.Object obj)
	{
		com.ofx.geometry.SxPolyline sxpolyline = new SxPolyline(ad, ad1, i);
		java.lang.Object obj1 = null;
		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxpolyline);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createPolylineSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxpolyline);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createMultiPolylineSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[][], int i, java.lang.Object obj)
	{
		java.lang.Object obj1 = null;
		Object obj2 = null;
		com.ofx.geometry.SxMultiPolyline sxmultipolyline = new SxMultiPolyline();
		Object obj3 = null;
		boolean flag = false;
		for(int k = 0; k < i; k++)
		{
			double ad1[] = ad[k];
			int j = (int)((double)ad1.length * 0.5D);
			com.ofx.geometry.SxPolyline sxpolyline = new SxPolyline(ad1, j);
			sxmultipolyline.addGeometry(sxpolyline, false);
		}

		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxmultipolyline);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createMultiPolylineSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxmultipolyline);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createNonUserDefinedPolylineSpatialObjectFrom(java.lang.Class class1, com.ofx.repository.SxClass sxclass, java.lang.String s, java.lang.String s1, double ad[], double ad1[], int i)
	{
		com.ofx.index.SxIndexable sxindexable = null;
		try
		{
			sxindexable = (com.ofx.index.SxIndexable)class1.newInstance();
		}
		catch(java.lang.Exception exception)
		{
			throw new RuntimeException("SxUtil.createNonUserDefinedPolylineSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
		}
		sxindexable.setIdentifier(s);
		sxindexable.setLabel(s1);
		sxindexable.setClassDefinition(sxclass);
		com.ofx.geometry.SxPolyline sxpolyline = new SxPolyline(ad, ad1, i);
		sxindexable.setGeometry(sxpolyline);
		return sxindexable;
	}

	public static com.ofx.index.SxIndexable createPolygonSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[], double ad1[], int i, java.lang.Object obj)
	{
		java.lang.Object obj1 = null;
		com.ofx.geometry.SxPolygon sxpolygon = new SxPolygon(ad, ad1, i);
		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxpolygon);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createPolygonSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxpolygon);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createMultiPolygonSpatialObjectFrom(com.ofx.repository.SxClass sxclass, java.lang.Class class1, java.lang.String s, java.lang.String s1, double ad[][], boolean aflag[], int i, java.lang.Object obj)
	{
		java.lang.Object obj1 = null;
		Object obj2 = null;
		com.ofx.geometry.SxMultiPolygon sxmultipolygon = new SxMultiPolygon();
		Object obj3 = null;
		boolean flag = false;
		for(int k = 0; k < i; k++)
		{
			double ad1[] = ad[k];
			int j = (int)((double)ad1.length * 0.5D);
			com.ofx.geometry.SxPolygon sxpolygon = new SxPolygon(ad1, j);
			sxmultipolygon.addGeometry(sxpolygon, aflag[k]);
		}

		if(sxclass.getIsUserDefined())
		{
			obj1 = new SxSpatialObject(sxclass, s, sxmultipolygon);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
		} else
		{
			try
			{
				obj1 = (com.ofx.index.SxIndexable)class1.newInstance();
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxUtil.createMultiPolygonSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
			}
			((com.ofx.index.SxIndexable) (obj1)).setIdentifier(s);
			((com.ofx.index.SxIndexable) (obj1)).setLabel(s1);
			((com.ofx.index.SxIndexable) (obj1)).setClassDefinition(sxclass);
			((com.ofx.index.SxIndexable) (obj1)).setGeometry(sxmultipolygon);
			((com.ofx.index.SxIndexable) (obj1)).setExtraAttributes(obj);
		}
		return ((com.ofx.index.SxIndexable) (obj1));
	}

	public static com.ofx.index.SxIndexable createNonUserDefinedPolygonSpatialObjectFrom(java.lang.Class class1, com.ofx.repository.SxClass sxclass, java.lang.String s, java.lang.String s1, double ad[], double ad1[], int i)
	{
		com.ofx.index.SxIndexable sxindexable = null;
		try
		{
			sxindexable = (com.ofx.index.SxIndexable)class1.newInstance();
		}
		catch(java.lang.Exception exception)
		{
			throw new RuntimeException("SxUtil.createNonUserDefinedPolygonSpatialObjectFrom() e: " + exception + " for class: " + class1.getName());
		}
		sxindexable.setIdentifier(s);
		sxindexable.setLabel(s1);
		sxindexable.setClassDefinition(sxclass);
		com.ofx.geometry.SxPolygon sxpolygon = new SxPolygon(ad, ad1, i);
		sxindexable.setGeometry(sxpolygon);
		return sxindexable;
	}

	public static com.ofx.base.SxEnvironment env()
	{
		return com.ofx.base.SxEnvironment.singleton();
	}

	public static java.awt.Frame imageLoaderFrame = new Frame("SxUtil");

}
