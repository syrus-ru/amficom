package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.Serializable;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.StubResource;


// ВНИМАНИЕ! При изменениях этого класса его надо отдавать Андрею как исходникик, так как он у него лежит в map !

// класс хранит только информацию о топологии решения (берёт его из класса Scheme)
// дополнительная информация ( цены оборудования, список оборудования и т.п.) -  в классе InfoToStore
//==========================================================================================================
public class SolutionCompact extends StubResource implements Serializable
{
	public static final String typ = "sm_solution";
	private static final long serialVersionUID = 01L;
	public SchemeMonitoringSolution_Transferable transferable;

	public String id = "";
	public String name = "";
	public String created = "";// время создания
	public String created_by = "";
	public String description = "";
	public String schemeId = ""; // идентификатор схемы , к которой это решение относится
	public String domainId = "";
	public double price = -1;
	public Collection paths; // список путей SchemePaths (инициализируем просто чтобы не было null pointer exception если что не пропишется)

	public SolutionCompact()
	{
		paths = new ArrayList();
		transferable = new SchemeMonitoringSolution_Transferable();
	}

	// по данным из решения создаём наш формат записи для БД
	public SolutionCompact(double price, String schemeId, Collection paths, String id, String name)
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.schemeId = schemeId;
		this.paths = paths;

		transferable = new SchemeMonitoringSolution_Transferable();
	}

	public SolutionCompact(SchemeMonitoringSolution_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		schemeId = transferable.schemeId;
		price = transferable.price;
		// прописываем пути SchemePath из массива в вектор
		paths = new ArrayList(transferable.paths.length);
		for(int i = 0; i < transferable.paths.length; i++)
			paths.add(new SchemePath(transferable.paths[i]));
	}

	public void  updateLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.schemeId = schemeId;
		transferable.price = price;
		transferable.paths = new SchemePath_Transferable[paths.size()];
		Iterator it = paths.iterator();
		for(int i = 0; i < transferable.paths.length; i++)
		{
			SchemePath path = (SchemePath)it.next();
			path.setTransferableFromLocal();
			transferable.paths[i] = (SchemePath_Transferable)path.getTransferable();
		}
	}

	public String getDomainId()
	{
		return domainId;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getTyp()
	{
		return typ;
	}
}
