/*-
 * $Id: ImageResourceFactory.java,v 1.1 2005/05/24 16:40:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/24 16:40:06 $
 * @module resource_v1
 */
final class ImageResourceFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	protected StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException {
		final ImageResource_Transferable imageResource = (ImageResource_Transferable) transferable;
		switch (imageResource.data.discriminator().value()) {
			case ImageResourceSort._BITMAP:
				return new BitmapImageResource(imageResource);
			case ImageResourceSort._FILE:
				return new FileImageResource(imageResource);
			case ImageResourceSort._SCHEME:
				return new SchemeImageResource(imageResource);
			default:
				throw new CreateObjectException("ImageResourceFactory.newInstance() | " + ErrorMessages.NATURE_INVALID);
		}
	}
}
