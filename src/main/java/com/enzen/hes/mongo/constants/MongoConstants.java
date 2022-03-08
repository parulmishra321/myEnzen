/* **********************************************************************
 * 83incs CONFIDENTIAL
 **********************************************************************
 *
 *  [2017] - [2022] 83incs Ltd.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of 83incs Ltd, IoT83 Ltd, its suppliers (if any), its subsidiaries (if any) and
 * Source Code Licensees (if any).  The intellectual and technical concepts contained
 * herein are proprietary to 83incs Ltd, IoT83 Ltd, its subsidiaries (if any) and
 * Source Code Licensees (if any) and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from 83incs Ltd or IoT83 Ltd.
 ****************************************************************************
 */
package com.enzen.hes.mongo.constants;

public interface MongoConstants {
    String MONGODB_SERVER_URL = "mongodb.server.url";
    String MONGODB_SERVER = "mongodb.server";
    String MONGODB_PORT = "mongodb.port";
    String MONGODB_DATABASE_NAME = "mongodb.database.name";
    String MONGODB_DATABASE_USERNAME = "mongodb.database.username";
    String MONGODB_DATABASE_PASSWORD = "mongodb.database.password";
    String MONGODB_AUTH_ENABLED = "mongodb.auth.enabled";
    String MONGODB_DB_USER_ADMIN = "mongodb.database.username.admin"; //admin username (with root role)
    String MONGODB_DB_PWD_ADMIN = "mongodb.database.password.admin";  //admin password (with root role)
    Integer MYSQL_QUERY_LOG_MAX_TIME = 300;
    Integer MONGODB_QUERY_LOG_MAX_TIME = 200;
    String MONGO_CONFIG = "mongo.config";
    String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    String _READ = "_read";
    String _READ_WRITE = "_readWrite";
    String ADMIN_DB = "admin";


    String PUSH = "$push";
    String PULL = "$pull";
    String INCREMENT = "$inc";
    String ADD_TO_SET = "$addToSet";
    String SET = "$set";
    String UNSET = "$unset";
    String LESS_THAN_EQUAL = "$lte";
    String GREATER_THAN_EQUAL = "$gte";
    String LESS_THAN = "$lt";
    String GREATER_THAN = "$gt";
    String MINIMUM = "$min";
    String MAXIMUM = "$max";

    String SERVER_PROPERTIES_PATH = "/root/setup/config/application.properties";
    String DEV = "dev";
    String SPRING_CONFIG_LOCATION = "spring.config.location";
    String _NOT_BLANK = " Can Not Be Blank !!!";
    String CONSUL_CONNECT_ENABLED="consul.connect.enabled";

    public static final String _ID = "_id";
    public static final String ENTITY_TEMPLATE_COLLECTION = "Entity_Template";
    public static final String DEVICE_TEMPLATE_COLLECTION = "Device_Template";
    public static final String ASSET_TEMPLATE_COLLECTION = "Asset_Template";
    public static final String ORG_TEMPLATE_COLLECTION = "Org_Template";
    public static final String DEVICE_COLLECTION = "Device_Details";
    public static final String ASSET_COLLECTION = "Asset_Details";
    public static final String ORG_COLLECTION = "Org_Details";
    public static final String ZONE_COLLECTION = "Zone_Details";
    public static final String PROJECT_COLLECTION = "Project_Details";
    public static final String MANUFACTURER_COLLECTION = "Manufacturer_Details";
    public static final String UTF8 = "UTF8";
}
