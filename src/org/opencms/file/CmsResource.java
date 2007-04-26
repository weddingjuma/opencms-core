/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/file/CmsResource.java,v $
 * Date   : $Date: 2007/04/26 14:31:00 $
 * Version: $Revision: 1.45.4.13 $
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

package org.opencms.file;

import org.opencms.db.CmsResourceState;
import org.opencms.main.CmsIllegalArgumentException;
import org.opencms.util.A_CmsModeIntEnumeration;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Base class for all OpenCms VFS resources like <code>{@link CmsFile}</code> or <code>{@link CmsFolder}</code>.<p>
 *
 * @author Alexander Kandzior 
 * @author Michael Emmerich 
 * @author Thomas Weckert  
 * 
 * @version $Revision: 1.45.4.13 $
 * 
 * @since 6.0.0 
 */
public class CmsResource extends Object implements Cloneable, Serializable, Comparable {

    /**
     *  Enumeration class for resource copy modes.<p>
     */
    public static final class CmsResourceCopyMode extends A_CmsModeIntEnumeration {

        /** Copy mode for copy resources as new resource. */
        protected static final CmsResourceCopyMode MODE_COPY_AS_NEW = new CmsResourceCopyMode(1);

        /** Copy mode for copy resources as sibling. */
        protected static final CmsResourceCopyMode MODE_COPY_AS_SIBLING = new CmsResourceCopyMode(2);

        /** Copy mode to preserve siblings during copy. */
        protected static final CmsResourceCopyMode MODE_COPY_PRESERVE_SIBLING = new CmsResourceCopyMode(3);

        /** Serializable version id. */
        private static final long serialVersionUID = 9081630878178799137L;

        /**
         * Private constructor.<p>
         * 
         * @param mode the copy mode integer representation
         */
        private CmsResourceCopyMode(int mode) {

            super(mode);
        }

        /**
         * Returns the copy mode object from the old copy mode integer.<p>
         * 
         * @param mode the old copy mode integer
         * 
         * @return the copy mode object
         */
        public static CmsResourceCopyMode valueOf(int mode) {

            switch (mode) {
                case 1:
                    return CmsResourceCopyMode.MODE_COPY_AS_NEW;
                case 2:
                    return CmsResourceCopyMode.MODE_COPY_AS_SIBLING;
                case 3:
                default:
                    return CmsResourceCopyMode.MODE_COPY_PRESERVE_SIBLING;
            }
        }
    }

    /**
     *  Enumeration class for resource delete modes.<p>
     */
    public static final class CmsResourceDeleteMode extends A_CmsModeIntEnumeration {

        /** Signals that siblings of this resource should not be deleted. */
        protected static final CmsResourceDeleteMode MODE_DELETE_PRESERVE_SIBLINGS = new CmsResourceDeleteMode(1);

        /** Signals that siblings of this resource should be deleted. */
        protected static final CmsResourceDeleteMode MODE_DELETE_REMOVE_SIBLINGS = new CmsResourceDeleteMode(2);

        /** Serializable version id. */
        private static final long serialVersionUID = 2010402524576925865L;

        /**
         * Private constructor.<p>
         * 
         * @param mode the delete mode integer representation
         */
        private CmsResourceDeleteMode(int mode) {

            super(mode);
        }

        /**
         * Returns the delete mode object from the old delete mode integer.<p>
         * 
         * @param mode the old delete mode integer
         * 
         * @return the delete mode object
         */
        public static CmsResourceDeleteMode valueOf(int mode) {

            switch (mode) {
                case 1:
                    return CmsResourceDeleteMode.MODE_DELETE_PRESERVE_SIBLINGS;
                case 2:
                default:
                    return CmsResourceDeleteMode.MODE_DELETE_REMOVE_SIBLINGS;
            }
        }
    }

    /**
     *  Enumeration class for resource undo changes modes.<p>
     */
    public static final class CmsResourceUndoMode extends A_CmsModeIntEnumeration {

        /** Indicates that the undo method will only undo content changes. */
        public static final CmsResourceUndoMode MODE_UNDO_CONTENT = new CmsResourceUndoMode(1);

        /** Indicates that the undo method will only recursive undo content changes. */
        public static final CmsResourceUndoMode MODE_UNDO_CONTENT_RECURSIVE = new CmsResourceUndoMode(2);

