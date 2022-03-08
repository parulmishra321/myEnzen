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

package com.enzen.hes.handler.exception;


import com.enzen.hes.handler.response.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to throw throw Api Exception.
 * <br><br>{@link #code} is used to give Api response code when throwing Api Exception.
 * <br><br>{@link #message} is used to give Exception message when throwing Api Exception.
 */

@Data
@NoArgsConstructor
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = -7788656746564343L;

    private int code;
    private String message;

    public ApiException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }
}
