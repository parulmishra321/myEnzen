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

package com.enzen.hes.handler.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.StringJoiner;

/**
 * This class is used as standard response for every API .
 * <br><br>{@link #code}  is used to give Api response code in ApiResponseDTO.
 * <br><br>{@link #message}  is used to give message in ApiResponseDTO.
 * <br><br>{@link #data} is used to provide Response data in ApiResponseDTO.
 */

@Data
@NoArgsConstructor
@JsonPropertyOrder({"code", "data", "message"})
@SuppressWarnings({"rawtypes", "unused"})
public class ApiResponseDTO<T> implements ResponseDTO<T> {

    private int code;
    private String message = "";
    private T data;

    private Locale getLocale(String locale) {
        return locale != null ? new Locale(locale) : Locale.UK;
    }

    public ApiResponseDTO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponseDTO(ResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    @Override
    public ResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }
}
