package com.syrus.AMFICOM.Client.Resource;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;

public class MiscUtil
{
	public static CharacteristicType getCharacteristicType(Identifier userId, String codename,
			CharacteristicTypeSort sort, DataType dataType)
	{
		TypicalCondition pTypeCondition = new TypicalCondition(codename, OperationSort.OPERATION_EQUALS,
																ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
																StorableObjectWrapper.COLUMN_CODENAME);
		

		try {
			Collection pTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext(); ) {
				CharacteristicType type = (CharacteristicType)it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch (ApplicationException ex) {
			System.err.println("Exception searching ParameterType. Creating new one.");
			ex.printStackTrace();
		}

		try {
			return CharacteristicType.createInstance(
					userId,
					codename,
					"",
					dataType.value(),
					sort);
		}
		catch (CreateObjectException e) {
			// FIXME
			e.printStackTrace();
			return null;
		}
	}


	public static List convert(Map map)
	{
		List list = new LinkedList();
		list.addAll(map.values());
		return list;
	}

	public static void addToCollection(Collection vec, Object []objs)
	{
		for(int i = 0; i < objs.length; i++)
			vec.add(objs[i]);
	}

	static public double fourdigits(double d)
	{
		return (long)(d * 10000) / 10000d;
	}

	static public float fourdigits(float d)
	{
		return (int)(d * 10000) / 10000f;
	}

	public static boolean validName(String name)
	{
		if(name == null)
			return false;
		if(name.trim().length() == 0)
			return false;
		return true;
	}
	
	public static String removeDigitsFromString(String str)
	{
		StringBuffer f = new StringBuffer(str);
		for (int i = f.length() - 1; i > 0; i--)
			if (!Character.isDigit(str.charAt(i)))
				return f.substring(0, i + 1); 
		return "";
	}

	public static double diagonale(double x1, double y1, double x1_, double y1_)
	{
		return Math.sqrt(
				(x1_ - x1) * (x1_ - x1) +
				(y1_ - y1) * (y1_ - y1) );
	}
}
