/*
 * File   : $Source: /alkacon/cvs/opencms/src/com/opencms/file/Attic/A_CmsResource.java,v $
 * Date   : $Date: 2000/04/07 15:22:16 $
 * Version: $Revision: 1.12 $
 *
 * Copyright (C) 2000  The OpenCms Group 
 * 
 * This File is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * For further information about OpenCms, please see the
 * OpenCms Website: http://www.opencms.com
 * 
 * You should have received a copy of the GNU General Public License
 * long with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.opencms.file;

import java.util.*;

/**
 * This abstact class describes a resource in the Cms.
 * This resource can be a A_CmsFile or a A_CmsFolder.
 * 
 * @author Andreas Schouten
 * @author Michael Emmerich
 * @version $Revision: 1.12 $ $Date: 2000/04/07 15:22:16 $
 */
public abstract class A_CmsResource  {
	/**
	 * Returns the absolute path for this resource.<BR/>
	 * Example: retuns /system/def/language.cms
	 * 
	 * @return the absolute path for this resource.
	 */
    abstract public String getAbsolutePath();
	
	/**
	 * Returns the absolute path of the parent.<BR/>
	 * Example: /system/def has the parent /system/<BR/>
	 * / has no parent
	 * 
	 * @return the parent absolute path, or null if this is the root-resource.
	 */
    abstract public String getParent();
	
	/**
	 * Returns the path for this resource.<BR/>
	 * Example: retuns /system/def/ for the
	 * resource /system/def/language.cms
	 * 
	 * @return the path for this resource.
	 */
	abstract public String getPath();
	
	/**
	 * Returns the name of this resource.<BR/>
	 * Example: retuns language.cms for the
	 * resource /system/def/language.cms
	 * 
	 * @return the name of this resource.
	 */
    abstract public String getName();
    	
	/**
	 * Gets the type id for this resource.
	 * 
	 * @return the type id of this resource.
	 */
	abstract public int getType();
    
     /**
	 * Sets the type id for this resource.
	 * 
	 * @param The new type id of this resource.
	 */
	abstract void setType(int type);
        	
	/**
	 * Gets the launcher type id for this resource.
	 * 
	 * @return the launcher type id of this resource.
	 */
	abstract public int getLauncherType();
    
     /**
	 * Sets launcher the type id for this resource.
	 * 
	 * @param The new launcher type id of this resource.
	 */
	abstract void setLauncherType(int type);
    
	/**
	 * Gets the launcher classname for this resource.
	 * 
	 * @return the launcher classname for this resource.
	 */
	abstract public String getLauncherClassname();    
    
     /**
	 * Sets launcher classname for this resource.
	 * 
	 * @param The new launcher classname for this resource.
	 */
	abstract void setLauncherClassname(String name);
    
	/**
	 * Returns the date of the creation for this resource.
	 * 
	 * @return the date of the creation for this resource.
	 */
	abstract public long getDateCreated();
	
	/**
	 * Returns the date of the last modification for this resource.
	 * 
	 * @return the date of the last modification for this resource.
	 */
    abstract public long getDateLastModified();

	/**
	 * Returns a string-representation for this object.
	 * This can be used for debugging.
	 * 
	 * @return string-representation for this object.
	 */
	abstract public String toString();
	
	/**
	 * Compares the overgiven object with this object.
	 * 
	 * @return true, if the object is identically else it returns false.
	 */
    abstract public boolean equals(Object obj);
    
	/**
	 * Returns the userid of the resource owner.
	 * 
	 * @return the userid of the resource owner.
	 */
	abstract int getOwnerId();
	
     /**
	 * Sets the userid of the resource owner.
	 * 
	 * @param The userid of the new resource owner.
	 */
	abstract void setOwnerId(int id);
    
	/**
	 * Returns the groupid of this resource.
	 * 
	 * @return the groupid of this resource.
	 */
    abstract int getGroupId();
	
     /**
	 * Sets the groupid of this resource.
	 * 
	 * @param The new groupid of this resource.
	 */
	abstract void setGroupId(int id);
    
	/**
	 * Returns the flags of this resource.
	 * 
	 * @return the flags of this resource.
	 */
    abstract int getFlags();
    
     /**
	 * Sets the flags of this resource.
	 * 
	 * @param The new flags of this resource.
	 */
	abstract void setFlags(int flags);

     /**
	 * Returns the accessflags of this resource.
	 * 
	 * @return the accessflags of this resource.
	 */
    abstract public int getAccessFlags();
   
     /**
	 * Sets the accessflags of this resource.
	 * 
	 * @param The new accessflags of this resource.
	 */
	abstract void setAccessFlags(int flags);
    
	/**
	 * Determines, if this resource is a folder.
	 * 
	 * @return true, if this resource is a folder, else it returns false.
	 */
    abstract public boolean isFolder();

	/**
	 * Determines, if this resource is a file.
	 * 
	 * @return true, if this resource is a file, else it returns false.
	 */
    abstract public boolean isFile();
	
	/**
	 * Returns the state of this resource.<BR/>
	 * This may be C_STATE_UNCHANGED, C_STATE_CHANGED, C_STATE_NEW or C_STATE_DELETED.
	 * 
	 * @return the state of this resource.
	 */
	abstract public int getState();
	
     /**
	 * Sets the state of this resource.
	 * 
	 * @param The new state of this resource.
	 */
	abstract void setState(int state);
    
	/**
	 * Determines, if this resource is locked by a user.
	 * 
	 * @return true, if this resource is locked by a user, else it returns false.
	 */
	abstract public boolean isLocked();

	/**
	 * Returns the user id that locked this resource.
	 * 
	 * @return the user id that locked this resource.
	 * If this resource is free it returns the unknown user id.
	 */
	abstract public int isLockedBy();
    
     /**
	 * Sets the the user id that locked this resource.
	 * 
	 * @param The new the user id that locked this resource.
	 */
	abstract void setLocked(int id);

	/**
	 * Returns the project id for this resource.
	 * 
	 * @return The project idfor this resource.
	 */
	abstract int getProjectId();
    
     /**
	 * Gets the length of the content (filesize).
	 * 
	 * @return the length of the content.
	 */
    abstract public int getLength();

    
     /** 
     * Checks if a resource belongs to a project.
     * @param project The project which the resources is checked about.
     * @return true if the resource is in the project, false otherwise.
     */
    abstract public boolean inProject(A_CmsProject project);
    
	/**
	 * Creates a Unix-Style string of access rights from the access right flag 
	 * of a resource
	 * 
	 * @return String of access rights
	 */
	public abstract String getFlagString();
}
