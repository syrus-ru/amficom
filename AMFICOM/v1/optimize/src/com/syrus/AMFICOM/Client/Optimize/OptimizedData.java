package com.syrus.AMFICOM.Client.Optimize;

//Структура получаемых данных
public class OptimizedData
{	String kis_type_id;//Тип RTU
	String[] vectorOfNodesIDs;//Массив строк идентификаторов узлов

	public OptimizedData(String kis_type_id, String[] vectorOfNodesIDs)
	{	this.kis_type_id = kis_type_id;
		this.vectorOfNodesIDs = vectorOfNodesIDs;
	}
}