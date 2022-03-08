package com.enzen.hes.utils;
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

import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.handler.response.ValidationErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.enzen.hes.constants.ApplicationConstants.EMPTY_STRING;


/**
 * This class consist of various method to check various types of validation.
 * <br><br>{@link #nonNullOrEmpty(String)} this method is used to check whether the passed variable has value or not
 * <br><br>{@link #isNullOrEmpty(Object)} this method is used to check whether the obj is null/empty or not
 * <br><br>{@link #nonNullOrEmpty(Object)} This method is used to check whether the passed obj has value or not
 * <br><br>{@link #isAnyNullOrEmpty(List)} This method is used to check whether any list is null/empty or not
 * <br><br>{@link #isNullAny(Object...)} This method is used to check whether any object is null or not
 * <br><br>{@link #isNullOrEmptyAny(String...)} This method is used to check whether any string is null/empty or not
 * <br><br>{@link #isNullOrEmptyAny(List...)} This method is used to check whether any list is null/empty or not
 * <br><br>{@link #isNullString(String)} This method is used to check whether data is null or not
 * <br><br>{@link #validateMAC(String)} This method is used to check whether mac matches the pattern
 * <br><br>{@link #isContainSpecialCharacter(String)} This method is used to check whether charSequenece contains any special character or not
 * <br><br>{@link #isValidURL(String)} This method is used to check whether url matches the pattern
 * <br><br>{@link #isValidMqttURL(String)} This method is used to check whether url matches the Mqtt url pattern
 * <br><br>{@link #isValidEmail(String)} This method is used to check whether email matches the email pattern
 * <br><br>{@link #validateIpAddress(String)} This method is used to check whether ip matches the ip address pattern
 * <br><br>{@link #nonNullOrEmpty(Collection)} This method is used to check whether the passed collection has value or not
 * <br><br>{@link #isNullOrEmpty(Map)} This method is used to check whether the map is null/empty or not
 * <br><br>{@link #nonNullOrEmpty(Map)} This method is used to check whether the passed map has value or not
 * <br><br>{@link #validateEmail(String)} This method is used to check whether email matches the regex
 * <br><br>{@link #hasErrors(Errors, HttpServletRequest)} This method is used to check whether errors has any error
 * <br><br>{@link #isNull(Object, HttpServletRequest, String)} This method is used to check whether object is null or having the empty string
 * <br><br>{@link #isRecurringScheduleEmpty(Object, HttpServletRequest, String)} This method is used to check whether object is empty or not
 * <br><br>{@link #validDaysOfTheWeek(HashSet, HttpServletRequest, String, String)} This method is used to check whether the allowedValues is contains the providedDay in uppercase or not
 */
@Component
@SuppressWarnings({"unused", "rawtypes"})
public final class ValidationUtils {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MAC_PATTERN = "^[a-fA-F0-9:]{17}|[a-fA-F0-9]{12}$";
    private static final String URL_PATTERN = "^(http|https)://.*$";
    private static final String MQTT_URL_PATTERN = "^(tcp|ssl)://.*$";
    private static final Map data = new HashMap();
    private static Pattern pattern;
    private static Matcher matcher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ResponseUtil responseUtil;

    /**
     * This method is used to check whether the passed variable has value or not
     *
     * @param str it is the string type variable
     * @return true or false
     */
    public static boolean nonNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * This method is used to check whether the obj is null/empty or not
     *
     * @param obj it is the obj of class
     * @return true or false
     */
    public static boolean isNullOrEmpty(Object obj) {
        return Objects.isNull(obj) || obj.equals(EMPTY_STRING) || (obj instanceof Map && ((Map) obj).isEmpty());
    }

