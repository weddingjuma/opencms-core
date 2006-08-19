/*
 * File   : $Source$
 * Date   : $Date$
 * Version: $Revision$
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

import org.opencms.file.CmsUser;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsRuntimeException;
import org.opencms.security.CmsPrincipal;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.list.A_CmsListDefaultJsAction;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDefaultAction;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.tools.CmsToolMacroResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * User selection dialog.<p>
 * 
 * @author Michael Moossen  
 * 
 * @version $Revision$ 
 * 
 * @since 6.0.0 
 */
public class CmsUserSelectionList extends A_CmsListDialog {

    /** list action id constant. */
    public static final String LIST_ACTION_ICON = "ai";

    /** list action id constant. */
    public static final String LIST_ACTION_SELECT = "js";

    /** list column id constant. */
    public static final String LIST_COLUMN_FULLNAME = "cf";

    /** list column id constant. */
    public static final String LIST_COLUMN_ICON = "ci";

    /** list column id constant. */
    public static final String LIST_COLUMN_LOGIN = "cn";

    /** list id constant. */
    public static final String LIST_ID = "lus";

    /** Stores the value of the request parameter for the group name. */
    private String m_paramGroup;

    /** Stores the value of the request parameter for the user type. */
    private String m_paramUsertype;

    /** Stores the value of the request parameter for the flags. */
    private String m_paramFlags;

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsUserSelectionList(CmsJspActionElement jsp) {

        super(
            jsp,
            LIST_ID,
            Messages.get().container(Messages.GUI_USERSELECTION_LIST_NAME_0),
            LIST_COLUMN_LOGIN,
            CmsListOrderEnum.ORDER_ASCENDING,
            LIST_COLUMN_LOGIN);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsUserSelectionList(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.tools.CmsToolDialog#dialogTitle()
     */
    public String dialogTitle() {

        // build title
        StringBuffer html = new StringBuffer(512);
        html.append("<div class='screenTitle'>\n");
        html.append("\t<table width='100%' cellspacing='0'>\n");
        html.append("\t\t<tr>\n");
        html.append("\t\t\t<td>\n");
        String param = "";
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(getParamGroup())) {
            param = Messages.get().getBundle(getLocale()).key(Messages.GUI_USERSELECTION_GROUP_BLOCK_1, getParamGroup());
        } else if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(getParamUsertype())) {
            if (Integer.parseInt(getParamUsertype()) == CmsUser.USER_TYPE_WEBUSER) {
                param = Messages.get().getBundle(getLocale()).key(Messages.GUI_USERSELECTION_TYPE_WEB_0);
            } else {
                param = Messages.get().getBundle(getLocale()).key(Messages.GUI_USERSELECTION_TYPE_SYSTEM_0);
            }
        }
        html.append(key(Messages.GUI_USERSELECTION_INTRO_TITLE_1, new Object[] {param}));
        html.append("\n\t\t\t</td>");
        html.append("\t\t</tr>\n");
        html.append("\t</table>\n");
        html.append("</div>\n");
        return CmsToolMacroResolver.resolveMacros(html.toString(), this);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    public void executeListMultiActions() throws CmsRuntimeException {

        throwListUnsupportedActionException();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() throws CmsRuntimeException {

        throwListUnsupportedActionException();
    }

    /**
     * Returns the Group name parameter value.<p>
     *
     * @return the Group name parameter value
     */
    public String getParamGroup() {

        return m_paramGroup;
    }

    /**
     * Returns the user type parameter value.<p>
     *
     * @return the user type parameter value
     */
    public String getParamUsertype() {

        return m_paramUsertype;
    }

    /**
     * Returns the flags parameter value.<p>
     *
     * @return the flags parameter value
     */
    public String getParamFlags() {

        return m_paramFlags;
    }

    /**
     * Sets the group name parameter value.<p>
     *
     * @param groupName the group name parameter value to set
     */
    public void setParamGroup(String groupName) {

        m_paramGroup = groupName;
    }

    /**
     * Sets the user type parameter value.<p>
     *
     * @param userType the user type parameter value to set
     */
    public void setParamUsertype(String userType) {

        m_paramUsertype = userType;
    }

    /**
     * Sets the flags parameter value.<p>
     *
     * @param flags the flags parameter value to set
     */
    public void setParamFlags(String flags) {

        m_paramFlags = flags;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {

        // noop
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#getListItems()
     */
    protected List getListItems() throws CmsException {

        List ret = new ArrayList();

        // get content        
        List users = getUsers();
        Iterator itUsers = users.iterator();
        while (itUsers.hasNext()) {
            CmsUser user = (CmsUser)itUsers.next();
            CmsListItem item = getList().newItem(user.getId().toString());
            item.set(LIST_COLUMN_LOGIN, user.getName());
            item.set(LIST_COLUMN_FULLNAME, user.getFullName());
            ret.add(item);
        }

        return ret;
    }

    /**
     * Returns the list of users for selection.<p>
     * 
     * @return a list of users
     * 
     * @throws CmsException if womething goes wrong
     */
    protected List getUsers() throws CmsException {

        List ret = new ArrayList();
        if (getParamGroup() != null) {
            ret.addAll(getCms().getUsersOfGroup(getParamGroup()));
        } else if (getParamUsertype() == null) {
            ret.addAll(getCms().getUsers());
        } else {
            ret.addAll(getCms().getUsers(Integer.parseInt(getParamUsertype())));
        }
        if (getParamFlags() != null) {
            int flags = Integer.parseInt(getParamFlags());
            return CmsPrincipal.filterFlag(ret, flags);
        }
        return ret;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setColumns(CmsListMetadata metadata) {

        // create column for icon display
        CmsListColumnDefinition iconCol = new CmsListColumnDefinition(LIST_COLUMN_ICON);
        iconCol.setName(Messages.get().container(Messages.GUI_USERSELECTION_LIST_COLS_ICON_0));
        iconCol.setHelpText(Messages.get().container(Messages.GUI_USERSELECTION_LIST_COLS_ICON_HELP_0));
        iconCol.setWidth("20");
        iconCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        iconCol.setSorteable(false);
        // set icon action
        CmsListDirectAction iconAction = new CmsListDirectAction(LIST_ACTION_ICON);
        iconAction.setName(Messages.get().container(Messages.GUI_USERSELECTION_LIST_ICON_NAME_0));
        iconAction.setHelpText(Messages.get().container(Messages.GUI_USERSELECTION_LIST_ICON_HELP_0));
        iconAction.setIconPath("buttons/user.png");
        iconAction.setEnabled(false);
        iconCol.addDirectAction(iconAction);
        // add it to the list definition
        metadata.addColumn(iconCol);

        // create column for login
        CmsListColumnDefinition loginCol = new CmsListColumnDefinition(LIST_COLUMN_LOGIN);
        loginCol.setName(Messages.get().container(Messages.GUI_USERSELECTION_LIST_COLS_LOGIN_0));
        loginCol.setWidth("35%");
        CmsListDefaultAction selectAction = new A_CmsListDefaultJsAction(LIST_ACTION_SELECT) {

            /**
             * @see org.opencms.workplace.list.A_CmsListDirectJsAction#jsCode()
             */
            public String jsCode() {

                return "window.opener.setUserFormValue('"
                    + getItem().get(LIST_COLUMN_LOGIN)
                    + "'); window.opener.focus(); window.close();";
            }
        };
        selectAction.setName(Messages.get().container(Messages.GUI_USERSELECTION_LIST_ACTION_SELECT_NAME_0));
        selectAction.setHelpText(Messages.get().container(Messages.GUI_USERSELECTION_LIST_ACTION_SELECT_HELP_0));
        loginCol.addDefaultAction(selectAction);
        // add it to the list definition
        metadata.addColumn(loginCol);

        // create column for fullname
        CmsListColumnDefinition fullnameCol = new CmsListColumnDefinition(LIST_COLUMN_FULLNAME);
        fullnameCol.setName(Messages.get().container(Messages.GUI_USERSELECTION_LIST_COLS_FULLNAME_0));
        fullnameCol.setWidth("65%");
        fullnameCol.setTextWrapping(true);
        // add it to the list definition
        metadata.addColumn(fullnameCol);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {

        // no-op        
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setMultiActions(CmsListMetadata metadata) {

        // no-op        
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#validateParamaters()
     */
    protected void validateParamaters() throws Exception {

        try {
            getCms().readGroup(getParamGroup()).getName();
        } catch (Exception e) {
            setParamGroup(null);
        }
        try {
            Integer.valueOf(getParamUsertype());
        } catch (Throwable e) {
            setParamUsertype(null);
        }
        try {
            Integer.valueOf(getParamFlags());
        } catch (Throwable e) {
            setParamFlags(null);
        }
    }
}