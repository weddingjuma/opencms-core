CREATE TABLE CMS_USERS (
	USER_ID VARCHAR(36) BINARY NOT NULL,
	USER_NAME VARCHAR(128) BINARY NOT NULL,
	USER_PASSWORD VARCHAR(64) BINARY NOT NULL,
	USER_DESCRIPTION VARCHAR(255) NOT NULL,
	USER_FIRSTNAME VARCHAR(128) NOT NULL,
	USER_LASTNAME VARCHAR(128) NOT NULL,
	USER_EMAIL VARCHAR(128) NOT NULL,
	USER_LASTLOGIN BIGINT NOT NULL,
	USER_FLAGS INT NOT NULL,
	USER_INFO TEXT,
	USER_ADDRESS VARCHAR(255) NOT NULL,
	USER_TYPE INT NOT NULL,
	PRIMARY KEY	(USER_ID), 
	UNIQUE INDEX USER_NAME_IDX (USER_NAME)
);

CREATE TABLE CMS_GROUPS (
	GROUP_ID VARCHAR(36) BINARY NOT NULL,
	PARENT_GROUP_ID VARCHAR(36) BINARY NOT NULL,
	GROUP_NAME VARCHAR(128) BINARY NOT NULL,
	GROUP_DESCRIPTION VARCHAR(255) NOT NULL,
	GROUP_FLAGS INT NOT NULL,
	PRIMARY KEY (GROUP_ID),
	UNIQUE INDEX GROUP_NAME_IDX (GROUP_NAME),
	INDEX PARENT_GROUP_ID_IDX (PARENT_GROUP_ID)
);

CREATE TABLE CMS_GROUPUSERS (
	GROUP_ID VARCHAR(36) BINARY NOT NULL,
	USER_ID VARCHAR(36) BINARY NOT NULL,
	GROUPUSER_FLAGS INT NOT NULL,
	PRIMARY KEY (GROUP_ID, USER_ID),
	INDEX GROUP_ID_IDX (GROUP_ID),
	INDEX USER_ID_IDX (USER_ID)
);

CREATE TABLE CMS_PROJECTS (
	PROJECT_ID INT NOT NULL,
	PROJECT_NAME VARCHAR(255) BINARY NOT NULL,
	PROJECT_DESCRIPTION VARCHAR(255) NOT NULL,
	PROJECT_FLAGS INT NOT NULL,
	PROJECT_TYPE INT NOT NULL,
	USER_ID VARCHAR(36) BINARY NOT NULL,
	GROUP_ID VARCHAR(36) BINARY NOT NULL, 
	MANAGERGROUP_ID VARCHAR(36) BINARY NOT NULL,
	TASK_ID INT NOT NULL,
	DATE_CREATED BIGINT NOT NULL,
	PRIMARY KEY (PROJECT_ID), 
	UNIQUE INDEX PROJECT_NAME_DATE_CREATED_IDX (PROJECT_NAME, DATE_CREATED),
	INDEX PROJECT_FLAGS_IDX (PROJECT_FLAGS),
	INDEX GROUP_ID_IDX (GROUP_ID),
	INDEX MANAGERGROUP_ID_IDX (MANAGERGROUP_ID),
	INDEX USER_ID_IDX (USER_ID),
	INDEX TASK_ID_IDX (TASK_ID)
);

CREATE TABLE CMS_BACKUP_PROJECTS (
	PROJECT_ID INT NOT NULL,
	PROJECT_NAME VARCHAR(255) BINARY NOT NULL,
	PROJECT_DESCRIPTION VARCHAR(255) NOT NULL,
	PROJECT_TYPE INT NOT NULL,
	USER_ID VARCHAR(36) BINARY NOT NULL,
	GROUP_ID VARCHAR(36) BINARY NOT NULL,
	MANAGERGROUP_ID VARCHAR(36) BINARY NOT NULL,
	TASK_ID INT NOT NULL,
	DATE_CREATED BIGINT NOT NULL,	
	PUBLISH_TAG INT NOT NULL,
	PROJECT_PUBLISHDATE DATETIME,
	PROJECT_PUBLISHED_BY VARCHAR(36) BINARY NOT NULL,
	PROJECT_PUBLISHED_BY_NAME VARCHAR(255),
	USER_NAME VARCHAR(128),
	GROUP_NAME VARCHAR(128) BINARY,
	MANAGERGROUP_NAME VARCHAR(128) BINARY,	
	PRIMARY KEY (PUBLISH_TAG)
);

