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


//////////////////////////////////////////////////////////////////////////////
// ���������� ������������ ������ pmRISDQuery
// ��� ����������� ������������ ��������
//////////////////////////////////////////////////////////////////////////////

public class QueryBuilder
{
	private int cond_counter;			// ������� ����������� ������� where
	private int ord_counter;			// ������� ����� ��� order by
	private int set_counter;			// ������� ����������� ����� ��� set
	private int val_counter;			// ������� ����������� ����� ��� value
	public String query;				// ������ �������
	private String order;				// ������ ����� order by
	private String fields;				// ������ ����� insert into tbl()
	private String values;				// ������ ����� values()
	private String ordarray[];			// ������ ����� �������������� order by
	static final int MAX_ORDER_BY = 5;	// ������������ ����� ����� ��������.

	// � ������������ ���������� ���� select ... from ...
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

	// ����������� ��� ���������� ������������� ������ �� ���������
	// - ������ ������
	public QueryBuilder()
	{
		this(null);
	}

	// ���������� �������� ������� s � �������� ���������� ����������
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

	// �����, ���� �� ��� � ����� order by ���� � ������ ������ �����
	// �� ��������� ��� � ����� ��������
	private boolean SearchSubstr(String ord)
	{
		int i;

		for(i = 0;i < ord_counter; i++)
			if (ord.equals(ordarray[i]))
				return true;
		return false;
	}

	// �������� ������� ������� where ... ��� and ...
	// ������������ � ��������� s � �������� ��� ���� ���� ord
	// � ���� order by
	public void addCondition(String s, String ord)
	{
		// ���� ��� ������ ������� - ��������� where cond
		// ���� where cond ��� ���� (� ���� ������ cond_counter != 0)
		// �� �������� and cond
		query = query + ((cond_counter == 0)?" where ":" and ") + s;
		cond_counter++;

		// ���� ord �� ����� � ������ ���� � order by ��� ���, �� ��������
		if (ord != null && ord != "")
			if (SearchSubstr(ord) == false)
			{
				if (ord_counter == MAX_ORDER_BY)
					return;
				order = order + ((ord_counter == 0)?" order by ":", ") + ord;
				ordarray[ord_counter++] = ord;
			}
	}

	// �������� ������� ��������� ���� set ... ��� ,...
	// ������������ � ��������� s
	public void addSet(String s)
	{
		// ���� ��� ������ ���� - ��������� set
		// ���� set s ��� ���� (� ���� ������ set_counter != 0)
		// �� �������� ,...
		query = query + ((set_counter == 0)?" set ":", ") + s;
		set_counter++;
	}

	// �������� ���� f ��������� insert into tbl(...) values(...)
	// ������������ � ��������� s
	public void addValue(String f, String s)
	{
		// ���� ��� ������ ���� - ��������� set
		// ���� set s ��� ���� (� ���� ������ set_counter != 0)
		// �� �������� ,...
		fields = fields + ((val_counter == 0)?"(":", ") + f;
		values = values + ((val_counter == 0)?" values(":", ") + s;
		val_counter++;
	}
	// ��������� ������������ �������, �������� ������
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

