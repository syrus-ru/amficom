package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.*;

public class MiscUtil
{
	public static CharacteristicType getCharacteristicType(Identifier userId, String codename,
			CharacteristicTypeSort sort, DataType dataType)
	{
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
				codename,
				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
				StringFieldSort.STRINGSORT_BASE);

		try {
			List pTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
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
		if(name.length() == 0)
			return false;
		if(name.trim().length() == 0)
			return false;
		return true;
	}

	public static double diagonale(double x1, double y1, double x1_, double y1_)
	{
		return Math.sqrt(
				(x1_ - x1) * (x1_ - x1) +
				(y1_ - y1) * (y1_ - y1) );
	}
}
