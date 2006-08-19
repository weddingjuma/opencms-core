/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/workplace/list/A_CmsListResourceCollector.java,v $
 * Date   : $Date: 2006/08/19 13:40:40 $
 * Version: $Revision: 1.1.2.7 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2005 Alkacon Software GmbH (http://www.alkacon.com)
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

package org.opencms.workplace.list;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsResourceUtil;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;
import org.opencms.workplace.CmsWorkplace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

/**
 * Collector to provide {@link CmsResource} objects for a explorer List.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.1.2.7 $ 
 * 
 * @since 6.1.0 
 */
public abstract class A_CmsListResourceCollector implements I_CmsListResourceCollector {

    /** Parameter name constant. */
    public static final String PARAM_FILTER = "filter";

    /** Parameter name constant. */
    public static final String PARAM_ORDER = "order";

    /** Parameter name constant. */
    public static final String PARAM_PAGE = "page";

    /** Parameter name constant. */
    public static final String PARAM_SORTBY = "sortby";

    /** Key-Value delimiter constant. */
    public static final String SEP_KEYVAL = ":";

    /** Parameter delimiter constant. */
    public static final String SEP_PARAM = "|";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(A_CmsListResourceCollector.class);

    /** The colelctor parameter. */
    protected String m_collectorParameter;

    /** List item cache. */
    protected Map m_liCache = new HashMap();

    /** Resource cache. */
    protected Map m_resCache = new HashMap();

    /** Cache for resource list result. */
    protected List m_resources = null;

    /** The workplace object where the collector is used from. */
    private A_CmsListExplorerDialog m_wp;

    /**
     * Constructor, creates a new list collector.<p>
     * 
     * @param wp the workplace object where the collector is used from
     */
    protected A_CmsListResourceCollector(A_CmsListExplorerDialog wp) {

        m_wp = wp;
        CmsListState state = (wp != null ? wp.getListStateForCollector() : new CmsListState());
        if (state.getPage() < 1) {
            state.setPage(1);
        }
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(state.getColumn())) {
            state.setColumn(A_CmsListExplorerDialog.LIST_COLUMN_NAME);
        }
        if (state.getOrder() == null) {
            state.setOrder(CmsListOrderEnum.ORDER_ASCENDING);
        }
        if (state.getFilter() == null) {
            state.setFilter("");
        }
        m_collectorParameter = PARAM_PAGE + SEP_KEYVAL + state.getPage();
        m_collectorParameter += SEP_PARAM + PARAM_SORTBY + SEP_KEYVAL + state.getColumn();
        m_collectorParameter += SEP_PARAM + PARAM_ORDER + SEP_KEYVAL + state.getOrder();
        m_collectorParameter += SEP_PARAM + PARAM_FILTER + SEP_KEYVAL + state.getFilter();
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {

        return 0;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCreateLink(org.opencms.file.CmsObject)
     */
    public String getCreateLink(CmsObject cms) {

        return null;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCreateLink(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public String getCreateLink(CmsObject cms, String collectorName, String param) {

        return null;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCreateParam(org.opencms.file.CmsObject)
     */
    public String getCreateParam(CmsObject cms) {

        return null;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCreateParam(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public String getCreateParam(CmsObject cms, String collectorName, String param) {

        return null;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getDefaultCollectorName()
     */
    public String getDefaultCollectorName() {

        return (String)getCollectorNames().get(0);
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getDefaultCollectorParam()
     */
    public String getDefaultCollectorParam() {

        return m_collectorParameter;
    }

    /**
     * Returns a list of list items from a list of resources.<p>
     * 
     * @param parameter the collector parameter or <code>null</code> for default.<p>
     * 
     * @return a list of {@link CmsListItem} objects
     * 
     * @throws CmsException if something goes wrong
     */
    public synchronized List getListItems(String parameter) throws CmsException {

        if (parameter == null) {
            parameter = m_collectorParameter;
        }
        Map params = CmsStringUtil.splitAsMap(parameter, SEP_PARAM, SEP_KEYVAL);
        CmsListState state = getState(params);
        List resources = getInternalResources(getWp().getCms(), params);
        List ret = new ArrayList();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(
                Messages.LOG_COLLECTOR_PROCESS_ITEMS_START_1,
                new Integer(resources.size())));
        }
        CmsResourceUtil resUtil = getWp().getResourceUtil();
        getWp().applyColumnVisibilities();
        CmsHtmlList list = getWp().getList();
        // get content
        Iterator itRes = resources.iterator();
        while (itRes.hasNext()) {
            Object obj = itRes.next();
            if (!(obj instanceof CmsResource)) {
                ret.add(getDummyListItem(list));
                continue;
            }
            CmsResource resource = (CmsResource)obj;
            if (!resource.getRootPath().startsWith(getWp().getJsp().getRequestContext().getSiteRoot())
                && !resource.getRootPath().startsWith(CmsWorkplace.VFS_PATH_SYSTEM)) {
                continue;
            }
            CmsListItem item = (CmsListItem)m_liCache.get(resource.getStructureId().toString());
            if (item == null) {
                resUtil.setResource(resource);
                item = list.newItem(resource.getStructureId().toString());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_NAME, getWp().getCms().getSitePath(resource));
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_TITLE, resUtil.getTitle());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_TYPE, resUtil.getResourceTypeName());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_SIZE, resUtil.getSizeString());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_PERMISSIONS, resUtil.getPermissions());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATELASTMOD, new Date(resource.getDateLastModified()));
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_USERLASTMOD, resUtil.getUserLastModified());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATECREATE, new Date(resource.getDateCreated()));
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_USERCREATE, resUtil.getUserCreated());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATEREL, new Date(resource.getDateReleased()));
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATEEXP, new Date(resource.getDateExpired()));
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_STATE, resUtil.getStateName());
                item.set(A_CmsListExplorerDialog.LIST_COLUMN_LOCKEDBY, resUtil.getLockedByName());
                setAdditionalColumns(item, resUtil);
                m_liCache.put(resource.getStructureId().toString(), item);
            }
            ret.add(item);
        }
        CmsListMetadata metadata = list.getMetadata();
        if (metadata != null) {
            if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(state.getFilter())) {
                // filter
                ret = metadata.getSearchAction().filter(ret, state.getFilter());
            }
            if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(state.getColumn())) {
                if ((metadata.getColumnDefinition(state.getColumn()) != null)
                    && metadata.getColumnDefinition(state.getColumn()).isSorteable()) {
                    // sort
                    I_CmsListItemComparator c = metadata.getColumnDefinition(state.getColumn()).getListItemComparator();
                    Collections.sort(ret, c.getComparator(state.getColumn(), getWp().getLocale()));
                    if (state.getOrder().equals(CmsListOrderEnum.ORDER_DESCENDING)) {
                        Collections.reverse(ret);
                    }
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(
                Messages.LOG_COLLECTOR_PROCESS_ITEMS_END_1,
                new Integer(ret.size())));
        }
        return ret;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getOrder()
     */
    public int getOrder() {

        return 0;
    }

    /**
     * Returns the resource for the given item.<p>
     * 
     * @param cms the cms object
     * @param item the item
     * 
     * @return the resource
     */
    public CmsResource getResource(CmsObject cms, CmsListItem item) {

        CmsResource res = (CmsResource)m_resCache.get(item.getId());
        if (res == null) {
            try {
                res = cms.readResource(
                    (String)item.get(A_CmsListExplorerDialog.LIST_COLUMN_NAME),
                    CmsResourceFilter.ALL);
                m_resCache.put(item.getId(), res);
            } catch (CmsException e) {
                // ignore
            }
        }
        return res;
    }

    /**
     * Returns all, unsorted and unfiltered, resources.<p>
     * 
     * Be sure to cache the resources.<p>
     * 
     * @param cms the cms object
     * @param params the parameter map
     * 
     * @return a list of {@link CmsResource} objects
     * 
     * @throws CmsException if something goes wrong
     */
    public abstract List getResources(CmsObject cms, Map params) throws CmsException;

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getResults(org.opencms.file.CmsObject)
     */
    public List getResults(CmsObject cms) throws CmsException {

        return getResults(cms, getDefaultCollectorName(), m_collectorParameter);
    }

    /**
     * The parameter must follow the syntax "page:nr" where nr is the number of the page to be displayed.
     * 
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getResults(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public List getResults(CmsObject cms, String collectorName, String parameter) throws CmsException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_COLLECTOR_GET_RESULTS_START_0));
        }
        List resources = new ArrayList();
        if (getWp().getList() != null) {
            Iterator itItems = getListItems(parameter).iterator();
            while (itItems.hasNext()) {
                CmsListItem item = (CmsListItem)itItems.next();
                resources.add(getResource(cms, item));
            }
        } else {
            if (parameter == null) {
                parameter = m_collectorParameter;
            }
            Map params = CmsStringUtil.splitAsMap(parameter, SEP_PARAM, SEP_KEYVAL);
            resources = getInternalResources(cms, params);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(
                Messages.LOG_COLLECTOR_GET_RESULTS_END_1,
                new Integer(resources.size())));
        }
        return resources;
    }

    /**
     * Returns the workplace object.<p>
     *
     * @return the workplace object
     */
    public A_CmsListExplorerDialog getWp() {

        return m_wp;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#setDefaultCollectorName(java.lang.String)
     */
    public void setDefaultCollectorName(String collectorName) {

        // ignore
    }

    /**
     * The parameter must follow the syntax "mode|projectId" where mode is either "new", "changed", "deleted" 
     * or "modified" and projectId is the id of the project to be displayed.
     * 
     * @see org.opencms.file.collectors.I_CmsResourceCollector#setDefaultCollectorParam(java.lang.String)
     */
    public void setDefaultCollectorParam(String param) {

        m_collectorParameter = param;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#setOrder(int)
     */
    public void setOrder(int order) {

        // ignore
    }

    /**
     * Sets the current display page.<p>
     * 
     * @param page the new display page
     */
    public void setPage(int page) {

        if (m_collectorParameter != null) {
            int pos = m_collectorParameter.indexOf(PARAM_PAGE);
            if (pos >= 0) {
                String params = "";
                int endPos = m_collectorParameter.indexOf(SEP_PARAM, pos);
                if (pos > 0) {
                    pos -= SEP_PARAM.length(); // remove also the SEP_PARAM 
                    params += m_collectorParameter.substring(0, pos);
                }
                if (endPos >= 0) {
                    if (pos == 0) {
                        endPos += SEP_PARAM.length(); // remove also the SEP_PARAM
                    }
                    params += m_collectorParameter.substring(endPos, m_collectorParameter.length());
                }
                m_collectorParameter = params;
            }
        }
        if (m_collectorParameter.length() > 0) {
            m_collectorParameter += SEP_PARAM;
        }
        m_collectorParameter += PARAM_PAGE + SEP_KEYVAL + page;
        m_resources = null;
    }

    /**
     * Returns a dummy list item.<p>
     * 
     * @param list the list object to create the entry for
     * 
     * @return a dummy list item
     */
    protected CmsListItem getDummyListItem(CmsHtmlList list) {

        CmsListItem item = list.newItem(CmsUUID.getNullUUID().toString());
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_NAME, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_TITLE, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_TYPE, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_SIZE, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_PERMISSIONS, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATELASTMOD, new Date());
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_USERLASTMOD, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATECREATE, new Date());
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_USERCREATE, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATEREL, new Date());
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_DATEEXP, new Date());
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_STATE, "");
        item.set(A_CmsListExplorerDialog.LIST_COLUMN_LOCKEDBY, "");
        return item;
    }

    /**
     * Wrapper method for caching the result of {@link #getResources(CmsObject, Map)}.<p>
     * 
     * @param cms the cms object
     * @param params the parameter map
     * 
     * @return the result of {@link #getResources(CmsObject, Map)}
     * 
     * @throws CmsException if something goes wrong 
     */
    protected List getInternalResources(CmsObject cms, Map params) throws CmsException {

        synchronized (this) {
            if (m_resources == null) {
                m_resources = getResources(cms, params);
                Iterator it = m_resources.iterator();
                while (it.hasNext()) {
                    CmsResource resource = (CmsResource)it.next();
                    m_resCache.put(resource.getStructureId().toString(), resource);
                }
            }
        }
        return m_resources;
    }

    /**
     * Returns the state of the parameter map.<p>
     * 
     * @param params the parameter map
     * 
     * @return the state of the list from the parameter map
     */
    protected CmsListState getState(Map params) {

        CmsListState state = new CmsListState();
        try {
            state.setPage(Integer.parseInt((String)params.get(PARAM_PAGE)));
        } catch (Throwable e) {
            // ignore
        }
        try {
            state.setOrder(CmsListOrderEnum.valueOf((String)params.get(PARAM_ORDER)));
        } catch (Throwable e) {
            // ignore
        }
        try {
            state.setFilter((String)params.get(PARAM_FILTER));
        } catch (Throwable e) {
            // ignore
        }
        try {
            state.setColumn((String)params.get(PARAM_SORTBY));
        } catch (Throwable e) {
            // ignore
        }
        return state;
    }

    /**
     * Set additional column entries for a resource.<p>
     * 
     * Overwrite this method to set additional column entries.<p>
     * 
     * @param item the current list item 
     * @param resUtil the resource util object for getting the info from
     */
    protected abstract void setAdditionalColumns(CmsListItem item, CmsResourceUtil resUtil);
}