CREATE TABLE CMS_PROJECTRESOURCES (
	PROJECT_ID INT NOT NULL,
	RESOURCE_PATH BLOB NOT NULL,
	PRIMARY KEY (PROJECT_ID, RESOURCE_PATH(255)),
	INDEX RESOURCE_PATH_IDX (RESOURCE_PATH(255))
);

CREATE TABLE CMS_BACKUP_PROJECTRESOURCES (
	PUBLISH_TAG INT NOT NULL,
	PROJECT_ID INT NOT NULL,
	RESOURCE_PATH BLOB NOT NULL,
	PRIMARY KEY (PUBLISH_TAG, PROJECT_ID, RESOURCE_PATH(255))
);

CREATE TABLE CMS_OFFLINE_PROPERTYDEF (
	PROPERTYDEF_ID VARCHAR(36) BINARY NOT NULL, 
	PROPERTYDEF_NAME VARCHAR(128) BINARY NOT NULL,
	PRIMARY KEY (PROPERTYDEF_ID), 
	UNIQUE INDEX PROPERTYDEF_NAME_IDX (PROPERTYDEF_NAME)
);
                           
CREATE TABLE CMS_ONLINE_PROPERTYDEF (
	PROPERTYDEF_ID VARCHAR(36) BINARY NOT NULL, 
	PROPERTYDEF_NAME VARCHAR(128) BINARY NOT NULL,
	PRIMARY KEY (PROPERTYDEF_ID), 
	UNIQUE INDEX PROPERTYDEF_NAME_IDX (PROPERTYDEF_NAME)	
);
                                        
CREATE TABLE CMS_BACKUP_PROPERTYDEF (
	PROPERTYDEF_ID VARCHAR(36) BINARY NOT NULL, 
	PROPERTYDEF_NAME VARCHAR(128) BINARY NOT NULL,
	PRIMARY KEY (PROPERTYDEF_ID), 
	UNIQUE INDEX PROPERTYDEF_NAME_IDX (PROPERTYDEF_NAME)
);

CREATE TABLE CMS_OFFLINE_PROPERTIES (
	PROPERTY_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTYDEF_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_MAPPING_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_MAPPING_TYPE INT NOT NULL,
	PROPERTY_VALUE TEXT NOT NULL,
	PRIMARY KEY (PROPERTY_ID),
	INDEX PROPERTYDEF_ID_IDX (PROPERTYDEF_ID),
	INDEX PROPERTY_MAPPING_ID_IDX (PROPERTY_MAPPING_ID),	
	UNIQUE INDEX PROPERTYDEF_ID_MAPPING_ID_IDX (PROPERTYDEF_ID, PROPERTY_MAPPING_ID)
);
                                         
CREATE TABLE CMS_ONLINE_PROPERTIES (
	PROPERTY_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTYDEF_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_MAPPING_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_MAPPING_TYPE INT NOT NULL,
	PROPERTY_VALUE TEXT NOT NULL,
	PRIMARY KEY(PROPERTY_ID),
	INDEX PROPERTYDEF_ID_IDX (PROPERTYDEF_ID),
	INDEX PROPERTY_MAPPING_ID_IDX (PROPERTY_MAPPING_ID),	
	UNIQUE INDEX PROPERTYDEF_ID_MAPPING_ID_IDX (PROPERTYDEF_ID, PROPERTY_MAPPING_ID)
);
                                                                              
