package com.syrus.AMFICOM.Client.Schematics.UI;

import com.syrus.AMFICOM.Client.Configure.UI.TransmissionPathPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

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
		super.setObjectResource(or);
	}
}

