/**
 * $Id: MapView.java,v 1.33 2005/02/01 11:34:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.map.Map;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической карте. Объекты включают в себя:
 * <br>&#9;- объект карты Map, то есть прокладку канализационную
 * <br>&#9;- набор физических схем {@link Scheme}, которые проложены по данному
 *        контексту
 * <br>Объект <code>MapView</code> являтся вроппером для класса 
 * <code>{@link com.syrus.AMFICOM.mapview.MapView}</code>
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.33 $, $Date: 2005/02/01 11:34:56 $
 * @module mapviewclient_v1
 * @deprecated use {@link LogicalNetLayer#getMapViewController()}
 */
public final class MapView
{
	private static final long serialVersionUID = 01L;

	/**
	 * Установить топологическую схему.
	 * @param map топологическая схема
	 */
	public void setMap(Map map)
	{
//		scanSchemes();
	}
	
/* from SiteNode

	//Возвращяет длинну линий внутри данного узла,
	//пересчитанную на коэффициент топологической привязки
	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		physical_length = 0.0;

		if(this.elementId == null || this.elementId.equals(""))
			return pe;
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, this.elementId);

		Vector vec = new Vector();
		Enumeration e = se.getAllSchemesLinks();
		for(;e.hasMoreElements();)
			vec.add(e.nextElement());

		for(;;)
		{
			if(pe.is_cable)
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.linkId);
				if(schemeCableLink == null)
				{
					System.out.println("Something wrong... - schemeCableLink == null");
					return pe;
				}
				if(!vec.contains(schemeCableLink))
					return pe;
				physical_length += schemeCableLink.getPhysicalLength();
			}
			else
			{
				SchemeLink schemeLink =
						(SchemeLink )Pool.get(SchemeLink.typ, pe.linkId);
				if(schemeLink == null)
				{
					System.out.println("Something wrong... - schemeLink == null");
					return pe;
				}
				if(!vec.contains(schemeLink))
					return pe;
				physical_length += schemeLink.getPhysicalLength();
			}
			if(pathelements.hasMoreElements())
				pe = (PathElement )pathelements.nextElement();
			else
				return null;
		}

		return pe;//stub
	}
*/	


}
