/*
 * File   : $Source: /alkacon/cvs/opencms/src/com/opencms/workplace/Attic/CmsPublishResourceThread.java,v $
 * Date   : $Date: 2002/12/13 09:16:19 $
 * Version: $Revision: 1.2 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 * 
 * Copyright (C) 2001  The OpenCms Group
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about OpenCms, please see the
 * OpenCms Website: http://www.opencms.org
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.opencms.workplace;

import com.opencms.core.A_OpenCms;
import com.opencms.core.CmsException;
import com.opencms.core.I_CmsLogChannels;
import com.opencms.file.CmsObject;
import com.opencms.report.CmsHtmlReport;
import com.opencms.report.I_CmsReport;

/**
 * Thread for publishing a resource.
 * 
 * @author Alexander Kandzior (a.kandzior@alkacon.com)
 */
public class CmsPublishResourceThread extends Thread {

    private String m_resourceName;
    private CmsObject m_cms;
    private boolean m_directPublish;
    private I_CmsReport m_report;

    public CmsPublishResourceThread(CmsObject cms, String resourceName) {
        m_cms = cms;
        m_cms.getRequestContext().setUpdateSessionEnabled(false);
        m_resourceName = resourceName;
        String locale = I_CmsWpConstants.C_DEFAULT_LANGUAGE;
        try {
            locale = CmsXmlLanguageFile.getCurrentUserLanguage(cms);
        } catch (CmsException e) {} // we will have the default then
        m_report = new CmsHtmlReport(locale);
    }

    public void run() {
        try {
            m_report.addSeperator(I_CmsReport.C_PUBLISH_RESOURCE_BEGIN);
            m_cms.publishResource(m_resourceName, false, m_report);
            m_report.addSeperator(I_CmsReport.C_PUBLISH_RESOURCE_END);
        }
        catch(CmsException e) {
            m_report.println(e);
            if(I_CmsLogChannels.C_LOGGING && A_OpenCms.isLogging(I_CmsLogChannels.C_OPENCMS_CRITICAL) ) {
                A_OpenCms.log(I_CmsLogChannels.C_OPENCMS_CRITICAL, e.getMessage());
            }
        }
    }

    /**
     * Returns the part of the report that is ready.
     *
     * @return the part of the report that is ready
     */
    public String getReportUpdate(){
        return m_report.getReportUpdate();
    }
}