CREATE TABLE CMS_BACKUP_PROPERTIES (
	BACKUP_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTYDEF_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_MAPPING_ID VARCHAR(36) BINARY NOT NULL,
	PROPERTY_MAPPING_TYPE INT NOT NULL,
	PROPERTY_VALUE TEXT NOT NULL,
	PUBLISH_TAG INT,
	VERSION_ID	INT NOT NULL,
	PRIMARY KEY(PROPERTY_ID),
	INDEX PROPERTYDEF_ID_IDX (PROPERTYDEF_ID),
	INDEX PROPERTY_MAPPING_ID_IDX (PROPERTY_MAPPING_ID),	
	INDEX PROPERTYDEF_ID_MAPPING_ID_IDX (PROPERTYDEF_ID, PROPERTY_MAPPING_ID),
	INDEX PUBLISH_TAG_IDX (PUBLISH_TAG)
);                                                                             

CREATE TABLE CMS_SYSTEMID (
	TABLE_KEY VARCHAR(255) NOT NULL,
	ID INT NOT NULL,
	PRIMARY KEY(TABLE_KEY)
);
                                                                                 
CREATE TABLE CMS_TASK (
	AUTOFINISH INT(11),
	ENDTIME DATETIME,
	ESCALATIONTYPEREF INT(11),
	ID INT(11) NOT NULL,
	INITIATORUSERREF VARCHAR(36) BINARY,
	MILESTONEREF INT(11),
	NAME VARCHAR(254),
	ORIGINALUSERREF VARCHAR(36) BINARY,
	AGENTUSERREF VARCHAR(36) BINARY,
	PARENT INT(11),
	PERCENTAGE VARCHAR(50),
	PERMISSION VARCHAR(50),
	PRIORITYREF INT(11) DEFAULT '2',
	ROLEREF VARCHAR(36) BINARY,
	ROOT INT(11),
	STARTTIME DATETIME,
	STATE INT(11),
	TASKTYPEREF INT(11),
	TIMEOUT DATETIME,
	WAKEUPTIME DATETIME,
	HTMLLINK VARCHAR(254),
	ESTIMATETIME INT(11) DEFAULT '86400',
	PRIMARY KEY (ID)
);
                                                                                 
CREATE TABLE CMS_TASKTYPE (
	AUTOFINISH INT(11),
	ESCALATIONTYPEREF INT(11),
	HTMLLINK VARCHAR(254),
	ID INT(11) NOT NULL,
	NAME VARCHAR(50),
	PERMISSION VARCHAR(50),
	PRIORITYREF INT(11),
	ROLEREF VARCHAR(36) BINARY,
	PRIMARY KEY (ID)
);
                                                                                 
CREATE TABLE CMS_TASKLOG (
	COMENT TEXT,
	EXTERNALUSERNAME VARCHAR(254),
	ID INT(11) NOT NULL,
	STARTTIME DATETIME,
	TASKREF INT(11),
	USERREF VARCHAR(36) BINARY NOT NULL,
	TYPE INT(18) DEFAULT '0',
	PRIMARY KEY (ID)
);
                                         
CREATE TABLE CMS_TASKPAR (
	ID INT(11) NOT NULL ,
	PARNAME VARCHAR(50),
	PARVALUE VARCHAR(50),
	REF INT(11),
	PRIMARY KEY (ID)
);

CREATE TABLE CMS_ONLINE_ACCESSCONTROL (
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	PRINCIPAL_ID VARCHAR(36) BINARY NOT NULL,
	ACCESS_ALLOWED INT,
	ACCESS_DENIED INT,
	ACCESS_FLAGS INT,
	PRIMARY KEY (RESOURCE_ID, PRINCIPAL_ID),
	INDEX PRINCIPAL_ID_IDX (PRINCIPAL_ID)
);

CREATE TABLE CMS_OFFLINE_ACCESSCONTROL (
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	PRINCIPAL_ID VARCHAR(36) BINARY NOT NULL,
	ACCESS_ALLOWED INT,
	ACCESS_DENIED INT,
	ACCESS_FLAGS INT,
	PRIMARY KEY (RESOURCE_ID, PRINCIPAL_ID),
	INDEX PRINCIPAL_ID_IDX (PRINCIPAL_ID)
);