    /**
     * This method is used to check whether the passed obj has value or not
     *
     * @param obj it is the obj of class
     * @return true or false
     */
    public static boolean nonNullOrEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }

    /**
     * This method is used to check whether any list is null/empty or not
     *
     * @param list it is of List type
     * @return true or false
     */
    public static boolean isAnyNullOrEmpty(List list) {
        return list.stream().anyMatch(ValidationUtils::isNullOrEmpty);
    }

    /**
     * This method is used to check whether any object is null or not
     *
     * @param objects these are the objects of class
     * @return true or false
     */
    public static boolean isNullAny(Object... objects) {
        for (Object object : objects) {
            if (Objects.isNull(object)) return true;
        }
        return false;
    }

    /**
     * This method is used to check whether any string is null/empty or not
     *
     * @param strings these are of type String
     * @return true or false
     */
    public static boolean isNullOrEmptyAny(String... strings) {
        for (String str : strings) {
            if (isNullOrEmpty(str)) return true;
        }
        Object object = strings;
        return false;
    }

    /**
     * This method is used to check whether any list is null/empty or not
     *
     * @param lists these are of type List
     * @return true or false
     */
    public static boolean isNullOrEmptyAny(List... lists) {
        for (List list : lists) {
            if (isNullOrEmpty(list)) return true;
        }
        return false;
    }

    /**
     * This method is used to check whether data is null or not
     *
     * @param data it is of type String
     * @return true or false
     */
    public static String isNullString(String data) {
        return data.toLowerCase().equals("null") ? null : data;
    }

    /**
     * This method is used to check whether mac matches the pattern
     *
     * @param mac it is of type String
     * @return true or false
     */
    public static boolean validateMAC(final String mac) {
        pattern = Pattern.compile(MAC_PATTERN);
        matcher = pattern.matcher(mac);
        return matcher.matches();
    }

    /**
     * This method is used to check whether charSequenece contains any special character or not
     *
     * @param charSequenece it is of type String
     * @return true or false
     */
    public static boolean isContainSpecialCharacter(final String charSequenece) {
        pattern = Pattern.compile("[a-zA-Z0-9-]*");
        matcher = pattern.matcher(charSequenece);

        return matcher.matches();
    }

    /**
     * This method is used to check whether url matches the pattern
     *
     * @param url it is of type String
     * @return true or false
     */
    public static boolean isValidURL(String url) {
        pattern = Pattern.compile(URL_PATTERN);
        matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * This method is used to check whether url matches the Mqtt url pattern
     *
     * @param url it is of type String
     * @return true or false
     */
    public static boolean isValidMqttURL(String url) {
        pattern = Pattern.compile(MQTT_URL_PATTERN);
        matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * This method is used to check whether email matches the email pattern
     *
     * @param email it is of type String
     * @return true or false
     */
    public static boolean isValidEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * This method is used to check whether ip matches the ip address pattern
     *
     * @param ip it is of type String
     * @return true or false
     */
    public static boolean validateIpAddress(final String ip) {
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * This method is used to check whether the str is null/empty or not
     *
     * @param str it is of type String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    /**
     * This method is used to check whether the list is null/empty or not
     *
     * @param list it is of type List
     * @return true or false
     */
    public static boolean isNullOrEmpty(List list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    /**
     * This method is used to check whether the passed list has value or not
     *
     * @param list it is of type List
     * @return true or false
     */
    public static boolean nonNullOrEmpty(List list) {
        return !isNullOrEmpty(list);
    }

    /**
     * This method is used to check whether the collection is null/empty or not
     *
     * @param collection it is of type Collection
     * @return true or false
     */
    public static boolean isNullOrEmpty(Collection collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

    /**
     * This method is used to check whether the passed collection has value or not
     *
     * @param collection it is of type Collection
     * @return true or false
     */
    public static boolean nonNullOrEmpty(Collection collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * This method is used to check whether the map is null/empty or not
     *
     * @param map it is of type Map
     * @return true or false
     */
    public static boolean isNullOrEmpty(Map map) {
        return Objects.isNull(map) || map.isEmpty();
    }

    /**
     * This method is used to check whether the passed map has value or not
     *
     * @param map it is of type Map
     * @return true or false
     */
    public static boolean nonNullOrEmpty(Map map) {
        return !isNullOrEmpty(map);
    }

    /**
     * This method is used to check whether email matches the regex
     *
     * @param email it is of type String
     * @return true or false
     */
    public static boolean validateEmail(String email) {
        String regx = "^[A-Za-z0-9+_.-]+@(.+)$";
        Matcher matcher = Pattern.compile(regx).matcher(email);
        return matcher.matches();
    }

    /**
     * This method is used to check whether number matches the regex
     *
     * @param number it is of type String
     * @return true or false
     */
    public static boolean validatePhoneNumber(String number) {
        String regx = "[0-9]+";
        Matcher matcher = Pattern.compile(regx).matcher(number);
        return matcher.matches();
    }

    /**
     * This method is used to check whether errors has any error
     *
     * @param errors             it is object of Errors
     * @param httpServletRequest it is the object of HttpServletRequest
     */
    public void hasErrors(Errors errors, HttpServletRequest httpServletRequest) {
        if (errors.hasErrors()) {
            ValidationErrorDTO errorResponse = new ValidationErrorDTO();
            errors.getFieldErrors().forEach(field ->
                    errorResponse.addError(new ValidationErrorDTO.FieldErrorDTO(field.getField(),
                            messageSource.getMessage(field.getDefaultMessage(),
                                    null,
                                    responseUtil.getLocale(CommonUtils.getLocaleStringFromClientHTTPRequest(httpServletRequest))))));
            throw new ValidationException(ApiResponseCode.VALIDATION_FAILED.getCode(), errorResponse.toJSON());
        }
    }

    /**
     * This method is used to check whether errors has any error
     *
     * @param errors it is the object of Errors
     */
    public void hasErrors(Errors errors) {
        if (errors.hasErrors()) {
            ValidationErrorDTO errorResponse = new ValidationErrorDTO();
            errors.getFieldErrors().forEach(field -> errorResponse.addError(new ValidationErrorDTO.FieldErrorDTO(field.getField(), messageSource.getMessage(field.getDefaultMessage(), null, null))));
            throw new ValidationException(ApiResponseCode.VALIDATION_FAILED.getCode(), errorResponse.toJSON());
        }
    }

    /**
     * This method is used to check whether object is null or having the empty string
     *
     * @param object             it is the object of Object
     * @param httpServletRequest it is the object of HttpServletRequest
     * @param fieldValue         it is of type String
     */
    public void isNull(Object object, HttpServletRequest httpServletRequest, String fieldValue) {
        if (Objects.isNull(object) || object.toString().equals("")) {
            ValidationErrorDTO errorResponse = new ValidationErrorDTO();
            FieldError error = new FieldError(fieldValue, fieldValue, fieldValue + " cannot be empty or null");
            List<FieldError> l = new ArrayList<>();
            l.add(error);
            l.forEach(field ->
                    errorResponse.addError(new ValidationErrorDTO.FieldErrorDTO(field.getField(),
                            messageSource.getMessage(field.getDefaultMessage(),
                                    null,
                                    responseUtil.getLocale(CommonUtils.getLocaleStringFromClientHTTPRequest(httpServletRequest))))));
            throw new ValidationException(ApiResponseCode.VALIDATION_FAILED.getCode(), errorResponse.toJSON());
        }
    }

    /**
     * This method is used to check whether object is empty or not
     *
     * @param object             it is the object of Object
     * @param httpServletRequest it is the object of HttpServletRequest
     * @param fieldValue         it is of type String
     */
    public void isRecurringScheduleEmpty(Object object, HttpServletRequest httpServletRequest, String fieldValue) {
        if (((List) object).isEmpty()) {
            ValidationErrorDTO errorResponse = new ValidationErrorDTO();
            FieldError error = new FieldError(fieldValue, fieldValue, fieldValue + " cannot be empty or null if schedule is recurring");
            List<FieldError> l = new ArrayList<>();
            l.add(error);
            l.forEach(field ->
                    errorResponse.addError(new ValidationErrorDTO.FieldErrorDTO(field.getField(),
                            messageSource.getMessage(field.getDefaultMessage(),
                                    null,
                                    responseUtil.getLocale(CommonUtils.getLocaleStringFromClientHTTPRequest(httpServletRequest))))));
            throw new ValidationException(ApiResponseCode.VALIDATION_FAILED.getCode(), errorResponse.toJSON());
        }
    }

    /**
     * This method is used to check whether the allowedValues is contains the providedDay in uppercase or not
     *
     * @param allowedValues      it is of type HashSet
     * @param httpServletRequest it is the object of HttpServletRequest
     * @param providedDay        it is of type String
     * @param fieldValue         it is of type String
     */
    public void validDaysOfTheWeek(HashSet allowedValues, HttpServletRequest httpServletRequest, String providedDay, String fieldValue) {
        if (!allowedValues.contains(providedDay.toUpperCase())) {
            ValidationErrorDTO errorResponse = new ValidationErrorDTO();
            FieldError error = new FieldError(fieldValue, fieldValue, providedDay + " is not a valid day of the week, allowed values are SUN, MON, TUE, WED, THU, FRI, SAT");
            List<FieldError> l = new ArrayList<>();
            l.add(error);
            l.forEach(field ->
                    errorResponse.addError(new ValidationErrorDTO.FieldErrorDTO(field.getField(),
                            messageSource.getMessage(field.getDefaultMessage(),
                                    null,
                                    responseUtil.getLocale(CommonUtils.getLocaleStringFromClientHTTPRequest(httpServletRequest))))));
            throw new ValidationException(ApiResponseCode.VALIDATION_FAILED.getCode(), errorResponse.toJSON());
        }
    }
}
