package com.ofx.dataAdapter;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.geometry.*;
import com.ofx.index.rtree.SxSpatialIndexLeafNode;
import com.ofx.query.SxQueryTransactionInterface;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxSpatialObject;
import com.ofx.server.SxObjectPool;
import java.io.*;
import java.util.*;

public class SxMapInfoTranslator extends SxTranslator
	implements SxDataAdapter
{

	public SxMapInfoTranslator(String s, SxImportFilter sximportfilter)
		throws SxImportException
	{
		super(sximportfilter);
		processingCompoundGeometry = false;
		nextGeoResult = new Vector(2);
		String s1 = ".mid";
		File file = new File(s + s1);
		if(!file.exists())
		{
			s1 = ".MID";
			file = new File(s + s1);
			if(!file.exists())
			{
				s1 = ".Mid";
				file = new File(s + s1);
			}
			if(!file.exists())
				throw new SxImportException("Couldn't find MID file named: " + s + " using extension '.MID', '.mid' or '.Mid'.");
		}
		String s2 = ".mif";
		file = new File(s + s2);
		if(!file.exists())
		{
			s2 = ".MIF";
			File file1 = new File(s + s2);
			if(!file1.exists())
			{
				s2 = ".Mif";
				file1 = new File(s + s2);
			}
			if(!file1.exists())
				throw new SxImportException("Couldn't find MIF file named: " + s + " using extension '.MIF', '.mif' or '.Mif'.");
		}
		midFilename = s + s1;
		mifFilename = s + s2;
		SxDataRecordObjectPool.getDataRecordObjectPool().setMaxCheckout(100);
		prepareForRead();
		if(!readMIFHeaderInfo())
		{
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			mifScanner.release();
			throw new SxImportException("Couldn't find MIF header information on file:" + mifFilename);
		} else
		{
			return;
		}
	}

	public SxMapInfoTranslator(String s, String s1, SxImportFilter sximportfilter)
		throws SxImportException
	{
		super(sximportfilter);
		processingCompoundGeometry = false;
		nextGeoResult = new Vector(2);
		midFilename = s;
		mifFilename = s1;
		SxDataRecordObjectPool.getDataRecordObjectPool().setMaxCheckout(100);
		prepareForRead();
		if(!readMIFHeaderInfo())
		{
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			mifScanner.release();
			throw new SxImportException("Couldn't find MIF header information on file:" + mifFilename);
		} else
		{
			return;
		}
	}

	public void release()
	{
		if(midScanner != null)
			midScanner.release();
		if(mifScanner != null)
			mifScanner.release();
		super.release();
	}

	public Vector getAttributeNames()
		throws SxImportException
	{
		Vector vector = new Vector();
		Object obj = null;
		mifScanner().release();
		prepareForRead();
		boolean flag = false;
		try
		{
			while(!mifScanner().isAtEnd() && !flag) 
			{
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				SxDataRecord sxdatarecord = mifScanner().readRecord(keywordRecordDefinition());
				if(sxdatarecord != null && "Columns".equalsIgnoreCase(sxdatarecord.stringAtField("keyword")))
					flag = true;
			}
		}
		catch(Exception exception)
		{
			throw new SxImportException("While trying to get a list of attribute names from the MIF file, the following exception occurred:" + exception);
		}
		if(flag)
		{
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord1 = mifScanner().parseRecord(columnsRecordDefinition());
			int i = sxdatarecord1.integerAtField("numOfColumns");
			try
			{
				for(int j = 0; j < i; j++)
				{
					if(mifScanner().isAtEnd())
						throw new SxImportException("While trying to get a list of attribute names from the MIF file, the premature end of the file was encountered.");
					SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
					SxDataRecord sxdatarecord2 = mifScanner().readRecord(attributeNamesRecordDefinition());
					vector.addElement(sxdatarecord2.atField("attributeName"));
				}

			}
			catch(Exception exception1)
			{
				throw new SxImportException("While trying to get a list of attribute names from the MIF file, the following exception occurred:" + exception1);
			}
		} else
		{
			throw new SxImportException("While trying to get a list of attribute names from the MIF file the 'Columns' keyword could not be found.");
		}
		mifScanner().release();
		prepareForRead();
		if(!readMIFHeaderInfo())
		{
			mifScanner().release();
			throw new SxImportException("Couldn't find MIF header information on file:" + mifFilename);
		} else
		{
			return vector;
		}
	}

	public int getGeometryType()
		throws SxImportException
	{
		Object obj = null;
		String s = findNextGraphicalKeyword();
		byte byte0 = 0;
		if(s == null)
			throw new SxImportException("The MIF file does not contain any recognizable geometric keywords.");
		if(s.equalsIgnoreCase("Point"))
			byte0 = 0;
		else if(s.equalsIgnoreCase("Text"))
			byte0 = 3;
		else
		if(s.equalsIgnoreCase("Line") || s.equalsIgnoreCase("Pline") || s.equalsIgnoreCase("Arc"))
			byte0 = 1;
		else
		if(s.equalsIgnoreCase("Region") || s.equalsIgnoreCase("Rect") || s.equalsIgnoreCase("RoundRect") || s.equalsIgnoreCase("Ellipse"))
			byte0 = 2;
		mifScanner().release();
		prepareForRead();
		if(!readMIFHeaderInfo())
		{
			mifScanner().release();
			throw new SxImportException("Couldn't find MIF header information on file:" + mifFilename);
		} else
		{
			return byte0;
		}
	}

	public Object[] getNextRawSpatialObject()
		throws SxImportException
	{
		SxClass sxclass = getSpatialClass();
		theMIDRecord = readNextMIDRecord();
		if(theMIDRecord == null)
			return null;
		processingCompoundGeometry = false;
		Vector vector = getNextGeometric();
		if(vector == null)
		{
			theMIDRecord = readNextMIDRecord();
			if(theMIDRecord == null)
				return null;
			vector = getNextGeometric();
			if(vector == null)
				return null;
		}
		SxRectangle sxrectangle = getIntersectsRectangle();
		if(sxrectangle != null)
		{
			boolean flag = SxSpatialIndexLeafNode.intersectsOrTouchesRawGeometryBounds((double[])vector.elementAt(0), sxrectangle);
			if(!flag)
			{
				for(; (theMIDRecord = readNextMIDRecord()) != null && !flag; flag = SxSpatialIndexLeafNode.intersectsOrTouchesRawGeometryBounds((double[])vector.elementAt(0), sxrectangle))
					vector = getNextGeometric();

				if(theMIDRecord == null)
					return null;
			}
		}
		Object aobj[] = new Object[7];
		if(sxclass.getNeedsGeneratedIdValues())
			aobj[0] = SxEnvironment.singleton().getQuery().getNextIdentifier(sxclass.getClassificationName());
		else
			aobj[0] = theMIDRecord.atKeyField();
		aobj[1] = theMIDRecord.atLabelField();
		if(aobj[1] == null)
			aobj[1] = aobj[0];
		if(sxclass.isTextType())
		{
			if(vector.size() > 5)
			{
				processingCompoundGeometry = false;
				aobj[1] = vector.elementAt(1);//text
				aobj[2] = vector.elementAt(0);//center location
				aobj[3] = vector.elementAt(2);//long1, lat1, long2, lat2 location
				aobj[4] = vector.elementAt(3);//font name
				aobj[5] = vector.elementAt(4);//font params
				aobj[6] = vector.elementAt(5);//angle
			}
			else
			if(vector.size() > 4)
			{
				processingCompoundGeometry = false;
				aobj[1] = vector.elementAt(1);//text
				aobj[2] = vector.elementAt(0);//center location
				aobj[3] = vector.elementAt(2);//long1, lat1, long2, lat2 location
				aobj[4] = vector.elementAt(3);//font name
				aobj[5] = vector.elementAt(4);//font params
			}
			else
			if(vector.size() > 3)
			{
				processingCompoundGeometry = false;
				aobj[1] = vector.elementAt(1);//text
				aobj[2] = vector.elementAt(0);//center location
				aobj[3] = vector.elementAt(2);//long1, lat1, long2, lat2 location
				aobj[6] = vector.elementAt(3);//angle
			}
			else
			if(vector.size() > 1)
			{
				processingCompoundGeometry = false;
				aobj[1] = vector.elementAt(1);//text
				aobj[2] = vector.elementAt(0);//center location
				aobj[3] = vector.elementAt(2);//long1, lat1, long2, lat2 location
			} else
			{
				processingCompoundGeometry = false;
				aobj[2] = vector.firstElement();//center location
			}
		} else
		if(sxclass.isPointType())
		{
			if(vector.size() > 1)
			{
				processingCompoundGeometry = false;
				aobj[1] = vector.elementAt(1);
				aobj[2] = vector.elementAt(0);
			} else
			{
				processingCompoundGeometry = false;
				aobj[2] = vector.firstElement();
			}
		} else
		if(sxclass.isPolylineType())
		{
			if(vector.size() > 1)
			{
				currentCompoundGeometryIndex = 0;
				processingCompoundGeometry = true;
				compoundGeometry = vector;
				compoundGeometryId = (String)aobj[0];
				compoundGeometryLabel = (String)aobj[1];
				aobj[0] = compoundGeometryId;
				aobj[2] = vector.firstElement();
			} else
			{
				processingCompoundGeometry = false;
				aobj[2] = vector.firstElement();
			}
		} else
		if(sxclass.isPolygonType())
		{
			if(vector.size() > 1)
			{
				currentCompoundGeometryIndex = 0;
				processingCompoundGeometry = true;
				compoundGeometry = vector;
				compoundGeometryId = (String)aobj[0];
				compoundGeometryLabel = (String)aobj[1];
				aobj[0] = compoundGeometryId;
				aobj[2] = vector.firstElement();
			} else
			{
				processingCompoundGeometry = false;
				aobj[2] = vector.firstElement();
			}
		} else
		{
			throw new SxImportException("Invalid geometry type for spatial class: " + sxclass.getIdentifier());
		}
		if(aobj[2] == null)
			throw new SxImportException("Null geometry for spatial object: " + aobj[0]);
		else
			return aobj;
	}

	protected Object[] getNextCompoundRawSpatialObject(String s, String s1, Vector vector, int i)
		throws SxImportException
	{
		Object aobj[] = new Object[3];
		aobj[0] = s;
		aobj[1] = s1;
		aobj[2] = (double[])vector.elementAt(i);
		return aobj;
	}

	protected SxDataRecord readNextMIDRecord()
		throws SxImportException
	{
		if(midScanner.isAtEnd())
			return null;
		SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(midScanner().getCurrentRecord());
		SxDataRecord sxdatarecord = midScanner.readRecord();
		if(sxdatarecord == null)
		{
			midScanner.skipToEnd();
			return null;
		} else
		{
			return sxdatarecord;
		}
	}

	public SxSpatialObject getNextSpatialObject()
		throws SxImportException
	{
		SxClass sxclass = getSpatialClass();
		if(sxclass == null)
		{
			SxEnvironment.log().println("Before getting a spatial object a class must be set.");
			return null;
		}
		Object aobj[] = getNextRawSpatialObject();
		if(aobj == null)
			return null;
		Object obj = null;
		double ad[] = (double[])aobj[2];
		int i = ad.length / 2;
		SxSpatialObject sxspatialobject = null;
		if(processingCompoundGeometry)
		{
			if(sxclass.isPointType())
			{
				SxMultiPoint sxmultipoint = new SxMultiPoint();
				for(Enumeration enumeration = compoundGeometry.elements(); enumeration.hasMoreElements();)
				{
					double ad1[] = (double[])enumeration.nextElement();
					sxmultipoint.addGeometry(ad1, 1);
					currentCompoundGeometryIndex++;
				}

				obj = sxmultipoint;
				sxspatialobject = new SxSpatialObject(sxclass, compoundGeometryId, ((com.ofx.geometry.SxGeometryInterface) (obj)));
			} else
			if(sxclass.isPolylineType())
			{
				SxMultiPolyline sxmultipolyline = new SxMultiPolyline();
				for(Enumeration enumeration1 = compoundGeometry.elements(); enumeration1.hasMoreElements();)
				{
					double ad2[] = (double[])enumeration1.nextElement();
					int j = (int)((double)ad2.length * 0.5D);
					sxmultipolyline.addGeometry(ad2, j);
					currentCompoundGeometryIndex++;
				}

				obj = sxmultipolyline;
				sxspatialobject = new SxSpatialObject(sxclass, compoundGeometryId, ((com.ofx.geometry.SxGeometryInterface) (obj)));
			} else
			{
				SxMultiPolygon sxmultipolygon = new SxMultiPolygon();
				for(Enumeration enumeration2 = compoundGeometry.elements(); enumeration2.hasMoreElements();)
				{
					double ad3[] = (double[])enumeration2.nextElement();
					int k = (int)((double)ad3.length * 0.5D);
					sxmultipolygon.addGeometry(ad3, k);
					currentCompoundGeometryIndex++;
				}

				obj = sxmultipolygon;
				sxspatialobject = new SxSpatialObject(sxclass, compoundGeometryId, ((com.ofx.geometry.SxGeometryInterface) (obj)));
			}
		} else
		if(sxclass.isPointType())
			obj = new SxPoint(ad[0], ad[1]);
		else
		if(sxclass.isTextType())
		{
			String text = "";
			if(aobj[1] != null)
				text = (String )aobj[1];
			double admy[] = (double[])aobj[3];
			String fontname = null;
			String fontparams = null;
			if(aobj[4] != null)
			{
				fontname = (String )aobj[4];
				fontparams = (String )aobj[5];
			}
			int angle = 0;
			if(aobj[6] != null)
				angle = ((Integer )aobj[6]).intValue();

			if(fontname != null)
				obj = new SxText(admy, text, fontname, fontparams, angle);
			else
				obj = new SxText(admy, text, angle);
			int np = ((SxText )obj).getNPoints();
			int dummy;
			if(admy.length < 4)
				dummy = 1;
		}
		else
		if(sxclass.isPolylineType())
			obj = new SxPolyline(ad, i);
		else
			obj = new SxPolygon(ad, i);
		if(sxspatialobject == null)
			sxspatialobject = new SxSpatialObject(sxclass, (String)aobj[0], ((com.ofx.geometry.SxGeometryInterface) (obj)));
		sxspatialobject.setLabel(((String)aobj[1]).trim());
		return sxspatialobject;
	}

	public SxClass getSpatialClass()
	{
		if(getImportFilter().hasMultipleClasses())
			return null;
		else
			return getImportFilter().getPrimaryClass();
	}

	protected String findNextGraphicalKeyword()
		throws SxImportException
	{
		String s = null;
		if(mifScanner().isAtEnd())
			return null;
		do
		{
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord = mifScanner().readRecord(keywordRecordDefinition());
			if(sxdatarecord != null)
				s = graphicalKeywordFor(sxdatarecord);
		} while(s == null && !mifScanner().isAtEnd());
		return s;
	}

	protected String findAllowedGraphicalKeyword(int i)
		throws SxImportException
	{
		String s = null;
		if(mifScanner().isAtEnd())
			return null;
		do
		{
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord = mifScanner().readRecord(keywordRecordDefinition());
			if(sxdatarecord != null)
			{
				String s1 = graphicalKeywordFor(sxdatarecord);
				if(s1 != null)
					if(s1.equalsIgnoreCase("None"))
						s = s1;
					else
					if(i == 0 && (s1.equalsIgnoreCase("Point")))
						s = s1;
					else
					if(i == 3 && (s1.equalsIgnoreCase("Text")))
						s = s1;
					else
					if(i == 1 && (s1.equalsIgnoreCase("Line") || s1.equalsIgnoreCase("Pline") || s1.equalsIgnoreCase("Arc")))
						s = s1;
					else
					if(i == 2 && (s1.equalsIgnoreCase("Region") || s1.equalsIgnoreCase("Rect") || s1.equalsIgnoreCase("RoundRect") || s1.equalsIgnoreCase("Ellipse")))
					{
						s = s1;
					} else
					{
						theMIDRecord = readNextMIDRecord();
						if(theMIDRecord == null)
							return null;
					}
			}
		} while(s == null && !mifScanner().isAtEnd());
		return s;
	}

	protected String findNextSymbologyKeyword()
		throws SxImportException
	{
		String s = null;
		String s1 = null;
		if(mifScanner().isAtEnd())
			return null;
		do
		{
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord = mifScanner().readRecord(keywordRecordDefinition());
			if(sxdatarecord != null)
			{
				s = graphicalKeywordFor(sxdatarecord);
				s1 = symbologyKeywordFor(sxdatarecord);
			}
		} while(s == null && s1 == null && !mifScanner().isAtEnd());
		return s1;
	}

	protected Vector getNextGeometric()
		throws SxImportException
	{
		String s = null;
		int i = getSpatialClass().getDimension();
		if((s = findAllowedGraphicalKeyword(i)) == null)
			return null;
		Object obj = null;
		Object obj1 = null;
		boolean flag = false;
		Vector vector = getNextGeometricResult();
		Object obj2 = null;
		Object obj3 = null;
		if(s.equalsIgnoreCase("Point"))
		{
			SxRecordDefinition sxrecorddefinition = (SxRecordDefinition)MIFRecordDefinitions().get("Point");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord = mifScanner().parseRecord(sxrecorddefinition);
			double ad5[] = new double[2];
			double ad[] = createTransformedPoint(sxdatarecord.doubleAtField("long"), sxdatarecord.doubleAtField("lat"));
			ad5[0] = ad[0];
			ad5[1] = ad[1];
			vector.addElement(ad5);
		} else
		if(s.equalsIgnoreCase("Text"))
		{
			SxRecordDefinition sxrecorddefinition1 = (SxRecordDefinition)MIFRecordDefinitions().get("Text");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord1;
			try
			{
				sxdatarecord1 = mifScanner().parseRecord(sxrecorddefinition1);
			}
			catch(SxImportException sximportexception)
			{
				sxrecorddefinition1 = (SxRecordDefinition)MIFRecordDefinitions().get("TextString");
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				sxdatarecord1 = mifScanner().readRecord(sxrecorddefinition1);
			}
			String s1 = sxdatarecord1.stringAtField("text");

			sxrecorddefinition1 = textRectangleRecordDefinition();
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			sxdatarecord1 = mifScanner().readRecord(sxrecorddefinition1);
			double d3 = sxdatarecord1.doubleAtField("long1");
			double d7 = sxdatarecord1.doubleAtField("lat1");
			double d11 = sxdatarecord1.doubleAtField("long2");
			double d15 = sxdatarecord1.doubleAtField("lat2");
			double ad1[] = createTransformedPoint((d3 + d11) / 2D, (d7 + d15) / 2D);
			double ad6[] = new double[2];
			ad6[0] = ad1[0];
			ad6[1] = ad1[1];

			vector.addElement(ad6);
			vector.addElement(s1);
			double ad7[] = new double[4];
			ad7[0] = d3;
			ad7[1] = d7;
			ad7[2] = d11;
			ad7[3] = d15;
			vector.addElement(ad7);

			sxrecorddefinition1 = textFontRecordDefinition();
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			try
			{
				String ts = mifScanner().scanLine();
				if(ts != null)
				{
					sxdatarecord1 = mifScanner().parseRecord(sxrecorddefinition1);
					if(sxdatarecord1 != null)
					{
						sxdatarecord1 = mifScanner().readRecord(sxrecorddefinition1);
						String mys1 = sxdatarecord1.stringAtField("kwd");//'    Font ('
						String mys2 = sxdatarecord1.stringAtField("fontname");//'Arial Cyr'
						String mys3 = sxdatarecord1.stringAtField("fontparams");//',0,0,0)'
						mys3 = mys3.substring(1, 6);//'0,0,0'

						vector.addElement(mys2);
						vector.addElement(mys3);
					}
				}
			}
			catch(SxImportException sximportexception)
			{
			}

			sxrecorddefinition1 = textAngleRecordDefinition();
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			try
			{
				String ts = mifScanner().scanLine();
				if(ts != null)
				{
					sxdatarecord1 = mifScanner().parseRecord(sxrecorddefinition1);
					if(sxdatarecord1 != null)
					{
						sxdatarecord1 = mifScanner().readRecord(sxrecorddefinition1);
						int int1 = sxdatarecord1.integerAtField("angle");
						vector.addElement(new Integer(int1));
					}
				}
			}
			catch(SxImportException sximportexception)
			{
			}


		} else
		if(s.equalsIgnoreCase("Line"))
		{
			SxRecordDefinition sxrecorddefinition2 = (SxRecordDefinition)MIFRecordDefinitions().get("Line");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord2 = mifScanner().parseRecord(sxrecorddefinition2);
			double ad7[] = new double[4];
			ad7[0] = sxdatarecord2.doubleAtField("long1");
			ad7[1] = sxdatarecord2.doubleAtField("long2");
			ad7[2] = sxdatarecord2.doubleAtField("lat1");
			ad7[3] = sxdatarecord2.doubleAtField("lat2");
			transformRawGeometry(getProjection(), ad7, 2);
			vector.addElement(ad7);
		} else
		if(s.equalsIgnoreCase("Pline"))
		{
			String s2 = mifScanner().getCurrentLine();
			if(s2.trim().equalsIgnoreCase("Pline"))
			{
				SxRecordDefinition sxrecorddefinition3 = (SxRecordDefinition)MIFRecordDefinitions().get("Pline3");
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				SxDataRecord sxdatarecord3 = mifScanner().parseRecord(sxrecorddefinition3);
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				sxdatarecord3 = mifScanner().readRecord(regionNumPtsRecordDefinition());
				int j = sxdatarecord3.integerAtField("numberOfGeometrics");
				sxrecorddefinition3 = coordinateRecordDefinition();
				double ad8[] = new double[j * 2];
				for(int k1 = 0; k1 < j; k1++)
				{
					SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
					SxDataRecord sxdatarecord4 = mifScanner().readRecord(sxrecorddefinition3);
					ad8[k1] = sxdatarecord4.doubleAtField("long");
					ad8[k1 + j] = sxdatarecord4.doubleAtField("lat");
				}

				transformRawGeometry(getProjection(), ad8, j);
				vector.addElement(ad8);
			} else
			{
				SxRecordDefinition sxrecorddefinition4 = (SxRecordDefinition)MIFRecordDefinitions().get("Keyword2");
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				SxDataRecord sxdatarecord5 = mifScanner().parseRecord(sxrecorddefinition4);
				if(sxdatarecord5.stringAtField("keyword2").equalsIgnoreCase("MULTIPLE"))
				{
					SxRecordDefinition sxrecorddefinition5 = (SxRecordDefinition)MIFRecordDefinitions().get("Pline2");
					SxDataRecord sxdatarecord6 = mifScanner().parseRecord(sxrecorddefinition5);
					int l1 = sxdatarecord6.integerAtField("numberOfSections");
					for(int k2 = 0; k2 < l1; k2++)
					{
						SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
						SxDataRecord sxdatarecord7 = mifScanner().readRecord(regionNumPtsRecordDefinition());
						int k = sxdatarecord7.integerAtField("numberOfGeometrics");
						SxRecordDefinition sxrecorddefinition6 = coordinateRecordDefinition();
						double ad9[] = new double[k * 2];
						for(int i3 = 0; i3 < k; i3++)
						{
							SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
							SxDataRecord sxdatarecord8 = mifScanner().readRecord(sxrecorddefinition6);
							ad9[i3] = sxdatarecord8.doubleAtField("long");
							ad9[i3 + k] = sxdatarecord8.doubleAtField("lat");
						}

						transformRawGeometry(getProjection(), ad9, k);
						vector.addElement(ad9);
					}

				} else
				{
					SxRecordDefinition sxrecorddefinition7 = (SxRecordDefinition)MIFRecordDefinitions().get("Pline");
					SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
					SxDataRecord sxdatarecord9 = mifScanner().parseRecord(sxrecorddefinition7);
					int l = sxdatarecord9.integerAtField("numberOfGeometrics");
					sxrecorddefinition7 = coordinateRecordDefinition();
					double ad10[] = new double[l * 2];
					for(int i2 = 0; i2 < l; i2++)
					{
						SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
						SxDataRecord sxdatarecord10 = mifScanner().readRecord(sxrecorddefinition7);
						ad10[i2] = sxdatarecord10.doubleAtField("long");
						ad10[i2 + l] = sxdatarecord10.doubleAtField("lat");
					}

					transformRawGeometry(getProjection(), ad10, l);
					vector.addElement(ad10);
				}
			}
		} else
		if(s.equalsIgnoreCase("Arc"))
		{
			SxRecordDefinition sxrecorddefinition8 = (SxRecordDefinition)MIFRecordDefinitions().get("Arc");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			double d;
			double d4;
			double d8;
			double d12;
			double d16;
			double d18;
			try
			{
				SxDataRecord sxdatarecord11 = mifScanner().parseRecord(sxrecorddefinition8);
				d8 = sxdatarecord11.doubleAtField("long1");
				d12 = sxdatarecord11.doubleAtField("lat1");
				d16 = sxdatarecord11.doubleAtField("long2");
				d18 = sxdatarecord11.doubleAtField("lat2");
				d = sxdatarecord11.doubleAtField("startAngle");
				d4 = sxdatarecord11.doubleAtField("endAngle");
			}
			catch(SxImportException sximportexception1)
			{
				SxRecordDefinition sxrecorddefinition9 = (SxRecordDefinition)MIFRecordDefinitions().get("ArcNoAngles");
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				SxDataRecord sxdatarecord12 = mifScanner().parseRecord(sxrecorddefinition9);
				d8 = sxdatarecord12.doubleAtField("long1");
				d12 = sxdatarecord12.doubleAtField("lat1");
				d16 = sxdatarecord12.doubleAtField("long2");
				d18 = sxdatarecord12.doubleAtField("lat2");
				sxrecorddefinition9 = (SxRecordDefinition)MIFRecordDefinitions().get("ArcAngles");
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				sxdatarecord12 = mifScanner().readRecord(sxrecorddefinition9);
				d = sxdatarecord12.doubleAtField("startAngle");
				d4 = sxdatarecord12.doubleAtField("endAngle");
			}
			double d20 = (d16 + d8) / 2D;
			double d21 = (d18 + d12) / 2D;
			double ad2[] = createTransformedPoint(d20, d21);
			SxDoublePoint sxdoublepoint5 = new SxDoublePoint(ad2[0], ad2[1]);
			ad2 = createTransformedPoint(d20, d12);
			SxDoublePoint sxdoublepoint6 = new SxDoublePoint(ad2[0], ad2[1]);
			ad2 = createTransformedPoint(d16, d21);
			SxDoublePoint sxdoublepoint7 = new SxDoublePoint(ad2[0], ad2[1]);
			double d24 = sxdoublepoint7.x - sxdoublepoint5.x;
			double d25 = sxdoublepoint6.y - sxdoublepoint5.y;
			SxEllipse sxellipse1 = new SxEllipse(sxdoublepoint5, d24, d25, 0.0D);
			double ad19[] = sxellipse1.getXPoints(33);
			double ad20[] = sxellipse1.getYPoints(33);
			int l3 = 32;
			int i4 = (int)(((double)l3 * (360D - d)) / 360D);
			int j4 = (int)(((double)l3 * (360D - d4)) / 360D);
			if(d4 == 0.0D)
				j4 = 0;
			int k4 = (i4 - j4) + 1;
			double ad11[] = new double[k4 * 2];
			for(int l4 = 0; l4 < k4; l4++)
			{
				ad11[l4] = ad19[i4 - l4];
				ad11[l4 + k4] = ad20[i4 - l4];
			}

			vector.addElement(ad11);
		} else
		if(s.equalsIgnoreCase("Region"))
		{
			SxRecordDefinition sxrecorddefinition10 = (SxRecordDefinition)MIFRecordDefinitions().get("Region");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord13 = mifScanner().parseRecord(sxrecorddefinition10);
			int j1 = sxdatarecord13.integerAtField("numberOfPolygons");
			for(int j2 = 1; j2 <= j1; j2++)
			{
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				SxDataRecord sxdatarecord14 = mifScanner().readRecord(regionNumPtsRecordDefinition());
				int i1 = sxdatarecord14.integerAtField("numberOfGeometrics");
				SxRecordDefinition sxrecorddefinition11 = coordinateRecordDefinition();
				double ad12[] = new double[i1 * 2];
				for(int l2 = 0; l2 < i1; l2++)
				{
					SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
					SxDataRecord sxdatarecord15 = mifScanner().readRecord(sxrecorddefinition11);
					ad12[l2] = sxdatarecord15.doubleAtField("long");
					ad12[l2 + i1] = sxdatarecord15.doubleAtField("lat");
				}

				transformRawGeometry(getProjection(), ad12, i1);
				vector.addElement(ad12);
			}

		} else
		if(s.equalsIgnoreCase("Rect") || s.equalsIgnoreCase("RoundRect"))
		{
			SxRecordDefinition sxrecorddefinition12;
			if(s.equalsIgnoreCase("Rect"))
				sxrecorddefinition12 = (SxRecordDefinition)MIFRecordDefinitions().get("Rect");
			else
				sxrecorddefinition12 = (SxRecordDefinition)MIFRecordDefinitions().get("RoundRect");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord16 = mifScanner().parseRecord(sxrecorddefinition12);
			double d1 = sxdatarecord16.doubleAtField("long1");
			double d5 = sxdatarecord16.doubleAtField("lat1");
			double d9 = sxdatarecord16.doubleAtField("long2");
			double d13 = sxdatarecord16.doubleAtField("lat2");
			double ad3[] = createTransformedPoint(d1, d5);
			SxDoublePoint sxdoublepoint = new SxDoublePoint(ad3[0], ad3[1]);
			ad3 = createTransformedPoint(d9, d13);
			SxDoublePoint sxdoublepoint1 = new SxDoublePoint(ad3[0], ad3[1]);
			SxRectangle sxrectangle = new SxRectangle(sxdoublepoint, sxdoublepoint1);
			double ad15[] = sxrectangle.getXPoints();
			double ad16[] = sxrectangle.getYPoints();
			int j3 = sxrectangle.getNPoints();
			double ad13[] = new double[j3 * 2];
			System.arraycopy(ad15, 0, ad13, 0, j3);
			System.arraycopy(ad16, 0, ad13, j3, j3);
			vector.addElement(ad13);
		} else
		if(s.equalsIgnoreCase("Ellipse"))
		{
			SxRecordDefinition sxrecorddefinition13 = (SxRecordDefinition)MIFRecordDefinitions().get("Ellipse");
			SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
			SxDataRecord sxdatarecord17 = mifScanner().parseRecord(sxrecorddefinition13);
			double d2 = sxdatarecord17.doubleAtField("long1");
			double d6 = sxdatarecord17.doubleAtField("lat1");
			double d10 = sxdatarecord17.doubleAtField("long2");
			double d14 = sxdatarecord17.doubleAtField("lat2");
			double d17 = (d10 + d2) / 2D;
			double d19 = (d14 + d6) / 2D;
			double ad4[] = createTransformedPoint(d17, d19);
			SxDoublePoint sxdoublepoint2 = new SxDoublePoint(ad4[0], ad4[1]);
			ad4 = createTransformedPoint(d17, d6);
			SxDoublePoint sxdoublepoint3 = new SxDoublePoint(ad4[0], ad4[1]);
			ad4 = createTransformedPoint(d10, d19);
			SxDoublePoint sxdoublepoint4 = new SxDoublePoint(ad4[0], ad4[1]);
			double d22 = sxdoublepoint4.x - sxdoublepoint2.x;
			double d23 = sxdoublepoint3.y - sxdoublepoint2.y;
			SxEllipse sxellipse = new SxEllipse(sxdoublepoint2, d22, d23, 0.0D);
			double ad17[] = sxellipse.getXPoints();
			double ad18[] = sxellipse.getYPoints();
			int k3 = sxellipse.getNPoints();
			double ad14[] = new double[k3 * 2];
			System.arraycopy(ad17, 0, ad14, 0, k3);
			System.arraycopy(ad18, 0, ad14, k3, k3);
			vector.addElement(ad14);
		} else
		if(s.equalsIgnoreCase("None"))
			return null;
		else
			throw new SxImportException(s + "s are not supported. Line: " + mifScanner().getCurrentLine());
		return vector;
	}

	protected Vector getNextGeometricResult()
	{
		nextGeoResult.removeAllElements();
		return nextGeoResult;
	}

	protected String graphicalKeywordFor(SxDataRecord sxdatarecord)
	{
		String s = (String)sxdatarecord.atField("keyword");
		if(s.equalsIgnoreCase("Point") || s.equalsIgnoreCase("Pline") || s.equalsIgnoreCase("Region") || s.equalsIgnoreCase("Line") || s.equalsIgnoreCase("Text") || s.equalsIgnoreCase("Arc") || s.equalsIgnoreCase("Ellipse") || s.equalsIgnoreCase("Rect") || s.equalsIgnoreCase("RoundRect") || s.equalsIgnoreCase("None"))
			return s;
		else
			return null;
	}

	protected String symbologyKeywordFor(SxDataRecord sxdatarecord)
	{
		String s = (String)sxdatarecord.atField("keyword");
		if(SymbologyKeywords.get(s) == null)
			return null;
		else
			return s;
	}

	protected SxRecordScanner midScanner()
	{
		return midScanner;
	}

	protected SxRecordScanner mifScanner()
	{
		return mifScanner;
	}

	protected boolean readMIFHeaderInfo()
		throws SxImportException
	{
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = (SxDelimitedRecordDefinition)midScanner.getRecordDefinition();
		sxdelimitedrecorddefinition.setDelimiterCharacter('\t');
		try
		{
			while(!mifScanner().isAtEnd()) 
			{
				SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
				SxDataRecord sxdatarecord = mifScanner().readRecord(keywordRecordDefinition());
				if("Delimiter".equalsIgnoreCase(sxdatarecord.stringAtField("keyword")))
				{
					SxDataRecordObjectPool.getDataRecordObjectPool().checkIn(mifScanner().getCurrentRecord());
					sxdatarecord = mifScanner().parseRecord(delimiterRecordDefinition());
					char ac[] = sxdatarecord.stringAtField("delimiter").toCharArray();
					sxdelimitedrecorddefinition.setDelimiterCharacter(ac[0]);
				}
				if("Data".equalsIgnoreCase(sxdatarecord.stringAtField("keyword")))
					break;
			}
		}
		catch(Exception exception)
		{
			throw new SxImportException("The following exception occurred reading MIF file:" + exception);
		}
		return true;
	}

	protected boolean isReady()
	{
		return midScanner != null && mifScanner != null && targetClass != null;
	}

	protected boolean prepareForRead()
		throws SxImportException
	{
		try
		{
			if(midScanner != null)
				midScanner.release();
			midScanner = SxRecordScanner.create(new FileInputStream(midFilename), getImportFilter().getPrimaryRecordDefinition());
		}
		catch(FileNotFoundException filenotfoundexception)
		{
			throw new SxImportException("Couldn't open " + midFilename);
		}
		try
		{
			if(mifScanner != null)
				mifScanner.release();
			mifScanner = SxRecordScanner.create(new FileInputStream(mifFilename), inputRecordDefinition());
		}
		catch(FileNotFoundException filenotfoundexception1)
		{
			throw new SxImportException("Couldn't open " + mifFilename);
		}
		Vector vector;
		if((vector = getImportFilter().getTargetClasses()).isEmpty())
			targetClass = null;
		targetClass = (SxClass)vector.firstElement();
		return true;
	}

	protected void finishReading()
	{
		if(midScanner != null)
		{
			midScanner.release();
			midScanner = null;
		}
		if(mifScanner != null)
		{
			mifScanner.release();
			mifScanner = null;
		}
	}

	protected static Dictionary MIFRecordDefinitions()
	{
		return RecordDefinitions;
	}

	protected static void initializeMIFRecordDefinitions()
	{
		RecordDefinitions = new Hashtable(25);
		RecordDefinitions.put("Arc", createMifArcRecord());
		RecordDefinitions.put("ArcNoAngles", createMifArcNoAnglesRecord());
		RecordDefinitions.put("ArcAngles", createMifArcAnglesRecord());
		RecordDefinitions.put("Coordinate", createMifCoordinateRecord());
		RecordDefinitions.put("Ellipse", createMifEllipseRecord());
		RecordDefinitions.put("GeometryType", createMifGeometryTypeRecord());
		RecordDefinitions.put("Input", createMifInputRecord());
		RecordDefinitions.put("Keyword", createMifKeywordRecord());
		RecordDefinitions.put("Keyword2", createMifTwoKeywordRecord());
		RecordDefinitions.put("Line", createMifLineRecord());
		RecordDefinitions.put("Pline", createMifPlineRecord());
		RecordDefinitions.put("Pline2", createMifPline2Record());
		RecordDefinitions.put("Pline3", createMifPline3Record());
		RecordDefinitions.put("Point", createMifPointRecord());
		RecordDefinitions.put("Rect", createMifRectRecord());
		RecordDefinitions.put("Region", createMifRegionRecord());
		RecordDefinitions.put("RegionNumPts", createMifRegionNumPtsRecord());
		RecordDefinitions.put("RoundRect", createMifRectRecord());
		RecordDefinitions.put("Text", createMifTextRecord());
		RecordDefinitions.put("TextAlone", createMifTextAloneRecord());
		RecordDefinitions.put("TextString", createMifTextStringRecord());
		RecordDefinitions.put("TextRectangle", createMifTextRectangleRecord());
		RecordDefinitions.put("TextFont", createMifTextFontRecord());
		RecordDefinitions.put("TextAngle", createMifTextAngleRecord());
		RecordDefinitions.put("AttributeNames", createMifAttributeNamesRecord());
		RecordDefinitions.put("Columns", createMifColumnsRecord());
		RecordDefinitions.put("Delimiter", createMifDelimiterRecord());
	}

	protected static SxRecordDefinition createMifArcRecord()
	{
		String s = "mif keyword keyword,String long1,Double lat1,Double long2,Double lat2,Double startAngle,Double endAngle,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifArcNoAnglesRecord()
	{
		String s = "mif keyword keyword,String long1,Double lat1,Double long2,Double lat2,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifArcAnglesRecord()
	{
		String s = "mif startAngle startAngle,Double endAngle,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifCoordinateRecord()
	{
		String s = "mif lat long,Double lat,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifEllipseRecord()
	{
		String s = "mif geometryType geometryType,String long1,Double lat1,Double long2,Double lat2,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifGeometryTypeRecord()
	{
		String s = "mif geometryType geometryType,String numberOfGeometrics,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifAttributeNamesRecord()
	{
		String s = "mif attributeName attributeName,String dataType,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifInputRecord()
	{
		String s = "mif inputLine inputLine,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifKeywordRecord()
	{
		String s = "mif keyword keyword,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifTwoKeywordRecord()
	{
		String s = "mif keyword1 keyword1,String keyword2,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifColumnsRecord()
	{
		String s = "mif keyword keyword,String numOfColumns,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifDelimiterRecord()
	{
		String s = "mif keyword keyword,String delimiter,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		sxdelimitedrecorddefinition.setEnquotedLiteralCharacter('"');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifLineRecord()
	{
		String s = "mif geometryType geometryType,String long1,Double lat1,Double long2,Double lat2,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifPlineRecord()
	{
		String s = "mif geometryType geometryType,String numberOfGeometrics,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifPline2Record()
	{
		String s = "mif geometryType geometryType,String multipleKeyword,String numberOfSections,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifPline3Record()
	{
		String s = "mif geometryType geometryType,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifPointRecord()
	{
		String s = "mif geometryType geometryType,String long,Double lat,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifRectRecord()
	{
		String s = "mif geometryType geometryType,String long1,Double lat1,Double long2,Double lat2,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifRegionRecord()
	{
		String s = "mif geometryType geometryType,String numberOfPolygons,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifRegionNumPtsRecord()
	{
		String s = "mif numberOfGeometrics numberOfGeometrics,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifTextRecord()
	{
		String s = "mif geometryType geometryType,String text,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		sxdelimitedrecorddefinition.setEnquotedLiteralCharacter('"');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifTextAloneRecord()
	{
		String s = "mif geometryType geometryType,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifTextStringRecord()
	{
		String s = "mif text text,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		sxdelimitedrecorddefinition.setEnquotedLiteralCharacter('"');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition createMifTextRectangleRecord()
	{
		String s = "mif long1 long1,Double lat1,Double long2,Double lat2,Double";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

//FONT («название_шрифта», стиль, размер, основной_цвет [цвет_фона])
//style
//0 Plain 
//1 Bold 
//2 Italic 
//4 Underline 
//16 Outline (only supported on the Macintosh) 
//32 Shadow 
//256 Halo 
//512 All Caps 
//1024 Expanded 

//Color
//RGB = (красный * 65536) + (зеленый * 256) + синий 

//kwd = '    Font ("'
//fontname = 'Arial Cyr'
//fontparams = ',0,0,0)'
	protected static SxRecordDefinition createMifTextFontRecord()
	{
		String s = "mif kwd kwd,String fontname,String fontparams,String";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter('"');
		return sxdelimitedrecorddefinition;
	}

//Angle угол_поворота
	protected static SxRecordDefinition createMifTextAngleRecord()
	{
		String s = "mif kwd kwd,String angle,Integer";
		SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition(s);
		sxdelimitedrecorddefinition.setDelimiterCharacter(' ');
		return sxdelimitedrecorddefinition;
	}

	protected static SxRecordDefinition arcRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Arc");
	}

	protected static SxRecordDefinition arcNoAnglesRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("ArcNoAngles");
	}

	protected static SxRecordDefinition arcAnglesRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("ArcAngles");
	}

	protected static SxRecordDefinition coordinateRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Coordinate");
	}

	protected static SxRecordDefinition ellipseRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Ellipse");
	}

	protected static SxRecordDefinition geometryTypeRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("GeometryType");
	}

	protected static SxRecordDefinition inputRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Input");
	}

	protected static SxRecordDefinition keywordRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Keyword");
	}

	protected static SxRecordDefinition lineRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Line");
	}

	protected static SxRecordDefinition plineRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Pline");
	}

	protected static SxRecordDefinition pointRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Point");
	}

	protected static SxRecordDefinition rectRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Rect");
	}

	protected static SxRecordDefinition regionRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Region");
	}

	protected static SxRecordDefinition regionNumPtsRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("RegionNumPts");
	}

	protected static SxRecordDefinition roundRectRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("RoundRect");
	}

	protected static SxRecordDefinition textRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Text");
	}

	protected static SxRecordDefinition textAloneRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("TextAlone");
	}

	protected static SxRecordDefinition textStringRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("TextString");
	}

	protected static SxRecordDefinition textRectangleRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("TextRectangle");
	}

	protected static SxRecordDefinition textFontRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("TextFont");
	}

	protected static SxRecordDefinition textAngleRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("TextAngle");
	}

	protected static SxRecordDefinition attributeNamesRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("AttributeNames");
	}

	protected static SxRecordDefinition columnsRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Columns");
	}

	protected static SxRecordDefinition delimiterRecordDefinition()
	{
		return (SxRecordDefinition)RecordDefinitions.get("Delimiter");
	}

	private static final String MSG_FILE_SYNC = "MID/MIF files out of sync";
	private static final String MSG_TEXT_ATTR = "No text attribute for MapInfo \"Text\" object";
	private static final String MSG_PT_COUNT = "Inappropriate number of points read";
	private static final String MSG_MIF_GEOM = "Unknown or inappropriate MapInfo geometry encountered";
	private static final String MSG_SPATIAL_GEOM = "Unknown spatial geometry type encountered";
	private static final String MSG_SCANNER = "Parse error";
	private static Hashtable keywordTable;
	private String compoundGeometryId;
	private String compoundGeometryLabel;
	private Vector compoundGeometry;
	private int currentCompoundGeometryIndex;
	private boolean processingCompoundGeometry;
	private SxClass targetClass;
	private Vector nextGeoResult;
	private String midFilename;
	private SxDataRecord theMIDRecord;
	private SxRecordScanner midScanner;
	private String mifFilename;
	private SxRecordScanner mifScanner;
	private static Hashtable SymbologyKeywords;
	private static Hashtable RecordDefinitions;

	static 
	{
		initializeMIFRecordDefinitions();
		SymbologyKeywords = new Hashtable(2);
		SymbologyKeywords.put("Pen", "Pen");
		SymbologyKeywords.put("Symbol", "Symbol");
	}
}