CREATE TABLE CMS_PUBLISH_HISTORY (
	HISTORY_ID VARCHAR(36) BINARY NOT NULL,
	PUBLISH_TAG INT NOT NULL,
	STRUCTURE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH BLOB NOT NULL,
	RESOURCE_STATE INT NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,
	PRIMARY KEY (HISTORY_ID, PUBLISH_TAG, STRUCTURE_ID, RESOURCE_ID),
	INDEX PUBLISH_TAG_IDX (PUBLISH_TAG)
);

CREATE TABLE CMS_RESOURCE_LOCKS (
  RESOURCE_PATH TEXT BINARY NOT NULL,
  USER_ID varchar(36) NOT NULL default '',
  PROJECT_ID INT NOT NULL default 0,
  LOCK_TYPE INT NOT NULL default 0
);


CREATE TABLE CMS_STATICEXPORT_LINKS (
	LINK_ID VARCHAR(36) BINARY NOT NULL,
	LINK_RFS_PATH BLOB NOT NULL,
	LINK_TYPE INT NOT NULL,
	LINK_PARAMETER TEXT,
	LINK_TIMESTAMP BIGINT,	
	PRIMARY KEY (LINK_ID),
	INDEX LINK_RFS_PATH_IDX (LINK_RFS_PATH(255))	
);

CREATE TABLE CMS_OFFLINE_STRUCTURE (
	STRUCTURE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	PARENT_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH BLOB NOT NULL,
	STRUCTURE_STATE SMALLINT UNSIGNED NOT NULL,
	DATE_RELEASED BIGINT NOT NULL,
	DATE_EXPIRED BIGINT NOT NULL,
	PRIMARY KEY (STRUCTURE_ID),
	INDEX STRUCTURE_ID_RESOURCE_PATH_IDX (STRUCTURE_ID, RESOURCE_PATH(255)),	
	INDEX RESOURCE_PATH_RESOURCE_ID_IDX (RESOURCE_PATH(255), RESOURCE_ID),
	INDEX STRUCTURE_ID_RESOURCE_ID_IDX (STRUCTURE_ID, RESOURCE_ID),
	INDEX STRUCTURE_STATE_IDX (STRUCTURE_STATE),
	INDEX PARENT_ID_IDX (PARENT_ID),
	INDEX RESOURCE_PATH_IDX (RESOURCE_PATH(255)),
	INDEX RESOURCE_ID_IDX (RESOURCE_ID)
);

CREATE TABLE CMS_ONLINE_STRUCTURE (
	STRUCTURE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	PARENT_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH BLOB NOT NULL,
	STRUCTURE_STATE SMALLINT UNSIGNED NOT NULL,
	DATE_RELEASED BIGINT NOT NULL,
	DATE_EXPIRED BIGINT NOT NULL,
	PRIMARY KEY (STRUCTURE_ID),
	INDEX STRUCTURE_ID_RESOURCE_PATH_IDX (STRUCTURE_ID, RESOURCE_PATH(255)),	
	INDEX RESOURCE_PATH_RESOURCE_ID_IDX (RESOURCE_PATH(255), RESOURCE_ID),
	INDEX STRUCTURE_ID_RESOURCE_ID_IDX (STRUCTURE_ID, RESOURCE_ID),
	INDEX STRUCTURE_STATE_IDX (STRUCTURE_STATE),
	INDEX PARENT_ID_IDX (PARENT_ID),
	INDEX RESOURCE_PATH_IDX (RESOURCE_PATH(255)),
	INDEX RESOURCE_ID_IDX (RESOURCE_ID)
);

