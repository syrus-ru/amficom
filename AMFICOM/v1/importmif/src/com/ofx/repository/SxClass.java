// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.repository;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.geometry.SxGeometryInterface;
import com.ofx.geometry.SxRectangle;
import com.ofx.mapViewer.SxRendererInterface;
import com.ofx.query.SxQueriableObjectInterface;
import com.ofx.query.SxQueryAdministrationInterface;
import com.ofx.query.SxQueryInterface;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.query.SxQueryRetrievalInterface;
import com.ofx.query.SxQuerySessionInterface;
import com.ofx.query.SxQueryTransactionInterface;
import com.ofx.server.SxRequestInterface;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ofx.repository:
//			SxRepositoryException, SxLabelTable, SxSymbology, SxTextSpec, 
//			SxDataSource, SxExternalDataDef, SxRepositoryUtil

public class SxClass
	implements java.io.Serializable, com.ofx.query.SxQueriableObjectInterface
{

	public static com.ofx.repository.SxClass getClassDefinition(java.lang.String s, com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		return (com.ofx.repository.SxClass)sxqueryinterface.retrieve(com.ofx.repository.SxClass.getInstanceQueryClassification(), s);
	}

	public static void setRenderer(java.lang.String s, com.ofx.mapViewer.SxRendererInterface sxrendererinterface, com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		com.ofx.repository.SxClass.retrieve(s, sxqueryinterface).setRenderer(sxrendererinterface);
	}

	public com.ofx.mapViewer.SxRendererInterface getRenderer()
	{
		return renderer;
	}

	public void setRenderer(com.ofx.mapViewer.SxRendererInterface sxrendererinterface)
	{
		renderer = sxrendererinterface;
	}

	public static com.ofx.repository.SxClass create(java.lang.String s, java.lang.String s1, java.lang.String s2, com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		com.ofx.repository.SxClass sxclass = new SxClass(s, s1, s2, true, sxqueryinterface);
		sxqueryinterface.makePersistent(sxclass);
		sxqueryinterface.removeSpatialClassification(s);
		sxqueryinterface.removeSpatialIndexOn(s);
		sxqueryinterface.addSpatialClassification(s, sxclass.getGeomType());
		sxqueryinterface.addSpatialIndexOn(s);
		return sxclass;
	}

	public static boolean delete(java.lang.String s, com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		com.ofx.repository.SxClass sxclass = (com.ofx.repository.SxClass)sxqueryinterface.retrieve(com.ofx.repository.SxClass.getInstanceQueryClassification(), s);
		if(sxclass != null)
			return sxclass.delete(sxqueryinterface);
		else
			return false;
	}

	public static java.util.Vector objects(com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		java.lang.String s = com.ofx.repository.SxClass.getInstanceQueryClassification();
		try
		{
			return sxqueryinterface.getObjects(s).asVector();
		}
		catch(java.lang.Exception exception)
		{
			throw new SxRepositoryException("objects() classification: " + s + " exception: " + exception);
		}
	}

	public static java.util.Enumeration objectIDs(com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		java.lang.String s = com.ofx.repository.SxClass.getInstanceQueryClassification();
		java.util.Vector vector = null;
		try
		{
			vector = sxqueryinterface.getObjects(s).asVector();
		}
		catch(java.lang.Exception exception)
		{
			throw new SxRepositoryException("objectIDs() classification: " + s + " exception: " + exception);
		}
		return com.ofx.repository.SxRepositoryUtil.sortByIdentifier(vector);
	}

	public static java.lang.String getInstanceQueryClassification()
	{
		return com.ofx.repository.SxClass.QUERYCLASSIFICATION;
	}

	public static com.ofx.repository.SxClass retrieve(java.lang.String s, com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		java.lang.String s1 = com.ofx.repository.SxClass.getInstanceQueryClassification();
		try
		{
			return (com.ofx.repository.SxClass)sxqueryinterface.retrieve(s1, s);
		}
		catch(java.lang.Exception exception)
		{
			throw new SxRepositoryException("retrieve() classification: " + s1 + " identifier: " + s + " exception: " + exception);
		}
	}

	public boolean update(com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		if(!sxqueryinterface.isPersistent(this))
		{
			sxqueryinterface.makePersistent(this);
			return true;
		}
		try
		{
			sxqueryinterface.lockToChange(this);
		}
		catch(java.lang.Exception exception)
		{
			throw new SxRepositoryException("update() classification: " + getQueryClassification() + " identifier: " + getIdentifier() + " exception: " + exception);
		}
		return true;
	}

	public boolean delete(com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		java.lang.String s = getQueryClassification();
		java.lang.String s1 = getIdentifier().toString();
		boolean flag = sxqueryinterface.lockToChange(this);
		if(!flag)
			throw new SxRepositoryException("Could not lock object, unable to delete classification: " + com.ofx.repository.SxClass.getInstanceQueryClassification() + " identifier: " + s1);
		sxqueryinterface.removeSpatialClassification(s1);
		sxqueryinterface.removeSpatialIndexOn(s1);
		if(getUseSharedLabels())
		{
			com.ofx.repository.SxLabelTable sxlabeltable = (com.ofx.repository.SxLabelTable)sxqueryinterface.retrieve(com.ofx.repository.SxLabelTable.getInstanceQueryClassification(), getClassificationName());
			if(sxlabeltable != null)
			{
				sxqueryinterface.lockToChange(sxlabeltable);
				sxqueryinterface.remove(sxlabeltable);
			}
		}
		return sxqueryinterface.remove(this);
	}

	public boolean deleteInstances(com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		return sxqueryinterface.removeAllInstances(getIdentifier().toString());
	}

	public static boolean exists(java.lang.String s, com.ofx.query.SxQueryInterface sxqueryinterface)
		throws com.ofx.repository.SxRepositoryException
	{
		for(java.util.Enumeration enumeration = com.ofx.repository.SxClass.objectIDs(sxqueryinterface); enumeration.hasMoreElements();)
			if(((java.lang.String)enumeration.nextElement()).equalsIgnoreCase(s))
				return true;

		return false;
	}

	public static com.ofx.repository.SxClass getClassOfClassification(java.lang.String s, com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		com.ofx.repository.SxClass sxclass = null;
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = sxqueryinterface.getObjects(com.ofx.repository.SxClass.getInstanceQueryClassification());
		for(java.util.Enumeration enumeration = sxqueryresultinterface.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.repository.SxClass sxclass1 = (com.ofx.repository.SxClass)enumeration.nextElement();
			if(s.equals(sxclass1.getClassificationName()))
			{
				sxclass = sxclass1;
				break;
			}
		}

		return sxclass;
	}

	public SxClass(java.lang.String s, java.lang.String s1, java.lang.String s2, boolean flag, com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		needsGeneratedIdValues = false;
		isUserDefined = true;
		useSharedLabels = false;
		javaClassName = "com.ofx.routing.objectfx.SxTravNetNode";
		hasExtraAttributes = false;
		isVisible = true;
		symbolMinScale = 0.0D;
		symbolMaxScale = 100000D;
		textMinScale = 0.0D;
		textMaxScale = 100000D;
		setID(s);
		setDescription(s1);
		try
		{
			setSymbology(com.ofx.repository.SxSymbology.retrieve("DEFAULT", sxqueryinterface));
			if(s2.equalsIgnoreCase("Point"))
			{
				setLabelSpec(com.ofx.repository.SxTextSpec.retrieve("DEFAULT", sxqueryinterface));
			} else
			{
				boolean flag1 = false;
				try
				{
					flag1 = com.ofx.repository.SxTextSpec.exists("DEFAULT_CENTERED", sxqueryinterface);
				}
				catch(java.lang.Exception exception)
				{
					com.ofx.base.SxEnvironment.singleton();
					com.ofx.base.SxEnvironment.log().println(exception);
				}
				if(flag1)
					setLabelSpec(com.ofx.repository.SxTextSpec.retrieve("DEFAULT_CENTERED", sxqueryinterface));
				else
					setLabelSpec(com.ofx.repository.SxTextSpec.retrieve("DEFAULT", sxqueryinterface));
			}
			setGeomType(s2);
		}
		catch(com.ofx.repository.SxRepositoryException sxrepositoryexception)
		{
			throw new RuntimeException("SxClass constructor, exception: " + sxrepositoryexception);
		}
		java.lang.String s3 = sxqueryinterface.composeValidClassificationName(s);
		setClassificationName(s3);
		if(flag)
		{
			com.ofx.query.SxQueryResultInterface sxqueryresultinterface = sxqueryinterface.getObjects(com.ofx.repository.SxClass.getInstanceQueryClassification());
			Object obj = null;
			for(java.util.Enumeration enumeration = sxqueryresultinterface.elements(); enumeration.hasMoreElements();)
			{
				com.ofx.repository.SxClass sxclass = (com.ofx.repository.SxClass)enumeration.nextElement();
				if(sxclass.getClassificationName().equalsIgnoreCase(s3))
					throw new RuntimeException("Cannot create spatial class:" + s + " due to duplicate internal classification name: '" + s3 + "' with class: " + sxclass.ID + ". Please rename the class and try again.");
			}

		}
	}

	public void setID(java.lang.String s)
	{
		ID = s;
	}

	public java.lang.String getID()
	{
		return ID;
	}

	public java.lang.String getSpatialObjectQueryClassification()
	{
		return getClassificationName();
	}

	public void setDescription(java.lang.String s)
	{
		description = s;
	}

	public java.lang.String getDescription()
	{
		return description;
	}

	public void setNeedsGeneratedIdValues(boolean flag)
	{
		needsGeneratedIdValues = flag;
	}

	public boolean getNeedsGeneratedIdValues()
	{
		return needsGeneratedIdValues;
	}

	public void setSymbology(com.ofx.repository.SxSymbology sxsymbology)
	{
		symbology = sxsymbology;
		if(sxsymbology == null)
			symbologyPrim = null;
		else
			symbologyPrim = sxsymbology.getIdentifier().toString();
	}

	public java.lang.String getSymbologyPrim()
	{
		return symbologyPrim;
	}

	public com.ofx.repository.SxSymbology getSymbology()
	{
		return symbology;
	}

	public com.ofx.repository.SxTextSpec getLabelSpec()
		throws com.ofx.repository.SxRepositoryException
	{
		return labelSpec;
	}

	public void setLabelSpec(com.ofx.repository.SxTextSpec sxtextspec)
	{
		labelSpec = sxtextspec;
		if(sxtextspec == null)
			labelSpecPrim = null;
		else
			labelSpecPrim = sxtextspec.getIdentifier().toString();
	}

	public java.lang.String getLabelSpecPrim()
	{
		return labelSpecPrim;
	}

	public void setGeomType(java.lang.String s)
	{
		geomType = s;
		if(s.equals("Point"))
			dimension = 0;
		else
		if(s.equals("Polyline"))
			dimension = 1;
		else
		if(s.equals("Polygon"))
			dimension = 2;
		else
		if(s.equals("Text"))
			dimension = 3;
	}

	public int getDimension()
	{
		return dimension;
	}

	public boolean isPointType()
	{
		return dimension == 0;
	}

	public boolean isPolylineType()
	{
		return dimension == 1;
	}

	public boolean isPolygonType()
	{
		return dimension == 2;
	}

	public boolean isTextType()
	{
		return dimension == 3;
	}

	public java.lang.String getGeomType()
	{
		return geomType;
	}

	public java.lang.String getClassificationName()
	{
		return classificationName;
	}

	protected void setClassificationName(java.lang.String s)
	{
		classificationName = s;
	}

	public void setIsVisible(boolean flag)
	{
		isVisible = flag;
	}

	public boolean getIsVisible()
	{
		return isVisible;
	}

	public void setSymbolMinScale(double d)
	{
		symbolMinScale = d;
	}

	public double getSymbolMinScale()
	{
		return symbolMinScale;
	}

	public void setSymbolMaxScale(double d)
	{
		symbolMaxScale = d;
	}

	public double getSymbolMaxScale()
	{
		return symbolMaxScale;
	}

	public void setTextMinScale(double d)
	{
		textMinScale = d;
	}

	public double getTextMinScale()
	{
		return textMinScale;
	}

	public void setTextMaxScale(double d)
	{
		textMaxScale = d;
	}

	public double getTextMaxScale()
	{
		return textMaxScale;
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
		return com.ofx.repository.SxClass.QUERYCLASSIFICATION;
	}

	public java.lang.String getQueryClassification(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		return com.ofx.repository.SxClass.QUERYCLASSIFICATION;
	}

	public boolean isSpatial()
	{
		return false;
	}

	public com.ofx.geometry.SxGeometryInterface getGeometry()
	{
		return null;
	}

	public com.ofx.geometry.SxRectangle getBounds()
	{
		return null;
	}

	public boolean getUseSharedLabels()
	{
		return useSharedLabels;
	}

	public void setUseSharedLabels(boolean flag)
	{
		useSharedLabels = flag;
	}

	public void useRawGeometry(boolean flag, com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(flag)
			sxqueryinterface.setSpatialCacheMode(getID(), "RawGeometry");
		else
			sxqueryinterface.setSpatialCacheMode(getID(), "SpatialObjects");
	}

	public void setExternalDataDef(com.ofx.repository.SxExternalDataDef sxexternaldatadef)
	{
		externalDataDef = sxexternaldatadef;
		if(sxexternaldatadef == null)
			externalDataDefPrim = null;
		else
			externalDataDefPrim = sxexternaldatadef.getIdentifier().toString();
	}

	public java.lang.String getExternalDataDefPrim()
	{
		return externalDataDefPrim;
	}

	public com.ofx.repository.SxExternalDataDef getExternalDataDef()
	{
		return externalDataDef;
	}

	public void setDataSource(com.ofx.repository.SxDataSource sxdatasource)
	{
		dataSource = sxdatasource;
		if(sxdatasource == null)
			dataSourcePrim = null;
		else
			dataSourcePrim = sxdatasource.getIdentifier().toString();
	}

	public java.lang.String getDataSourcePrim()
	{
		return dataSourcePrim;
	}

	public com.ofx.repository.SxDataSource getDataSource()
	{
		return dataSource;
	}

	public boolean getIsUserDefined()
	{
		return isUserDefined;
	}

	public void setIsUserDefined(boolean flag)
	{
		isUserDefined = flag;
	}

	public boolean getHasExtraAttributes()
	{
		return hasExtraAttributes;
	}

	public void setHasExtraAttributes(boolean flag)
	{
		hasExtraAttributes = flag;
	}

	public java.lang.String getJavaClassName()
	{
		return javaClassName;
	}

	public void setJavaClassName(java.lang.String s)
	{
		javaClassName = s;
	}

	public void resolveSoftLinks(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		queryServiceName = sxqueryinterface.getRequestID();
		resolveSymbology(sxqueryinterface);
		resolveLabelSpec(sxqueryinterface);
		resolveDataSource(sxqueryinterface);
		resolveExternalDataDef(sxqueryinterface);
	}

	protected void resolveSymbology(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(symbology == null)
		{
			Object obj = null;
			try
			{
				if(getSymbologyPrim() != null)
				{
					com.ofx.repository.SxSymbology sxsymbology = (com.ofx.repository.SxSymbology)sxqueryinterface.retrieve(com.ofx.repository.SxSymbology.getInstanceQueryClassification(), getSymbologyPrim());
					symbology = (com.ofx.repository.SxSymbology)sxsymbology.clone();
				}
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxClass.resolveSymbology(aQI) " + getID() + " could not find symbology named " + getSymbologyPrim());
			}
		}
	}

	protected void resolveLabelSpec(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(labelSpec == null)
		{
			Object obj = null;
			try
			{
				if(getLabelSpecPrim() != null)
				{
					com.ofx.repository.SxTextSpec sxtextspec = (com.ofx.repository.SxTextSpec)sxqueryinterface.retrieve(com.ofx.repository.SxTextSpec.getInstanceQueryClassification(), getLabelSpecPrim());
					labelSpec = (com.ofx.repository.SxTextSpec)sxtextspec.clone();
				}
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxClass.resolveLabelSpec(aQI) " + getID() + " could not find label spec named " + getLabelSpecPrim());
			}
		}
	}

	protected void resolveDataSource(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(dataSource == null)
			try
			{
				if(getDataSourcePrim() != null)
					dataSource = (com.ofx.repository.SxDataSource)sxqueryinterface.retrieve(com.ofx.repository.SxDataSource.getInstanceQueryClassification(), getDataSourcePrim());
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxClass.resolveDataSource(aQI) " + getID() + " could not find data source named " + getDataSourcePrim());
			}
	}

	protected void resolveExternalDataDef(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		if(externalDataDef == null)
			try
			{
				if(getDataSourcePrim() != null)
					externalDataDef = (com.ofx.repository.SxExternalDataDef)sxqueryinterface.retrieve(com.ofx.repository.SxExternalDataDef.getInstanceQueryClassification(), getExternalDataDefPrim());
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxClass.resolveExternalDataDef(aQI) " + getID() + " could not find EDFO layer named " + getExternalDataDefPrim() + " SxExternalDataDef.getInstanceQueryClassification()=" + com.ofx.repository.SxExternalDataDef.getInstanceQueryClassification());
			}
	}

	private void writeObject(java.io.ObjectOutputStream objectoutputstream)
		throws java.io.IOException
	{
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

	public java.lang.String toString()
	{
		return "SxClass( " + getIdentifier() + ") ";
	}

	private static final long serialVersionUID = 0xffffffff8c6626f5L;
	private static final int OFX_CLASS_VERSION = 0x10000;
	protected static com.ofx.repository.SxClass last = null;
	protected static java.lang.String lastString = "";
	protected static java.lang.String QUERYCLASSIFICATION = "OfxCSxClass";
	private java.lang.String ID;
	private java.lang.String description;
	private java.lang.String db;
	private java.lang.String queryServiceName;
	private java.lang.String classificationName;
	protected boolean needsGeneratedIdValues;
	private transient com.ofx.repository.SxSymbology symbology;
	private java.lang.String symbologyPrim;
	private transient com.ofx.repository.SxTextSpec labelSpec;
	private java.lang.String labelSpecPrim;
	private java.lang.String geomType;
	private transient com.ofx.repository.SxDataSource dataSource;
	private java.lang.String dataSourcePrim;
	private transient com.ofx.repository.SxExternalDataDef externalDataDef;
	private java.lang.String externalDataDefPrim;
	private boolean isUserDefined;
	private boolean useSharedLabels;
	private java.lang.String javaClassName;
	private boolean hasExtraAttributes;
	private int dimension;
	private transient com.ofx.mapViewer.SxRendererInterface renderer;
	public boolean isVisible;
	protected double symbolMinScale;
	protected double symbolMaxScale;
	protected double textMinScale;
	protected double textMaxScale;

}
