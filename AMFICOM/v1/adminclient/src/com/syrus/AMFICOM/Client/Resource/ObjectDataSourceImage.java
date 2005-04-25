package com.syrus.AMFICOM.Client.Resource;

import java.util.Vector;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.Object.OperatorCategory;
import com.syrus.AMFICOM.Client.Resource.Object.OperatorGroup;
import com.syrus.AMFICOM.Client.Resource.Object.OperatorProfile;
import com.syrus.AMFICOM.Client.Resource.System.Agent;
import com.syrus.AMFICOM.Client.Resource.System.Client;
import com.syrus.AMFICOM.Client.Resource.System.Server;

public final class ObjectDataSourceImage extends DataSourceImage {
	public ObjectDataSourceImage(DataSourceInterface di) {
		super(di);
	}

	public void GetObjects() {
		ResourceDescriptor_Transferable[] desc = GetDescriptors(OperatorCategory.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(OperatorGroup.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors(OperatorProfile.typ);

		//		Pool.removeHash(OperatorCategory.typ);
		//		Pool.removeHash(OperatorGroup.typ);
		//		Pool.removeHash(OperatorProfile.typ);

		load(OperatorCategory.typ);
		load(OperatorGroup.typ);
		load(OperatorProfile.typ);
		Vector ids = filter(OperatorCategory.typ, desc, true);
		Vector ids2 = filter(OperatorGroup.typ, desc2, true);
		Vector ids3 = filter(OperatorProfile.typ, desc3, true);
		if (ids.size() > 0 || ids2.size() > 0 || ids3.size() > 0) {
			String[] s1 = new String[ids.size()];
			ids.copyInto(s1);
			String[] s2 = new String[ids2.size()];
			ids2.copyInto(s2);
			String[] s3 = new String[ids3.size()];
			ids3.copyInto(s3);
			di.GetObjects(s1, s2, s3);
			save(OperatorCategory.typ);
			save(OperatorGroup.typ);
			save(OperatorProfile.typ);
		}
	}

	public void GetAdminObjects() {
		ResourceDescriptor_Transferable[] desc = GetDescriptors(Server.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(Client.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors(Agent.typ);

		//		Pool.removeHash(Server.typ);
		//		Pool.removeHash(Client.typ);
		//		Pool.removeHash(Agent.typ);

		load(Server.typ);
		load(Client.typ);
		load(Agent.typ);
		Vector ids = filter(Server.typ, desc, true);
		Vector ids2 = filter(Client.typ, desc2, true);
		Vector ids3 = filter(Agent.typ, desc3, true);
		if (ids.size() > 0 || ids2.size() > 0 || ids3.size() > 0) {
			String[] s1 = new String[ids.size()];
			ids.copyInto(s1);
			String[] s2 = new String[ids2.size()];
			ids2.copyInto(s2);
			String[] s3 = new String[ids3.size()];
			ids3.copyInto(s3);
			di.GetAdminObjects(s1, s2, s3);
			save(Server.typ);
			save(Client.typ);
			save(Agent.typ);
		}
	}
}
