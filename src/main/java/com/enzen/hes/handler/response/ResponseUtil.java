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

import com.enzen.hes.localization.MessageByLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class is used as standard response whenever API executed Successfully.
 * This class contains multiple methods for showing standard response on successful execution or if any error will occur.
 * <br><br>{@link #ok()} This method returns the message and the data on successful execution.
 * <br><br>{@link #ok(Object)} This method returns the message and the data on successful execution where data will be set with the help of object passed in parameter.
 * <br><br>{@link #ok(ResponseCode)} This method returns the message and the data on successful execution where message will be set with the help of object of ResponseCode class passed in parameter.
 * <br><br>{@link #ok(Object,ResponseCode)} This method returns the message and the data on successful execution where message will be set with the help of object of ResponseCode class and data will be set with the help of object passed in parameter.
 * <br><br>{@link #ok(Object,ResponseCode,String)} This method returns the message and the data on successful execution where message will be set with the help of object of ResponseCode class and data will be set with the help of object passed in parameter.
 * <br><br>{@link #ok(ResponseCode,String)} This method returns the message and the data on successful execution where message will be set with the help of object of ResponseCode class and the locale of string type passed in parameter.
 * <br><br>{@link #exception(int)} This method returns the message, error code and the data when exception occurs where code will be set with the help of parameter passed in this method.
 * <br><br>{@link #exception(int,String)} This method returns the message, error code and the data when exception occurs where code and message will be set with the help of parameter passed in this method.
 * <br><br>{@link #exception(ResponseCode)} This method returns the message, error code and the data when exception occurs where code and message will be set with the help of object of ResponseCode class passed in parameter.
 * <br><br>{@link #validationFailed(int,String)} This method returns the message, error code and the data when exception occurs where code and message will be set with the help of parameter passed in this method.
 * <br><br>{@link #validationFailed(int,ValidationErrorDTO)} This method returns the message, and error code when exception occurs where code will be set with the help of parameter passed in this method.
 */
@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ResponseUtil {

    private static final Map data = new HashMap();
    public static ResponseUtil responseUtil;

    @Autowired
    private MessageByLocale messageByLocale;
//    @Autowired
//    private ResponseUtil util;

    public static ResponseUtil getBean() {
        return responseUtil;
    }

    public Locale getLocale(String locale) {
        return locale != null ? new Locale(locale) : Locale.UK;
    }

    @PostConstruct
    private void init() {
        responseUtil = new ResponseUtil();
    }

    /**
     * This method sets the values in message and the data.
     * @return the anonymous object with parameter message and data.
     */
    public ResponseDTO ok() {
        String message = messageByLocale.getMessage(String.valueOf(ApiResponseCode.SUCCESS.getCode()));
        return new ApiResponseDTO(message, data);
    }

    /**
     * This method sets the values in message and the data.
     * @param data it's a object which consist of some information.
     * @return the anonymous object with parameter message and data.
     */
    public ResponseDTO ok(Object data) {
        String message = messageByLocale.getMessage(String.valueOf(ApiResponseCode.SUCCESS.getCode()));
        return new ApiResponseDTO(message, data);
    }

    /**
     * This method sets the values in message and the data.
     * @param responseCode it's a object which helps in setting up the value of message.
     * @return the anonymous object with parameter message and data.
     */
    public ResponseDTO ok(ResponseCode responseCode) {
        String message = messageByLocale.getMessage(String.valueOf(responseCode.getCode()));
        return new ApiResponseDTO(message, data);
    }

    /**
     * This method sets the values in message and the data.
     * @param data it's a object which consist of some information.
     * @param responseCode it's a object which helps in setting up the value of message.
     * @return the anonymous object with parameter message and data.
     */
    public ResponseDTO ok(Object data, ResponseCode responseCode) {
        String message = messageByLocale.getMessage(String.valueOf(responseCode.getCode()));
        return new ApiResponseDTO(message, data);
    }

    /**
     * This method sets the values in message and the data.
     * @param data it's a object which consist of some information.
     * @param responseCode it's a object which helps in setting up the value of message.
     * @param locale it is used for setting up the country code.
     * @return the anonymous object with parameter message and data.
     */
    public ResponseDTO ok(Object data, ResponseCode responseCode, String locale) {
        String message = messageByLocale.getMessage(String.valueOf(responseCode.getCode()));
        return new ApiResponseDTO(message, data);
    }

    /**
     * This method sets the values in message and the data.
     * @param responseCode it's a object which helps in setting up the value of message.
     * @param locale it is used for setting up the country code.
     * @return the anonymous object with parameter message and data.
     */
    public ResponseDTO ok(ResponseCode responseCode, String locale) {
        String message = messageByLocale.getMessage(String.valueOf(responseCode.getCode()));
        return new ApiResponseDTO(message, data);
    }

    /**
     * This method sets the values in code, message and the data.
     * @param code it's a error code which is of integer type.
     * @return the anonymous object with parameter code,message and data.
     */
    public ResponseDTO exception(int code) {
        String message = messageByLocale.getMessage(String.valueOf(code));
        return new ApiResponseDTO(code, message, data);
    }

    /**
     * This method sets the values in code, message and the data.
     * @param code it's a error code which is of integer type.
     * @param message it's a error message which is of String type.
     * @return the anonymous object with parameter code,message and data.
     */
    public ResponseDTO exception(int code, String message) {
        return new ApiResponseDTO(code, message, data);
    }

    /**
     * This method sets the values in code, message and the data.
     * @param responseCode it's a object which helps in setting up the value of message and the code.
     * @return the anonymous object with parameter code,message and data.
     */
    public ResponseDTO exception(ResponseCode responseCode) {
        String message = messageByLocale.getMessage(String.valueOf(responseCode.getCode()));
        return new ApiResponseDTO(responseCode.getCode(), message, data);
    }

    /**
     * This method sets the values in code, message and the data.
     * @param code it's a error code which is of integer type.
     * @param message it's a error message which is of String type.
     * @return the anonymous object with parameter code,message and data.
     */
    public ResponseDTO validationFailed(int code, String message) {
        return new ApiResponseDTO(code, message, data);
    }

    /**
     * This method sets the values in code, message and the data.
     * @param code it's a error code which is of integer type.
     * @param validationErrorDTO it's a object which helps in setting up the error message.
     * @return the anonymous object with parameter code,message and data.
     */
    public ResponseDTO validationFailed(int code, ValidationErrorDTO validationErrorDTO) {
        return new ApiResponseDTO(code, "Missing / Invalid Parameter(s)", validationErrorDTO.getFieldErrors());
    }

    /**
     * This method sets the values in code, message and the data.
     * @param code it's a error code which is of integer type.
     * @param errorResponse it's a object which helps in setting up the error message.
     * @return the anonymous object with parameter code,message and data.
     */
    public ResponseDTO validationFailed(int code, ValidationErrorResponse errorResponse) {
        return new ApiResponseDTO(code, "Missing / Invalid Parameter(s)", errorResponse.getErrorMessages());
    }

}
