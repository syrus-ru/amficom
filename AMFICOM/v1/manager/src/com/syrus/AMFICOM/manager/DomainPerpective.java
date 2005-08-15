/*-
* $Id: DomainPerpective.java,v 1.2 2005/08/15 14:20:05 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;


/**
 * @version $Revision: 1.2 $, $Date: 2005/08/15 14:20:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainPerpective implements Perspective {

	private final DomainBean domainBean;
	
	public DomainPerpective(final DomainBean domainBean) {
		this.domainBean = domainBean;
	}
	
	public String getPerspectiveName() {
		return this.getDomainId().getIdentifierString();
	}
	
	public final Identifier getDomainId() {
		return this.domainBean.getId();
	}

	public void insertBean(AbstractBean bean) {
		Identifier beanId = bean.getId();
		if (beanId != null) {
			try {
				StorableObject storableObject = StorableObjectPool.getStorableObject(beanId, true);
				if (storableObject instanceof DomainMember) {
					DomainMember  domainMember = (DomainMember)storableObject;
					domainMember.setDomainId(this.getDomainId());
				}
				if (storableObject instanceof SystemUser) {
					SystemUser systemUser = (SystemUser) storableObject;
					Identifier userId = systemUser.getId();
					PermissionAttributes permissionAttributes = 
						this.domainBean.domain.getPermissionAttributes(systemUser.getId());
					
					if (permissionAttributes == null) {
						permissionAttributes = PermissionAttributes.createInstance(
							LoginManager.getUserId(),
							this.getDomainId(),
							userId,
							0L);
					}
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

