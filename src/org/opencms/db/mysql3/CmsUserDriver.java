/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/db/mysql3/Attic/CmsUserDriver.java,v $
 * Date   : $Date: 2006/08/19 13:40:46 $
 * Version: $Revision: 1.1.8.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.db.mysql3;

import org.opencms.db.generic.CmsSqlManager;

/**
 * MySQL3 implementation of the user driver methods.<p>
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.1.8.1 $
 * @since 6.0.0 
 */
public class CmsUserDriver extends org.opencms.db.mysql.CmsUserDriver {

    /**
     * @see org.opencms.db.I_CmsUserDriver#initSqlManager(String)
     */
    public org.opencms.db.generic.CmsSqlManager initSqlManager(String classname) {

        return CmsSqlManager.getInstance(classname);
    }
}
