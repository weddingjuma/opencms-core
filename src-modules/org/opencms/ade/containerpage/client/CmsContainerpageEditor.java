/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/containerpage/client/Attic/CmsContainerpageEditor.java,v $
 * Date   : $Date: 2011/04/07 15:07:35 $
 * Version: $Revision: 1.38 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
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
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ade.containerpage.client;

import org.opencms.ade.containerpage.client.ui.CmsAddToFavoritesButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarClipboardMenu;
import org.opencms.ade.containerpage.client.ui.CmsToolbarContextButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarEditButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarGalleryMenu;
import org.opencms.ade.containerpage.client.ui.CmsToolbarMoveButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarPropertiesButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarPublishButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarRemoveButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarResetButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarSaveButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarSelectionButton;
import org.opencms.ade.containerpage.client.ui.CmsToolbarSitemapButton;
import org.opencms.ade.containerpage.client.ui.I_CmsToolbarButton;
import org.opencms.ade.containerpage.client.ui.css.I_CmsLayoutBundle;
import org.opencms.gwt.client.A_CmsEntryPoint;
import org.opencms.gwt.client.dnd.CmsDNDHandler;
import org.opencms.gwt.client.ui.CmsPushButton;
import org.opencms.gwt.client.ui.CmsToolbar;
import org.opencms.gwt.client.ui.I_CmsButton.ButtonStyle;
import org.opencms.gwt.client.ui.I_CmsButton.Size;
import org.opencms.gwt.client.ui.css.I_CmsImageBundle;
import org.opencms.gwt.client.util.CmsFadeAnimation;
import org.opencms.gwt.client.util.CmsStyleVariable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The container page editor.<p>
 * 
 * @author Tobias Herrmann
 * 
 * @version $Revision: 1.38 $
 * 
 * @since 8.0.0
 */
public class CmsContainerpageEditor extends A_CmsEntryPoint {

    /** Add menu. */
    private CmsToolbarGalleryMenu m_add;

    /** Margin-top added to the document body element when the tool-bar is shown. */
    //    private int m_bodyMarginTop;

    /** Clip-board menu. */
    private CmsToolbarClipboardMenu m_clipboard;

    /** The Button for the context menu. */
    private CmsToolbarContextButton m_context;

    /** Edit button. */
    private CmsToolbarEditButton m_edit;

    /** Add to favorites button. */
    private CmsAddToFavoritesButton m_addToFavorites;

    /** Move button. */
    private CmsToolbarMoveButton m_move;

    /** Properties button. */
    private CmsToolbarPropertiesButton m_properties;

    /** Publish button. */
    private CmsToolbarPublishButton m_publish;

    /** Remove button. */
    private CmsToolbarRemoveButton m_remove;

    /** Reset button. */
    private CmsToolbarResetButton m_reset;

    /** Save button. */
    private CmsToolbarSaveButton m_save;

    /** Selection button. */
    private CmsToolbarSelectionButton m_selection;

    /** Sitemap button. */
    private CmsToolbarSitemapButton m_sitemap;

    /** The tool-bar. */
    private CmsToolbar m_toolbar;

    /** Style to toggle toolbar visibility. */
    protected CmsStyleVariable m_toolbarVisibility;

    /** The Z index manager. */
    private static final I_CmsContainerZIndexManager Z_INDEX_MANAGER = GWT.create(I_CmsContainerZIndexManager.class);

    /** 
     * Returns the Z index manager for the container page editor.<p> 
     * 
     * @return the Z index manager
     **/
    public static I_CmsContainerZIndexManager getZIndexManager() {

        return Z_INDEX_MANAGER;
    }

    /**
     * Returns the add gallery menu.<p>
     *
     * @return the add gallery menu
     */
    public CmsToolbarGalleryMenu getAdd() {

        return m_add;
    }

    /**
     * Returns the clip-board menu.<p>
     *
     * @return the clip-board menu
     */
    public CmsToolbarClipboardMenu getClipboard() {

        return m_clipboard;
    }

    /**
     * Returns the context menu.<p>
     *
     * @return the context menu
     */
    public CmsToolbarContextButton getContext() {

        return m_context;
    }

    /**
     * Returns the publish.<p>
     *
     * @return the publish
     */
    public CmsToolbarPublishButton getPublish() {

        return m_publish;
    }

    /**
     * Returns the reset button.<p>
     *
     * @return the reset button
     */
    public CmsToolbarResetButton getReset() {

        return m_reset;
    }

    /**
     * Returns the save button.<p>
     *
     * @return the save button
     */
    public CmsToolbarSaveButton getSave() {

        return m_save;
    }

    /**
     * Returns the selection button.<p>
     *
     * @return the selection button
     */
    public CmsToolbarSelectionButton getSelection() {

        return m_selection;
    }

    /**
     * Returns the tool-bar widget.<p>
     * 
     * @return the tool-bar widget
     */
    public CmsToolbar getToolbar() {

        return m_toolbar;
    }

