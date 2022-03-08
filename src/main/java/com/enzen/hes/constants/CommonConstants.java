package com.enzen.hes.constants;
/* ***********************************************************************
 * 83incs CONFIDENTIAL
 * ***********************************************************************
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


import java.text.DecimalFormat;

public interface CommonConstants {
    DecimalFormat TWO_DECIMAL_PLACE = new DecimalFormat("#.##");
    int ZERO = 0;
    String NOT_A_NUMBER = "NaN";
    String TODAY = "Today";
    String LAST_7_DAYS = "Last 7 days";
    String LAST_30_DAYS = "Last 30 days";
    String LAST_90_DAYS = "Last 90 days";
    String DAY = "Day";
    String TIMESTAMP = "timestamp";
    String DATE_CREATED = "dateCreated";
    String CREATED_AT = "createdAt";
    Integer MONGODB_QUERY_LOG_MAX_TIME = 200;
    String LAST_3_DAYS = "Last 3 days";
    String START_TIME_END_TIME = "startTime&EndTime";
    String DATE = "date";
}
