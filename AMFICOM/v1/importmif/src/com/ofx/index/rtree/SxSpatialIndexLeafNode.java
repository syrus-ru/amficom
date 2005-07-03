// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.index.rtree;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.base.SxProperties;
import com.ofx.base.SxStreamObjectPool;
import com.ofx.base.SxUtil;
import com.ofx.collections.SxComparatorInterface;
import com.ofx.collections.SxQSort;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxGeometryCollection;
import com.ofx.geometry.SxGeometryInterface;
import com.ofx.geometry.SxPoint;
import com.ofx.geometry.SxRectangle;
import com.ofx.geometry.SxText;
import com.ofx.geometry.temp_sxtext_class;
import com.ofx.index.SxIndexable;
import com.ofx.persistence.SxCache;
import com.ofx.persistence.SxDataInputStream;
import com.ofx.persistence.SxStringCondenser;
import com.ofx.query.SxQueriableObjectInterface;
import com.ofx.query.SxQueryInterface;
import com.ofx.repository.SxClass;
import com.ofx.server.SxObjectPool;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.zip.InflaterInputStream;

// Referenced classes of package com.ofx.index.rtree:
//			SxRtreeSearchResult

public class SxSpatialIndexLeafNode
	implements java.io.Serializable, com.ofx.query.SxQueriableObjectInterface
{
	public class SxLexIdComparator
		implements com.ofx.collections.SxComparatorInterface
	{

		public int rank(java.lang.Object obj, java.lang.Object obj1)
		{
			if(obj == null && obj1 == null)
				return 0;
			if(obj == null)
				return 1;
			if(obj1 == null)
				return -1;
			com.ofx.query.SxQueriableObjectInterface sxqueriableobjectinterface = (com.ofx.query.SxQueriableObjectInterface)obj;
			com.ofx.query.SxQueriableObjectInterface sxqueriableobjectinterface1 = (com.ofx.query.SxQueriableObjectInterface)obj1;
			java.lang.String s = (java.lang.String)sxqueriableobjectinterface.getIdentifier();
			java.lang.String s1 = (java.lang.String)sxqueriableobjectinterface1.getIdentifier();
			int i = s.compareTo(s1);
			if(i < 0)
				return 1;
			return i != 0 ? -1 : 0;
		}

		public SxLexIdComparator()
		{
		}
	}

	public class Enumerator
		implements java.util.Enumeration
	{

		public boolean hasMoreElements()
		{
			return nextElementExists;
		}

		public java.lang.Object nextElement()
		{
			if(!nextElementExists)
				throw new NoSuchElementException();
			if(nextElementIsObject)
			{
				if(reusingSpatialObject)
				{
					reusedSpatialObject.setInternalState(nextObjectToReuse);
					nextElement = reusedSpatialObject;
				} else
				{
					nextElement = nextObjectToReuse;
				}
			} else
			{
				int i = 0;
				int j = 0;
				int k = nextObjectToReuseIndex - numOfCollections;
				if(isPointType)
				{
					i = k * 2;
					j = i + 2;
				} else
				{
					i = vOffsets[k];
					j = i + getNumOfCoordsAt(k);
				}
				switch(nodeFormat)
				{
				case 1: // '\001'
					reusedSpatialObject.setInternalState(nextObjectToReuseIndex, com.ofx.index.rtree.SxSpatialIndexLeafNode.this, getLabelAt(nextObjectToReuseIndex), i, j, vInts, nodeRefPoint, leftMultiplier, extraAttributes);
					if(isTextType)
					{
						SxText sxtext = (SxText )reusedSpatialObject.getGeometry();
						sxtext.settextcontents(textsOfCollection[k]);
					}
					break;

				case 2: // '\002'
					reusedSpatialObject.setInternalState(nextObjectToReuseIndex, com.ofx.index.rtree.SxSpatialIndexLeafNode.this, getLabelAt(nextObjectToReuseIndex), i, j, vDoubles, extraAttributes);
					break;

				default:
					throw new RuntimeException("SxSpatialIndexLeafNode/Enumerator.nextElement() unknown node format: " + nodeFormat);
				}
				nextElement = reusedSpatialObject;
			}
			nextElementExists = getNextElement();
			return nextElement;
		}

		protected boolean getNextElement()
		{
			boolean flag = true;
			if(isReversed)
				flag = nextElementIndex >= 0;
			else
				flag = nextElementIndex < numOfObjects;
			nextElementIsObject = false;
			boolean flag1;
			for(flag1 = false; flag && !flag1;)
			{
				nextObjectToReuseIndex = nextElementIndex;
				if(cachingSpatialObjects)
				{
					if(isReversed)
						nextObjectToReuse = getSpatialObjectsInNode()[nextElementIndex];
					else
						nextObjectToReuse = getSpatialObjectsInNode()[nextElementIndex];
					if(isFiltered || targetRectangle == null || targetRectangle.intersectsOrTouches(nextObjectToReuse.getBounds()))
					{
						nextElementIsObject = true;
						flag1 = true;
					}
				} else
				{
					switch(geometryType)
					{
					case 0: // '\0'
						if(nextElementIndex >= numOfCollections)
						{
							if(intersectsOrTouchesRawGeometryBounds(nextElementIndex, targetRectangle))
							{
								if(!reusingSpatialObject)
								{
									nextElementIsObject = true;
									int i = nextElementIndex - numOfCollections;
									if(hasExtraAttributes)
										xattributes = extraAttributes[nextElementIndex];
									nextObjectToReuse = com.ofx.base.SxUtil.createPointSpatialObjectFrom(classDefn, javaClass, getIds()[nextElementIndex], getLabelAt(nextElementIndex), getXPointsAt(i), getYPointsAt(i), xattributes);
								}
								flag1 = true;
							}
							break;
						}
						nextObjectToReuse = collectionObjects[nextElementIndex];
						if(targetRectangle == null || targetRectangle.intersectsOrTouches(nextObjectToReuse.getBounds()))
						{
							nextElementIsObject = true;
							flag1 = true;
						}
						break;

					case 3: // '\003'
						if(nextElementIndex >= numOfCollections)
						{
							if(intersectsOrTouchesRawGeometryBounds(nextElementIndex, targetRectangle))
							{
								if(!reusingSpatialObject)
								{
									nextElementIsObject = true;
									int i = nextElementIndex - numOfCollections;
									if(hasExtraAttributes)
										xattributes = extraAttributes[nextElementIndex];
									nextObjectToReuse = com.ofx.base.SxUtil.createTextSpatialObjectFrom(classDefn, javaClass, getIds()[nextElementIndex], getLabelAt(nextElementIndex), getXPointsAt(i), getYPointsAt(i), xattributes);
									SxText sxtext = (SxText )nextObjectToReuse.getGeometry();
									sxtext.settextcontents(textsOfCollection[i]);
								}
								flag1 = true;
							}
							break;
						}
						nextObjectToReuse = collectionObjects[nextElementIndex];
						if(targetRectangle == null || targetRectangle.intersectsOrTouches(nextObjectToReuse.getBounds()))
						{
							nextElementIsObject = true;
							flag1 = true;
						}
						break;

					case 1: // '\001'
						if(nextElementIndex >= numOfCollections)
						{
							if(!intersectsOrTouchesRawGeometryBounds(nextElementIndex, targetRectangle))
								break;
							if(!reusingSpatialObject)
							{
								nextElementIsObject = true;
								int j = nextElementIndex - numOfCollections;
								double ad[] = getXPointsAt(j);
								if(hasExtraAttributes)
									xattributes = extraAttributes[nextElementIndex];
								nextObjectToReuse = com.ofx.base.SxUtil.createPolylineSpatialObjectFrom(classDefn, javaClass, getIds()[nextElementIndex], getLabelAt(nextElementIndex), ad, getYPointsAt(j), ad.length, xattributes);
							}
							flag1 = true;
							break;
						}
						nextObjectToReuse = collectionObjects[nextElementIndex];
						if(targetRectangle == null || targetRectangle.intersectsOrTouches(nextObjectToReuse.getBounds()))
						{
							nextElementIsObject = true;
							flag1 = true;
						}
						break;

					case 2: // '\002'
						if(nextElementIndex >= numOfCollections)
						{
							if(!intersectsOrTouchesRawGeometryBounds(nextElementIndex, targetRectangle))
								break;
							if(!reusingSpatialObject)
							{
								nextElementIsObject = true;
								int k = nextElementIndex - numOfCollections;
								double ad1[] = getXPointsAt(k);
								if(hasExtraAttributes)
									xattributes = extraAttributes[nextElementIndex];
								nextObjectToReuse = com.ofx.base.SxUtil.createPolygonSpatialObjectFrom(classDefn, javaClass, getIds()[nextElementIndex], getLabelAt(nextElementIndex), ad1, getYPointsAt(k), ad1.length, xattributes);
							}
							flag1 = true;
							break;
						}
						nextObjectToReuse = collectionObjects[nextElementIndex];
						if(targetRectangle == null || targetRectangle.intersectsOrTouches(nextObjectToReuse.getBounds()))
						{
							nextElementIsObject = true;
							flag1 = true;
						}
						break;

					default:
						throw new RuntimeException("SxSpatialIndexLeafNode.getNextElement() unknown geometryType: " + geometryType);
					}
				}
				if(isReversed)
				{
					nextElementIndex--;
					flag = nextElementIndex >= 0;
				} else
				{
					nextElementIndex++;
					flag = nextElementIndex < numOfObjects;
				}
			}

			return flag1;
		}

		private com.ofx.index.SxIndexable reusedSpatialObject;
		private int nextElementIndex;
		private int numOfObjects;
		private com.ofx.geometry.SxRectangle targetRectangle;
		private int geometryType;
		private boolean hasExtraAttributes;
		private java.lang.Object xattributes;
		private com.ofx.repository.SxClass classDefn;
		private java.lang.Class javaClass;
		private boolean nextElementExists;
		private com.ofx.index.SxIndexable nextElement;
		private boolean reusingSpatialObject;
		private int nextObjectToReuseIndex;
		private boolean nextElementIsObject;
		private com.ofx.index.SxIndexable nextObjectToReuse;
		private boolean isReversed;

		private Enumerator()
		{
			nextElementIndex = 0;
			numOfObjects = size();
			hasExtraAttributes = false;
			xattributes = null;
			nextElementExists = false;
			nextElement = null;
			reusingSpatialObject = false;
			nextObjectToReuseIndex = -1;
			nextElementIsObject = false;
			nextObjectToReuse = null;
			isReversed = false;
		}

		public Enumerator(com.ofx.index.SxIndexable sxindexable, com.ofx.geometry.SxRectangle sxrectangle)
		{
			this(sxindexable, sxrectangle, false);
		}

		public Enumerator(com.ofx.index.SxIndexable sxindexable, com.ofx.geometry.SxRectangle sxrectangle, boolean flag)
		{
			nextElementIndex = 0;
			numOfObjects = size();
			hasExtraAttributes = false;
			xattributes = null;
			nextElementExists = false;
			nextElement = null;
			reusingSpatialObject = false;
			nextObjectToReuseIndex = -1;
			nextElementIsObject = false;
			nextObjectToReuse = null;
			isReversed = false;
			isReversed = flag;
			if(isReversed)
				nextElementIndex = numOfObjects - 1;
			reusedSpatialObject = sxindexable;
			reusingSpatialObject = reusedSpatialObject != null;
			targetRectangle = sxrectangle;
			classDefn = getClassDefinition();
			geometryType = classDefn.getDimension();
			hasExtraAttributes = false;
			javaClass = null;
			if(!classDefn.getIsUserDefined())
			{
				hasExtraAttributes = classDefn.getHasExtraAttributes();
				java.lang.String s = classDefn.getJavaClassName();
				try
				{
					javaClass = java.lang.Class.forName(s);
				}
				catch(java.lang.Exception exception)
				{
					throw new RuntimeException("SxSpatialIndexLeafNode.Enumerator() constructor class named: " + s + " does not exist.");
				}
			}
			nextElementExists = getNextElement();
		}
	}


	protected SxSpatialIndexLeafNode()
	{
		nodeFormat = -1;
		vOffsets = null;
		vInts = null;
		vDoubles = null;
		extraAttributes = null;
		numOfCollections = 0;
		numCollGeometries = 0;
		numCollVerts = 0;
		collectionObjects = null;
		numNonCollVerts = 0;
		numNonCollObjects = 0;
		count = 0;
		capacity = 0;
		cachingSpatialObjects = false;
		precisionTolerance = 0.10000000000000001D;
		rightMultiplier = 100D;
		leftMultiplier = 0.01D;
		spatialCacheMode = "RawGeometry";
		isCompressed = false;
		idBytes = null;
		nonCollIdStartPos = 0;
		initSpatialObjects = false;
		isFiltered = false;
		labelsAreCondensed = true;
		condensedLabelsIndexes = null;
		condensedLabels = null;
		sharedLabels = null;
		numOfLabelChars = 0;
		numOfIdChars = 0;
		isPointType = false;
		isTextType = false;
		cacheRef = null;
		sourceBytes = null;
	}

	public SxSpatialIndexLeafNode(com.ofx.repository.SxClass sxclass, long l, java.lang.String s, double d)
	{
		nodeFormat = -1;
		vOffsets = null;
		vInts = null;
		vDoubles = null;
		extraAttributes = null;
		numOfCollections = 0;
		numCollGeometries = 0;
		numCollVerts = 0;
		collectionObjects = null;
		numNonCollVerts = 0;
		numNonCollObjects = 0;
		count = 0;
		capacity = 0;
		cachingSpatialObjects = false;
		precisionTolerance = 0.10000000000000001D;
		rightMultiplier = 100D;
		leftMultiplier = 0.01D;
		spatialCacheMode = "RawGeometry";
		isCompressed = false;
		idBytes = null;
		nonCollIdStartPos = 0;
		initSpatialObjects = false;
		isFiltered = false;
		labelsAreCondensed = true;
		condensedLabelsIndexes = null;
		condensedLabels = null;
		sharedLabels = null;
		numOfLabelChars = 0;
		numOfIdChars = 0;
		isPointType = false;
		isTextType = false;
		cacheRef = null;
		sourceBytes = null;
		isPointType = sxclass.isPointType();
		isTextType = sxclass.isTextType();
		precisionTolerance = d;
		setIdentifier(java.lang.Long.toString(l));
		classDefinition = sxclass;
		setNodeSizeInBytes(0);
		setMemoryNodeSizeInBytes(0);
		queryClassification = getClassification() + com.ofx.index.rtree.SxSpatialIndexLeafNode.getSpatialIndexSuffix();
		if(isPointType)
			capacity = 1500;
		else
		if(sxclass.isTextType())
			capacity = 1000;
		else
		if(getClassDefinition().isPolylineType())
			capacity = 1000;
		else
			capacity = 500;
		if(getClassDefinition().getIsUserDefined())
			setSpatialCacheMode("SpatialObjects");
		else
			setSpatialCacheMode("SpatialObjects");
		boundsOfNode = null;
	}

	public SxSpatialIndexLeafNode(java.lang.String s, com.ofx.repository.SxClass sxclass, long l, double d, int i, 
			com.ofx.index.SxIndexable asxindexable[])
	{
		nodeFormat = -1;
		vOffsets = null;
		vInts = null;
		vDoubles = null;
		extraAttributes = null;
		numOfCollections = 0;
		numCollGeometries = 0;
		numCollVerts = 0;
		collectionObjects = null;
		numNonCollVerts = 0;
		numNonCollObjects = 0;
		count = 0;
		capacity = 0;
		cachingSpatialObjects = false;
		precisionTolerance = 0.10000000000000001D;
		rightMultiplier = 100D;
		leftMultiplier = 0.01D;
		spatialCacheMode = "RawGeometry";
		isCompressed = false;
		idBytes = null;
		nonCollIdStartPos = 0;
		initSpatialObjects = false;
		isFiltered = false;
		labelsAreCondensed = true;
		condensedLabelsIndexes = null;
		condensedLabels = null;
		sharedLabels = null;
		numOfLabelChars = 0;
		numOfIdChars = 0;
		isPointType = false;
		isTextType = false;
		cacheRef = null;
		sourceBytes = null;
		isPointType = sxclass.isPointType();
		isTextType = sxclass.isTextType();
		precisionTolerance = d;
		setIdentifier(java.lang.Long.toString(l));
		classDefinition = sxclass;
		setNodeSizeInBytes(0);
		setMemoryNodeSizeInBytes(0);
		queryClassification = s;
		setSpatialObjectsInNode(asxindexable);
		count = i;
		setSpatialIdentifiersInNode(buildSpatialIdentifiersInNode());
		cachingSpatialObjects = true;
		spatialCacheMode = "SpatialObjects";
		boundsOfNode = null;
		isFiltered = true;
	}

	public SxSpatialIndexLeafNode(byte abyte0[], com.ofx.repository.SxClass sxclass, boolean flag, java.lang.String s, double d, java.lang.String as[])
	{
		nodeFormat = -1;
		vOffsets = null;
		vInts = null;
		vDoubles = null;
		extraAttributes = null;
		numOfCollections = 0;
		numCollGeometries = 0;
		numCollVerts = 0;
		collectionObjects = null;
		numNonCollVerts = 0;
		numNonCollObjects = 0;
		count = 0;
		capacity = 0;
		cachingSpatialObjects = false;
		precisionTolerance = 0.10000000000000001D;
		rightMultiplier = 100D;
		leftMultiplier = 0.01D;
		spatialCacheMode = "RawGeometry";
		isCompressed = false;
		idBytes = null;
		nonCollIdStartPos = 0;
		initSpatialObjects = false;
		isFiltered = false;
		labelsAreCondensed = true;
		condensedLabelsIndexes = null;
		condensedLabels = null;
		sharedLabels = null;
		numOfLabelChars = 0;
		numOfIdChars = 0;
		isPointType = false;
		isTextType = false;
		cacheRef = null;
		sourceBytes = null;
		isPointType = sxclass.isPointType();
		isTextType = sxclass.isTextType();
		sharedLabels = as;
		precisionTolerance = d;
		classDefinition = sxclass;
		queryClassification = getClassification() + com.ofx.index.rtree.SxSpatialIndexLeafNode.getSpatialIndexSuffix();
		setNodeSizeInBytes(abyte0.length);
		boundsOfNode = null;
		Object obj = null;
		isCompressed = flag;
		if(isPointType)
		{
			if(flag)
				nodeFromBinary(abyte0, abyte0.length, sxclass);
			else
				nodeFromBinary(abyte0, abyte0.length, sxclass);
		} else
		if(flag)
			nodeFromBinary(abyte0, abyte0.length, sxclass);
		else
			nodeFromBinary(abyte0, abyte0.length, sxclass);
		if(com.ofx.index.rtree.SxSpatialIndexLeafNode.CacheBlobsForTransport.booleanValue())
		{
			setMemoryNodeSizeInBytes(getMemoryNodeSizeInBytes() + abyte0.length);
			setSourceBytes(abyte0);
		} else
		{
			setSourceBytes(null);
		}
		setSpatialCacheMode(s);
		getBounds();
	}

	public static java.lang.String getSpatialIndexSuffix()
	{
		return "IN";
	}

	public final boolean intersectsOrTouchesRawGeometryBounds(int i, com.ofx.geometry.SxRectangle sxrectangle)
	{
		if(sxrectangle == null)
			return true;
		if(isPointType)
		{
			double d = 0.0D;
			double d1 = 0.0D;
			int j1 = i - numOfCollections;
			int k1 = j1 * 2;
			switch(nodeFormat)
			{
			case 1: // '\001'
				d = nodeRefPoint.x + (double)vInts[k1++] * leftMultiplier;
				d1 = nodeRefPoint.y + (double)vInts[k1] * leftMultiplier;
				break;

			case 2: // '\002'
				d = vDoubles[k1++];
				d1 = vDoubles[k1];
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.intersectsOrTouchesRawGeometryBounds() unknown node format: " + nodeFormat);
			}
			return d >= sxrectangle.x && d - sxrectangle.x <= sxrectangle.width && d1 >= sxrectangle.y && d1 - sxrectangle.y <= sxrectangle.height;
		}
		int j = i - numOfCollections;
		int k = vOffsets[j + 1] - vOffsets[j];
		int l = (int)((double)k * 0.5D);
		int i1 = vOffsets[j];
		double d2 = 1.7976931348623157E+308D;
		double d3 = 1.7976931348623157E+308D;
		double d4 = -1.7976931348623157E+308D;
		double d5 = -1.7976931348623157E+308D;
		switch(nodeFormat)
		{
		case 1: // '\001'
			int l1 = 0x7fffffff;
			int i2 = 0x7fffffff;
			int j2 = 0x80000000;
			int k2 = 0x80000000;
			for(int l2 = 0; l2 < l; l2++)
			{
				int i3 = vInts[i1];
				int j3 = vInts[i1 + l];
				if(i3 < l1)
					l1 = i3;
				if(i3 > j2)
					j2 = i3;
				if(j3 < i2)
					i2 = j3;
				if(j3 > k2)
					k2 = j3;
				i1++;
			}

			d2 = nodeRefPoint.x + (double)l1 * leftMultiplier;
			d3 = nodeRefPoint.y + (double)i2 * leftMultiplier;
			d4 = nodeRefPoint.x + (double)j2 * leftMultiplier;
			d5 = nodeRefPoint.y + (double)k2 * leftMultiplier;
			break;

		case 2: // '\002'
			double d6 = 0.0D;
			double d8 = 0.0D;
			double d10 = sxrectangle.x;
			double d11 = sxrectangle.x + sxrectangle.width;
			double d12 = sxrectangle.y;
			double d13 = sxrectangle.y + sxrectangle.height;
			for(int k3 = 0; k3 < l; k3++)
			{
				double d7 = vDoubles[i1];
				double d9 = vDoubles[i1 + l];
				if(d7 >= d10 && d7 <= d11 && d9 >= d12 && d9 <= d13)
					return true;
				if(d7 < d2)
					d2 = d7;
				if(d7 > d4)
					d4 = d7;
				if(d9 < d3)
					d3 = d9;
				if(d9 > d5)
					d5 = d9;
				i1++;
			}

			break;

		default:
			throw new RuntimeException("SxSpatialIndexLeafNode.intersectsOrTouchesRawGeometryBounds() unknown node format: " + nodeFormat);
		}
		return sxrectangle.x + sxrectangle.width >= d2 && sxrectangle.y + sxrectangle.height >= d3 && sxrectangle.x <= d4 && sxrectangle.y <= d5;
	}

	public static final boolean intersectsOrTouchesRawGeometryBounds(double ad[], com.ofx.geometry.SxRectangle sxrectangle)
	{
		if(sxrectangle == null)
			return true;
		int i = (int)((double)ad.length * 0.5D);
		double d = 0.0D;
		double d3 = 0.0D;
		for(int j = 0; j < i; j++)
		{
			double d1 = ad[j];
			double d4 = ad[j + i];
			if(d1 >= sxrectangle.x && d1 - sxrectangle.x <= sxrectangle.width && d4 >= sxrectangle.y && d4 - sxrectangle.y <= sxrectangle.height)
				return true;
		}

		double d6 = 1.7976931348623157E+308D;
		double d7 = 1.7976931348623157E+308D;
		double d8 = -1.7976931348623157E+308D;
		double d9 = -1.7976931348623157E+308D;
		for(int k = 0; k < i; k++)
		{
			double d2 = ad[k];
			if(d2 < d6)
				d6 = d2;
			if(d2 > d8)
				d8 = d2;
			double d5 = ad[k + i];
			if(d5 < d7)
				d7 = d5;
			if(d5 > d9)
				d9 = d5;
		}

		return sxrectangle.x + sxrectangle.width >= d6 && sxrectangle.y + sxrectangle.height >= d7 && sxrectangle.x <= d6 + (d8 - d6) && sxrectangle.y <= d7 + (d9 - d7);
	}

	public java.lang.String getSpatialCacheMode()
	{
		return spatialCacheMode;
	}

	public void setSpatialCacheMode(java.lang.String s)
	{
		if(s.equalsIgnoreCase("SpatialObjects"))
		{
			if(!cachingSpatialObjects)
				switchToCachingSpatialObjects();
		} else
		if(!s.equalsIgnoreCase("RawGeometry"))
			throw new RuntimeException("SxSpatialIndexLeafNode.setSpatialCacheMode bad value for parameter aSpatialCacheMode:" + s);
		spatialCacheMode = s;
	}

	public void add(com.ofx.index.SxIndexable sxindexable)
	{
		if(!cachingSpatialObjects)
			switchToCachingSpatialObjects();
		int i = size() + 1;
		if(i >= capacity)
			growCapacity();
		count++;
		getSpatialObjectsInNode()[count - 1] = sxindexable;
		getSpatialIdentifiersInNode().put(sxindexable.getIdentifier(), sxindexable);
		setNodeSizeInBytes(getNodeSizeInBytes() + sxindexable.getSizeInBytes());
		setMemoryNodeSizeInBytes(getMemoryNodeSizeInBytes() + sxindexable.getMemorySizeInBytes());
		boundsOfNode = null;
	}

	public void replace(com.ofx.index.SxIndexable sxindexable)
	{
		if(!cachingSpatialObjects)
			switchToCachingSpatialObjects();
		getSpatialIdentifiersInNode().put(sxindexable.getIdentifier(), sxindexable);
		initSpatialObjects = true;
		boundsOfNode = null;
	}

	public void remove(com.ofx.index.SxIndexable asxindexable[], int i, int j)
	{
		if(!cachingSpatialObjects)
			switchToCachingSpatialObjects();
		for(int k = 0; k < i; k++)
		{
			count--;
			getSpatialIdentifiersInNode().remove(asxindexable[k].getIdentifier());
		}

		initSpatialObjects = true;
		setNodeSizeInBytes(getNodeSizeInBytes() - j);
		setMemoryNodeSizeInBytes(getMemoryNodeSizeInBytes() - j);
		boundsOfNode = null;
	}

	public void remove(com.ofx.index.SxIndexable sxindexable)
	{
		if(!cachingSpatialObjects)
			switchToCachingSpatialObjects();
		count--;
		initSpatialObjects = true;
		getSpatialIdentifiersInNode().remove(sxindexable.getIdentifier());
		setNodeSizeInBytes(getNodeSizeInBytes() - sxindexable.getSizeInBytes());
		setMemoryNodeSizeInBytes(getMemoryNodeSizeInBytes() - sxindexable.getMemorySizeInBytes());
		boundsOfNode = null;
	}

	public void removeAll()
	{
		if(!cachingSpatialObjects)
			switchToCachingSpatialObjects();
		initSpatialObjects = true;
		getSpatialIdentifiersInNode().clear();
		count = 0;
		setNodeSizeInBytes(0);
		setMemoryNodeSizeInBytes(0);
		boundsOfNode = null;
	}

	public boolean contains(java.lang.String s)
	{
		if(cachingSpatialObjects)
			return getSpatialIdentifiersInNode().get(s) != null;
//		System.out.println("get index of " + s);
		int i = indexOfIdentifier(s.toString());
		return i != -1;
	}

	public java.util.Enumeration elements(com.ofx.index.SxIndexable sxindexable, com.ofx.geometry.SxRectangle sxrectangle)
	{
		return new Enumerator(sxindexable, sxrectangle, false);
	}

	public java.util.Enumeration elements(com.ofx.index.SxIndexable sxindexable, com.ofx.geometry.SxRectangle sxrectangle, boolean flag)
	{
		return new Enumerator(sxindexable, sxrectangle, flag);
	}

	public com.ofx.index.SxIndexable elementAt(int i, com.ofx.index.SxIndexable sxindexable)
	{
		if(cachingSpatialObjects)
			return getSpatialObjectsInNode()[i];
		if(i >= numOfCollections)
		{
			int j = i - numOfCollections;
			if(sxindexable == null)
			{
				double ad[] = getXPointsAt(j);
				com.ofx.repository.SxClass sxclass = getClassDefinition();
				java.lang.Object obj = null;
				if(!sxclass.getIsUserDefined() && sxclass.getHasExtraAttributes())
					obj = extraAttributes[i];
				return com.ofx.base.SxUtil.createSpatialObjectFrom(sxclass, getJavaClass(sxclass), getIds()[i], getLabelAt(i), ad, getYPointsAt(j), ad.length, false, null, null, 0, obj);
			}
			int k = 0;
			int l = 0;
			if(isPointType)
			{
				k = j * 2;
				l = k + 2;
			} else
			{
				k = vOffsets[j];
				l = k + getNumOfCoordsAt(j);
			}
			switch(nodeFormat)
			{
			case 1: // '\001'
				sxindexable.setInternalState(i, this, getLabelAt(i), k, l, vInts, nodeRefPoint, leftMultiplier, extraAttributes);
				break;

			case 2: // '\002'
				sxindexable.setInternalState(i, this, getLabelAt(i), k, l, vDoubles, extraAttributes);
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.elementAt() unknown node format: " + nodeFormat);
			}
			return sxindexable;
		} else
		{
			return collectionObjects[i];
		}
	}

	public com.ofx.index.SxIndexable firstElement()
	{
		int i = size();
		if(i == 0)
			throw new NoSuchElementException("SxSpatialIndexLeafNode.firstElement() size: 0");
		else
			return elementAt(0, null);
	}

	public com.ofx.index.SxIndexable lastElement()
	{
		int i = size();
		if(i == 0)
			throw new NoSuchElementException("SxSpatialIndexLeafNode.lastElement() size: 0");
		else
			return elementAt(i - 1, null);
	}

	public boolean isEmpty()
	{
		return count == 0;
	}

	public int size()
	{
		return count;
	}

	public com.ofx.index.SxIndexable getSpatialObjectByID(java.lang.Object obj)
	{
		if(cachingSpatialObjects)
			return (com.ofx.index.SxIndexable)getSpatialIdentifiersInNode().get(obj);
		int i = indexOfIdentifier(obj.toString());
		if(i == -1)
			return null;
		else
			return elementAt(i, null);
	}

	public void getFilteredNodeIntersecting(com.ofx.geometry.SxRectangle sxrectangle, com.ofx.index.rtree.SxRtreeSearchResult sxrtreesearchresult)
	{
		if(sxrectangle == null || sxrectangle.inside(getBounds()))
		{
			if(cachingSpatialObjects)
			{
				sxrtreesearchresult.addFilteredNode(new SxSpatialIndexLeafNode(getQueryClassification(), getClassDefinition(), getNodeId(), getPrecisionTolerance(), size(), getSpatialObjectsInNode()));
			} else
			{
				java.lang.Object aobj[] = getSpatialObjectsIntersecting(null);
				sxrtreesearchresult.addFilteredNode(new SxSpatialIndexLeafNode(getQueryClassification(), getClassDefinition(), getNodeId(), getPrecisionTolerance(), size(), (com.ofx.index.SxIndexable[])aobj[1]));
			}
			return;
		}
		if(!sxrectangle.intersectsOrTouches(getBounds()))
			return;
		if(cachingSpatialObjects)
		{
			int i = size();
			com.ofx.index.SxIndexable asxindexable[] = new com.ofx.index.SxIndexable[i];
			int k = 0;
			for(int l = 0; l < i; l++)
			{
				com.ofx.index.SxIndexable sxindexable = getSpatialObjectsInNode()[l];
				if(sxrectangle.intersectsOrTouches(sxindexable.getBounds()))
					asxindexable[k++] = sxindexable;
			}

			if(k > 0)
				sxrtreesearchresult.addFilteredNode(new SxSpatialIndexLeafNode(getQueryClassification(), getClassDefinition(), getNodeId(), getPrecisionTolerance(), k, asxindexable));
			return;
		}
		java.lang.Object aobj1[] = getSpatialObjectsIntersecting(sxrectangle);
		int j = ((java.lang.Integer)aobj1[0]).intValue();
		com.ofx.index.SxIndexable asxindexable1[] = (com.ofx.index.SxIndexable[])aobj1[1];
		if(j > 0)
			sxrtreesearchresult.addFilteredNode(new SxSpatialIndexLeafNode(getQueryClassification(), getClassDefinition(), getNodeId(), getPrecisionTolerance(), j, asxindexable1));
	}

	protected com.ofx.index.SxIndexable[] getSpatialObjects()
	{
		if(cachingSpatialObjects)
			return getSpatialObjectsInNode();
		int i = size();
		if(i > 0)
		{
			java.lang.Object aobj[] = getSpatialObjectsIntersecting(null);
			return (com.ofx.index.SxIndexable[])aobj[1];
		} else
		{
			return new com.ofx.index.SxIndexable[capacity];
		}
	}

	public com.ofx.geometry.SxDoublePoint getCenter()
	{
		if(isEmpty())
			return new SxDoublePoint(0, 0);
		else
			return getBounds().getCenter();
	}

	public double getPrecisionTolerance()
	{
		return precisionTolerance;
	}

	public boolean isCompressed()
	{
		return isCompressed;
	}

	public byte[] getIdBytes()
	{
		return idBytes;
	}

	public byte[] getSourceBytes()
	{
		if(sourceBytes == null)
			return binaryForSpatialObjectNode(getPrecisionTolerance(), isCompressed());
		else
			return sourceBytes;
	}

	public void setSourceBytes(byte abyte0[])
	{
		sourceBytes = abyte0;
	}

	public com.ofx.geometry.SxRectangle calculateBounds()
	{
		com.ofx.geometry.SxRectangle sxrectangle = new SxRectangle(0.0D, 0.0D, 0.0D, 0.0D);
		if(size() == 0)
			return sxrectangle;
		if(cachingSpatialObjects)
		{
			com.ofx.index.SxIndexable asxindexable[] = getSpatialObjectsInNode();
			com.ofx.geometry.SxRectangle sxrectangle1 = asxindexable[0].getBounds();
			double d1 = sxrectangle1.x;
			double d3 = sxrectangle1.width;
			double d5 = sxrectangle1.y;
			double d7 = sxrectangle1.height;
			Object obj = null;
			int l2 = size();
			for(int j3 = 1; j3 < l2; j3++)
			{
				com.ofx.geometry.SxRectangle sxrectangle2 = asxindexable[j3].getBounds();
				double d8 = java.lang.Math.min(d1, sxrectangle2.x);
				double d9 = java.lang.Math.max(d1 + d3, sxrectangle2.x + sxrectangle2.width);
				double d13 = java.lang.Math.min(d5, sxrectangle2.y);
				double d17 = java.lang.Math.max(d5 + d7, sxrectangle2.y + sxrectangle2.height);
				d1 = d8;
				d5 = d13;
				d3 = d9 - d8;
				d7 = d17 - d13;
			}

			sxrectangle1 = new SxRectangle(d1, d5, d3, d7);
			return sxrectangle1;
		}
		double d = 1.7976931348623157E+308D;
		double d2 = 1.7976931348623157E+308D;
		double d4 = -1.7976931348623157E+308D;
		double d6 = -1.7976931348623157E+308D;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		double d10 = 0.0D;
		double d14 = 0.0D;
		int j2 = size();
		switch(nodeFormat)
		{
		case 1: // '\001'
			for(int k2 = 0; k2 < j2; k2++)
			{
				int i;
				int i1;
				if(isPointType)
				{
					i = k2 * 2;
					i1 = 1;
				} else
				{
					i = vOffsets[k2];
					int k = getNumOfCoordsAt(k2);
					i1 = (int)((double)k * 0.5D);
				}
				boolean flag4 = false;
				for(int k1 = 0; k1 < i1; k1++)
				{
					double d11 = nodeRefPoint.x + (double)vInts[i] * leftMultiplier;
					d = java.lang.Math.min(d, d11);
					d4 = java.lang.Math.max(d4, d11);
					double d15 = nodeRefPoint.y + (double)vInts[i + i1] * leftMultiplier;
					d2 = java.lang.Math.min(d2, d15);
					d6 = java.lang.Math.max(d6, d15);
					i++;
				}

			}

			break;

		case 3: // '\003'
			for(int k2 = 0; k2 < j2; k2++)
			{
				int i;
				int i1;
				if(isPointType)
				{
					i = k2 * 2;
					i1 = 1;
				} else
				{
					i = vOffsets[k2];
					int k = getNumOfCoordsAt(k2);
					i1 = (int)((double)k * 0.5D);
				}
				boolean flag4 = false;
				for(int k1 = 0; k1 < i1; k1++)
				{
					double d11 = nodeRefPoint.x + (double)vInts[i] * leftMultiplier;
					d = java.lang.Math.min(d, d11);
					d4 = java.lang.Math.max(d4, d11);
					double d15 = nodeRefPoint.y + (double)vInts[i + i1] * leftMultiplier;
					d2 = java.lang.Math.min(d2, d15);
					d6 = java.lang.Math.max(d6, d15);
					i++;
				}

			}

			break;

		case 2: // '\002'
			for(int i3 = 0; i3 < j2; i3++)
			{
				int j;
				int j1;
				if(isPointType)
				{
					j = i3 * 2;
					j1 = 1;
				} else
				{
					j = vOffsets[i3];
					int l = getNumOfCoordsAt(i3);
					j1 = (int)((double)l * 0.5D);
				}
				boolean flag5 = false;
				for(int l1 = 0; l1 < j1; l1++)
				{
					double d12 = vDoubles[j];
					d = java.lang.Math.min(d, d12);
					d4 = java.lang.Math.max(d4, d12);
					double d16 = vDoubles[j + j1];
					d2 = java.lang.Math.min(d2, d16);
					d6 = java.lang.Math.max(d6, d16);
					j++;
				}

			}

			break;

		default:
			throw new RuntimeException("SxSpatialIndexLeafNode.calculateBounds() unknown node format: " + nodeFormat);
		}
		if(numOfCollections > 0)
		{
			Object obj1 = null;
			for(int i2 = 0; i2 < numOfCollections; i2++)
			{
				com.ofx.geometry.SxRectangle sxrectangle3 = collectionObjects[i2].getBounds();
				d = java.lang.Math.min(d, sxrectangle3.x);
				d4 = java.lang.Math.max(d4, sxrectangle3.x + sxrectangle3.width);
				d2 = java.lang.Math.min(d2, sxrectangle3.y);
				d6 = java.lang.Math.max(d6, sxrectangle3.y + sxrectangle3.height);
			}

		}
		return new SxRectangle(d, d2, d4 - d, d6 - d2);
	}

	public com.ofx.geometry.SxRectangle getBounds()
	{
		if(boundsOfNode != null)
		{
			return boundsOfNode;
		} else
		{
			boundsOfNode = calculateBounds();
			return boundsOfNode;
		}
	}

	public long getNodeId()
	{
		return (new Long(ID)).longValue();
	}

	public java.lang.Object getIdentifier()
	{
		return ID;
	}

	public void setIdentifier(java.lang.Object obj)
	{
		ID = obj.toString();
	}

	public java.lang.String getSpatialClassification()
	{
		return getClassDefinition().getClassificationName();
	}

	public java.lang.String getQueryClassification()
	{
		return queryClassification;
	}

	public java.lang.String getQueryClassification(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		return getClassification() + com.ofx.index.rtree.SxSpatialIndexLeafNode.getSpatialIndexSuffix();
	}

	public void resolveSoftLinks(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
	}

	public boolean isSpatial()
	{
		return false;
	}

	public com.ofx.geometry.SxGeometryInterface getGeometry()
	{
		return null;
	}

	public java.lang.String getClassification()
	{
		return getClassDefinition().getClassificationName();
	}

	public com.ofx.repository.SxClass getClassDefinition()
	{
		return classDefinition;
	}

	public java.lang.Class getJavaClass(com.ofx.repository.SxClass sxclass)
	{
		java.lang.Class class1 = null;
		if(!sxclass.getIsUserDefined())
		{
			java.lang.String s = sxclass.getJavaClassName();
			if(s == null)
				return null;
			try
			{
				class1 = java.lang.Class.forName(s);
			}
			catch(java.lang.Exception exception)
			{
				throw new RuntimeException("SxSpatialIndexLeafNode.getJavaClass() java class named: " + s + " does not exist.");
			}
		}
		return class1;
	}

	public com.ofx.index.SxIndexable getJavaClassInstance(com.ofx.repository.SxClass sxclass)
	{
		com.ofx.index.SxIndexable sxindexable = null;
		double ad[] = new double[1];
		double ad1[] = new double[1];
		ad[0] = 0.0D;
		ad1[0] = 0.0D;
		if(sxclass.isPointType())
			sxindexable = com.ofx.base.SxUtil.createNonUserDefinedPointSpatialObjectFrom(getJavaClass(sxclass), sxclass, "123", "", ad, ad1);
		else
		if(sxclass.isPolylineType())
			sxindexable = com.ofx.base.SxUtil.createNonUserDefinedPolylineSpatialObjectFrom(getJavaClass(sxclass), sxclass, "123", "", ad, ad1, 1);
		else
		if(sxclass.isPolygonType())
			sxindexable = com.ofx.base.SxUtil.createNonUserDefinedPolygonSpatialObjectFrom(getJavaClass(sxclass), sxclass, "123", "", ad, ad1, 1);
		else
		if(sxclass.isTextType())
			sxindexable = com.ofx.base.SxUtil.createNonUserDefinedTextSpatialObjectFrom(getJavaClass(sxclass), sxclass, "123", "", ad, ad1);
		else
			throw new RuntimeException("SxSpatialIndexLeafNode.getJavaClassInstance() invalid geometry type: " + sxclass.getGeomType());
		return sxindexable;
	}

	public int getNodeSizeInBytes()
	{
		return nodeSize;
	}

	public void setNodeSizeInBytes(int i)
	{
		nodeSize = i;
	}

	public int getMemoryNodeSizeInBytes()
	{
		return memoryNodeSize;
	}

	public void setMemoryNodeSizeInBytes(int i)
	{
		memoryNodeSize = i;
	}

	protected void growCapacity()
	{
		int i = capacity * 2;
		int j = size();
		if(cachingSpatialObjects)
		{
			com.ofx.index.SxIndexable asxindexable[] = new com.ofx.index.SxIndexable[i];
			java.lang.System.arraycopy(getSpatialObjectsInNode(), 0, asxindexable, 0, j);
			setSpatialObjectsInNode(asxindexable);
		}
		capacity = i;
	}

	protected int indexOfIdentifier(java.lang.String s)
	{
		int i = size();
		java.lang.String as[] = getIds();
		if(as == null)
			return -1;
//		System.out.println("get ids as length " + as.length);
		if(numOfCollections == 0)
		{
			int j = 0;
			int l = i - 1;
			boolean flag = false;
			boolean flag1 = false;
			while(j <= l) 
			{
				int j1 = (int)((double)(j + l) * 0.5D + 0.5D);
//				System.out.println("at index " + j1 + " as[] is '" + as[j1] + "'");
				int i2 = as[j1].compareTo(s);
				if(i2 < 0)
					j = j1 + 1;
				else
				if(i2 > 0)
					l = j1 - 1;
				else
					return j1;
			}
			return -1;
		}
		int k = 0;
		int i1 = numOfCollections - 1;
		int k1 = 0;
		int j2 = 0;
		while(k <= i1) 
		{
			k1 = (int)((double)(k + i1) * 0.5D + 0.5D);
			j2 = as[k1].compareTo(s);
			if(j2 < 0)
				k = k1 + 1;
			else
			if(j2 > 0)
				i1 = k1 - 1;
			else
				return k1;
		}
		k = numOfCollections;
		i1 = i - 1;
		k1 = 0;
		j2 = 0;
		while(k <= i1) 
		{
			int l1 = (int)((double)(k + i1) * 0.5D + 0.5D);
			int k2 = as[l1].compareTo(s);
			if(k2 < 0)
				k = l1 + 1;
			else
			if(k2 > 0)
				i1 = l1 - 1;
			else
				return l1;
		}
		return -1;
	}

	public synchronized void switchToCachingSpatialObjects()
	{
		if(cachingSpatialObjects)
			return;
		int i = size();
		int j = 0;
		if(i > 0)
		{
			java.lang.Object aobj[] = getSpatialObjectsIntersecting(null);
			com.ofx.index.SxIndexable asxindexable[] = (com.ofx.index.SxIndexable[])aobj[1];
			setSpatialObjectsInNode(asxindexable);
			int k = getMemoryNodeSizeInBytes();
			for(int l = 0; l < i; l++)
				j += spatialObjectsInNode[l].getMemorySizeInBytes();

			setMemoryNodeSizeInBytes(j);
			if(cacheRef != null)
			{
				int i1 = j - k;
				if(i1 > 0)
					cacheRef.leafNodeHasGrown(i1);
			}
		} else
		{
			setSpatialObjectsInNode(new com.ofx.index.SxIndexable[capacity]);
		}
		setSpatialIdentifiersInNode(buildSpatialIdentifiersInNode());
		cachingSpatialObjects = true;
		vInts = null;
		vDoubles = null;
		condensedLabels = null;
		condensedLabelsIndexes = null;
		setIds(null);
		isHoles = null;
		verticesOfCollection = null;
		textsOfCollection = null;
	}

	protected java.lang.Object[] getSpatialObjectsIntersecting(com.ofx.geometry.SxRectangle sxrectangle)
	{
		java.lang.Object aobj[] = new java.lang.Object[2];
		int i = size();
		if(i == 0)
		{
			aobj[0] = new java.lang.Integer[0];
			aobj[1] = new com.ofx.index.SxIndexable[0];
			return aobj;
		}
		com.ofx.index.SxIndexable asxindexable[] = new com.ofx.index.SxIndexable[i];
		int j = 0;
		for(java.util.Enumeration enumeration = elements(null, sxrectangle); enumeration.hasMoreElements();)
			asxindexable[j++] = (com.ofx.index.SxIndexable)enumeration.nextElement();

		aobj[0] = new Integer(j);
		aobj[1] = asxindexable;
		return aobj;
	}

	protected void setSpatialIdentifiersInNode(java.util.Hashtable hashtable)
	{
		spatialIdentifiers = hashtable;
	}

	protected java.util.Hashtable getSpatialIdentifiersInNode()
	{
		return spatialIdentifiers;
	}

	protected java.util.Hashtable buildSpatialIdentifiersInNode()
	{
		int i = size();
		java.util.Hashtable hashtable = new Hashtable(i + 100);
		if(getIds() != null)
		{
			for(int j = 0; j < i; j++)
				hashtable.put(getIds()[j], getSpatialObjectsInNode()[j]);

		} else
		{
			Object obj = null;
			for(int k = 0; k < i; k++)
			{
				com.ofx.index.SxIndexable sxindexable = getSpatialObjectsInNode()[k];
				hashtable.put(sxindexable.getIdentifier(), sxindexable);
			}

		}
		return hashtable;
	}

	public java.lang.String[] getIds()
	{
		if(identifiersInNode != null)
			return identifiersInNode;
		synchronized(this)
		{
			if(idBytes != null)
				setIdentifiersFromBinary(idBytes);
		}
		return identifiersInNode;
	}

	protected void setIds(java.lang.String as[])
	{
		identifiersInNode = as;
	}

	public int getNumOfLabelChars()
	{
		return numOfLabelChars;
	}

	public int getCondensedLabelSize()
	{
		if(condensedLabels != null)
			return condensedLabels.length;
		else
			return 0;
	}

	public boolean getAreLabelsCondensed()
	{
		return labelsAreCondensed;
	}

	public java.lang.String[] getSharedLabels()
	{
		return sharedLabels;
	}

	public void setSharedLabels(java.lang.String as[])
	{
		sharedLabels = as;
	}

	public java.lang.String getLabelAt(int i)
	{
		if(labelsAreCondensed)
		{
			short word0 = condensedLabelsIndexes[i];
			if(word0 < 0)
				return sharedLabels[-word0];
			else
				return condensedLabels[word0];
		} else
		{
			return condensedLabels[i];
		}
	}

	public java.lang.String[] getCondensedLabels()
	{
		return condensedLabels;
	}

	public short[] getCondensedLabelsIndexes()
	{
		return condensedLabelsIndexes;
	}

	protected double[] getXPointsAt(int i)
	{
		if(isPointType)
		{
			double ad[] = new double[1];
			switch(nodeFormat)
			{
			case 1: // '\001'
				ad[0] = nodeRefPoint.x + (double)vInts[i * 2] * leftMultiplier;
				break;

			case 2: // '\002'
				ad[0] = vDoubles[i * 2];
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.getXPointsAt() unknown node format: " + nodeFormat);
			}
			return ad;
		}
		int j = getNumOfCoordsAt(i);
		int k = (int)((double)j * 0.5D);
		double ad1[] = new double[k];
		int l = vOffsets[i];
		int i1 = l + k;
		int j1 = 0;
		switch(nodeFormat)
		{
		case 1: // '\001'
			while(l < i1) 
				ad1[j1++] = nodeRefPoint.x + (double)vInts[l++] * leftMultiplier;
			break;

		case 2: // '\002'
			while(l < i1) 
				ad1[j1++] = vDoubles[l++];
			break;

		default:
			throw new RuntimeException("SxSpatialIndexLeafNode.getXPointsAt() unknown node format: " + nodeFormat);
		}
		return ad1;
	}

	protected double[] getYPointsAt(int i)
	{
		if(isPointType)
		{
			double ad[] = new double[1];
			switch(nodeFormat)
			{
			case 1: // '\001'
				ad[0] = nodeRefPoint.y + (double)vInts[i * 2 + 1] * leftMultiplier;
				break;

			case 2: // '\002'
				ad[0] = vDoubles[i * 2 + 1];
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.getYPointsAt() unknown node format: " + nodeFormat);
			}
			return ad;
		}
		int j = getNumOfCoordsAt(i);
		int k = (int)((double)j * 0.5D);
		double ad1[] = new double[k];
		int l = vOffsets[i] + k;
		int i1 = l + k;
		int j1 = 0;
		switch(nodeFormat)
		{
		case 1: // '\001'
			while(l < i1) 
				ad1[j1++] = nodeRefPoint.y + (double)vInts[l++] * leftMultiplier;
			break;

		case 2: // '\002'
			while(l < i1) 
				ad1[j1++] = vDoubles[l++];
			break;

		default:
			throw new RuntimeException("SxSpatialIndexLeafNode.binaryForSpatialObjectNode() unknown node format: " + nodeFormat);
		}
		return ad1;
	}

	public int getNumOfNonCollCoords()
	{
		return numNonCollVerts;
	}

	protected int getNumOfCoordsAt(int i)
	{
		return vOffsets[i + 1] - vOffsets[i];
	}

	protected void setSpatialObjectsInNode(com.ofx.index.SxIndexable asxindexable[])
	{
		spatialObjectsInNode = asxindexable;
	}

	protected com.ofx.index.SxIndexable[] getSpatialObjectsInNode()
	{
		if(initSpatialObjects || spatialObjectsInNode == null)
			synchronized(this)
			{
				initSpatialObjects = false;
				if(spatialObjectsInNode == null)
					spatialObjectsInNode = new com.ofx.index.SxIndexable[capacity];
				java.util.Enumeration enumeration = getSpatialIdentifiersInNode().elements();
				int i = 0;
				while(enumeration.hasMoreElements()) 
					spatialObjectsInNode[i++] = (com.ofx.index.SxIndexable)enumeration.nextElement();
			}
		return spatialObjectsInNode;
	}

	public static java.util.Vector uncompress(byte abyte0[])
	{
		java.util.Vector vector = new Vector(2);
		int i = abyte0.length;
		int j = i * 6;
		byte abyte1[] = new byte[j];
		try
		{
			int k = 0;
			java.io.ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			java.util.zip.InflaterInputStream inflaterinputstream = new InflaterInputStream(bytearrayinputstream);
			int l = 0;
			int i1 = (int)((double)j * 0.25D);
			int j1 = j - (i1 + 10);
			while((k = inflaterinputstream.read(abyte1, l, i1)) != -1) 
			{
				l = k + l;
				if(l > j1)
				{
					j *= 2;
					byte abyte2[] = new byte[l];
					java.lang.System.arraycopy(abyte1, 0, abyte2, 0, l);
					abyte1 = new byte[j];
					java.lang.System.arraycopy(abyte2, 0, abyte1, 0, l);
					j1 = j - (i1 + 10);
				}
			}
			inflaterinputstream.close();
			bytearrayinputstream.close();
			vector.addElement(new Integer(l));
		}
		catch(java.lang.Exception exception)
		{
			vector.addElement(new Integer(abyte0.length));
			vector.addElement(abyte0);
			return vector;
		}
		vector.addElement(abyte1);
		return vector;
	}

	public void log(java.lang.String s)
	{
		com.ofx.base.SxEnvironment.log().println(s);
	}

	public void log(java.lang.Exception exception)
	{
		com.ofx.base.SxEnvironment.log().println(exception);
	}

	public byte[] binaryForSpatialObjectNode(double d, boolean flag)
	{
		isPointType = getClassDefinition().isPointType();
		precisionTolerance = d;
		int i = digitsToRightOfDecimal(d) + 1;
		leftMultiplier = java.lang.Math.pow(10D, -i);
		rightMultiplier = java.lang.Math.pow(10D, i);
		byte abyte0[] = new byte[0];
		Object obj = null;
		Object obj1 = null;
		java.lang.Object aobj[] = null;
		boolean flag1 = getClassDefinition().isPolygonType();
		if(!cachingSpatialObjects)
			switchToCachingSpatialObjects();
		try
		{
			aobj = (java.lang.Object[])com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkOut(0, 10000);
			java.io.DataOutputStream dataoutputstream = (java.io.DataOutputStream)aobj[0];
			java.io.ByteArrayOutputStream bytearrayoutputstream = (java.io.ByteArrayOutputStream)aobj[1];
			dataoutputstream.writeInt(0x20000);
			dataoutputstream.writeLong(getNodeId());
			com.ofx.geometry.SxRectangle sxrectangle = getBounds();
			if(sxrectangle == null)
				log("Null bounds binaryForSpatialObjectNode() nodeid=" + getNodeId());
			nodeFormat = determineNodeFormat(d, sxrectangle);
			dataoutputstream.writeInt(nodeFormat);
			dataoutputstream.writeDouble(sxrectangle.x);
			dataoutputstream.writeDouble(sxrectangle.y);
			dataoutputstream.writeDouble(sxrectangle.width);
			dataoutputstream.writeDouble(sxrectangle.height);
			dataoutputstream.writeDouble(nodeRefPoint.x);
			dataoutputstream.writeDouble(nodeRefPoint.y);
			int j = size();
			dataoutputstream.writeInt(j);
			com.ofx.index.SxIndexable asxindexable[] = getSpatialObjects();
			com.ofx.index.SxIndexable sxindexable = asxindexable[0];
			asxindexable = reorderGeometryCollections(asxindexable);
			dataoutputstream.writeShort(numOfCollections);
			dataoutputstream.writeInt(numCollVerts);
			dataoutputstream.writeInt(numNonCollVerts);
			numNonCollObjects = j - numOfCollections;
			java.lang.String as[] = new java.lang.String[j];
			java.lang.String as1[] = new java.lang.String[j];
			switch(nodeFormat)
			{
			case 1: // '\001'
				writeIntOffsetFromPoint(asxindexable, as, as1, dataoutputstream, flag1);
				break;

			case 2: // '\002'
				writeAllDouble(asxindexable, as, as1, dataoutputstream, flag1);
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.binaryForSpatialObjectNode() unknown node format: " + nodeFormat);
			}
			com.ofx.persistence.SxStringCondenser sxstringcondenser = new SxStringCondenser();
			byte abyte1[] = null;
			int k = 0;
			if(!getClassDefinition().getIsUserDefined() && getClassDefinition().getHasExtraAttributes() && sxindexable != null)
			{
				abyte1 = sxindexable.serializeExtraAttributes(asxindexable, size(), sxstringcondenser);
				k = abyte1.length;
			}
			byte abyte2[] = binaryForLabels(as1, j);
			dataoutputstream.writeInt(numOfLabelChars);
			int l = abyte2.length;
			dataoutputstream.writeInt(l);
			if(flag)
			{
				abyte2 = com.ofx.base.SxUtil.compress(abyte2);
				int i1 = abyte2.length;
				dataoutputstream.writeInt(i1);
				dataoutputstream.write(abyte2, 0, i1);
			} else
			{
				dataoutputstream.write(abyte2, 0, l);
			}
			byte abyte3[] = binaryForIdentifiers(as, j);
			dataoutputstream.writeInt(numOfIdChars);
			int j1 = abyte3.length;
			dataoutputstream.writeInt(j1);
			if(flag)
			{
				abyte3 = com.ofx.base.SxUtil.compress(abyte3);
				int k1 = abyte3.length;
				dataoutputstream.writeInt(k1);
				dataoutputstream.write(abyte3, 0, k1);
			} else
			{
				dataoutputstream.write(abyte3, 0, j1);
			}
			if(!getClassDefinition().getIsUserDefined() && getClassDefinition().getHasExtraAttributes())
			{
				byte abyte4[] = binaryForCondensedStrings(sxstringcondenser);
				int l1 = abyte4.length;
				if(l1 == 0)
				{
					dataoutputstream.writeInt(l1);
				} else
				{
					dataoutputstream.writeInt(l1);
					dataoutputstream.write(abyte4, 0, l1);
				}
				if(k == 0)
				{
					dataoutputstream.writeInt(k);
				} else
				{
					dataoutputstream.writeInt(k);
					dataoutputstream.write(abyte1, 0, k);
				}
			}
			dataoutputstream.flush();
			abyte0 = bytearrayoutputstream.toByteArray();
		}
		catch(java.lang.Exception exception)
		{
			log("SxSpatialIndexLeafNode.binaryForSpatialObjectNode(...) exception: " + exception);
			exception.printStackTrace();
		}
		finally
		{
			if(aobj != null)
				com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkIn(((java.lang.Object) (aobj)));
		}
		return abyte0;
	}

	protected void writeIntOffsetFromPoint(com.ofx.index.SxIndexable asxindexable[], java.lang.String as[], java.lang.String as1[], java.io.DataOutputStream dataoutputstream, boolean flag)
		throws java.io.IOException
	{
		int i = size();
		boolean flag1 = false;
		Object obj = null;
		Object obj1 = null;
		for(int k = 0; k < numOfCollections; k++)
		{
			com.ofx.index.SxIndexable sxindexable = asxindexable[k];
			as[k] = sxindexable.getIdentifier().toString();
			as1[k] = sxindexable.getLabel();
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface = sxindexable.getGeometry();
			writeGeometryCollection((com.ofx.geometry.SxGeometryCollection)sxgeometryinterface, nodeFormat, dataoutputstream, flag);
		}

		if(!isPointType && numNonCollObjects > 0)
			vOffsets = new int[numNonCollObjects + 1];
		int l = 0;
		int i1 = 0;
		for(int j1 = numOfCollections; j1 < i; j1++)
		{
			com.ofx.index.SxIndexable sxindexable1 = asxindexable[j1];
			as[j1] = sxindexable1.getIdentifier().toString();
			as1[j1] = sxindexable1.getLabel();
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface1 = sxindexable1.getGeometry();
			int j = sxgeometryinterface1.getNPoints();
			if(!isPointType)
			{
				vOffsets[l++] = i1;
				i1 += j * 2;
			}
			writeIntOffsetFromPointCoords(sxgeometryinterface1, dataoutputstream, j, false);
		}

		if(!isPointType && numNonCollObjects > 0)
		{
			vOffsets[numNonCollObjects] = i1;
			writevOffsets(dataoutputstream);
		}
	}

	protected void writevOffsets(java.io.DataOutputStream dataoutputstream)
		throws java.io.IOException
	{
		int i = vOffsets.length;
		for(int j = 0; j < i; j++)
			dataoutputstream.writeInt(vOffsets[j]);

	}

	protected void writeGeometryCollection(com.ofx.geometry.SxGeometryCollection sxgeometrycollection, int i, java.io.DataOutputStream dataoutputstream, boolean flag)
		throws java.io.IOException
	{
		java.util.Vector vector = sxgeometrycollection.getGeometry();
		dataoutputstream.writeInt(vector.size());
		Object obj = null;
		boolean flag1 = false;
		for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface = (com.ofx.geometry.SxGeometryInterface)enumeration.nextElement();
			if(flag)
				dataoutputstream.writeBoolean(sxgeometryinterface.isHole());
			int j = sxgeometryinterface.getNPoints();
			int dummy;
			if(j < 2)
				dummy = 1;
			switch(i)
			{
			case 1: // '\001'
				writeIntOffsetFromPointCoords(sxgeometryinterface, dataoutputstream, j, true);
				break;

			case 2: // '\002'
				writeAllDoubleCoords(sxgeometryinterface, dataoutputstream, j, true);
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.writeGeometryCollection() unknown node format: " + i);
			}
		}

	}

	protected com.ofx.index.SxIndexable[] reorderGeometryCollections(com.ofx.index.SxIndexable asxindexable[])
	{
		int i = size();
		Object obj = null;
		Object obj1 = null;
		com.ofx.index.SxIndexable asxindexable1[] = new com.ofx.index.SxIndexable[asxindexable.length];
		numOfCollections = 0;
		numNonCollVerts = 0;
		for(int j = 0; j < i; j++)
		{
			com.ofx.index.SxIndexable sxindexable = asxindexable[j];
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface = sxindexable.getGeometry();
			if(sxgeometryinterface.isCollection())
			{
				asxindexable1[numOfCollections] = sxindexable;
				numCollVerts = numCollVerts + sxgeometryinterface.getNPoints() * 2;
				numOfCollections++;
			} else
			{
				numNonCollVerts = numNonCollVerts + sxgeometryinterface.getNPoints() * 2;
			}
		}

		SxLexIdComparator sxlexidcomparator = new SxLexIdComparator();
		if(numOfCollections == 0)
		{
			com.ofx.collections.SxQSort sxqsort = new SxQSort();
			try
			{
				sxqsort.sort(asxindexable, sxlexidcomparator, 0, i - 1);
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log().println("SxSpatialIndexLeafNode Lex ID sort failed with exception: " + exception);
			}
			return asxindexable;
		}
		int k = numOfCollections;
		for(int l = 0; l < i; l++)
		{
			com.ofx.index.SxIndexable sxindexable1 = asxindexable[l];
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface1 = sxindexable1.getGeometry();
			if(!sxgeometryinterface1.isCollection())
				asxindexable1[k++] = sxindexable1;
		}

		com.ofx.collections.SxQSort sxqsort1 = new SxQSort();
		try
		{
			sxqsort1.sort(asxindexable1, sxlexidcomparator, 0, numOfCollections - 1);
			sxqsort1.sort(asxindexable1, sxlexidcomparator, numOfCollections, i - 1);
		}
		catch(java.lang.Exception exception1)
		{
			com.ofx.base.SxEnvironment.log().println("SxSpatialIndexLeafNode Lex. ID sort failed with exception: " + exception1);
		}
		return asxindexable1;
	}

	protected void writeIntOffsetFromPointCoords(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, java.io.DataOutputStream dataoutputstream, int i, boolean flag)
		throws java.io.IOException
	{
		if(isPointType)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint = ((com.ofx.geometry.SxPoint)sxgeometryinterface).getLocation();
			dataoutputstream.writeInt((int)((sxdoublepoint.x - nodeRefPoint.x) * rightMultiplier + 0.5D));
			dataoutputstream.writeInt((int)((sxdoublepoint.y - nodeRefPoint.y) * rightMultiplier + 0.5D));
		} else
		{
			if(flag)
				dataoutputstream.writeInt(i);
			double ad[] = sxgeometryinterface.getXPoints();
			for(int j = 0; j < i; j++)
				dataoutputstream.writeInt((int)((ad[j] - nodeRefPoint.x) * rightMultiplier + 0.5D));

			ad = sxgeometryinterface.getYPoints();
			for(int k = 0; k < i; k++)
				dataoutputstream.writeInt((int)((ad[k] - nodeRefPoint.y) * rightMultiplier + 0.5D));

			if(sxgeometryinterface.getDimension() == 3)//SxText
			{
				SxText sxtext = (SxText )sxgeometryinterface;
				dataoutputstream.writeInt(sxtext.angle);
				dataoutputstream.writeUTF(sxtext.text);
				dataoutputstream.writeUTF(sxtext.font_name);
				dataoutputstream.writeUTF(sxtext.font_params);
				dataoutputstream.writeInt(sxtext.font_size);
				dataoutputstream.writeInt(sxtext.font_style);
				dataoutputstream.writeInt(sxtext.font_color.getRGB());
			}
		}
	}

	protected void writeAllDouble(com.ofx.index.SxIndexable asxindexable[], java.lang.String as[], java.lang.String as1[], java.io.DataOutputStream dataoutputstream, boolean flag)
		throws java.io.IOException
	{
		int i = size();
		boolean flag1 = false;
		Object obj = null;
		Object obj1 = null;
		for(int k = 0; k < numOfCollections; k++)
		{
			com.ofx.index.SxIndexable sxindexable = asxindexable[k];
			as[k] = sxindexable.getIdentifier().toString();
			as1[k] = sxindexable.getLabel();
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface = sxindexable.getGeometry();
			writeGeometryCollection((com.ofx.geometry.SxGeometryCollection)sxgeometryinterface, nodeFormat, dataoutputstream, flag);
		}

		if(!isPointType && numNonCollObjects > 0)
			vOffsets = new int[numNonCollObjects + 1];
		int l = 0;
		int i1 = 0;
		for(int j1 = numOfCollections; j1 < i; j1++)
		{
			com.ofx.index.SxIndexable sxindexable1 = asxindexable[j1];
			as[j1] = sxindexable1.getIdentifier().toString();
			as1[j1] = sxindexable1.getLabel();
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface1 = sxindexable1.getGeometry();
			int j = sxgeometryinterface1.getNPoints();
			if(!isPointType)
			{
				vOffsets[l++] = i1;
				i1 += j * 2;
			}
			writeAllDoubleCoords(sxgeometryinterface1, dataoutputstream, j, false);
		}

		if(!isPointType && numNonCollObjects > 0)
		{
			vOffsets[numNonCollObjects] = i1;
			writevOffsets(dataoutputstream);
		}
	}

	protected void writeAllDoubleCoords(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, java.io.DataOutputStream dataoutputstream, int i, boolean flag)
		throws java.io.IOException
	{
		boolean flag1 = false;
		if(isPointType)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint = ((com.ofx.geometry.SxPoint)sxgeometryinterface).getLocation();
			dataoutputstream.writeDouble(sxdoublepoint.x);
			dataoutputstream.writeDouble(sxdoublepoint.y);
		} else
		{
			if(flag)
				dataoutputstream.writeInt(i);
			double ad[] = sxgeometryinterface.getXPoints();
			for(int j = 0; j < i; j++)
				dataoutputstream.writeDouble(ad[j]);

			ad = sxgeometryinterface.getYPoints();
			for(int k = 0; k < i; k++)
				dataoutputstream.writeDouble(ad[k]);

		}
	}

	public int digitsToRightOfDecimal(double d)
	{
		if(d - java.lang.Math.floor(d) <= 0.0D)
			return 0;
		for(int j = 1; j < 10; j++)
		{
			int i = (int)(d * java.lang.Math.pow(10D, j) + 0.5D);
			double d1 = (double)i * java.lang.Math.pow(10D, -j);
			if((i != 0 || d1 != 0.0D) && java.lang.Math.abs(d1 - d) < java.lang.Math.pow(10D, -(j + 1)))
				return j;
		}

		return 9;
	}

	public int determineNodeFormat(double d, com.ofx.geometry.SxRectangle sxrectangle)
	{
		nodeRefPoint = new SxDoublePoint(0.0D, 0.0D);
		int i = 0;
		int j = 0;
		double d1 = 0.0D;
		double d2 = 0.0D;
		int k = 0;
		int l = 0;
		double d3 = 0.0D;
		double d4 = 0.0D;
		i = (int)(sxrectangle.x * rightMultiplier + 0.5D);
		j = (int)(sxrectangle.y * rightMultiplier + 0.5D);
		k = (int)(sxrectangle.cornerX() * rightMultiplier + 0.5D);
		l = (int)(sxrectangle.cornerY() * rightMultiplier + 0.5D);
		d1 = (double)i * leftMultiplier;
		d2 = (double)j * leftMultiplier;
		d3 = (double)k * leftMultiplier;
		d4 = (double)l * leftMultiplier;
		if(java.lang.Math.abs(d1 - sxrectangle.x) < d && java.lang.Math.abs(d2 - sxrectangle.y) < d && java.lang.Math.abs(d3 - sxrectangle.cornerX()) < d && java.lang.Math.abs(d4 - sxrectangle.cornerY()) < d)
			return 1;
		com.ofx.geometry.SxDoublePoint sxdoublepoint = sxrectangle.getCenter();
		i = (int)((sxrectangle.x - sxdoublepoint.x) * rightMultiplier + 0.5D);
		j = (int)((sxrectangle.y - sxdoublepoint.y) * rightMultiplier + 0.5D);
		k = (int)((sxrectangle.cornerX() - sxdoublepoint.x) * rightMultiplier + 0.5D);
		l = (int)((sxrectangle.cornerY() - sxdoublepoint.y) * rightMultiplier + 0.5D);
		d1 = (double)i * leftMultiplier + sxdoublepoint.x;
		d2 = (double)j * leftMultiplier + sxdoublepoint.y;
		d3 = (double)k * leftMultiplier + sxdoublepoint.x;
		d4 = (double)l * leftMultiplier + sxdoublepoint.y;
		if(java.lang.Math.abs(sxrectangle.x - d1) < d && java.lang.Math.abs(sxrectangle.y - d2) < d && java.lang.Math.abs(sxrectangle.cornerX() - d3) < d && java.lang.Math.abs(sxrectangle.cornerY() - d4) < d)
		{
			nodeRefPoint = sxdoublepoint;
			return 1;
		} else
		{
			return 2;
		}
	}

	public boolean canFormatBeFixedPtShort(double d, com.ofx.geometry.SxRectangle sxrectangle)
	{
		int i = (int)java.lang.Math.round(sxrectangle.width * 0.0D);
		int j = (int)java.lang.Math.round(sxrectangle.height * 0.0D);
		int k = i * j;
		return false;
	}

	protected void nodeFromBinary(byte abyte0[], int i, com.ofx.repository.SxClass sxclass)
	{
		Object obj = null;
		Object obj1 = null;
		long l = 0L;
		int j = abyte0.length;
		com.ofx.persistence.SxDataInputStream sxdatainputstream = null;
		Object obj2 = null;
		java.lang.Object aobj[] = new java.lang.Object[2];
		boolean flag = false;
		Object obj3 = null;
		Object obj4 = null;
		boolean flag1 = sxclass.isPolygonType();
		boolean flag2 = false;
		try
		{
			sxdatainputstream = new SxDataInputStream(abyte0, 0, i);
			int k = sxdatainputstream.readInt();
			if(k != 0x20000)
				throw new RuntimeException(getClass().getName() + ".readObject: " + " expected OFX_CLASS_VERSION of " + 0x20000 + " got " + k);
			setIdentifier(java.lang.Long.toString(sxdatainputstream.readLong()));
			nodeFormat = sxdatainputstream.readInt();
			double d = sxdatainputstream.readDouble();
			double d1 = sxdatainputstream.readDouble();
			double d2 = sxdatainputstream.readDouble();
			double d3 = sxdatainputstream.readDouble();
			int i1 = digitsToRightOfDecimal(precisionTolerance) + 1;
			leftMultiplier = java.lang.Math.pow(10D, -i1);
			rightMultiplier = java.lang.Math.pow(10D, i1);
			boundsOfNode = new SxRectangle(d, d1, d2, d3);
			nodeRefPoint = new SxDoublePoint(sxdatainputstream.readDouble(), sxdatainputstream.readDouble());
			count = sxdatainputstream.readInt();
			numOfCollections = sxdatainputstream.readShort();
			numCollVerts = sxdatainputstream.readInt();
			numNonCollVerts = sxdatainputstream.readInt();
			if(numOfCollections > 0)
			{
				collectionObjects = new com.ofx.index.SxIndexable[numOfCollections];
				isHoles = new boolean[numOfCollections][];
				verticesOfCollection = new double[numOfCollections][][];
				textsOfCollection = new temp_sxtext_class[numOfCollections];
				for(int j1 = 0; j1 < numOfCollections; j1++)
					readGeometryCollection(j1, nodeFormat, sxdatainputstream);

			} else
			{
				collectionObjects = null;
				isHoles = null;
				verticesOfCollection = null;
				textsOfCollection = null;
			}
			numNonCollObjects = count - numOfCollections;
			if(count > 0)
				capacity = count;
			else
				capacity = 500;
			if(!isPointType)
				vOffsets = new int[numNonCollObjects + 1];
			boolean flag3 = false;
			boolean flag4 = false;
			boolean flag5 = false;
			if(numNonCollVerts > 0)
				switch(nodeFormat)
				{
				case 1: // '\001'
					vInts = new int[numNonCollVerts];

					if(isTextType)
					{
						int[] temp_vInts = new int[4];
						textsOfCollection = new temp_sxtext_class[numNonCollVerts / 4];
						for(int ii = 0; ii < numNonCollVerts;)
						{
							sxdatainputstream.readInt(temp_vInts, 4, 0);
							vInts[ii++] = temp_vInts[0];
							vInts[ii++] = temp_vInts[1];
							vInts[ii++] = temp_vInts[2];
							vInts[ii++] = temp_vInts[3];
							temp_sxtext_instance = new temp_sxtext_class();
							try
							{
								temp_sxtext_instance.sxtext_angle = sxdatainputstream.readInt();
								temp_sxtext_instance.sxtext_text = sxdatainputstream.readUTF();
								temp_sxtext_instance.sxtext_font_name = sxdatainputstream.readUTF();
								temp_sxtext_instance.sxtext_font_params = sxdatainputstream.readUTF();
								temp_sxtext_instance.sxtext_font_size = sxdatainputstream.readInt();
								temp_sxtext_instance.sxtext_font_style = sxdatainputstream.readInt();
								temp_sxtext_instance.sxtext_font_color_rgb = sxdatainputstream.readInt();

								textsOfCollection[ii / 4 - 1] = temp_sxtext_instance;
							}
							catch(Exception ex)
							{
							}
						}
					}
					else
						sxdatainputstream.readInt(vInts, numNonCollVerts, 0);


					if(!isPointType)
						sxdatainputstream.readInt(vOffsets, numNonCollObjects + 1, 0);
					break;

				case 2: // '\002'
					vDoubles = new double[numNonCollVerts];
					sxdatainputstream.readDouble(vDoubles, numNonCollVerts, 0);
					if(!isPointType)
						sxdatainputstream.readInt(vOffsets, numNonCollObjects + 1, 0);
					break;

				default:
					throw new RuntimeException("SxSpatialIndexLeafNode.nodeFromBinary() unknown node format: " + nodeFormat);
				}
			numOfLabelChars = sxdatainputstream.readInt();
			byte abyte1[] = null;
			int k1 = sxdatainputstream.readInt();
			if(isCompressed())
			{
				int l1 = sxdatainputstream.readInt();
				abyte1 = new byte[l1];
				sxdatainputstream.read(abyte1, 0, l1);
				if(isCompressed())
				{
					java.util.Vector vector = com.ofx.base.SxUtil.uncompress(abyte1, k1);
					abyte1 = (byte[])vector.elementAt(1);
				}
			} else
			{
				abyte1 = new byte[k1];
				sxdatainputstream.read(abyte1, 0, k1);
			}
			setLabelsFromBinary(abyte1);
			numOfIdChars = sxdatainputstream.readInt();
			int i2 = sxdatainputstream.readInt();
			if(isCompressed())
			{
				int j2 = sxdatainputstream.readInt();
				idBytes = new byte[j2];
				sxdatainputstream.read(idBytes, 0, j2);
				if(isCompressed())
				{
					java.util.Vector vector1 = com.ofx.base.SxUtil.uncompress(idBytes, i2);
					idBytes = (byte[])vector1.elementAt(1);
				}
			} else
			{
				idBytes = new byte[i2];
				sxdatainputstream.read(idBytes, 0, i2);
			}
			if(numOfCollections > 0)
			{
				java.lang.String as[] = getCollectionIdentifiersFromBinary(idBytes, numOfCollections);
				createCollectionObjects(as, numOfCollections);
				isHoles = null;
				verticesOfCollection = null;
				textsOfCollection = null;
			}
			if(!getClassDefinition().getIsUserDefined() && getClassDefinition().getHasExtraAttributes())
			{
				int k2 = sxdatainputstream.readInt();
				byte abyte2[] = new byte[k2];
				sxdatainputstream.read(abyte2, 0, k2);
				com.ofx.persistence.SxStringCondenser sxstringcondenser = stringCondenserFromBinary(abyte2);
				int j3 = sxdatainputstream.readInt();
				byte abyte3[] = new byte[j3];
				sxdatainputstream.read(abyte3, 0, j3);
				com.ofx.index.SxIndexable sxindexable = getJavaClassInstance(getClassDefinition());
				extraAttributes = sxindexable.deserializeExtraAttributes(size(), abyte3, sxstringcondenser);
			}
			if(getSpatialCacheMode().equalsIgnoreCase("SpatialObjects"))
				switchToCachingSpatialObjects();
			int l2 = idBytes.length;
			if(condensedLabels != null)
				l2 = l2 + 38 * condensedLabels.length + numOfLabelChars * 2;
			switch(nodeFormat)
			{
			case 1: // '\001'
				l2 += 4 * numNonCollVerts;
				break;

			case 2: // '\002'
				l2 += 8 * numNonCollVerts;
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.nodeFromBinary() unknown node format: " + nodeFormat);
			}
			if(numOfCollections > 0)
			{
				l2 += 64 * numOfCollections;
				int i3 = 52;
				if(isPointType)
					i3 += 4;
				else
					i3 += 32;
				l2 += i3 * numCollGeometries;
				l2 += 8 * numCollVerts;
			}
			if(!getClassDefinition().getIsUserDefined() && extraAttributes != null)
				l2 = firstElement().getTotalMemorySizeInBytes(l2, extraAttributes, extraAttributes.length);
			l2 += 500;
			setMemoryNodeSizeInBytes(l2);
		}
		catch(java.lang.Exception exception)
		{
			if(getClassDefinition() != null)
				log("Error deserializing class: " + getClassDefinition().getIdentifier());
			log(exception);
		}
		finally
		{
			sxdatainputstream.close();
		}
	}

	public void logMemUsage()
	{
		log("nodeFromBinary classification " + getClassDefinition().getIdentifier() + " id = " + getIdentifier());
		log("   num objs: " + size() + " perobjsize: " + java.lang.Math.round(getMemoryNodeSizeInBytes() / size()));
		log("   memSizeInBytes: " + getMemoryNodeSizeInBytes());
		log("   idBytes.length: " + idBytes.length);
		log("   label.length: " + (38 * condensedLabels.length + numOfLabelChars * 2));
		log("   condensedLabels.length: " + condensedLabels.length + " numOfLabelChars: " + numOfLabelChars);
		log("   numNonCollVerts: " + numNonCollVerts);
		switch(nodeFormat)
		{
		case 1: // '\001'
			log("   intoffset (4 * numNonCollVerts): " + 4 * numNonCollVerts);
			break;

		case 2: // '\002'
			log("   alldouble (8 * numNonCollVerts): " + 8 * numNonCollVerts);
			break;

		default:
			throw new RuntimeException("SxSpatialIndexLeafNode.nodeFromBinary() unknown node format: " + nodeFormat);
		}
		log("numOfCollections: " + numOfCollections + "numCollGeometries: " + numCollGeometries + "numCollVerts: " + numCollVerts);
	}

	public int coordMemAmount()
	{
		switch(nodeFormat)
		{
		case 1: // '\001'
			return 4 * numNonCollVerts;

		case 2: // '\002'
			return 8 * numNonCollVerts;
		}
		throw new RuntimeException("SxSpatialIndexLeafNode.nodeFromBinary() unknown node format: " + nodeFormat);
	}

	protected void readGeometryCollection(int i, int j, com.ofx.persistence.SxDataInputStream sxdatainputstream)
		throws java.io.IOException
	{
		boolean flag = getClassDefinition().isPolygonType();
		Object obj = null;
		Object obj1 = null;
		boolean flag1 = false;
		int k = 0;
		Object obj2 = null;
		k = sxdatainputstream.readInt();
		double ad1[][] = new double[k][];
		boolean aflag[] = null;
		if(flag)
			aflag = new boolean[k];
		numCollGeometries = numCollGeometries + k;
		for(int l = 0; l < k; l++)
		{
			if(flag)
				aflag[l] = sxdatainputstream.readBoolean();
			double ad[];
			switch(j)
			{
			case 1: // '\001'
				ad = readIntOffsetFromPointVertices(sxdatainputstream);
				break;

			case 2: // '\002'
				ad = readAllDoubleVertices(sxdatainputstream);
				break;

			default:
				throw new RuntimeException("SxSpatialIndexLeafNode.readGeometryCollection() unknown node format: " + j);
			}
			ad1[l] = ad;
		}

		verticesOfCollection[i] = ad1;
		textsOfCollection[i] = temp_sxtext_instance;
		if(flag)
			isHoles[i] = aflag;
	}

	protected void createCollectionObjects(java.lang.String as[], int i)
	{
		com.ofx.repository.SxClass sxclass = getClassDefinition();
		java.lang.Object obj = null;
		boolean flag = !sxclass.getIsUserDefined() && sxclass.getHasExtraAttributes();
		if(sxclass.isPolygonType())
		{
			for(int j = 0; j < i; j++)
			{
				if(flag)
					obj = extraAttributes[j];
				double ad[][] = verticesOfCollection[j];
				int i1 = ad.length;
				collectionObjects[j] = com.ofx.base.SxUtil.createMultiPolygonSpatialObjectFrom(sxclass, getJavaClass(sxclass), as[j], getLabelAt(j), ad, isHoles[j], i1, obj);
			}

		} else
		if(sxclass.isPolylineType())
		{
			for(int k = 0; k < numOfCollections; k++)
			{
				if(flag)
					obj = extraAttributes[k];
				double ad1[][] = verticesOfCollection[k];
				int j1 = ad1.length;
				collectionObjects[k] = com.ofx.base.SxUtil.createMultiPolylineSpatialObjectFrom(sxclass, getJavaClass(sxclass), as[k], getLabelAt(k), ad1, j1, obj);
			}

		} else
		if(sxclass.isPointType())
		{
			for(int l = 0; l < numOfCollections; l++)
			{
				if(flag)
					obj = extraAttributes[l];
				double ad2[][] = verticesOfCollection[l];
				int k1 = ad2.length;
				collectionObjects[l] = com.ofx.base.SxUtil.createMultiPointSpatialObjectFrom(sxclass, getJavaClass(sxclass), as[l], getLabelAt(l), ad2, k1, obj);
			}

		} else
		if(sxclass.isTextType())
		{
			for(int l = 0; l < numOfCollections; l++)
			{
				if(flag)
					obj = extraAttributes[l];
				double ad2[][] = verticesOfCollection[l];
				int k1 = ad2.length;
				collectionObjects[l] = com.ofx.base.SxUtil.createMultiPointSpatialObjectFrom(sxclass, getJavaClass(sxclass), as[l], getLabelAt(l), ad2, k1, obj);
			}

		} else
		{
			throw new RuntimeException("SxSpatialIndexLeafNode.createCollectionObjects() Unknown geometry type for class: " + sxclass.getIdentifier() + " type: " + sxclass.getGeomType());
		}
	}

	protected double[] readIntOffsetFromPointVertices(com.ofx.persistence.SxDataInputStream sxdatainputstream)
	{
		int i = 1;
		if(!isPointType)
			i = sxdatainputstream.readInt();
		int j = i * 2;
		double ad[] = new double[j];
		sxdatainputstream.readInt(ad, i, leftMultiplier, nodeRefPoint.x, nodeRefPoint.y);

		if(isTextType)
		{
			temp_sxtext_instance = new temp_sxtext_class();
			try
			{
				temp_sxtext_instance.sxtext_angle = sxdatainputstream.readInt();
				temp_sxtext_instance.sxtext_text = sxdatainputstream.readUTF();
				temp_sxtext_instance.sxtext_font_name = sxdatainputstream.readUTF();
				temp_sxtext_instance.sxtext_font_params = sxdatainputstream.readUTF();
				temp_sxtext_instance.sxtext_font_size = sxdatainputstream.readInt();
				temp_sxtext_instance.sxtext_font_style = sxdatainputstream.readInt();
				temp_sxtext_instance.sxtext_font_color_rgb = sxdatainputstream.readInt();
			}
			catch(Exception ex)
			{
			}
		}

		return ad;
	}

	protected double[] readAllDoubleVertices(com.ofx.persistence.SxDataInputStream sxdatainputstream)
	{
		int i = 1;
		if(!isPointType)
			i = sxdatainputstream.readInt();
		int j = i * 2;
		double ad[] = new double[j];
		for(int k = 0; k < j; k++)
			ad[k] = sxdatainputstream.readDouble();

		return ad;
	}

	protected byte[] binaryForIdentifiers(java.lang.String as[], int i)
	{
		byte abyte0[] = new byte[0];
		Object obj = null;
		Object obj1 = null;
		java.lang.Object aobj[] = null;
		numOfIdChars = 0;
		try
		{
			aobj = (java.lang.Object[])com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkOut(0, 10000);
			java.io.DataOutputStream dataoutputstream = (java.io.DataOutputStream)aobj[0];
			java.io.ByteArrayOutputStream bytearrayoutputstream = (java.io.ByteArrayOutputStream)aobj[1];
			for(int j = 0; j < i; j++)
			{
				dataoutputstream.writeUTF(as[j]);
				numOfIdChars = numOfIdChars + as[j].length();
			}

			dataoutputstream.flush();
			abyte0 = bytearrayoutputstream.toByteArray();
		}
		catch(java.lang.Exception exception)
		{
			log("SxSpatialIndexLeafNode.binaryForAttributes(...) exception: " + exception);
		}
		finally
		{
			if(aobj != null)
				com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkIn(((java.lang.Object) (aobj)));
		}
		return abyte0;
	}

	protected byte[] binaryForLabels(java.lang.String as[], int i)
	{
		byte abyte0[] = new byte[0];
		Object obj = null;
		Object obj1 = null;
		java.lang.Object aobj[] = null;
		com.ofx.persistence.SxStringCondenser sxstringcondenser = new SxStringCondenser(sharedLabels);
		sxstringcondenser.add(as, i);
		java.lang.String as1[] = sxstringcondenser.getCondensedStrings();
		int j = sxstringcondenser.size();
		Object obj2 = null;
		int k = sxstringcondenser.uniqueStringCount();
		if(sharedLabels == null)
			labelsAreCondensed = (double)k / (double)i < 0.80000000000000004D;
		else
			labelsAreCondensed = true;
		try
		{
			aobj = (java.lang.Object[])com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkOut(0, 10000);
			java.io.DataOutputStream dataoutputstream = (java.io.DataOutputStream)aobj[0];
			java.io.ByteArrayOutputStream bytearrayoutputstream = (java.io.ByteArrayOutputStream)aobj[1];
			dataoutputstream.writeBoolean(labelsAreCondensed);
			if(labelsAreCondensed)
			{
				int ai[] = sxstringcondenser.indexesOf(as, i);
				dataoutputstream.writeInt(j);
				for(int l = 0; l < j; l++)
				{
					dataoutputstream.writeUTF(as1[l]);
					numOfLabelChars = numOfLabelChars + as1[l].length();
				}

				for(int j1 = 0; j1 < i; j1++)
					dataoutputstream.writeShort(ai[j1]);

			} else
			{
				for(int i1 = 0; i1 < i; i1++)
				{
					dataoutputstream.writeUTF(as[i1]);
					numOfLabelChars = numOfLabelChars + as[i1].length();
				}

			}
			dataoutputstream.flush();
			abyte0 = bytearrayoutputstream.toByteArray();
		}
		catch(java.lang.Exception exception)
		{
			log("SxSpatialIndexLeafNode.binaryForAttributes(...) exception: " + exception);
		}
		finally
		{
			if(aobj != null)
				com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkIn(((java.lang.Object) (aobj)));
		}
		return abyte0;
	}

	protected byte[] binaryForCondensedStrings(com.ofx.persistence.SxStringCondenser sxstringcondenser)
	{
		byte abyte0[] = new byte[0];
		Object obj = null;
		Object obj1 = null;
		java.lang.Object aobj[] = null;
		java.lang.String as[] = sxstringcondenser.getCondensedStrings();
		int i = sxstringcondenser.size();
		try
		{
			aobj = (java.lang.Object[])com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkOut(0, 10000);
			java.io.DataOutputStream dataoutputstream = (java.io.DataOutputStream)aobj[0];
			java.io.ByteArrayOutputStream bytearrayoutputstream = (java.io.ByteArrayOutputStream)aobj[1];
			dataoutputstream.writeInt(i);
			for(int j = 0; j < i; j++)
				dataoutputstream.writeUTF(as[j]);

			dataoutputstream.flush();
			abyte0 = bytearrayoutputstream.toByteArray();
		}
		catch(java.lang.Exception exception)
		{
			log("SxSpatialIndexLeafNode.binaryForCondensedStrings(...) exception: " + exception);
		}
		finally
		{
			if(aobj != null)
				com.ofx.base.SxStreamObjectPool.getStreamObjectPool().checkIn(((java.lang.Object) (aobj)));
		}
		return abyte0;
	}

	protected com.ofx.persistence.SxStringCondenser stringCondenserFromBinary(byte abyte0[])
	{
		com.ofx.persistence.SxDataInputStream sxdatainputstream = null;
		java.lang.String as[] = null;
		int i = 0;
		try
		{
			sxdatainputstream = new SxDataInputStream(abyte0, 0, abyte0.length);
			i = sxdatainputstream.readInt();
			as = new java.lang.String[i];
			for(int j = 0; j < i; j++)
				as[j] = sxdatainputstream.readUTF();

		}
		catch(java.lang.Exception exception)
		{
			log("SwSpatialIndexLeafNode.stringCondenserFromBinary method failed due to the following exception: " + exception);
		}
		finally
		{
			sxdatainputstream.close();
		}
		return new SxStringCondenser(as, i, null);
	}

	protected synchronized void setIdentifiersFromBinary(byte abyte0[])
	{
		int i = size();
		if(abyte0 == null || i == 0)
			return;
		com.ofx.persistence.SxDataInputStream sxdatainputstream = null;
		try
		{
			int j = abyte0.length;
			sxdatainputstream = new SxDataInputStream(abyte0, 0, abyte0.length);
			sxdatainputstream.skipForward(nonCollIdStartPos);
			java.lang.String as[] = new java.lang.String[i];
			for(int k = 0; k < numOfCollections; k++)
			{
				as[k] = collectionObjects[k].getIdentifier().toString();
//				System.out.println("identifier at '" + k + "' is '" + as[k] + "'");
			}

			sxdatainputstream.readUTF(as, i, numOfCollections);
			setIds(as);
			int l = size() * 44 + numOfIdChars * 2;
			int i1 = l - j;
			setMemoryNodeSizeInBytes(getMemoryNodeSizeInBytes() + i1);
			if(cacheRef != null)
				cacheRef.leafNodeHasGrown(i1);
			idBytes = null;
		}
		catch(java.lang.Exception exception)
		{
			log("SwSpatialIndexLeafNode.setIdentifiersFromBinary method failed due to the following exception:");
			log(exception);
		}
		finally
		{
			sxdatainputstream.close();
		}
	}

	protected synchronized void setLabelsFromBinary(byte abyte0[])
	{
		int i = size();
		if(abyte0 == null || i == 0)
			return;
		com.ofx.persistence.SxDataInputStream sxdatainputstream = null;
		Object obj = null;
		try
		{
			sxdatainputstream = new SxDataInputStream(abyte0, 0, abyte0.length);
			labelsAreCondensed = sxdatainputstream.readBoolean();
			if(labelsAreCondensed)
			{
				int j = sxdatainputstream.readInt();
				condensedLabels = new java.lang.String[j];
				sxdatainputstream.readUTF(condensedLabels, j, 0);
				condensedLabelsIndexes = new short[i];
				boolean flag = false;
				for(int k = 0; k < i; k++)
					condensedLabelsIndexes[k] = sxdatainputstream.readShort();

			} else
			{
				condensedLabels = new java.lang.String[i];
				sxdatainputstream.readUTF(condensedLabels, i, 0);
			}
		}
		catch(java.lang.Exception exception)
		{
			log("SwSpatialIndexLeafNode.setLabelsFromBinary method failed due to the following exception:");
			if(getClassDefinition() != null)
				log("Error deserializing class: " + getClassDefinition().getIdentifier());
			log(exception);
		}
		finally
		{
			sxdatainputstream.close();
		}
	}

	protected java.lang.String[] getCollectionIdentifiersFromBinary(byte abyte0[], int i)
	{
		com.ofx.persistence.SxDataInputStream sxdatainputstream = null;
		java.lang.String as[] = new java.lang.String[i];
		try
		{
			sxdatainputstream = new SxDataInputStream(abyte0, 0, abyte0.length);
			sxdatainputstream.readUTF(as, i, 0);
			nonCollIdStartPos = abyte0.length - sxdatainputstream.available();
		}
		catch(java.lang.Exception exception)
		{
			log("SwSpatialIndexLeafNode.getCollectionIdentifersFromBinary method failed due to the following exception:");
			log(exception);
		}
		finally
		{
			sxdatainputstream.close();
		}
		return as;
	}

	public static long totalSize = 0L;
	public static long totalObjects = 0L;
	private static final int OFX_CLASS_VERSION = 0x20000;
	private static final long serialVersionUID = 0x119a19ba0a4a8b98L;
	protected static java.lang.Boolean CacheBlobsForTransport;
	public static final int INT_OFFSET_FROM_POINT = 1;
	public static final int ALL_DOUBLE = 2;
	protected com.ofx.repository.SxClass classDefinition;
	protected java.lang.String ID;
	protected com.ofx.geometry.SxRectangle boundsOfNode;
	protected transient com.ofx.geometry.SxDoublePoint nodeRefPoint;
	protected int nodeSize;
	protected int memoryNodeSize;
	protected com.ofx.index.SxIndexable spatialObjectsInNode[];
	protected java.util.Hashtable spatialIdentifiers;
	protected java.lang.String identifiersInNode[];
	protected int nodeFormat;
	protected int vOffsets[];
	protected int vInts[];
	protected double vDoubles[];
	protected java.lang.Object extraAttributes[];
	protected short numOfCollections;
	protected int numCollGeometries;
	protected int numCollVerts;
	protected boolean isHoles[][];
	protected double verticesOfCollection[][][];
	protected com.ofx.index.SxIndexable collectionObjects[];
	protected int numNonCollVerts;
	protected int numNonCollObjects;
	protected int count;
	protected int capacity;
	protected boolean cachingSpatialObjects;
	protected java.lang.String queryClassification;
	protected double precisionTolerance;
	protected double rightMultiplier;
	protected double leftMultiplier;
	protected java.lang.String spatialCacheMode;
	protected boolean isCompressed;
	protected byte idBytes[];
	protected int nonCollIdStartPos;
	protected transient boolean initSpatialObjects;
	protected transient boolean isFiltered;
	protected transient boolean labelsAreCondensed;
	protected transient short condensedLabelsIndexes[];
	protected transient java.lang.String condensedLabels[];
	protected transient java.lang.String sharedLabels[];
	protected transient int numOfLabelChars;
	protected transient int numOfIdChars;
	protected transient boolean isPointType;
	public transient com.ofx.persistence.SxCache cacheRef;
	protected byte sourceBytes[];

	protected temp_sxtext_class textsOfCollection[];
	protected transient boolean isTextType = false;

	temp_sxtext_class temp_sxtext_instance = null;

	static 
	{
		com.ofx.index.rtree.SxSpatialIndexLeafNode.CacheBlobsForTransport = com.ofx.base.SxEnvironment.singleton().getProperties().getBoolean("ofx.cache.blobs.for.transport", java.lang.Boolean.TRUE);
	}
}
