package com.syrus.AMFICOM.report;

import com.syrus.AMFICOM.general.Identifier;

/**
 * <p>Title: </p>
 * <p>Description: ������� �������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public abstract class DataStorableElement extends StorableElement
{
	/**
	 * �������� ������������� ������.
	 * �� ����� ����� �� ����� ������������ Renderer'�� � ������� ReportModel.
	 */
	protected String reportName = null;

	/**
	 * ������ ��� ������ ������, ������� "�����" ��� ������� ���� �����.
	 */
	protected String modelClassName = null;
	
	/**
	 * ���� ����� �������� �� ������-���� �������, �� � ������ ���� ��������
	 * ������������� ������ ��������. ���� �� ����� �������� �� ���� ��������
	 * ������� ����, �� ��� ������� �� ���� � � ��� ����������� ������.
	 * ����� � ���� ���� ����� ��������� ������ �� ����, ������ ������ ����
	 * ���������� ����������.
	 */
	private Object reportObjectData = null;
	
	/**
	 * �������� �� ��, ��� ������� ����� ������ ����������� ������ � ������
	 * ������� ������, �� ����� ��������������, ��������� �� ��� ���������
	 * ����������� �������
	 */
	protected Identifier id = Identifier.VOID_IDENTIFIER;

	public String getReportName() {
		return this.reportName;
	}

	public Identifier getId() {
		return this.id;
	}

	public String getModelClassName() {
		return this.modelClassName;
	}

	public Object getReportObjectData() {
		return this.reportObjectData;
	}

	/**
	 * ����� ��� �������� ����������� ������ ����������� ��� �����������
	 * @param data
	 */
	public void setReportObjectData(Object data) {
		this.reportObjectData = data;
	}
	
	public DataStorableElement(String reportName, String modelClassName)
	{
		this.reportName = reportName;
		this.modelClassName = modelClassName;
		//TODO ����� ������ ���� ��������� ID
		this.id = Identifier.VOID_IDENTIFIER;
	}
}
