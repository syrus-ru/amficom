//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Класс хранения отображения БД на клиентскую часть          * //
// *           задача модуля - для минимизации трафика клиент-сервер      * //
// *           хранить подгружаемые с сервера объекты, так что при        * //
// *           последующем запуске клиентской части проверяется образ     * //
// *           на наличие необходимых объектов, и в случае их отсутствия  * //
// *           они подгружаются с сервера                                 * //
// *                                                                      * //
// * Тип: Java 1.4.0                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 24 mar 2003                                                      * //
// * Расположение: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\DataSourceImage.java                                 * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 9.0.3.9.93                       * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.0)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
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
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;

import java.util.Vector;

public class MapDataSourceImage extends DataSourceImage
{
	RISDMapDataSource di1;

	protected MapDataSourceImage()
	{
	}
	
	public MapDataSourceImage(DataSourceInterface di)
	{
		super(di);
		this.di1 = (RISDMapDataSource )di;
	}

	public void LoadProtoElements()
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
			di1.LoadMapProtoElements(
				(String[] )ids.toArray(), 
				(String[] )ids2.toArray());
			save(MapNodeProtoElement.typ);
			save(MapLinkProtoElement.typ);
			save(ImageResource.typ, ImageCatalogue.getHash());
		}
/*		
		ImageCatalogue.add(
				"node",
				new ImageResource("node", "node", "images/node.gif"));
		ImageCatalogue.add(
				"void",
				new ImageResource("void", "void", "images/void.gif"));
		ImageCatalogue.add(
				"cable",
				new ImageResource("cable", "cable", "images/linkmode.gif"));
		ImageCatalogue.add(
				"path",
				new ImageResource("path", "path", "images/pathmode.gif"));
		ImageCatalogue.add(
				"net",
				new ImageResource("net", "net", "images/net.gif"));
*/
	}

	public void LoadMaps()
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
			di1.LoadMaps((String[] )ids.toArray());
			save(Map.typ);
			save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}
	
}