CREATE TABLE CMS_BACKUP_STRUCTURE (
	BACKUP_ID VARCHAR(36) BINARY NOT NULL,
	PUBLISH_TAG INT NOT NULL,
	VERSION_ID INT NOT NULL,
	STRUCTURE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_PATH BLOB NOT NULL,
	STRUCTURE_STATE SMALLINT UNSIGNED NOT NULL,
	DATE_RELEASED BIGINT NOT NULL,
	DATE_EXPIRED BIGINT NOT NULL,
	PRIMARY KEY (BACKUP_ID),
	INDEX STRUCTURE_ID_RESOURCE_PATH_IDX (STRUCTURE_ID, RESOURCE_PATH(255)),	
	INDEX RESOURCE_PATH_RESOURCE_ID_IDX (RESOURCE_PATH(255), RESOURCE_ID),
	INDEX STRUCTURE_ID_RESOURCE_ID_IDX (STRUCTURE_ID, RESOURCE_ID),
	INDEX STRUCTURE_STATE_IDX (STRUCTURE_STATE),
	INDEX RESOURCE_ID_IDX (RESOURCE_ID),
    INDEX RESOURCE_PATH_IDX (RESOURCE_PATH(255)),
	INDEX PUBLISH_TAG_IDX (PUBLISH_TAG),
	INDEX VERSION_ID_IDX (VERSION_ID)	
);

CREATE TABLE CMS_OFFLINE_RESOURCES (
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	RESOURCE_FLAGS INT NOT NULL,
	RESOURCE_STATE	SMALLINT UNSIGNED NOT NULL,
	RESOURCE_SIZE INT NOT NULL,                                         
	SIBLING_COUNT INT NOT NULL,
	DATE_CREATED BIGINT NOT NULL,
	DATE_LASTMODIFIED BIGINT NOT NULL,
	USER_CREATED VARCHAR(36) BINARY NOT NULL,                                         
	USER_LASTMODIFIED VARCHAR(36) BINARY NOT NULL,
	PROJECT_LASTMODIFIED SMALLINT UNSIGNED NOT NULL,          
	PRIMARY KEY(RESOURCE_ID),
	INDEX PROJECT_LASTMODIFIED_IDX (PROJECT_LASTMODIFIED),
	INDEX PROJECT_LASTMODIFIED_RESOURCE_SIZE_IDX (PROJECT_LASTMODIFIED, RESOURCE_SIZE),
	INDEX RESOURCE_SIZE_IDX (RESOURCE_SIZE),
	INDEX DATE_LASTMODIFIED_IDX (DATE_LASTMODIFIED),
	INDEX RESOURCE_TYPE_IDX (RESOURCE_TYPE)
);

CREATE TABLE CMS_ONLINE_RESOURCES (
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	RESOURCE_FLAGS INT NOT NULL,
	RESOURCE_STATE	SMALLINT UNSIGNED NOT NULL,
	RESOURCE_SIZE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,	
	DATE_CREATED BIGINT NOT NULL,
	DATE_LASTMODIFIED BIGINT NOT NULL,
	USER_CREATED VARCHAR(36) BINARY NOT NULL,                                         
	USER_LASTMODIFIED VARCHAR(36) BINARY NOT NULL,
	PROJECT_LASTMODIFIED SMALLINT UNSIGNED NOT NULL,
	PRIMARY KEY(RESOURCE_ID),
	INDEX PROJECT_LASTMODIFIED_IDX (PROJECT_LASTMODIFIED),
	INDEX PROJECT_LASTMODIFIED_RESOURCE_SIZE_IDX (PROJECT_LASTMODIFIED, RESOURCE_SIZE),
	INDEX RESOURCE_SIZE_IDX (RESOURCE_SIZE),
	INDEX DATE_LASTMODIFIED_IDX (DATE_LASTMODIFIED),
	INDEX RESOURCE_TYPE_IDX (RESOURCE_TYPE)
);
                                         
