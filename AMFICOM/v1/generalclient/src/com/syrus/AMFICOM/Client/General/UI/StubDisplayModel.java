//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: �����, ����������� ��������, ���������� ��� ���� ��������, * //
// *           ����������� � ���������� �� � ����. ������ ������          * //
// *           �������� ��������� ������� ������ � ����� ������������     * //
// *           �� ���� ����������� ����� ��� �������� ����������,         * //
// *           ��������� � ������� ����������� �� � ����                  * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\ObjectResource.java                           * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;

public class StubDisplayModel implements ObjectResourceDisplayModel
{
	LinkedList cols = new LinkedList();
	LinkedList colnames = new LinkedList();
	int[] colsizes = new int[0];
	
	public StubDisplayModel()
	{
	}
	
	public StubDisplayModel(Vector cols, Vector colnames)
	{
		this.cols.addAll(cols);
		this.colnames.addAll(colnames);
	}
	
	public StubDisplayModel(Vector cols, Vector colnames, int[] colsizes)
	{
		this.cols.addAll(cols);
		this.colnames.addAll(colnames);
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

	public List getColumns()
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
