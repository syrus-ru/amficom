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
// * Название: класс, описывающий свойства, одинаковые для всех объектов, * //
// *           загружаемых в клиентское ПО с РИСД. Каждый объект          * //
// *           содержит экземпляр данного класса и может использовать     * //
// *           из него необходимые члены для хранения информации,         * //
// *           связанной с обменом клиентского ПО с РИСД                  * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\ObjectResource.java                           * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
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

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Color;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class StubDisplayModel implements ObjectResourceDisplayModel
{
	Vector cols = new Vector();
	Vector colnames = new Vector();
	int[] colsizes = new int[0];
	
	public StubDisplayModel()
	{
	}
	
	public StubDisplayModel(Vector cols, Vector colnames)
	{
		this.cols = cols;
		this.colnames = colnames;
	}
	
	public StubDisplayModel(Vector cols, Vector colnames, int[] colsizes)
	{
		this.cols = cols;
		this.colnames = colnames;
		this.colsizes = colsizes;
	}

	public StubDisplayModel(String[] cols, String[] colnames)
	{
		for(int i = 0; i < cols.length; i++)
			this.cols.add(cols[i]);
		for(int i = 0; i < colnames.length; i++)
			this.colnames.add(colnames[i]);
	}

	public StubDisplayModel(String[] cols, String[] colnames, int[] colsizes)
	{
		for(int i = 0; i < cols.length; i++)
			this.cols.add(cols[i]);
		for(int i = 0; i < colnames.length; i++)
			this.colnames.add(colnames[i]);
		this.colsizes = colsizes;
	}

	public Vector getColumns()
	{
		return cols;
	}
	
	public String getColumnName(String col_id)
	{
		try
		{
			for(int i = 0; i < cols.size(); i++)
			{
				String col = (String)cols.get(i);
				if(col.equals(col_id))
					return (String)colnames.get(i);
			}
		}
        catch (Exception ex) 
        {
        }
		return "";
	}
	
	public int getColumnSize(String col_id)
	{
		try
		{
			for(int i = 0; i < cols.size(); i++)
			{
				String col = (String)cols.get(i);
				if(col.equals(col_id))
					return colsizes[i];
			}
		}
        catch (Exception ex) 
        {
        }
		return 100;
	}
	
	public boolean isColumnEditable(String col_id)
	{
		return false;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
    {
//		ObjectResourceModel
		return null;//new JLabelRenderer(col_id);
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
    {
		return null;//new TextFieldEditor(col_id);
	}

	public boolean isColumnColored(String col_id)
	{
		return false;
	}
	
	public Color getColumnColor(ObjectResource or, String col_id)
	{
		return Color.white;
	}
}
