/**
 * $Id: MapViewDataSourceImage.java,v 1.3 2004/12/22 16:38:42 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Vector;

/**
 * Название: Класс хранения отображения БД на клиентскую часть
 *           задача модуля - для минимизации трафика клиент-сервер
 *           хранить подгружаемые с сервера объекты, так что при
 *           последующем запуске клиентской части проверяется образ
 *           на наличие необходимых объектов, и в случае их отсутствия
 *           они подгружаются с сервера
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/22 16:38:42 $
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
/*
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
		Vector ids = filter(MapView.typ, desc, true);
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
*/	
}

