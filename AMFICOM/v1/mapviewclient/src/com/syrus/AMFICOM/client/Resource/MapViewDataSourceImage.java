/**
 * $Id: MapViewDataSourceImage.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Vector;

/**
 * ��������: ����� �������� ����������� �� �� ���������� �����
 *           ������ ������ - ��� ����������� ������� ������-������
 *           ������� ������������ � ������� �������, ��� ��� ���
 *           ����������� ������� ���������� ����� ����������� �����
 *           �� ������� ����������� ��������, � � ������ �� ����������
 *           ��� ������������ � �������
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapViewDataSourceImage extends DataSourceImage
{
	protected MapViewDataSourceImage()
	{
	}
	
	public MapViewDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void loadMapViews()
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
				((RISDMapViewDataSource )this.di).loadMapViews((String[] )ids.toArray(new String[ids.size()]));
			}
			else
			{
				((EmptyMapViewDataSource )this.di).loadMapViews((String[] )ids.toArray(new String[ids.size()]));
			}
			save(MapView.typ);
			save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}
	
}

