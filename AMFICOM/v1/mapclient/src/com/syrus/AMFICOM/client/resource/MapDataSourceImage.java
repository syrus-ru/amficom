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

import java.util.*;
import java.util.zip.*;
import java.io.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;

public class MapDataSourceImage extends SchemeDataSourceImage
{
	protected MapDataSourceImage()
	{
	}
	
	public MapDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void LoadProtoElements()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors("mapprotoelement");
		ResourceDescriptor_Transferable[] desc1 = GetDescriptors("mapprotogroup");
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors("maplinkproto");
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors("mappathproto");

//		Pool.removeHash("imageresource");
//		Pool.removeHash("mapprotoelement");
//		Pool.removeHash("maplinkproto");
//		Pool.removeHash("mappathproto");

		load("imageresource", ImageCatalogue.hash);
		load("mapprotogroup");
		load("mapprotoelement");
		load("maplinkproto");
		load("mappathproto");
		Vector ids = filter("mapprotoelement", desc, true);
		Vector ids1 = filter("mapprotogroup", desc1, true);
		Vector ids2 = filter("maplinkproto", desc2, true);
		Vector ids3 = filter("mappathproto", desc3, true);
		if(	ids.size() > 0 ||
			ids1.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 )
		{
			di.LoadMapProtoElements(ids1, ids, ids2, ids3);
			save("mapprotogroup");
			save("mapprotoelement");
			save("maplinkproto");
			save("mappathproto");
			save("imageresource", ImageCatalogue.hash);
//			if(ids.size() > 0)
//				save("imageresource", ImageCatalogue.hash);
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
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors("mapcontext");

//		Pool.removeHash("mapcontext");
//		Pool.removeHash("mapequipmentelement");
//		Pool.removeHash("mapphysicallinkelement");
//		Pool.removeHash("mapphysicalnodeelement");
//		Pool.removeHash("mapnodelinkelement");
//		Pool.removeHash("mappathelement");
//		Pool.removeHash("mapmarker");

		load("imageresource", ImageCatalogue.hash);
		load("mapcontext");
		Vector ids = filter("mapcontext", desc, true);
		if(ids.size() > 0)
		{
			di.LoadMaps(ids);
			save("mapcontext");
			save("imageresource", ImageCatalogue.hash);
		}
	}
	
	public void LoadJMaps() 
	{
//		Pool.removeHash("ismmapcontext");
//		Pool.removeHash("mapequipmentelement");
//		Pool.removeHash("mapphysicallinkelement");
//		Pool.removeHash("mapphysicalnodeelement");
//		Pool.removeHash("mapnodelinkelement");
//		Pool.removeHash("mappathelement");
//		Pool.removeHash("mapmarker");

		LoadMaps();

		ResourceDescriptor_Transferable[] desc = GetDescriptors("ismmapcontext");

		load("ismmapcontext");
		Vector ids = filter("ismmapcontext", desc, true);
		if(ids.size() > 0)
		{
			di.LoadMaps(ids);
			save("ismmapcontext");
		}
	}

	public void LoadMapDescriptors() 
	{
		LoadMaps();
	}
	
	public void LoadJMapDescriptors() 
	{
		LoadJMaps();
	}
}

