package com.enzen.hes.handler.exception;
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


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to throw exception when signed in authority violates the parameter
 *  which was set to accept data
 * <br><br>{@link #code}  is used to give Api response code when throwing ValidationException Exception.
 * <br><br>{@link #message}  is used to give Exception message when throwing ValidationException Exception..
 *
 */

@Data
@NoArgsConstructor
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -6874215221854343L;

    private int code;
    private String message;

    public ValidationException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