        /** Indicates that the undo method will undo move operations and content changes. */
        public static final CmsResourceUndoMode MODE_UNDO_MOVE_CONTENT = new CmsResourceUndoMode(3);

        /** Indicates that the undo method will undo move operations and recursive content changes. */
        public static final CmsResourceUndoMode MODE_UNDO_MOVE_CONTENT_RECURSIVE = new CmsResourceUndoMode(4);

        /** Serializable version id. */
        private static final long serialVersionUID = 3521620626485212068L;

        /**
         * private constructor.<p>
         * 
         * @param mode the undo changes mode integer representation
         */
        private CmsResourceUndoMode(int mode) {

            super(mode);
        }

        /**
         * Returns the undo mode object from the old undo mode integer.<p>
         * 
         * @param mode the old undo mode integer
         * 
         * @return the undo mode object
         */
        public static CmsResourceUndoMode valueOf(int mode) {

            switch (mode) {
                case 1:
                    return CmsResourceUndoMode.MODE_UNDO_CONTENT;
                case 2:
                    return CmsResourceUndoMode.MODE_UNDO_CONTENT_RECURSIVE;
                case 3:
                    return CmsResourceUndoMode.MODE_UNDO_MOVE_CONTENT;
                case 4:
                default:
                    return CmsResourceUndoMode.MODE_UNDO_MOVE_CONTENT_RECURSIVE;
            }
        }

        /**
         * Returns a mode that includes the move operation with the same semantic as this mode.<p>
         * 
         * @return a mode that includes the move operation with the same semantic as this mode
         */
        public CmsResourceUndoMode includeMove() {

            if (!isUndoMove()) {
                // keep the same semantic but including move 
                return CmsResourceUndoMode.valueOf(getMode() + 2);
            }
            return this;
        }

        /**
         * Returns <code>true</code> if this undo operation is recursive.<p>
         * 
         * @return <code>true</code> if this undo operation is recursive
         */
        public boolean isRecursive() {

            return getMode() > CmsResource.UNDO_CONTENT.getMode();
        }

        /**
         * Returns <code>true</code> if this undo mode will undo move operations.<p>
         * 
         * @return <code>true</code> if this undo mode will undo move operations
         */
        public boolean isUndoMove() {

            return getMode() > CmsResource.UNDO_CONTENT_RECURSIVE.getMode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return String.valueOf(getMode());
        }
    }

