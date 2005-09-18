package com.syrus.AMFICOM.report;

import java.io.IOException;
import java.io.Serializable;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * <p>Title: </p>
 * <p>Description: ������� ������� - �� ���������. ��� �������� ���������
 * ��������� ������� ������������ TableDataStorableElement</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public class DataStorableElement extends StorableElement implements Serializable 
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
	 * �������� �� ��, ��� ������� ����� ������ ����������� ������ � ������
	 * ������� ������, �� ����� ��������������, ��������� �� ��� ���������
	 * ����������� �������
	 */
	protected Identifier id = Identifier.VOID_IDENTIFIER;
	/**
	 * ������������� �������, �� �������� �������� �����.
	 */
	protected Identifier reportObjectId = null;

	public String getReportName() {
		return this.reportName;
	}

	public Identifier getId() {
		return this.id;
	}

	public String getModelClassName() {
		return this.modelClassName;
	}

	public DataStorableElement(String reportName, String modelClassName)
	{
		this.reportName = reportName;
		this.modelClassName = modelClassName;
		//TODO ����� ������ ���� ��������� ID
		this.id = Identifier.VOID_IDENTIFIER;
	}
	
	public void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.getSize());
		out.writeObject(this.getLocation());		

		out.writeObject(this.getId());		
		out.writeObject(this.getReportName());		
		out.writeObject(this.getModelClassName());
		out.writeObject(this.getReportObjectId());		
	}

	public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.setSize((IntDimension)in.readObject());
		this.setLocation((IntPoint)in.readObject());

		this.id = (Identifier)in.readObject();
		this.reportName = (String)in.readObject();
		this.modelClassName = (String)in.readObject();
		this.reportObjectId = (Identifier)in.readObject();
	}

	public Identifier getReportObjectId() {
		return this.reportObjectId;
	}

	public void setReportObjectId(Identifier reportObjectId) {
		this.reportObjectId = reportObjectId;
	}
}
