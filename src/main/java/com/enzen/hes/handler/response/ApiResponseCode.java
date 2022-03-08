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

public enum ApiResponseCode implements ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    BAD_REQUEST(2, "BAD_REQUEST"),
    NOT_A_VALID_REQUEST(3, "NOT_A_VALID_REQUEST"),
    AUTH_FAILED(4, "AUTH_FAILED"),
    TOKEN_ISSUER_MISMATCH(5, "TOKEN_ISSUER_MISMATCH"),
    INVALID_USERNAME_PASSWORD(6, "INVALID_USERNAME_PASSWORD"),
    TOKEN_EXPIRED(7, "TOKEN_EXPIRED"),
    RESOURCE_PERMISSION_DENIED(8, "RESOURCE_PERMISSION_DENIED"),
    RESOURCE_NOT_ALLOWED(9, "RESOURCE_NOT_ALLOWED"),
    PARENT_REFERENCE_FOUND(10, "PARENT_REFERENCE_FOUND"),
    FILE_IO_ERROR(11, "FILE_IO_ERROR"),
    FILE_IS_EMPTY(12, "FILE_IS_EMPTY"),
    FILE_UPLOAD_FAILED(13, "FILE_UPLOAD_FAILED"),
    OLD_PASSWORD_MISMATCH(14, "OLD_PASSWORD_MISMATCH"),
    VALIDATION_FAILED(15, "VALIDATION_FAILED"),
    USER_NOT_FOUND(16, "USER_NOT_FOUND"),
    ROLE_NOT_FOUND(17, "ROLE_NOT_FOUND"),
    COMPARTMENT_NOT_FOUND(18, "COMPARTMENT_NOT_FOUND"),
    REQUEST_NOT_FOUND(19, "REQUEST_NOT_FOUND"),
    RECORD_NOT_EXIST(20, "RECORD_NOT_EXIST"),
    INVALID_VERSION(21, "INVALID_VERSION"),
    INVALID_SUB_VERSION(22, "INVALID_SUB_VERSION"),
    VERSION_NOT_NUMERIC(23, "VERSION_NOT_NUMERIC"),
    DEFAULT_ACCOUNT_DEL_ATTEMPT(24, "DEFAULT_ACCOUNT_DEL_ATTEMPT"),
    PROMPT_ROLE_DELETION(25, "PROMPT_ROLE_DELETION"),
    PROMPT_COMPARTMENT_DELETION(26, "PROMPT_COMPARTMENT_DELETION"),
    MAIL_NOT_REGISTERED(29, "MAIL_NOT_REGISTERED"),
    RESOURCE_ALREADY_EXIST(30, "RESOURCE_ALREADY_EXIST"),
    SERVICE_NOT_EXIST(31, "SERVICE_NOT_EXIST"),
    UNKNOWN_DATABASE(32, "UNKNOWN_DATABASE"),
    DEVICE_NOT_FOUND(34, "UNKNOWN_DEVICE"),
    PROMPT_MENU_DELETION(33, "PROMPT_MENU_DELETION"),
    APPLICATION_NOT_FOUND(38, "APPLICATION_NOT_FOUND"),
    RESOURCE_EXCEED_EXCEPTION(47, "RESOURCE_EXCEED_EXCEPTION"),
    EMAIL_SEND_SCUESSFULLY(40, "EMAIL_SEND_SUCCESSFULLY");
    private int code;
    private String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
