package com.syrus.AMFICOM.Client.Resource.ISMDirectory;

import com.syrus.AMFICOM.CORBA.ISMDirectory.KISType_Transferable;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;

public class KISType extends EquipmentType
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "kistype";

	public KISType()
	{
		super();
	}

	public KISType(KISType_Transferable transferable)
	{
//		super(transferable);
		setLocalFromTransferable();
	}

	public String getTyp()
	{
		return typ;
	}

}

