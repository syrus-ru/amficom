/*-
 * $Id: SchemeStorableObjectFactory.java,v 1.2 2005/05/13 17:47:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.MapStorableObjectFactory;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem_Transferable;
import com.syrus.AMFICOM.scheme.corba.PathElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeCableThread_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeDevice_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeLink_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePath_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePort_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup_Transferable;
import com.syrus.AMFICOM.scheme.corba.Scheme_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/05/13 17:47:53 $
 * @module mshserver_v1
 */
public abstract class SchemeStorableObjectFactory extends MapStorableObjectFactory {
	/**
	 * @param schemeProtoGroup
	 */
	protected final SchemeProtoGroup newSchemeProtoGroup(final SchemeProtoGroup_Transferable schemeProtoGroup) {
		return new SchemeProtoGroup(schemeProtoGroup);
	}

	/**
	 * @param schemeProtoElement
	 * @throws CreateObjectException
	 */
	protected final SchemeProtoElement newSchemeProtoElement(final SchemeProtoElement_Transferable schemeProtoElement) throws CreateObjectException {
		return new SchemeProtoElement(schemeProtoElement);
	}

	/**
	 * @param scheme
	 */
	protected final Scheme newScheme(final Scheme_Transferable scheme) {
		return new Scheme(scheme);
	}

	/**
	 * @param schemeElement
	 * @throws CreateObjectException
	 */
	protected final SchemeElement newSchemeElement(final SchemeElement_Transferable schemeElement) throws CreateObjectException {
		return new SchemeElement(schemeElement);
	}

	/**
	 * @param schemeOptimizeInfo
	 */
	protected final SchemeOptimizeInfo newSchemeOptimizeInfo(final SchemeOptimizeInfo_Transferable schemeOptimizeInfo) {
		return new SchemeOptimizeInfo(schemeOptimizeInfo);
	}

	/**
	 * @param schemeMonitoringSolution
	 */
	protected final SchemeMonitoringSolution newSchemeMonitoringSolution(final SchemeMonitoringSolution_Transferable schemeMonitoringSolution) {
		return new SchemeMonitoringSolution(schemeMonitoringSolution);
	}

	/**
	 * @param schemeDevice
	 * @throws CreateObjectException
	 */
	protected final SchemeDevice newSchemeDevice(final SchemeDevice_Transferable schemeDevice) throws CreateObjectException {
		return new SchemeDevice(schemeDevice);
	}

	/**
	 * @param schemePort
	 * @throws CreateObjectException
	 */
	protected final SchemePort newSchemePort(final SchemePort_Transferable schemePort) throws CreateObjectException {
		return new SchemePort(schemePort);
	}

	/**
	 * @param schemeCablePort
	 * @throws CreateObjectException
	 */
	protected final SchemeCablePort newSchemeCablePort(final SchemeCablePort_Transferable schemeCablePort) throws CreateObjectException {
		return new SchemeCablePort(schemeCablePort);
	}

	/**
	 * @param schemeLink
	 * @throws CreateObjectException
	 */
	protected final SchemeLink newSchemeLink(final SchemeLink_Transferable schemeLink) throws CreateObjectException {
		return new SchemeLink(schemeLink);
	}

	/**
	 * @param schemeCableLink
	 * @throws CreateObjectException
	 */
	protected final SchemeCableLink newSchemeCableLink(final SchemeCableLink_Transferable schemeCableLink) throws CreateObjectException {
		return new SchemeCableLink(schemeCableLink);
	}

	/**
	 * @param schemeCableThread
	 * @throws CreateObjectException
	 */
	protected final SchemeCableThread newSchemeCableThread(final SchemeCableThread_Transferable schemeCableThread) throws CreateObjectException {
		return new SchemeCableThread(schemeCableThread);
	}

	/**
	 * @param cableChannelingItem
	 */
	protected final CableChannelingItem newCableChannelingItem(final CableChannelingItem_Transferable cableChannelingItem) {
		return new CableChannelingItem(cableChannelingItem);
	}

	/**
	 * @param schemePath
	 * @throws CreateObjectException
	 */
	protected final SchemePath newSchemePath(final SchemePath_Transferable schemePath) throws CreateObjectException {
		return new SchemePath(schemePath);
	}

	/**
	 * @param pathElement
	 */
	protected final PathElement newPathElement(final PathElement_Transferable pathElement) {
		return new PathElement(pathElement);
	}
}