    /**
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad() {

        super.onModuleLoad();

        I_CmsLayoutBundle.INSTANCE.containerpageCss().ensureInjected();
        I_CmsLayoutBundle.INSTANCE.dragdropCss().ensureInjected();
        I_CmsLayoutBundle.INSTANCE.groupcontainerCss().ensureInjected();

        CmsContainerpageController controller = new CmsContainerpageController();
        final CmsContainerpageHandler containerpageHandler = new CmsContainerpageHandler(controller, this);
        CmsContentEditorHandler contentEditorHandler = new CmsContentEditorHandler(containerpageHandler);
        CmsContainerpageDNDController dndController = new CmsContainerpageDNDController(controller);
        CmsDNDHandler dndHandler = new CmsDNDHandler(dndController);

        ClickHandler clickHandler = new ClickHandler() {

            /**
             * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
             */
            public void onClick(ClickEvent event) {

                I_CmsToolbarButton source = (I_CmsToolbarButton)event.getSource();
                source.onToolbarClick();
            }
        };

        //        m_bodyMarginTop = CmsDomUtil.getCurrentStyleInt(Document.get().getBody(), Style.marginTop);
        m_toolbar = new CmsToolbar();
        RootPanel root = RootPanel.get();
        root.add(m_toolbar);
        CmsPushButton toggleToolbarButton = new CmsPushButton();
        toggleToolbarButton.setButtonStyle(ButtonStyle.TEXT, null);
        toggleToolbarButton.setSize(Size.small);
        toggleToolbarButton.setImageClass(I_CmsImageBundle.INSTANCE.style().opencmsSymbol());
        toggleToolbarButton.removeStyleName(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.generalCss().buttonCornerAll());
        toggleToolbarButton.addStyleName(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.generalCss().cornerAll());
        root.add(toggleToolbarButton);
        toggleToolbarButton.addClickHandler(new ClickHandler() {

            /**
             * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
             */
            public void onClick(ClickEvent event) {

                containerpageHandler.toggleToolbar();
            }

        });
        toggleToolbarButton.addStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().toolbarToggle());

        m_save = new CmsToolbarSaveButton(containerpageHandler);
        m_save.addClickHandler(clickHandler);
        m_toolbar.addLeft(m_save);

        m_publish = new CmsToolbarPublishButton(containerpageHandler);
        m_publish.addClickHandler(clickHandler);
        m_toolbar.addLeft(m_publish);

        m_move = new CmsToolbarMoveButton(containerpageHandler, dndHandler);

        m_edit = new CmsToolbarEditButton(containerpageHandler);

        m_addToFavorites = new CmsAddToFavoritesButton(containerpageHandler);

        m_remove = new CmsToolbarRemoveButton(containerpageHandler);

        m_properties = new CmsToolbarPropertiesButton(containerpageHandler);

        m_clipboard = new CmsToolbarClipboardMenu(containerpageHandler);
        m_clipboard.addClickHandler(clickHandler);
        m_toolbar.addLeft(m_clipboard);

        m_add = new CmsToolbarGalleryMenu(containerpageHandler, dndHandler);
        m_add.addClickHandler(clickHandler);
        m_toolbar.addLeft(m_add);

        m_selection = new CmsToolbarSelectionButton(containerpageHandler);
        m_selection.addClickHandler(clickHandler);
        m_toolbar.addLeft(m_selection);

        m_context = new CmsToolbarContextButton(containerpageHandler);
        m_context.addClickHandler(clickHandler);
        m_toolbar.addRight(m_context);

        m_sitemap = new CmsToolbarSitemapButton(containerpageHandler);
        m_sitemap.addClickHandler(clickHandler);
        m_toolbar.addRight(m_sitemap);

        if (controller.getData().getSitemapUri().equals("")) {
            m_sitemap.setEnabled(false);
        }

        m_reset = new CmsToolbarResetButton(containerpageHandler);
        m_reset.addClickHandler(clickHandler);
        m_toolbar.addRight(m_reset);

        containerpageHandler.enableSaveReset(false);
        m_toolbarVisibility = new CmsStyleVariable(root);
        m_toolbarVisibility.setValue(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.toolbarCss().toolbarHide());
        if (controller.getData().isToolbarVisible()) {
            showToolbar(true);
            containerpageHandler.activateSelection();
        }

        CmsContainerpageUtil containerpageUtil = new CmsContainerpageUtil(
            controller,
            m_selection,
            m_move,
            m_edit,
            m_remove,
            m_properties,
            m_addToFavorites);
        controller.init(containerpageHandler, dndHandler, contentEditorHandler, containerpageUtil);

    }

    /**
     * Shows the tool-bar.<p>
     * 
     * @param show if <code>true</code> the tool-bar will be shown
     */
    public void showToolbar(boolean show) {

        if (show) {
            m_toolbarVisibility.setValue(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.toolbarCss().toolbarShow());
            CmsFadeAnimation.fadeIn(m_toolbar.getElement(), null, 300);
            // body.getStyle().setMarginTop(m_bodyMarginTop + 36, Unit.PX);
        } else {
            CmsFadeAnimation.fadeOut(m_toolbar.getElement(), new Command() {

                public void execute() {

                    m_toolbarVisibility.setValue(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.toolbarCss().toolbarHide());
                }
            },
                300);

            // body.getStyle().setMarginTop(m_bodyMarginTop, Unit.PX);
        }
    }

    /**
     * Returns if the tool-bar is visible.<p>
     * 
     * @return <code>true</code> if the tool-bar is visible
     */
    public boolean isToolbarVisible() {

        return !org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.toolbarCss().toolbarHide().equals(
            m_toolbarVisibility.getValue());
    }
}
