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
// * ��������: ����� �������� ����������� �� �� ���������� �����          * //
// *           ������ ������ - ��� ����������� ������� ������-������      * //
// *           ������� ������������ � ������� �������, ��� ��� ���        * //
// *           ����������� ������� ���������� ����� ����������� �����     * //
// *           �� ������� ����������� ��������, � � ������ �� ����������  * //
// *           ��� ������������ � �������                                 * //
// *                                                                      * //
// * ���: Java 1.4.0                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 24 mar 2003                                                      * //
// * ������������: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\DataSourceImage.java                                 * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 9.0.3.9.93                       * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.0)   * //
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

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.DataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;

import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;

import java.util.Vector;

public class MapViewDataSourceImage extends DataSourceImage
{
	protected MapViewDataSourceImage()
	{
	}
	
	public MapViewDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void LoadMapViews()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(MapView.typ);

//		Pool.removeHash("mapcontext");
//		Pool.removeHash("mapequipmentelement");
//		Pool.removeHash("mapphysicallinkelement");
//		Pool.removeHash("mapphysicalnodeelement");
//		Pool.removeHash("mapnodelinkelement");
//		Pool.removeHash("mappathelement");
//		Pool.removeHash("mapmarker");

		load(ImageResource.typ, ImageCatalogue.getHash());
		load(MapView.typ);
		Vector ids = filter(Map.typ, desc, true);
		if(ids.size() > 0)
		{
			if(this.di instanceof RISDMapViewDataSource)
			{
				((RISDMapViewDataSource )this.di).LoadMapViews((String[] )ids.toArray());
			}
			else
			{
				((EmptyMapViewDataSource )this.di).LoadMapViews((String[] )ids.toArray());
			}
			save(MapView.typ);
			save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}
	
}

