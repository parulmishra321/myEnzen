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

package com.enzen.hes.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.UUID;


@Service
public final class CommonUtils {

    private static final Logger LOG = LogManager.getLogger();
    /**
     * It is used to get the time in millisecond.
     *
     * @return it returns the time in mills which of long type.
     */
    public static long getCurrentTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }
    /**
     * It is used to get the local language of the place where client belongs
     *
     * @param httpServletRequest it helps in getting the language which is used at the place of client
     * @return it returns the local language which of type string.
     */
    public static String getLocaleStringFromClientHTTPRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getLocale().getLanguage();
    }
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
