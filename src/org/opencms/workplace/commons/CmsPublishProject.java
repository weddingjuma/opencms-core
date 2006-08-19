/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/workplace/commons/CmsPublishProject.java,v $
 * Date   : $Date: 2006/08/19 13:40:46 $
 * Version: $Revision: 1.27.4.1 $
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

package org.opencms.workplace.commons;

import org.opencms.db.CmsPublishList;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.lock.CmsLock;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.security.CmsPermissionSet;
import org.opencms.util.CmsDateUtil;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsReport;
import org.opencms.workplace.CmsWorkplaceManager;
import org.opencms.workplace.CmsWorkplaceSettings;
import org.opencms.workplace.threads.CmsPublishThread;
import org.opencms.workplace.threads.CmsRelationsValidatorThread;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;

/**
 * Creates the dialogs for publishing a project or a resource.<p> 
 * 
 * The following files use this class:
 * <ul>
 * <li>/commons/publishproject.jsp
 * <li>/commons/publishresource.jsp
 * </ul>
 * <p>
 *
 * @author  Andreas Zahner 
 * 
 * @version $Revision: 1.27.4.1 $ 
 * 
 * @since 6.0.0 
 */
public class CmsPublishProject extends CmsReport {

    /** Value for the action: show unlock confirmation. */
    public static final int ACTION_UNLOCK_CONFIRMATION = 200;

    /** Value for the action: unlock confirmed. */
    public static final int ACTION_UNLOCK_CONFIRMED = 210;

    /** The dialog type. */
    public static final String DIALOG_TYPE = "publishproject";

    /** Request parameter value for the action: show unlock confirmation. */
    public static final String DIALOG_UNLOCK_CONFIRMATION = "unlockconfirmation";

    /** Request parameter value for the action: unlock confirmed. */
    public static final String DIALOG_UNLOCK_CONFIRMED = "unlockconfirmed";

    /** Request parameter name for the publishsiblings parameter. */
    public static final String PARAM_PUBLISHSIBLINGS = "publishsiblings";

