//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ƒепартамент —истемных »сследований и –азработок                      * //
// *                                                                      * //
// * ѕроект: прототип »нтегрированной —истемы ћониторинга                 * //
// *                                                                      * //
// * Ќазвание: класс pmRISDQuery используетс€ дл€ формировани€            * //
// *           динамических запросов к –»—ƒ с различными услови€ми выбора * //
// * “ип: Java 1.2.2 (.JAVA)                                              * //
// *                                                                      * //
// * јвтор:  рупенников ј.¬.                                              * //
// *                                                                      * //
// * ¬ерси€: 0.1                                                          * //
// * ќт: 26 jun 2001                                                      * //
// * –асположение: ISM\prog\java\pm\pmCommon\pmRISDQuery.java             * //
// *                                                                      * //
// *  омпил€тор: Oracle JDeveloper                                        * //
// *                                                                      * //
// * —татус: провер€етс€                                                  * //
// *                                                                      * //
// * »зменени€:                                                           * //
// *   ем               огда       омментарии                             * //
// * --------------   ---------- ---------------------------------------- * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////
package com.syrus.AMFICOM.server;


//////////////////////////////////////////////////////////////////////////////
// –еализаци€ статического класса pmRISDQuery
// дл€ формировани динамических запросов
//////////////////////////////////////////////////////////////////////////////

public class QueryBuilder
{
	private int cond_counter;			// счетчик добавленных условий where
	private int ord_counter;			// счетчик полей дл€ order by
	private int set_counter;			// счетчик добавленных полей дл€ set
	private int val_counter;			// счетчик добавленных полей дл€ value
	public String query;				// строка запроса
	private String order;				// строка блока order by
	private String fields;				// строка блока insert into tbl()
	private String values;				// строка блока values()
	private String ordarray[];			// массим полей упор€дочивани€ order by
	static final int MAX_ORDER_BY = 5;	// максимальной число полей упор€доч.

	// в конструкторе установить блок select ... from ...
	public QueryBuilder(String s)
	{
		query = (s == null)?"":s;
		order = "";
		fields = "";
		values = "";
		cond_counter = 0;
		ord_counter = 0;
		set_counter = 0;
		val_counter = 0;
		ordarray = new String[MAX_ORDER_BY];
	}

	// конструктор без параметров устанавливает запрос по умолчанию
	// - пуста€ строка
	public QueryBuilder()
	{
		this(null);
	}

	// установить значение запроса s и сбросить внутренние переменные
	public void setQuery(String s)
	{
		query = (s == null)?"":s;
		order = "";
		fields = "";
		values = "";
		cond_counter = 0;
		ord_counter = 0;
		set_counter = 0;
		val_counter = 0;
	}

	// найти, есть ли уже в блоке order by поле с данным именем чтобы
	// не добавл€ть его в поиск повторно
	private boolean SearchSubstr(String ord)
	{
		int i;

		for(i = 0;i < ord_counter; i++)
			if (ord.equals(ordarray[i]))
				return true;
		return false;
	}

	// добавить условие выборки where ... или and ...
	// передаваемое в параметре s и добавить при этом поле ord
	// в блок order by
	public void addCondition(String s, String ord)
	{
		// если это первое условие - поставить where cond
		// если where cond уже есть (в этом случае cond_counter != 0)
		// то добавить and cond
		query = query + ((cond_counter == 0)?" where ":" and ") + s;
		cond_counter++;

		// если ord не пусто и такого пол€ в order by еще нет, то добавить
		if (ord != null && ord != "")
			if (SearchSubstr(ord) == false)
			{
				if (ord_counter == MAX_ORDER_BY)
					return;
				order = order + ((ord_counter == 0)?" order by ":", ") + ord;
				ordarray[ord_counter++] = ord;
			}
	}

	// добавить условие установки пол€ set ... или ,...
	// передаваемое в параметре s
	public void addSet(String s)
	{
		// если это первое поле - поставить set
		// если set s уже есть (в этом случае set_counter != 0)
		// то добавить ,...
		query = query + ((set_counter == 0)?" set ":", ") + s;
		set_counter++;
	}

	// добавить поле f установки insert into tbl(...) values(...)
	// передаваемое в параметре s
	public void addValue(String f, String s)
	{
		// если это первое поле - поставить set
		// если set s уже есть (в этом случае set_counter != 0)
		// то добавить ,...
		fields = fields + ((val_counter == 0)?"(":", ") + f;
		values = values + ((val_counter == 0)?" values(":", ") + s;
		val_counter++;
	}
	// завершить формирование запроса, соединив вместе
	// select ... from ... where ... order by ...
	public void finishQuery()
	{
		if(val_counter > 0)
		{
			fields = fields + ")";
			values = values + ")";
		}
		query = query + fields + values + order;
	}
}

