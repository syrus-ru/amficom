//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: �������� ��������������� ������� �����������                 * //
// *                                                                      * //
// * ��������: ����� pmRISDQuery ������������ ��� ������������            * //
// *           ������������ �������� � ���� � ���������� ��������� ������ * //
// * ���: Java 1.2.2 (.JAVA)                                              * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 26 jun 2001                                                      * //
// * ������������: ISM\prog\java\pm\pmCommon\pmRISDQuery.java             * //
// *                                                                      * //
// * ����������: Oracle JDeveloper                                        * //
// *                                                                      * //
// * ������: �����������                                                  * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���              �����      �����������                             * //
// * --------------   ---------- ---------------------------------------- * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.server;

/**
 * ���������� ������������ ������ pmRISDQuery
 * ��� ������������ ������������ ��������
 *
 * @version $Revision: 1.2 $, $Date: 2004/06/07 15:36:09 $
 * @module servermisc
 */
public class QueryBuilder
{
	/**
	 * ������������ ����� ����� ��������.
	 */
	static final int MAX_ORDER_BY = 5;

	/**
	 * ������ �������
	 */
	public String query;

	/**
	 * ������� ����������� ������� where
	 */
	private int condCounter;

	/**
	 * ������� ����� ��� order by
	 */
	private int ordCounter;

	/**
	 * ������� ����������� ����� ��� set
	 */
	private int setCounter;

	/**
	 * ������� ����������� ����� ��� value
	 */
	private int valCounter;

	/**
	 * ������ ����� order by
	 */
	private String order;

	/**
	 * ������ ����� insert into tbl()
	 */
	private String fields;

	/**
	 * ������ ����� values()
	 */
	private String values;

	/**
	 * ������ ����� �������������� order by
	 */
	private String ordarray[];

	/**
	 * � ������������ ���������� ���� select ... from ...
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
	 * ����������� ��� ���������� ������������� ������ �� ���������
	 * - ������ ������
	 */
	public QueryBuilder()
	{
		this(null);
	}

	/**
	 * ���������� �������� ������� s � �������� ���������� ����������
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
	 * �����, ���� �� ��� � ����� order by ���� � ������ ������ �����
	 * �� ��������� ��� � ����� ��������
	 */
	private boolean searchSubstr(String ord)
	{
		for (int i = 0; i < ordCounter; i++)
			if (ord.equals(ordarray[i]))
				return true;
		return false;
	}

	/**
	 * �������� ������� ������� where ... ��� and ...
	 * ������������ � ��������� s � �������� ��� ���� ���� ord
	 * � ���� order by
	 */
	public void addCondition(String s, String ord)
	{
		// ���� ��� ������ ������� - ��������� where cond
		// ���� where cond ��� ���� (� ���� ������ condCounter != 0)
		// �� �������� and cond
		query += ((condCounter == 0) ? " where " : " and ") + s;
		condCounter++;

		// ���� ord �� ����� � ������ ���� � order by ��� ���, �� ��������
		if ((ord != null) && (ord.length() != 0) && (!searchSubstr(ord)))
		{
			if (ordCounter == MAX_ORDER_BY)
				return;
			order += ((ordCounter == 0) ? " order by " : ", ") + ord;
			ordarray[ordCounter++] = ord;
		}
	}

	/**
	 * �������� ������� ��������� ���� set ... ��� ,...
	 * ������������ � ��������� s
	 */
	public void addSet(String s)
	{
		// ���� ��� ������ ���� - ��������� set
		// ���� set s ��� ���� (� ���� ������ setCounter != 0)
		// �� �������� ,...
		query += ((setCounter == 0) ? " set " : ", ") + s;
		setCounter++;
	}

	/**
	 * �������� ���� f ��������� insert into tbl(...) values(...)
	 * ������������ � ��������� s
	 */
	public void addValue(String f, String s)
	{
		// ���� ��� ������ ���� - ��������� set
		// ���� set s ��� ���� (� ���� ������ setCounter != 0)
		// �� �������� ,...
		fields += ((valCounter == 0) ? "(" : ", ") + f;
		values += ((valCounter == 0) ? " values(" : ", ") + s;
		valCounter++;
	}

	/**
	 * ��������� ������������ �������, �������� ������
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
