//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: прототип Интегрированной Системы Мониторинга                 * //
// *                                                                      * //
// * Название: класс pmRISDQuery используется для формирования            * //
// *           динамических запросов к РИСД с различными условиями выбора * //
// * Тип: Java 1.2.2 (.JAVA)                                              * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 26 jun 2001                                                      * //
// * Расположение: ISM\prog\java\pm\pmCommon\pmRISDQuery.java             * //
// *                                                                      * //
// * Компилятор: Oracle JDeveloper                                        * //
// *                                                                      * //
// * Статус: проверяется                                                  * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем              Когда      Комментарии                             * //
// * --------------   ---------- ---------------------------------------- * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.server;

/**
 * Реализация статического класса pmRISDQuery
 * для формирования динамических запросов
 *
 * @version $Revision: 1.2 $, $Date: 2004/06/07 15:36:09 $
 * @module servermisc
 */
public class QueryBuilder
{
	/**
	 * максимальное число полей упорядоч.
	 */
	static final int MAX_ORDER_BY = 5;

	/**
	 * строка запроса
	 */
	public String query;

	/**
	 * счётчик добавленных условий where
	 */
	private int condCounter;

	/**
	 * счётчик полей для order by
	 */
	private int ordCounter;

	/**
	 * счётчик добавленных полей для set
	 */
	private int setCounter;

	/**
	 * счётчик добавленных полей для value
	 */
	private int valCounter;

	/**
	 * строка блока order by
	 */
	private String order;

	/**
	 * строка блока insert into tbl()
	 */
	private String fields;

	/**
	 * строка блока values()
	 */
	private String values;

	/**
	 * массив полей упорядочивания order by
	 */
	private String ordarray[];

	/**
	 * в конструкторе установить блок select ... from ...
	 */
	public QueryBuilder(String s)
	{
		query = (s == null) ? "" : s;
		order = "";
		fields = "";
		values = "";
		condCounter = 0;
		ordCounter = 0;
		setCounter = 0;
		valCounter = 0;
		ordarray = new String[MAX_ORDER_BY];
	}

	/**
	 * конструктор без параметров устанавливает запрос по умолчанию
	 * - пустая строка
	 */
	public QueryBuilder()
	{
		this(null);
	}

	/**
	 * установить значение запроса s и сбросить внутренние переменные
	 */
	public void setQuery(String s)
	{
		query = (s == null)? "" : s;
		order = "";
		fields = "";
		values = "";
		condCounter = 0;
		ordCounter = 0;
		setCounter = 0;
		valCounter = 0;
	}

	/**
	 * найти, есть ли уже в блоке order by поле с данным именем чтобы
	 * не добавлять его в поиск повторно
	 */
	private boolean searchSubstr(String ord)
	{
		for (int i = 0; i < ordCounter; i++)
			if (ord.equals(ordarray[i]))
				return true;
		return false;
	}

	/**
	 * добавить условие выборки where ... или and ...
	 * передаваемое в параметре s и добавить при этом поле ord
	 * в блок order by
	 */
	public void addCondition(String s, String ord)
	{
		// если это первое условие - поставить where cond
		// если where cond уже есть (в этом случае condCounter != 0)
		// то добавить and cond
		query += ((condCounter == 0) ? " where " : " and ") + s;
		condCounter++;

		// если ord не пусто и такого поля в order by еще нет, то добавить
		if ((ord != null) && (ord.length() != 0) && (!searchSubstr(ord)))
		{
			if (ordCounter == MAX_ORDER_BY)
				return;
			order += ((ordCounter == 0) ? " order by " : ", ") + ord;
			ordarray[ordCounter++] = ord;
		}
	}

	/**
	 * добавить условие установки поля set ... или ,...
	 * передаваемое в параметре s
	 */
	public void addSet(String s)
	{
		// если это первое поле - поставить set
		// если set s уже есть (в этом случае setCounter != 0)
		// то добавить ,...
		query += ((setCounter == 0) ? " set " : ", ") + s;
		setCounter++;
	}

	/**
	 * добавить поле f установки insert into tbl(...) values(...)
	 * передаваемое в параметре s
	 */
	public void addValue(String f, String s)
	{
		// если это первое поле - поставить set
		// если set s уже есть (в этом случае setCounter != 0)
		// то добавить ,...
		fields += ((valCounter == 0) ? "(" : ", ") + f;
		values += ((valCounter == 0) ? " values(" : ", ") + s;
		valCounter++;
	}

	/**
	 * завершить формирование запроса, соединив вместе
	 * select ... from ... where ... order by ...
	 */
	public void finishQuery()
	{
		if(valCounter > 0)
		{
			fields += ")";
			values += ")";
		}
		query += fields + values + order;
	}
}
