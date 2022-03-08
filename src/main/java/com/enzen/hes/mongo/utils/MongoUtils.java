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
package com.enzen.hes.mongo.utils;

import org.bson.Document;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to perform validations.
 */
public class MongoUtils {

    public static boolean isNullOrEmpty(Document document) {
        return ((Objects.isNull(document) || document.keySet().size() == 0));
    }

    public static boolean nonNullAndEmpty(String str) {
        return !(Objects.isNull(str) || "".equals(str));
    }

    public static boolean nonNullAndEmpty(Document document) {
        return !((Objects.isNull(document) || document.isEmpty()));
    }

    public static boolean validateKeys(Document document, List<String> keys) {
         return keys.stream().allMatch(document::containsKey);
    }

    public static long getCurrentTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }


}