CREATE TABLE CMS_BACKUP_RESOURCES (
	BACKUP_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	RESOURCE_FLAGS INT NOT NULL,
	RESOURCE_STATE	SMALLINT UNSIGNED NOT NULL,
	RESOURCE_SIZE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,	
	DATE_CREATED BIGINT NOT NULL,
	DATE_LASTMODIFIED BIGINT NOT NULL,
	USER_CREATED VARCHAR(36) BINARY NOT NULL,
	USER_LASTMODIFIED VARCHAR(36) BINARY NOT NULL,
	PROJECT_LASTMODIFIED SMALLINT UNSIGNED NOT NULL,
	PUBLISH_TAG INT NOT NULL,
	VERSION_ID INT NOT NULL,
	USER_CREATED_NAME VARCHAR(128) NOT NULL,
	USER_LASTMODIFIED_NAME VARCHAR(128) NOT NULL,
	PRIMARY KEY(BACKUP_ID),
	UNIQUE INDEX PUBTAG_RESOURCE_IDX (PUBLISH_TAG, RESOURCE_ID),
	INDEX RESOURCE_ID_IDX (RESOURCE_ID),
	INDEX PROJECT_LASTMODIFIED_IDX (PROJECT_LASTMODIFIED),
	INDEX PROJECT_LASTMODIFIED_RESOURCE_SIZE_IDX (PROJECT_LASTMODIFIED, RESOURCE_SIZE),
	INDEX RESOURCE_SIZE_IDX (RESOURCE_SIZE),
	INDEX DATE_LASTMODIFIED_IDX (DATE_LASTMODIFIED),
	INDEX RESOURCE_TYPE_IDX (RESOURCE_TYPE),
	INDEX PUBLISH_TAG_IDX (PUBLISH_TAG)
);
                                         
CREATE TABLE CMS_OFFLINE_CONTENTS (
	CONTENT_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	FILE_CONTENT LONGBLOB NOT NULL,
	PRIMARY KEY(CONTENT_ID),
	UNIQUE INDEX RESOURCE_ID_IDX (RESOURCE_ID)
);

CREATE TABLE CMS_ONLINE_CONTENTS (
	CONTENT_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	FILE_CONTENT LONGBLOB NOT NULL,
	PRIMARY KEY(CONTENT_ID),
	UNIQUE INDEX RESOURCE_ID_IDX (RESOURCE_ID)
);

CREATE TABLE CMS_BACKUP_CONTENTS (
	BACKUP_ID VARCHAR(36) BINARY NOT NULL,
	CONTENT_ID VARCHAR(36) BINARY NOT NULL,
	RESOURCE_ID VARCHAR(36) BINARY NOT NULL,
	FILE_CONTENT LONGBLOB NOT NULL,
	PUBLISH_TAG INT,
	VERSION_ID INT NOT NULL,
	PRIMARY KEY(BACKUP_ID),
	INDEX CONTENT_ID_IDX (CONTENT_ID),
	INDEX RESOURCE_ID_IDX (RESOURCE_ID),
	INDEX PUBLISH_TAG_IDX (PUBLISH_TAG)
);

CREATE TABLE CMS_ONLINE_RESOURCE_RELATIONS (
	RELATION_SOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RELATION_SOURCE_PATH BLOB NOT NULL,
	RELATION_TARGET_ID VARCHAR(36) BINARY NOT NULL,
	RELATION_TARGET_PATH BLOB NOT NULL,
	RELATION_DATE_BEGIN BIGINT NOT NULL,
	RELATION_DATE_END BIGINT NOT NULL,
	RELATION_TYPE INT NOT NULL,
	INDEX SOURCE_ID_IDX (RELATION_SOURCE_ID),
	INDEX SOURCE_PATH_IDX (RELATION_SOURCE_PATH(255)),
	INDEX TARGET_ID_IDX (RELATION_TARGET_ID),
	INDEX TARGET_PATH_IDX (RELATION_TARGET_PATH(255))
);

CREATE TABLE CMS_OFFLINE_RESOURCE_RELATIONS (
	RELATION_SOURCE_ID VARCHAR(36) BINARY NOT NULL,
	RELATION_SOURCE_PATH BLOB NOT NULL,
	RELATION_TARGET_ID VARCHAR(36) BINARY NOT NULL,
	RELATION_TARGET_PATH BLOB NOT NULL,
	RELATION_DATE_BEGIN BIGINT NOT NULL,
	RELATION_DATE_END BIGINT NOT NULL,
	RELATION_TYPE INT NOT NULL,
	INDEX SOURCE_ID_IDX (RELATION_SOURCE_ID),
	INDEX SOURCE_PATH_IDX (RELATION_SOURCE_PATH(255)),
	INDEX TARGET_ID_IDX (RELATION_TARGET_ID),
	INDEX TARGET_PATH_IDX (RELATION_TARGET_PATH(255))
);