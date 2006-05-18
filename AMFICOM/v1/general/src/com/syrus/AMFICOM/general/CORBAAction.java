/*-
* $Id: CORBAAction.java,v 1.2 2005/10/11 14:14:36 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;


/**
 * @version $Revision: 1.2 $, $Date: 2005/10/11 14:14:36 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module csbridge
 */
public interface CORBAAction {

	void perform() throws AMFICOMRemoteException, ApplicationException;

}