    /**
     * A comparator for the release date of 2 resources.<p>
     * 
     * If the release date of a resource is not set, the
     * creation date is used instead.<p>
     */
    public static final Comparator COMPARE_DATE_RELEASED = new Comparator() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {

            if ((o1 == o2) || !(o1 instanceof CmsResource) || !(o2 instanceof CmsResource)) {
                return 0;
            }

            CmsResource r1 = (CmsResource)o1;
            CmsResource r2 = (CmsResource)o2;

            long date1 = r1.getDateReleased();
            if (date1 == CmsResource.DATE_RELEASED_DEFAULT) {
                // use creation date if release date is not set
                date1 = r1.getDateLastModified();
            }

            long date2 = r2.getDateReleased();
            if (date2 == CmsResource.DATE_RELEASED_DEFAULT) {
                // use creation date if release date is not set
                date2 = r2.getDateLastModified();
            }

            return (date1 > date2) ? -1 : (date1 < date2) ? 1 : 0;
        }
    };

    /**
     * A comparator for the root path of 2 resources.<p>
     */
    public static final Comparator COMPARE_ROOT_PATH = new Comparator() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {

            if ((o1 == o2) || !(o1 instanceof CmsResource) || !(o2 instanceof CmsResource)) {
                return 0;
            }

            CmsResource r1 = (CmsResource)o1;
            CmsResource r2 = (CmsResource)o2;

            return r1.getRootPath().compareTo(r2.getRootPath());
        }
    };

    /**
     * A comparator for the root path of 2 resources ignoring case differences.<p>
     */
    public static final Comparator COMPARE_ROOT_PATH_IGNORE_CASE = new Comparator() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {

            if ((o1 == o2) || !(o1 instanceof CmsResource) || !(o2 instanceof CmsResource)) {
                return 0;
            }

            CmsResource r1 = (CmsResource)o1;
            CmsResource r2 = (CmsResource)o2;

            return r1.getRootPath().compareToIgnoreCase(r2.getRootPath());
        }
    };

    /**
     * A comparator for the root path of 2 resources ignoring case differences, putting folders before files.<p>
     */
    public static final Comparator COMPARE_ROOT_PATH_IGNORE_CASE_FOLDERS_FIRST = new Comparator() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {

            if ((o1 == o2) || !(o1 instanceof CmsResource) || !(o2 instanceof CmsResource)) {
                return 0;
            }

            CmsResource r1 = (CmsResource)o1;
            CmsResource r2 = (CmsResource)o2;

            if (r1.isFolder() && !r2.isFolder()) {
                return -1;
            } else if (r2.isFolder() && !r1.isFolder()) {
                return 1;
            }
            // if same type, compare the name of the resource
            return r1.getRootPath().compareToIgnoreCase(r2.getRootPath());
        }
    };

    /** Copy mode for copy resources as new resource. */
    public static final CmsResourceCopyMode COPY_AS_NEW = CmsResourceCopyMode.MODE_COPY_AS_NEW;

    /** Copy mode for copy resources as sibling. */
    public static final CmsResourceCopyMode COPY_AS_SIBLING = CmsResourceCopyMode.MODE_COPY_AS_SIBLING;

    /** Copy mode to preserve siblings during copy. */
    public static final CmsResourceCopyMode COPY_PRESERVE_SIBLING = CmsResourceCopyMode.MODE_COPY_PRESERVE_SIBLING;

    /** The default expiration date of a resource (which is: never expires). */
    public static final long DATE_EXPIRED_DEFAULT = Long.MAX_VALUE;

    /** The default release date of a resource (which is: always released). */
    public static final long DATE_RELEASED_DEFAULT = 0;

    /** A special date that indicates release and expiration information are to be ignored. */
    public static final long DATE_RELEASED_EXPIRED_IGNORE = Long.MIN_VALUE;

    /** Signals that siblings of this resource should not be deleted. */
    public static final CmsResourceDeleteMode DELETE_PRESERVE_SIBLINGS = CmsResourceDeleteMode.MODE_DELETE_PRESERVE_SIBLINGS;

    /** Signals that siblings of this resource should be deleted. */
    public static final CmsResourceDeleteMode DELETE_REMOVE_SIBLINGS = CmsResourceDeleteMode.MODE_DELETE_REMOVE_SIBLINGS;

    /** Flag to indicate that this is an internal resource, that can't be accessed directly. */
    public static final int FLAG_INTERNAL = 512;

    /** The resource is linked inside a site folder specified in the OpenCms configuration. */
    public static final int FLAG_LABELED = 2;

    /** Flag to indicate that this is a temporary resource. */
    public static final int FLAG_TEMPFILE = 1024;

    /** The name constraints when generating new resources. */
    public static final String NAME_CONSTRAINTS = "-._~$";

    /** Indicates if a resource has been changed in the offline version when compared to the online version. */
    public static final CmsResourceState STATE_CHANGED = CmsResourceState.STATE_CHANGED;

    /** Indicates if a resource has been deleted in the offline version when compared to the online version. */
    public static final CmsResourceState STATE_DELETED = CmsResourceState.STATE_DELETED;

    /**
     * Special state value that indicates the current state must be kept on a resource,
     * this value must never be written to the database.
     */
    public static final CmsResourceState STATE_KEEP = CmsResourceState.STATE_KEEP;

    /** Indicates if a resource is new in the offline version when compared to the online version. */
    public static final CmsResourceState STATE_NEW = CmsResourceState.STATE_NEW;

    /** Indicates if a resource is unchanged in the offline version when compared to the online version. */
    public static final CmsResourceState STATE_UNCHANGED = CmsResourceState.STATE_UNCHANGED;

    /** Flag for leaving a date unchanged during a touch operation. */
    public static final long TOUCH_DATE_UNCHANGED = -1;

    /** Indicates that the undo method will only undo content changes. */
    public static final CmsResourceUndoMode UNDO_CONTENT = CmsResourceUndoMode.MODE_UNDO_CONTENT;

    /** Indicates that the undo method will only recursive undo content changes. */
    public static final CmsResourceUndoMode UNDO_CONTENT_RECURSIVE = CmsResourceUndoMode.MODE_UNDO_CONTENT_RECURSIVE;

    /** Indicates that the undo method will undo move operations and content changes. */
    public static final CmsResourceUndoMode UNDO_MOVE_CONTENT = CmsResourceUndoMode.MODE_UNDO_MOVE_CONTENT;

    /** Indicates that the undo method will undo move operations and recursive content changes. */
    public static final CmsResourceUndoMode UNDO_MOVE_CONTENT_RECURSIVE = CmsResourceUndoMode.MODE_UNDO_MOVE_CONTENT_RECURSIVE;

    /** The vfs path of the sites master folder. */
    public static final String VFS_FOLDER_SITES = "/sites";

    /** The vfs path of the system folder. */
    public static final String VFS_FOLDER_SYSTEM = "/system";

    /** Serial version UID required for safe serialization. */
    private static final long serialVersionUID = 257325098790850498L;

    /** The size of the content. */
    protected int m_length;

    /** The date of the last modification of the content of this resource. */
    private long m_dateContent;

    /** The creation date of this resource. */
    private long m_dateCreated;

    /** The expiration date of this resource. */
    private long m_dateExpired;

    /** The date of the last modification of this resource. */
    private long m_dateLastModified;

    /** The release date of this resource. */
    private long m_dateReleased;

    /** The flags of this resource. */
    private int m_flags;

    /** Indicates if this resource is a folder or not. */
    private boolean m_isFolder;

    /** Boolean flag whether the timestamp of this resource was modified by a touch command. */
    private boolean m_isTouched;

    /** The project id where this resource has been last modified in. */
    private CmsUUID m_projectLastModified;

    /** The id of the resource database record. */
    private CmsUUID m_resourceId;

    /** The name of a resource with it's full path from the root folder including the current site root. */
    private String m_rootPath;

    /** The number of links that point to this resource. */
    private int m_siblingCount;

    /** The state of this resource. */
    private CmsResourceState m_state;

    /** The id of the structure database record. */
    private CmsUUID m_structureId;

    /** The resource type id of this resource. */
    private int m_typeId;

    /** The id of the user who created this resource. */
    private CmsUUID m_userCreated;

    /** The id of the user who modified this resource last. */
    private CmsUUID m_userLastModified;

    /** The version number of this resource. */
    private int m_version;

    /**
     * Constructor, creates a new CmsRecource object.<p>
     * 
     * @param structureId the id of this resources structure record
     * @param resourceId the id of this resources resource record
     * @param rootPath the root path to the resource
     * @param type the type of this resource
     * @param isFolder must be true if thr resource is a folder, or false if it is a file
     * @param flags the flags of this resource
     * @param projectId the project id this resource was last modified in
     * @param state the state of this resource
     * @param dateCreated the creation date of this resource
     * @param userCreated the id of the user who created this resource
     * @param dateLastModified the date of the last modification of this resource
     * @param userLastModified the id of the user who did the last modification of this resource
     * @param dateReleased the release date of this resource
     * @param dateExpired the expiration date of this resource
     * @param linkCount the count of all siblings of this resource 
     * @param size the size of the file content of this resource 
     * @param dateContent the date of the last modification of the content of this resource 
     * @param version the version number of this resource   
     */
    public CmsResource(
        CmsUUID structureId,
        CmsUUID resourceId,
        String rootPath,
        int type,
        boolean isFolder,
        int flags,
        CmsUUID projectId,
        CmsResourceState state,
        long dateCreated,
        CmsUUID userCreated,
        long dateLastModified,
        CmsUUID userLastModified,
        long dateReleased,
        long dateExpired,
        int linkCount,
        int size,
        long dateContent,
        int version) {

        m_structureId = structureId;
        m_resourceId = resourceId;
        m_rootPath = rootPath;
        m_typeId = type;
        m_isFolder = isFolder;
        m_flags = flags;
        m_projectLastModified = projectId;
        m_state = state;
        m_dateCreated = dateCreated;
        m_userCreated = userCreated;
        m_dateLastModified = dateLastModified;
        m_userLastModified = userLastModified;
        m_dateReleased = dateReleased;
        m_dateExpired = dateExpired;
        m_siblingCount = linkCount;
        m_length = size;
        m_dateContent = dateContent;
        m_version = version;
        m_isTouched = false;
    }

    /**
     * Checks if the provided resource name is a valid resource name, 
     * that is contains only valid characters.<p>
     * 
     * A resource name can only be composed of digits, 
     * standard ASCII letters and the symbols defined in {@link #NAME_CONSTRAINTS}.
     * A resource name must also not contain only dots.<p>
     *
     * @param name the resource name to check
     * 
     * @throws CmsIllegalArgumentException if the given resource name is not valid
     */
    public static void checkResourceName(String name) throws CmsIllegalArgumentException {

        if (CmsStringUtil.isEmptyOrWhitespaceOnly(name)) {
            throw new CmsIllegalArgumentException(Messages.get().container(Messages.ERR_BAD_RESOURCENAME_EMPTY_0, name));
        }

        CmsStringUtil.checkName(name, NAME_CONSTRAINTS, Messages.ERR_BAD_RESOURCENAME_4, Messages.get());

        // check for filenames that have only dots (which will cause issues in the static export)
        boolean onlydots = true;
        // this must be done only for the last name (not for parent folders)
        String lastName = CmsResource.getName(name);
        int l = lastName.length();
        for (int i = 0; i < l; i++) {
            char c = lastName.charAt(i);
            if ((c != '.') && (c != '/')) {
                onlydots = false;
            }
        }
        if (onlydots) {
            throw new CmsIllegalArgumentException(Messages.get().container(
                Messages.ERR_BAD_RESOURCENAME_DOTS_1,
                lastName));
        }
    }

    /**
     * Returns the folder path of the resource with the given name,
     * if the resource is a folder (i.e. ends with a "/"), the complete path of the folder 
     * is returned (not the parent folder path).<p>
     * 
     * This is achived by just cutting of everthing behind the last occurence of a "/" character
     * in the String, no check if performed if the resource exists or not in the VFS, 
     * only resources that end with a "/" are considered to be folders.
     * 
     * Example: Returns <code>/system/def/</code> for the
     * resource <code>/system/def/file.html</code> and 
     * <code>/system/def/</code> for the (folder) resource <code>/system/def/</code>.
     *
     * @param resource the name of a resource
     * @return the folder of the given resource
     */
    public static String getFolderPath(String resource) {

        return resource.substring(0, resource.lastIndexOf('/') + 1);
    }

    /**
     * Returns the name of a resource without the path information.<p>
     * 
     * The resource name of a file is the name of the file.
     * The resource name of a folder is the folder name with trailing "/".
     * The resource name of the root folder is <code>/</code>.<p>
     * 
     * Example: <code>/system/workplace/</code> has the resource name <code>workplace/</code>.
     * 
     * @param resource the resource to get the name for
     * @return the name of a resource without the path information
     */
    public static String getName(String resource) {

        if ("/".equals(resource)) {
            return "/";
        }
        // remove the last char, for a folder this will be "/", for a file it does not matter
        String parent = (resource.substring(0, resource.length() - 1));
        // now as the name does not end with "/", check for the last "/" which is the parent folder name
        return resource.substring(parent.lastIndexOf('/') + 1);
    }

    /**
     * Returns the absolute parent folder name of a resource.<p>
     * 
     * The parent resource of a file is the folder of the file.
     * The parent resource of a folder is the parent folder.
     * The parent resource of the root folder is <code>null</code>.<p>
     * 
     * Example: <code>/system/workplace/</code> has the parent <code>/system/</code>.
     * 
     * @param resource the resource to find the parent folder for
     * @return the calculated parent absolute folder path, or <code>null</code> for the root folder 
     */
    public static String getParentFolder(String resource) {

        if ("/".equals(resource)) {
            return null;
        }
        // remove the last char, for a folder this will be "/", for a file it does not matter
        String parent = (resource.substring(0, resource.length() - 1));
        // now as the name does not end with "/", check for the last "/" which is the parent folder name
        return parent.substring(0, parent.lastIndexOf('/') + 1);
    }

    /**
     * Returns the directory level of a resource.<p>
     * 
     * The root folder "/" has level 0,
     * a folder "/foo/" would have level 1,
     * a folfer "/foo/bar/" level 2 etc.<p> 
     * 
     * @param resource the resource to determin the directory level for
     * @return the directory level of a resource
     */
    public static int getPathLevel(String resource) {

        int level = -1;
        int pos = 0;
        while (resource.indexOf('/', pos) >= 0) {
            pos = resource.indexOf('/', pos) + 1;
            level++;
        }
        return level;
    }

    /**
     * Returns the name of a parent folder of the given resource, 
     * that is either minus levels up 
     * from the current folder, or that is plus levels down from the 
     * root folder.<p>
     * 
     * @param resource the name of a resource
     * @param level of levels to walk up or down
     * @return the name of a parent folder of the given resource 
     */
    public static String getPathPart(String resource, int level) {

        resource = getFolderPath(resource);
        String result = null;
        int pos = 0, count = 0;
        if (level >= 0) {
            // Walk down from the root folder /
            while ((count < level) && (pos > -1)) {
                count++;
                pos = resource.indexOf('/', pos + 1);
            }
        } else {
            // Walk up from the current folder
            pos = resource.length();
            while ((count > level) && (pos > -1)) {
                count--;
                pos = resource.lastIndexOf('/', pos - 1);
            }
        }
        if (pos > -1) {
            // To many levels walked
            result = resource.substring(0, pos + 1);
        } else {
            // Add trailing slash
            result = (level < 0) ? "/" : resource;
        }
        return result;
    }

    /**
     * Returns true if the resource name is a folder name, i.e. ends with a "/".<p>
     * 
     * @param resource the resource to check
     * @return true if the resource name is a folder name, i.e. ends with a "/"
     */
    public static boolean isFolder(String resource) {

        return CmsStringUtil.isNotEmpty(resource) && (resource.charAt(resource.length() - 1) == '/');
    }

    /**
     * Returns a clone of this Objects instance.<p>
     * 
     * @return a clone of this instance
     */
    public Object clone() {

        CmsResource clone = new CmsResource(
            m_structureId,
            m_resourceId,
            m_rootPath,
            m_typeId,
            m_isFolder,
            m_flags,
            m_projectLastModified,
            m_state,
            m_dateCreated,
            m_userCreated,
            m_dateLastModified,
            m_userLastModified,
            m_dateReleased,
            m_dateExpired,
            m_siblingCount,
            m_length,
            m_dateContent,
            m_version);

        if (isTouched()) {
            clone.setDateLastModified(m_dateLastModified);
        }

        return clone;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {

        if (obj == this) {
            return 0;
        }
        if (obj instanceof CmsResource) {
            return m_rootPath.compareTo(((CmsResource)obj).m_rootPath);
        }
        return 0;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (obj instanceof CmsResource) {
            return ((CmsResource)obj).m_structureId.equals(m_structureId);
        }
        return false;
    }

    /**
     * Returns the date of the last modification of the content of this resource.<p>
     *
     * @return the date of the last modification of the content of this resource
     */
    public long getDateContent() {

        return m_dateContent;
    }

    /**
     * Returns the date of the creation of this resource.<p>
     *
     * @return the date of the creation of this resource
     */
    public long getDateCreated() {

        return m_dateCreated;
    }

    /**
     * Returns the expiration date this resource.<p>
     *
     * @return the expiration date of this resource
     */
    public long getDateExpired() {

        return m_dateExpired;
    }

    /**
     * Returns the date of the last modification of this resource.<p>
     *
     * @return the date of the last modification of this resource
     */
    public long getDateLastModified() {

        return m_dateLastModified;
    }

    /**
     * Returns the release date this resource.<p>
     *
     * @return the release date of this resource
     */
    public long getDateReleased() {

        return m_dateReleased;
    }

    /**
     * Returns the flags of this resource.<p>
     *
     * @return the flags of this resource
     */
    public int getFlags() {

        return m_flags;
    }

    /**
     * Returns the length of the resource.<p>
     *
     * If the resource is a file, then this is the byte size of the file content.
     * If the resource is a folder, then the size is always -1.<p>
     *
     * @return the length of the content
     */
    public int getLength() {

        // make sure folders always have a -1 size
        return m_isFolder ? -1 : m_length;
    }

    /**
     * Returns the name of this resource, e.g. <code>index.html</code>.<p>
     *
     * @return the name of this resource
     */
    public String getName() {

        String name = getName(m_rootPath);
        if (name.charAt(name.length() - 1) == '/') {
            return name.substring(0, name.length() - 1);
        } else {
            return name;
        }
    }

    /**
     * Returns the id of the project where the resource has been last modified.<p>
     *
     * @return the id of the project where the resource has been last modified, or <code>null</code>
     */
    public CmsUUID getProjectLastModified() {

        return m_projectLastModified;
    }

    /**
     * Returns the id of the resource database entry of this resource.<p>
     *
     * @return the id of the resource database entry
     */
    public CmsUUID getResourceId() {

        return m_resourceId;
    }

    /**
     * Returns the name of a resource with it's full path from the root folder 
     * including the current site root, 
     * for example <code>/sites/default/myfolder/index.html</code>.<p>
     *
     * In a presentation level application usually the current site root must be
     * cut of from the root path. Use {@link CmsObject#getSitePath(CmsResource)} 
     * to get the "absolute" path of a resource in the current site.<p>
     *
     * @return the name of a resource with it's full path from the root folder 
     *      including the current site root
     * 
     * @see CmsObject#getSitePath(CmsResource)
     * @see CmsRequestContext#getSitePath(CmsResource)
     * @see CmsRequestContext#removeSiteRoot(String) 
     */
    public String getRootPath() {

        return m_rootPath;
    }

    /**
     * Returns the number of siblings of the resource, also counting this resource.<p>
     * 
     * If a resource has no sibling, the total sibling count for this resource is <code>1</code>, 
     * if a resource has <code>n</code> siblings, the sibling count is <code>n + 1</code>.<p> 
     * 
     * @return the number of siblings
     */
    public int getSiblingCount() {

        return m_siblingCount;
    }

    /**
     * Returns the state of this resource.<p>
     *
     * This may be {@link CmsResource#STATE_UNCHANGED}, 
     * {@link CmsResource#STATE_CHANGED}, {@link CmsResource#STATE_NEW} 
     * or {@link CmsResource#STATE_DELETED}.<p>
     *
     * @return the state of this resource
     */
    public CmsResourceState getState() {

        return m_state;
    }

    /**
     * Returns the id of the structure record of this resource.<p>
     * 
     * @return the id of the structure record of this resource
     */
    public CmsUUID getStructureId() {

        return m_structureId;
    }

    /**
     * Returns the resource type id for this resource.<p>
     *
     * @return the resource type id of this resource
     */
    public int getTypeId() {

        return m_typeId;
    }

    /**
     * Returns the user id of the user who created this resource.<p>
     * 
     * @return the user id
     */
    public CmsUUID getUserCreated() {

        return m_userCreated;
    }

    /**
     * Returns the user id of the user who made the last change on this resource.<p>
     *
     * @return the user id of the user who made the last change<p>
     */
    public CmsUUID getUserLastModified() {

        return m_userLastModified;
    }

    /**
     * Returns the version number of this resource.<p>
     *
     * @return the version number of this resource
     */
    public int getVersion() {

        return m_version;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        if (m_structureId != null) {
            return m_structureId.hashCode();
        }

        return CmsUUID.getNullUUID().hashCode();
    }

    /** 
     * Returns <code>true</code> if this resource is expired at the given time according to the 
     * information stored in {@link #getDateExpired()}.<p>
     * 
     * @param time the time to check the expiration date against
     * 
     * @return <code>true</code> if this resource is expired at the given time
     *      
     * @see #isReleased(long)
     * @see #isReleasedAndNotExpired(long)
     * @see #DATE_RELEASED_EXPIRED_IGNORE
     * @see CmsResource#getDateReleased()
     * @see CmsRequestContext#getRequestTime()
     */
    public boolean isExpired(long time) {

        return (time > m_dateExpired) && (time != DATE_RELEASED_EXPIRED_IGNORE);
    }

    /**
     * Returns <code>true</code> if the resource is a file, i.e. can have no sub-resources.<p>
     *
     * @return true if this resource is a file, false otherwise
     */
    public boolean isFile() {

        return !m_isFolder;
    }

    /**
     * Returns <code>true</code> if the resource is a folder, i.e. can have sub-resources.<p>
     *
     * @return true if this resource is a folder, false otherwise
     */
    public boolean isFolder() {

        return m_isFolder;
    }

    /**
     * Checks if the resource is internal.<p>
     * 
     * This state is stored as bit 1 in the resource flags.<p>
     * 
     * @return true if the resource is internal, otherwise false
     */
    public boolean isInternal() {

        return ((m_flags & FLAG_INTERNAL) > 0);
    }

    /**
     * Checks if the link has to be labeled with a special icon in the explorer view.<p>
     *
     * This state is stored as bit 2 in the resource flags.<p>
     * 
     * @return true if a link to the resource has to be labeled, otherwise false
     */
    public boolean isLabeled() {

        return ((m_flags & CmsResource.FLAG_LABELED) > 0);
    }

    /** 
     * Returns <code>true</code> if this resource is released at the given time according to the 
     * information stored in {@link #getDateReleased()}.<p>
     * 
     * @param time the time to check the release date against
     * 
     * @return <code>true</code> if this resource is released at the given time
     *      
     * @see #isExpired(long)
     * @see #isReleasedAndNotExpired(long)
     * @see #DATE_RELEASED_EXPIRED_IGNORE
     * @see CmsResource#getDateReleased()
     * @see CmsRequestContext#getRequestTime()
     */
    public boolean isReleased(long time) {

        return (time > m_dateReleased) || (time == DATE_RELEASED_EXPIRED_IGNORE);
    }

    /** 
     * Returns <code>true</code> if this resource is valid at the given time according to the 
     * information stored in {@link #getDateReleased()} and {@link #getDateExpired()}.<p>
     * 
     * A resource is valid if it is released and not yet expired.<p>
     * 
     * @param time the time to check the release and expiration date against
     * 
     * @return <code>true</code> if this resource is valid at the given time
     *      
     * @see #isExpired(long)
     * @see #isReleased(long)
     * @see #DATE_RELEASED_EXPIRED_IGNORE
     * @see CmsResource#getDateReleased()
     * @see CmsRequestContext#getRequestTime()
     */
    public boolean isReleasedAndNotExpired(long time) {

        return ((time < m_dateExpired) && (time > m_dateReleased)) || (time == DATE_RELEASED_EXPIRED_IGNORE);
    }

    /**
     * Returns true if this resource was touched.<p>
     * 
     * @return boolean true if this resource was touched
     */
    public boolean isTouched() {

        return m_isTouched;
    }

    /**
     * Sets the expiration date this resource.<p>
     * 
     * @param time the date to set
     */
    public void setDateExpired(long time) {

        m_dateExpired = time;
    }

    /**
     * Sets the date of the last modification of this resource.<p>
     * 
     * @param time the date to set
     */
    public void setDateLastModified(long time) {

        m_isTouched = true;
        m_dateLastModified = time;
    }

    /**
     * Sets the release date this resource.<p>
     * 
     * @param time the date to set
     */
    public void setDateReleased(long time) {

        m_dateReleased = time;
    }

    /**
     * Sets the flags of this resource.<p>
     *
     * @param flags int value with flag values to set
     */
    public void setFlags(int flags) {

        m_flags = flags;
    }

    /**
     * Sets the state of this resource.<p>
     *
     * @param state the state to set
     */
    public void setState(CmsResourceState state) {

        m_state = state;
    }

    /**
     * Sets the type of this resource.<p>
     *
     * @param type the type to set
     */
    public void setType(int type) {

        m_typeId = type;
    }

    /**
     * Sets the user id of the user who changed this resource.<p>
     *
     * @param resourceLastModifiedByUserId the user id of the user who changed the resource
     */
    public void setUserLastModified(CmsUUID resourceLastModifiedByUserId) {

        m_userLastModified = resourceLastModifiedByUserId;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer result = new StringBuffer();

        result.append("[");
        result.append(this.getClass().getName());
        result.append(", path: ");
        result.append(m_rootPath);
        result.append(", structure id ");
        result.append(m_structureId);
        result.append(", resource id: ");
        result.append(m_resourceId);
        result.append(", type id: ");
        result.append(m_typeId);
        result.append(", folder: ");
        result.append(m_isFolder);
        result.append(", flags: ");
        result.append(m_flags);
        result.append(", project: ");
        result.append(m_projectLastModified);
        result.append(", state: ");
        result.append(m_state);
        result.append(", date created: ");
        result.append(new java.util.Date(m_dateCreated));
        result.append(", user created: ");
        result.append(m_userCreated);
        result.append(", date lastmodified: ");
        result.append(new java.util.Date(m_dateLastModified));
        result.append(", user lastmodified: ");
        result.append(m_userLastModified);
        result.append(", date released: ");
        result.append(new java.util.Date(m_dateReleased));
        result.append(", date expired: ");
        result.append(new java.util.Date(m_dateExpired));
        result.append(", size: ");
        result.append(m_length);
        result.append(", sibling count: ");
        result.append(m_siblingCount);
        result.append(", version: ");
        result.append(m_version);
        result.append("]");

        return result.toString();
    }
}
