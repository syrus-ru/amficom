/**
 * $Id: MapDataSourceImage.java,v 1.7 2004/12/22 16:19:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;

import java.util.Vector;

/**
 * Класс хранения отображения БД на клиентскую часть
 * задача модуля - для минимизации трафика клиент-сервер
 * хранить подгружаемые с сервера объекты, так что при
 * последующем запуске клиентской части проверяется образ
 * на наличие необходимых объектов, и в случае их отсутствия
 * они подгружаются с сервера
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/22 16:19:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapDataSourceImage extends DataSourceImage
{
	protected MapDataSourceImage()
	{
	}
	
	public MapDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}
/*
	public void loadProtoElements()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(MapNodeProtoElement.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(MapLinkProtoElement.typ);

//		Pool.removeHash("imageresource");
//		Pool.removeHash("mapprotoelement");
//		Pool.removeHash("maplinkproto");
//		Pool.removeHash("mappathproto");

		load(ImageResource.typ, ImageCatalogue.getHash());
		load(MapNodeProtoElement.typ);
		load(MapLinkProtoElement.typ);
		Vector ids = filter(MapNodeProtoElement.typ, desc, true);
		Vector ids2 = filter(MapLinkProtoElement.typ, desc2, true);
		if(	ids.size() > 0 ||
			ids2.size() > 0)
		{
			if(this.di instanceof RISDMapDataSource)
			{
				((RISDMapDataSource )this.di).loadMapProtoElements(
					(String[] )ids.toArray(new String[ids.size()]), 
					(String[] )ids2.toArray(new String[ids2.size()]));
			}
			else
			{
				((EmptyMapDataSource )this.di).loadMapProtoElements(
					(String[] )ids.toArray(new String[ids.size()]), 
					(String[] )ids2.toArray(new String[ids2.size()]));
			}
			save(MapNodeProtoElement.typ);
			save(MapLinkProtoElement.typ);
			save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}

	public void loadMaps()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Map.typ);

//		Pool.removeHash("mapcontext");
//		Pool.removeHash("mapequipmentelement");
//		Pool.removeHash("mapphysicallinkelement");
//		Pool.removeHash("mapphysicalnodeelement");
//		Pool.removeHash("mapnodelinkelement");
//		Pool.removeHash("mappathelement");
//		Pool.removeHash("mapmarker");

		load(ImageResource.typ, ImageCatalogue.getHash());
		load(Map.typ);
		Vector ids = filter(Map.typ, desc, true);
		if(ids.size() > 0)
		{
			if(this.di instanceof RISDMapDataSource)
			{
				((RISDMapDataSource )this.di).loadMaps((String[] )ids.toArray(new String[ids.size()]));
			}
			else
			{
				((EmptyMapDataSource )this.di).loadMaps((String[] )ids.toArray(new String[ids.size()]));
			}
			save(Map.typ);
			save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}
*/	
}

