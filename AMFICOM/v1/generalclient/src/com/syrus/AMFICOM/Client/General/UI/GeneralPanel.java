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
// * ��������: ���������� ��������� ����� ���������� ��������� ����       * //
// *           (�������� ���������� ������ pmServer � ������ pmRISDImpl)  * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\UI\GeneralPanel.java                           * //
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

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.text.SimpleDateFormat;

import java.util.Date;

public class GeneralPanel
		extends PropertiesPanel
{
	XYLayout xYLayout1 = new XYLayout();

	public ApplicationContext aContext = new ApplicationContext();
	
	public GeneralPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setName(LangModel.String("labelTabbedProperties"));
	
//		xYLayout1.setWidth(610);
//		xYLayout1.setHeight(410);

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.setLayout(xYLayout1);
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		return true;
	}
	
	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public boolean modify()
	{
		return false;
	}
	
	public boolean create()
	{
		return false;
	}
	
	public boolean delete()
	{
		return false;
//		or.prepareRemoval();
//		Pool.remove(or.getTyp(), or);
	}

	public boolean open()
	{
		return false;
	}
	
	public boolean save()
	{
		return false;
	}
	
}


