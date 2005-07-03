// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.repository;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxGeometry;
import com.ofx.geometry.SxGeometryInterface;
import com.ofx.geometry.SxPoint;
import com.ofx.geometry.SxPolygon;
import com.ofx.geometry.SxPolyline;
import com.ofx.geometry.SxRectangle;
import com.ofx.geometry.SxText;
import com.ofx.index.SxIndexable;
import com.ofx.index.rtree.SxSpatialIndexLeafNode;
import com.ofx.mapViewer.SxDisplayHint;
import com.ofx.mapViewer.SxDisplayable;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxRendererInterface;
import com.ofx.persistence.SxStringCondenser;
import com.ofx.projection.SxProjection;
import com.ofx.projection.SxProjectionGeoLatLong;
import com.ofx.projection.SxProjectionInterface;
import com.ofx.query.SxQueriableObjectInterface;
import com.ofx.query.SxQueryInterface;
import com.ofx.query.SxQueryRetrievalInterface;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

// Referenced classes of package com.ofx.repository:
//			SxClass, SxSymbology

public class SxSpatialObject
	implements com.ofx.index.SxIndexable, java.lang.Cloneable, java.io.Serializable, com.ofx.query.SxQueriableObjectInterface
{

	public SxSpatialObject(com.ofx.repository.SxClass sxclass)
	{
		label = "";
		setClassDefinition(sxclass);
	}

	public SxSpatialObject(com.ofx.repository.SxClass sxclass, java.lang.String s, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		label = "";
		setID(s);
		setGeometry(sxgeometryinterface);
		setClassDefinition(sxclass);
	}

	public SxSpatialObject(com.ofx.repository.SxClass sxclass, java.lang.String s)
	{
		label = "";
		setID(s);
		setClassDefinition(sxclass);
	}

	public java.lang.Object clone()
	{
		com.ofx.repository.SxSpatialObject sxspatialobject = null;
		try
		{
			sxspatialobject = new SxSpatialObject(getClassDefinition(), getID(), (com.ofx.geometry.SxGeometryInterface)geometry.clone());
			sxspatialobject.setLabel(getLabel());
		}
		catch(java.lang.CloneNotSupportedException clonenotsupportedexception)
		{
			throw new RuntimeException("SxSpatialObject.clone failed because geometry instance does not support clone. Geometry class: " + geometry.getClass().getName());
		}
		return sxspatialobject;
	}

	public java.lang.Object shallowCopy()
	{
		com.ofx.repository.SxSpatialObject sxspatialobject = null;
		try
		{
			sxspatialobject = new SxSpatialObject(getClassDefinition(), id, (com.ofx.geometry.SxGeometryInterface)geometry.shallowCopy());
			sxspatialobject.setLabel(getLabel());
			sxspatialobject.indexIntoNode = indexIntoNode;
			sxspatialobject.leafNode = leafNode;
		}
		catch(java.lang.CloneNotSupportedException clonenotsupportedexception)
		{
			throw new RuntimeException("SxSpatialObject.clone failed because geometry instance does not support clone. Geometry class: " + geometry.getClass().getName());
		}
		return sxspatialobject;
	}

	public static com.ofx.repository.SxSpatialObject createReusedSpatialObject(com.ofx.repository.SxClass sxclass)
	{
		com.ofx.repository.SxSpatialObject sxspatialobject = new SxSpatialObject(sxclass);
		sxspatialobject.setGeometry(com.ofx.repository.SxSpatialObject.createReusedGeometric(sxclass));
		return sxspatialobject;
	}

	public static com.ofx.geometry.SxGeometryInterface createReusedGeometric(com.ofx.repository.SxClass sxclass)
	{
		if(sxclass.isPolylineType())
			return new SxPolyline(new double[800], 400);
		if(sxclass.isPointType())
			return new SxPoint(1.0D, 1.0D);
		if(sxclass.isTextType())
			return new SxText(new double[4], "");
		if(sxclass.isPolygonType())
			return new SxPolygon(new double[800], 400);
		else
			throw new RuntimeException("SxUtil.createReusedSpatialObject(SxClass) unknown geometry type, not a point, polyline, or polygon.");
	}

	public boolean isInside(com.ofx.geometry.SxRectangle sxrectangle)
	{
		if(sxrectangle == null)
			return true;
		else
			return getBounds().intersects(sxrectangle);
	}

	public com.ofx.mapViewer.SxRendererInterface getRenderer()
	{
		return renderer;
	}

	public void setRenderer(com.ofx.mapViewer.SxRendererInterface sxrendererinterface)
	{
		renderer = sxrendererinterface;
	}

	public void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, int ai[], 
			int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint)
	{
		setMapLayerInterface(sxmaplayerinterface);
		com.ofx.mapViewer.SxDisplayable sxdisplayable = getDisplayable();
		if(sxdisplayable != null)
			sxdisplayable.display(g, d, sxdoublepoint, sxsymbology, this, sxmaplayerinterface, ai, ai1, sxdisplayhint);
	}

	public void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface, 
			com.ofx.projection.SxProjectionInterface sxprojectioninterface1, int ai[], int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint)
	{
		setMapLayerInterface(sxmaplayerinterface);
		com.ofx.mapViewer.SxDisplayable sxdisplayable = getDisplayable();
		if(sxdisplayable != null)
			sxdisplayable.display(g, d, sxdoublepoint, sxsymbology, this, sxmaplayerinterface, sxprojectioninterface, sxprojectioninterface1, ai, ai1, sxdisplayhint);
	}

	public double[] getLatLong(com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		com.ofx.geometry.SxGeometryInterface sxgeometryinterface = getGeometry();
		int i = sxgeometryinterface.getNPoints();
		double ad[] = new double[2 * i];
		double ad1[] = sxgeometryinterface.getXPoints();
		double ad2[] = sxgeometryinterface.getYPoints();
		for(int j = 0; j < i; j++)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(ad1[j], ad2[j]);
			com.ofx.geometry.SxDoublePoint sxdoublepoint1 = convertToLatLong(sxdoublepoint, sxprojectioninterface);
			ad[2 * j] = sxdoublepoint1.x;
			ad[2 * j + 1] = sxdoublepoint1.y;
		}

		return ad;
	}

	private com.ofx.geometry.SxDoublePoint convertToLatLong(com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		com.ofx.projection.SxProjectionGeoLatLong sxprojectiongeolatlong = new SxProjectionGeoLatLong();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(0, 0);
		try
		{
			com.ofx.projection.SxProjection.transform(sxdoublepoint, sxprojectioninterface, sxprojectiongeolatlong, sxdoublepoint1);
		}
		catch(java.lang.Exception exception)
		{
			return null;
		}
		return sxdoublepoint1;
	}

	public void setLabel(java.lang.String s)
	{
		label = s;
	}

	public java.lang.String getLabel()
	{
		return label;
	}

	public void setInternalState(java.lang.String s, java.lang.String s1, double ad[], double ad1[], int i, boolean flag)
	{
		id = s;
		label = s1;
		if(geometry.isCollection())
			geometry = com.ofx.repository.SxSpatialObject.createReusedGeometric(getClassDefinition());
		geometry.setInternalState(ad, ad1, i, flag);
	}

	public void setInternalState(int i, com.ofx.index.rtree.SxSpatialIndexLeafNode sxspatialindexleafnode, java.lang.String s, int j, int k, int ai[], com.ofx.geometry.SxDoublePoint sxdoublepoint, 
			double d, java.lang.Object aobj[])
	{
		indexIntoNode = i;
		leafNode = sxspatialindexleafnode;
		id = null;
		label = s;
		if(geometry.isCollection())
			geometry = com.ofx.repository.SxSpatialObject.createReusedGeometric(getClassDefinition());
		geometry.setInternalState(j, k, ai, sxdoublepoint, d);
	}

	public void setInternalState(int i, com.ofx.index.rtree.SxSpatialIndexLeafNode sxspatialindexleafnode, java.lang.String s, int j, int k, double ad[], java.lang.Object aobj[])
	{
		indexIntoNode = i;
		leafNode = sxspatialindexleafnode;
		id = null;
		label = s;
		if(geometry.isCollection())
			geometry = com.ofx.repository.SxSpatialObject.createReusedGeometric(getClassDefinition());
		geometry.setInternalState(j, k, ad);
	}

	public void setInternalState(com.ofx.index.SxIndexable sxindexable)
	{
		com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)sxindexable;
		if(sxindexable == null)
			return;
		if(sxspatialobject.leafNode == null)
		{
			id = (java.lang.String)sxspatialobject.getIdentifier();
		} else
		{
			indexIntoNode = sxspatialobject.indexIntoNode;
			leafNode = sxspatialobject.leafNode;
		}
		label = sxindexable.getLabel();
		com.ofx.geometry.SxGeometryInterface sxgeometryinterface = sxindexable.getGeometry();
		if(sxgeometryinterface.isCollection())
		{
			geometry = sxgeometryinterface;
		} else
		{
			if(geometry.isCollection())
				geometry = com.ofx.repository.SxSpatialObject.createReusedGeometric(getClassDefinition());
			geometry.setInternalState(sxgeometryinterface.getXPoints(), sxgeometryinterface.getYPoints(), sxgeometryinterface.getNPoints(), true);
		}
	}

	public int getTotalMemorySizeInBytes(int i, java.lang.Object aobj[], int j)
	{
		return i;
	}

	public byte[] serializeExtraAttributes(com.ofx.index.SxIndexable asxindexable[], int i, com.ofx.persistence.SxStringCondenser sxstringcondenser)
	{
		return null;
	}

	public java.lang.Object[] deserializeExtraAttributes(int i, byte abyte0[], com.ofx.persistence.SxStringCondenser sxstringcondenser)
	{
		return null;
	}

	public void setExtraAttributes(java.lang.Object obj)
	{
	}

	public void setMapLayerInterface(com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface)
	{
		mli = sxmaplayerinterface;
	}

	public com.ofx.mapViewer.SxMapLayerInterface getMapLayerInterface()
	{
		return mli;
	}

	public void setClassDefinition(com.ofx.repository.SxClass sxclass)
	{
		classDefinition = sxclass;
		classDefinitionPrim = classDefinition.getID();
	}

	public void setID(java.lang.String s)
	{
		id = s;
	}

	public void setGeometry(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		geometry = sxgeometryinterface;
	}

	public com.ofx.geometry.SxGeometryInterface getGeometry()
	{
		return geometry;
	}

	public com.ofx.mapViewer.SxDisplayable getDisplayable()
	{
		if(getRenderer() == null)
		{
			return getGeometry();
		} else
		{
			getRenderer().setGeometry(getGeometry());
			return getRenderer();
		}
	}

	public boolean isEqualByIdentifier(com.ofx.index.SxIndexable sxindexable)
	{
		return getIdentifier().toString().equals(sxindexable.getIdentifier().toString());
	}

	public int getMemorySizeInBytes()
	{
		int i = 0;
		i += getID().length();
		i += getLabel().length();
		if(getClassDefinition().isPointType())
		{
			i += 32;
		} else
		{
			i += 32 * getGeometry().getNPoints();
			i += 112;
			if(getClassDefinition().isTextType())
			{
				com.ofx.geometry.SxText sxtext = (com.ofx.geometry.SxText)getGeometry();
				i = (i += 16) + sxtext.text.length() * 2;
				i += sxtext.font_name.length() * 2;
				i += sxtext.font_params.length() * 2;
			}
		}
		i += 40;
		return i;
	}

	public int getSizeInBytes()
	{
		int i = 4;
		i += getID().length();
		i += getLabel().length();
		if(getClassDefinition().isPointType())
		{
			i += 16;
		} else
		{
			com.ofx.geometry.SxGeometry sxgeometry = (com.ofx.geometry.SxGeometry)getGeometry();
			int j = sxgeometry.getNPoints();
			i = i + 20 + 8 * j;
			if(getClassDefinition().isTextType())
				i += binaryForTextLength((com.ofx.geometry.SxText)sxgeometry);
		}
		return i;
	}

	protected int binaryForTextLength(com.ofx.geometry.SxText sxtext)
	{
		byte abyte0[] = new byte[0];
		try
		{
			java.io.ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1000);
			java.io.BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(bytearrayoutputstream, 1000);
			java.io.DataOutputStream dataoutputstream = new DataOutputStream(bufferedoutputstream);
			dataoutputstream.writeInt(sxtext.angle);
			dataoutputstream.writeUTF(sxtext.text);
			dataoutputstream.writeUTF(sxtext.font_name);
			dataoutputstream.writeUTF(sxtext.font_params);
			dataoutputstream.writeInt(sxtext.font_size);
			dataoutputstream.writeInt(sxtext.font_style);
			dataoutputstream.writeInt(sxtext.font_color.getRGB());
			dataoutputstream.flush();
			abyte0 = bytearrayoutputstream.toByteArray();
		}
		catch(java.lang.Exception exception)
		{
			log("SxSpatialIndexLeafNode.binaryForAttributes(...) exception: " + exception);
		}
		return abyte0.length;
	}

	public byte[] asBinary()
	{
		byte abyte0[] = new byte[0];
		java.io.DataOutputStream dataoutputstream = null;
		java.io.ByteArrayOutputStream bytearrayoutputstream = null;
		try
		{
			bytearrayoutputstream = new ByteArrayOutputStream();
			dataoutputstream = new DataOutputStream(bytearrayoutputstream);
			Object obj = null;
			dataoutputstream.writeUTF(getID());
			dataoutputstream.writeUTF(getLabel());
			if(getClassDefinition().isPointType())
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint = ((com.ofx.geometry.SxPoint)getGeometry()).getCenter();
				dataoutputstream.writeDouble(sxdoublepoint.x);
				dataoutputstream.writeDouble(sxdoublepoint.y);
			} else
			if(getClassDefinition().isTextType())
			{
				com.ofx.geometry.SxText sxtext = (com.ofx.geometry.SxText)getGeometry();
				dataoutputstream.writeDouble(sxtext.rectcoords[0]);
				dataoutputstream.writeDouble(sxtext.rectcoords[1]);
				dataoutputstream.writeDouble(sxtext.rectcoords[2]);
				dataoutputstream.writeDouble(sxtext.rectcoords[3]);
				dataoutputstream.writeInt(sxtext.angle);
				dataoutputstream.writeUTF(sxtext.text);
				dataoutputstream.writeUTF(sxtext.font_name);
				dataoutputstream.writeUTF(sxtext.font_params);
				dataoutputstream.writeInt(sxtext.font_size);
				dataoutputstream.writeInt(sxtext.font_style);
				dataoutputstream.writeInt(sxtext.font_color.getRGB());
			} else
			{
				com.ofx.geometry.SxPolyline sxpolyline = (com.ofx.geometry.SxPolyline)getGeometry();
				int i = sxpolyline.getNPoints();
				double ad[] = sxpolyline.getXPoints();
				double ad1[] = sxpolyline.getYPoints();
				dataoutputstream.writeInt(i);
				float af[] = new float[i];
				boolean flag = false;
				com.ofx.geometry.SxDoublePoint sxdoublepoint1 = sxpolyline.getCenter();
				dataoutputstream.writeDouble(sxdoublepoint1.x);
				dataoutputstream.writeDouble(sxdoublepoint1.y);
				for(int j = 0; j < i; j++)
					dataoutputstream.writeFloat((float)(ad[j] = sxdoublepoint1.x));

				for(int k = 0; k < i; k++)
					dataoutputstream.writeFloat((float)(ad1[k] = sxdoublepoint1.y));

			}
			abyte0 = bytearrayoutputstream.toByteArray();
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		finally
		{
			try
			{
				dataoutputstream.close();
				bytearrayoutputstream.close();
			}
			catch(java.io.IOException ioexception)
			{
				log(ioexception);
			}
		}
		return abyte0;
	}

	public java.lang.String getID()
	{
		if(id == null && leafNode != null)
			id = leafNode.getIds()[indexIntoNode];
		return id;
	}

	public com.ofx.repository.SxClass getClassDefinition()
	{
		if(classDefinition == null)
		{
			java.io.StringWriter stringwriter = new StringWriter();
			java.io.PrintWriter printwriter = new PrintWriter(stringwriter);
			(new Exception(getClass().getName() + ".getClassDefinition() classDefinition instance variable is null. Stack trace")).printStackTrace(printwriter);
			printwriter.flush();
			log(stringwriter.toString());
			throw new RuntimeException(getClass().getName() + ".getClassDefinition() classDefinition instance variable is null.");
		} else
		{
			return classDefinition;
		}
	}

	public com.ofx.repository.SxClass getClassDefinition(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(classDefinition == null)
			try
			{
				classDefinition = (com.ofx.repository.SxClass)sxqueryinterface.retrieve(com.ofx.repository.SxClass.getInstanceQueryClassification(), classDefinitionPrim);
			}
			catch(java.lang.Exception exception)
			{
				log(exception);
			}
		return classDefinition;
	}

	public java.lang.String getSpatialClassName()
	{
		com.ofx.repository.SxClass sxclass = getClassDefinition();
		java.lang.String s;
		if(sxclass != null)
			s = sxclass.getID();
		else
			s = new String();
		return s;
	}

	public com.ofx.geometry.SxRectangle getBounds()
	{
		return getGeometry().getBounds();
	}

	public java.awt.Rectangle boundsRounded()
	{
		return getBounds().rounded();
	}

	public static java.lang.String getInstanceQueryClassification()
	{
		return null;
	}

	public java.lang.Object getIdentifier()
	{
		return getID();
	}

	public void setIdentifier(java.lang.Object obj)
	{
		setID((java.lang.String)obj);
	}

	public java.lang.String getQueryClassification()
	{
		return getClassDefinition().getClassificationName();
	}

	public java.lang.String getQueryClassification(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		return getClassDefinition(sxqueryinterface).getClassificationName();
	}

	public boolean isSpatial()
	{
		return true;
	}

	public void resolveSoftLinks(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		resolveClassDefinition(sxqueryinterface);
	}

	protected void resolveClassDefinition(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(classDefinition == null)
		{
			try
			{
				classDefinition = (com.ofx.repository.SxClass)sxqueryinterface.retrieve(com.ofx.repository.SxClass.getInstanceQueryClassification(), classDefinitionPrim);
			}
			catch(java.lang.Exception exception)
			{
				log("SxSpatialObject.resolveClassDefinition() ex: " + exception);
				throw new RuntimeException("SxSpatialObject.resolveClassDefinition() could not find SxClass named: " + classDefinitionPrim + " due to exception: " + exception);
			}
			if(classDefinition == null)
				throw new RuntimeException("SxSpatialObject.resolveClassDefinition() could not find SxClass named: " + classDefinitionPrim);
		}
	}

	public void log(java.lang.String s)
	{
		com.ofx.base.SxEnvironment.log().println(s);
	}

	public void log(java.lang.Exception exception)
	{
		com.ofx.base.SxEnvironment.log().println(exception);
	}

	private void writeObject(java.io.ObjectOutputStream objectoutputstream)
		throws java.io.IOException
	{
		if(leafNode != null)
			getID();
		objectoutputstream.writeInt(0x10000);
		objectoutputstream.defaultWriteObject();
	}

	private void readObject(java.io.ObjectInputStream objectinputstream)
		throws java.io.IOException, java.lang.ClassNotFoundException
	{
		int i = objectinputstream.readInt();
		if(i != 0x10000)
		{
			throw new RuntimeException(getClass().getName() + ".readObject: " + " expected OFX_CLASS_VERSION of " + 0x10000 + " got " + i);
		} else
		{
			objectinputstream.defaultReadObject();
			return;
		}
	}

	public static final java.lang.String SPATIALCLASSPREFIX = "OfxS";
	private static final int APPROXIMATE_DOUBLE_POINT_SIZE = 32;
	private static final int APPROXIMATE_SPATIAL_OBJECT_OVERHEAD_SIZE = 40;
	private static final int APPROXIMATE_POLYLINE_OVERHEAD_SIZE = 112;
	private static final int OFX_CLASS_VERSION = 0x10000;
	private com.ofx.mapViewer.SxMapLayerInterface mli;
	private com.ofx.mapViewer.SxRendererInterface renderer;
	protected transient int indexIntoNode;
	protected transient com.ofx.index.rtree.SxSpatialIndexLeafNode leafNode;
	public java.lang.String id;
	public com.ofx.geometry.SxGeometryInterface geometry;
	public com.ofx.repository.SxClass classDefinition;
	private java.lang.String classDefinitionPrim;
	private java.lang.String label;
}
