package com.syrus.AMFICOM.Client.Optimize;

//��������� ���������� ������
public class OptimizedData
{	String kis_type_id;//��� RTU
	String[] vectorOfNodesIDs;//������ ����� ��������������� �����

	public OptimizedData(String kis_type_id, String[] vectorOfNodesIDs)
	{	this.kis_type_id = kis_type_id;
		this.vectorOfNodesIDs = vectorOfNodesIDs;
	}
}