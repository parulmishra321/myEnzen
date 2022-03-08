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
package com.enzen.hes.mongo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to get mongodb connection details like mongodbServerUrl, authEnabled, userName, password,server,
 * port, databaseName from consul server.
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties
public class MongoConfig {
    @Value("${mongodb.database.username}")
    private String mongodbDatabaseUsernameAdmin;
    @Value("${mongodb.database.password}")
    private String mongodbDatabasePasswordAdmin;
    @Value("${mongodb.server}")
    private String mongodbServer;
    @Value("${mongodb.port:27017}")
    private String mongodbPort;
    @Value("${mongodb.database:enzen}")
    private String mongodbDatabaseName;
}
