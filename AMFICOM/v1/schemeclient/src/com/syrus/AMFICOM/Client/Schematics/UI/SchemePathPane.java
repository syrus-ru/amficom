package com.syrus.AMFICOM.Client.Schematics.UI;

import com.syrus.AMFICOM.Client.Configure.UI.TransmissionPathPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemePathPane extends TransmissionPathPane
{
	public SchemePathPane()
	{
	}

	public SchemePathPane(SchemePath path)
	{
		super();
		setObjectResource(path);
	}

	public void setObjectResource(ObjectResource or)
	{
		if (or instanceof TransmissionPath)
			super.setObjectResource(or);
		else if (or instanceof SchemePath)
		{
			SchemePath sp = (SchemePath)or;
			if (sp.path_id.length() != 0)
				super.setObjectResource ((TransmissionPath)Pool.get(TransmissionPath.typ, sp.path_id));
		}
	}
}