    /** Request parameter name for the subresources parameter. */
    public static final String PARAM_SUBRESOURCES = "subresources";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsPublishProject.class);

    private String m_paramDirectpublish;
    private String m_paramModifieddate;
    private String m_paramModifieduser;
    private String m_paramProjectid;
    private String m_paramProjectname;
    private String m_paramPublishsiblings;
    private String m_paramResourcename;
    private String m_paramSubresources;

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsPublishProject(CmsJspActionElement jsp) {

        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsPublishProject(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Performs the publish report, will be called by the JSP page.<p>
     * 
     * @throws JspException if problems including sub-elements occur
     */
    public void actionReport() throws JspException {

        // save initialized instance of this class in request attribute for included sub-elements
        getJsp().getRequest().setAttribute(SESSION_WORKPLACE_CLASS, this);
        switch (getAction()) {
            case ACTION_REPORT_END:
                actionCloseDialog();
                break;
            case ACTION_REPORT_UPDATE:
                setParamAction(REPORT_UPDATE);
                getJsp().include(FILE_REPORT_OUTPUT);

                break;
            case ACTION_REPORT_BEGIN:
            case ACTION_CONFIRMED:
            default:
                try {
                    List publishResources = null;
                    boolean directPublish = Boolean.valueOf(getParamDirectpublish()).booleanValue();

                    if (directPublish) {
                        // get the offline resource(s) in direct publish mode
                        publishResources = new ArrayList(getResourceList().size());
                        Iterator i = getResourceList().iterator();
                        while (i.hasNext()) {
                            String resName = (String)i.next();
                            try {
                                CmsResource res = getCms().readResource(resName, CmsResourceFilter.ALL);
                                publishResources.add(res);
                                // check if the resource is locked                   
                                CmsLock lock = getCms().getLock(resName);
                                if (!lock.isNullLock()) {
                                    // resource is locked, so unlock it
                                    getCms().unlockResource(resName);
                                }
                            } catch (CmsException e) {
                                addMultiOperationException(e);
                            }
                        }
                        // for error(s) unlocking resource(s), throw exception
                        checkMultiOperationException(Messages.get(), Messages.ERR_PUBLISH_MULTI_UNLOCK_0);
                    } else {
                        if (getCms().getRequestContext().currentProject().getType() == CmsProject.PROJECT_TYPE_TEMPORARY) {
                            // set the flag that this is a temporary project
                            setParamRefreshWorkplace(CmsStringUtil.TRUE);
                        }
                    }

                    if (showUnlockConfirmation()) {
                        // some subresources are locked, unlock them before publishing                                 
                        if (directPublish) {
                            // unlock subresources of a folder
                            Iterator i = getResourceList().iterator();
                            while (i.hasNext()) {
                                String resName = (String)i.next();
                                try {
                                    CmsResource res = getCms().readResource(resName, CmsResourceFilter.ALL);
                                    if (res.isFolder()) {
                                        String folderName = resName;
                                        if (!folderName.endsWith("/")) {
                                            folderName += "/";
                                        }
                                        getCms().lockResource(folderName);
                                        getCms().unlockResource(folderName);
                                    }
                                } catch (CmsException e) {
                                    addMultiOperationException(e);
                                }
                            }
                            // for error(s) unlocking resource(s), throw exception
                            checkMultiOperationException(Messages.get(), Messages.ERR_PUBLISH_MULTI_UNLOCK_0);
                        } else {
                            // unlock all project resources
                            getCms().unlockProject(Integer.parseInt(getParamProjectid()));
                        }
                    }

                    CmsPublishList publishList = null;
                    if (directPublish) {
                        // create publish list for direct publish
                        boolean publishSubResources = Boolean.valueOf(getParamSubresources()).booleanValue();
                        publishList = getCms().getPublishList(
                            publishResources,
                            Boolean.valueOf(getParamPublishsiblings()).booleanValue(),
                            publishSubResources);
                        getCms().checkPublishPermissions(publishList);
                    }

                    // start the link validation thread before publishing
                    CmsRelationsValidatorThread thread = new CmsRelationsValidatorThread(
                        getCms(),
                        publishList,
                        getSettings());
                    setParamAction(REPORT_BEGIN);
                    setParamThread(thread.getUUID().toString());

                    // set the flag that another thread is following
                    setParamThreadHasNext(CmsStringUtil.TRUE);
                    // set the key name for the continue checkbox
                    setParamReportContinueKey(Messages.GUI_PUBLISH_CONTINUE_BROKEN_LINKS_0);
                    getJsp().include(FILE_REPORT_OUTPUT);

                } catch (Throwable e) {
                    // error while unlocking resources, show error screen
                    includeErrorpage(this, e);
                }
        }
    }

    /**
     * Builds the HTML for the "publish siblings" and "publish sub resources" checkboxes when direct publishing a file.<p>
     * 
     * @return the HTMl for the "publish siblings" and "publish sub resources" checkboxes  
     */
    public String buildCheckSiblings() {

        CmsResource res = null;
        if (!isMultiOperation()) {
            try {
                res = getCms().readResource(getParamResource(), CmsResourceFilter.ALL);
            } catch (CmsException e) {
                // res will be null
                if (LOG.isInfoEnabled()) {
                    LOG.info(e.getLocalizedMessage());
                }
            }
        }
        StringBuffer result = new StringBuffer(128);
        boolean showSiblingCheckBox = false;
        if (isMultiOperation()
            || ((res != null) && res.isFile() && (res.getSiblingCount() > 1))
            || ((res != null) && res.isFolder())) {
            // resource is file and has siblings, so create checkbox

            result.append(dialogSpacer());
            result.append("<input type=\"checkbox\" name=\"");
            result.append(PARAM_PUBLISHSIBLINGS);
            result.append("\" value=\"true\"");
            // set the checkbox state to the default value defined in the opencms.properties
            if (getSettings().getUserSettings().getDialogPublishSiblings()) {
                result.append(" checked=\"checked\"");
            }
            result.append(">&nbsp;");
            result.append(key(Messages.GUI_PUBLISH_ALLSIBLINGS_0));
            showSiblingCheckBox = true;
        }
        if (isOperationOnFolder()) {
            // at least one folder is selected, show "publish subresources" checkbox
            if (showSiblingCheckBox) {
                result.append("<br>");
            }
            result.append("<input type=\"checkbox\" name=\"");
            result.append(PARAM_SUBRESOURCES);
            result.append("\" value=\"true\" checked=\"checked\">&nbsp;");
            if (isMultiOperation()) {
                result.append(key(Messages.GUI_PUBLISH_MULTI_SUBRESOURCES_0));
            } else {
                result.append(key(Messages.GUI_PUBLISH_SUBRESOURCES_0));
            }
        }
        return result.toString();
    }

    /**
     * Returns if a resource will be directly published.<p>
     * 
     * @return <code>"true"</code> if a resource will be directly published
     */
    public String getParamDirectpublish() {

        return m_paramDirectpublish;
    }

    /**
     * Returns the last modification date of the resource which will be published.<p>
     * 
     * @return the last modification date of the resource
     */
    public String getParamModifieddate() {

        return m_paramModifieddate;
    }

    /**
     * Returns the user who modified the resource which will be published.<p>
     * 
     * @return the user who modified the resource
     */
    public String getParamModifieduser() {

        return m_paramModifieduser;
    }

    /**
     * Returns the value of the project id which will be published.<p>
     * 
     * @return the String value of the project id
     */
    public String getParamProjectid() {

        return m_paramProjectid;
    }

    /**
     * Returns the value of the project name which will be published.<p>
     * 
     * @return the String value of the project name
     */
    public String getParamProjectname() {

        return m_paramProjectname;
    }

    /**
     * Returns if siblings of the resource should be published.<p>
     * 
     * @return <code>"true"</code> (String) if siblings of the resource should be published
     */
    public String getParamPublishsiblings() {

        return m_paramPublishsiblings;
    }

    /**
     * Returns the name of the resource which will be published.<p>
     * 
     * @return the name of the resource
     */
    public String getParamResourcename() {

        return m_paramResourcename;
    }

    /**
     * Returns the value of the subresources parameter.<p>
     * 
     * @return the value of the subresources parameter
     */
    public String getParamSubresources() {

        return m_paramSubresources;
    }

    /**
     * Sets if a resource will be directly published.<p>
     * 
     * @param value <code>"true"</code> (String) if a resource will be directly published
     */
    public void setParamDirectpublish(String value) {

        m_paramDirectpublish = value;
    }

    /**
     * Sets the last modification date of the resource which will be published.<p> 
     * 
     * @param value the last modification date of the resource
     */
    public void setParamModifieddate(String value) {

        m_paramModifieddate = value;
    }

    /**
     * Sets the user who modified the resource which will be published.<p> 
     * 
     * @param value the user who modified the resource
     */
    public void setParamModifieduser(String value) {

        m_paramModifieduser = value;
    }

    /**
     * Sets the value of the project id which will be published.<p> 
     * 
     * @param value the String value of the project id
     */
    public void setParamProjectid(String value) {

        m_paramProjectid = value;
    }

    /**
     * Sets the value of the project name which will be published.<p> 
     * 
     * @param value the String value of the project name
     */
    public void setParamProjectname(String value) {

        m_paramProjectname = value;
    }

    /**
     * Sets if siblings of the resource should be published.<p> 
     * 
     * @param value <code>"true"</code> (String) if siblings of the resource should be published
     */
    public void setParamPublishsiblings(String value) {

        m_paramPublishsiblings = value;
    }

    /**
     * Sets the name of the resource which will be published.<p> 
     * 
     * @param value the name of the resource
     */
    public void setParamResourcename(String value) {

        m_paramResourcename = value;
    }

    /**
     * Sets the value of the subresources parameter.<p>
     * 
     * @param paramSubresources the value of the subresources parameter
     */
    public void setParamSubresources(String paramSubresources) {

        m_paramSubresources = paramSubresources;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {

        // fill the parameter values in the get/set methods
        fillParamValues(request);
        // set the dialog type
        setParamDialogtype(DIALOG_TYPE);
        // set the publishing type: publish project or direct publish

        if (CmsStringUtil.isNotEmpty(getParamResource()) || isMultiOperation()) {
            setParamDirectpublish(CmsStringUtil.TRUE);
        }
        // set the action for the JSP switch 
        if (DIALOG_CONFIRMED.equals(getParamAction())) {
            if (showUnlockConfirmation()) {
                // show unlock confirmation dialog
                setAction(ACTION_UNLOCK_CONFIRMATION);
            } else {
                // skip unlock confirmation dialog
                setAction(ACTION_CONFIRMED);
            }
        } else if (DIALOG_UNLOCK_CONFIRMED.equals(getParamAction())) {
            setAction(ACTION_CONFIRMED);
        } else if (REPORT_UPDATE.equals(getParamAction())) {
            setAction(ACTION_REPORT_UPDATE);
        } else if (REPORT_BEGIN.equals(getParamAction())) {
            setAction(ACTION_REPORT_BEGIN);
        } else if (REPORT_END.equals(getParamAction())) {
            if (Boolean.valueOf(getParamThreadHasNext()).booleanValue()) {
                // after the link check start the publish thread
                startPublishThread();

                setParamAction(REPORT_UPDATE);
                setAction(ACTION_REPORT_UPDATE);
            } else {
                // ends the publish thread
                setAction(ACTION_REPORT_END);
            }
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            setAction(ACTION_CANCEL);
        } else {
            setAction(ACTION_DEFAULT);
            // set parameters depending on publishing type
            if (Boolean.valueOf(getParamDirectpublish()).booleanValue()) {
                // check the required permissions to publish the resource directly
                if (!getCms().isManagerOfProject()
                    && !checkResourcePermissions(CmsPermissionSet.ACCESS_DIRECT_PUBLISH, false)) {
                    // no publish permissions for the single resource, set cancel action to close dialog
                    setAction(ACTION_CANCEL);
                    return;
                }
                // determine resource name, last modified date and last modified user of resource
                computePublishResource();
                // add the title for the direct publish dialog 
                setDialogTitle(Messages.GUI_PUBLISH_RESOURCE_1, Messages.GUI_PUBLISH_MULTI_2);
            } else {
                // add the title for the publish project dialog 
                setParamTitle(key(Messages.GUI_PUBLISH_PROJECT_0));
                // determine the project id and name for publishing
                computePublishProject();
                // determine target to close the report
            }
        }
    }

    /**
     * Determine the right project id and name if no request parameter "projectid" is given.<p>
     */
    private void computePublishProject() {

        String projectId = getParamProjectid();
        int id;
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(projectId)) {
            // projectid not found in request parameter,
            id = getCms().getRequestContext().currentProject().getId();
            setParamProjectname(getCms().getRequestContext().currentProject().getName());
            setParamProjectid("" + id);
        } else {
            id = Integer.parseInt(projectId);
            try {
                setParamProjectname(getCms().readProject(id).getName());
            } catch (CmsException e) {
                LOG.error(Messages.get().getBundle().key(Messages.LOG_SET_PROJECT_NAME_FAILED_0), e);
            }
        }
    }

    /**
     * Fills the resource information "resource name", "date last modified" and "last modified by" in parameter values.<p>
     */
    private void computePublishResource() {

        if (!isMultiOperation()) {
            try {
                CmsResource res = getCms().readResource(getParamResource(), CmsResourceFilter.ALL);
                setParamResourcename(res.getName());
                setParamModifieddate(CmsDateUtil.getDateTime(
                    new Date(res.getDateLastModified()),
                    DateFormat.SHORT,
                    getLocale()));
                setParamModifieduser(getCms().readUser(res.getUserLastModified()).getName());
            } catch (CmsException e) {
                LOG.error(Messages.get().getBundle().key(Messages.LOG_COMPUTING_PUBRES_FAILED_0), e);
            }
        }
    }

    /**
     * Checks if the unlock confirmation dialog should be displayed.<p>
     * 
     * @return true if some resources of the project are locked, otherwise false 
     */
    private boolean showUnlockConfirmation() {

        try {
            if (Boolean.valueOf(getParamDirectpublish()).booleanValue()) {
                // direct publish: check sub resources of a folder
                if (isOperationOnFolder()) {
                    int count = 0;
                    Iterator i = getResourceList().iterator();
                    while (i.hasNext()) {
                        String resName = (String)i.next();
                        CmsResource res = getCms().readResource(resName, CmsResourceFilter.ALL);
                        if ((res.getState() != CmsResource.STATE_DELETED) && res.isFolder()) {
                            count += getCms().countLockedResources(resName);
                        }
                    }
                    return (count > 0);
                }
            } else {
                // publish project: check all project resources
                int id = Integer.parseInt(getParamProjectid());
                return (getCms().countLockedResources(id) > 0);
            }
        } catch (CmsException e) {
            LOG.error(Messages.get().getBundle().key(Messages.LOG_DISPLAY_UNLOCK_INF_FAILED_0), e);
        }
        return false;
    }

    /**
     * Starts the publish thread for the project or a resource.<p>
     * 
     * The type of publish thread is determined by the value of the "directpublish" parameter.<p>
     */
    private void startPublishThread() {

        // create a publish thread from the current publish list
        CmsPublishList publishList = getSettings().getPublishList();
        CmsWorkplaceSettings settings = (CmsWorkplaceSettings)getJsp().getRequest().getSession().getAttribute(
            CmsWorkplaceManager.SESSION_WORKPLACE_SETTINGS);
        CmsPublishThread thread = new CmsPublishThread(getCms(), publishList, settings);

        // set the new thread id and flag that no thread is following
        setParamThread(thread.getUUID().toString());
        setParamThreadHasNext(CmsStringUtil.FALSE);

        // start the publish thread
        thread.start();
    }

}
