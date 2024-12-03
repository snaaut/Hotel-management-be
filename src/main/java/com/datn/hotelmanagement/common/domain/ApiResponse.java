package com.datn.hotelmanagement.common.domain;

import java.util.HashMap;


public class ApiResponse extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** status code */
    public static final String CODE_TAG = "code";

    /** Return content */
    public static final String MSG_TAG = "msg";

    /** Data object */
    public static final String DATA_TAG = "data";

    /**
     * State type
     */
    public enum Type
    {
        /** success */
        SUCCESS(0),
        /** warning */
        WARN(301),
        /** error */
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    /**
     * Initialize a newly created ApiResponse object so that it represents an empty message.
     */
    public ApiResponse()
    {
    }

    /**
     * Initialize a newly created ApiResponse object
     *
     * @param type State type
     * @param msg Return content
     */
    public ApiResponse(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * Initialize a newly created ApiResponse object
     *
     * @param type State type
     * @param msg Return content
     * @param data Data object
     */
    public ApiResponse(Type type, String msg, Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (data != null && data != "")
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * Return success message
     *
     * @return Success message
     */
    public static ApiResponse success()
    {
        return ApiResponse.success("Thành công");
    }

    /**
     * Return success data
     *
     * @return Success message
     */
    public static ApiResponse success(Object data)
    {
        return ApiResponse.success("Thành công", data);
    }

    /**
     * Return success message
     *
     * @param msg Return content
     * @return Success message
     */
    public static ApiResponse success(String msg)
    {
        return ApiResponse.success(msg, null);
    }

    /**
     * Return success message
     *
     * @param msg Return content
     * @param data Data object
     * @return Success message
     */
    public static ApiResponse success(String msg, Object data)
    {
        return new ApiResponse(Type.SUCCESS, msg, data);
    }

    /**
     * Return warning message
     *
     * @param msg Return content
     * @return Warning message
     */
    public static ApiResponse warn(String msg)
    {
        return ApiResponse.warn(msg, null);
    }

    /**
     * Return warning message
     *
     * @param msg Return content
     * @param data Data object
     * @return Warning message
     */
    public static ApiResponse warn(String msg, Object data)
    {
        return new ApiResponse(Type.WARN, msg, data);
    }

    /**
     * Return error message
     *
     * @return
     */
    public static ApiResponse error()
    {
        return ApiResponse.error("Thất bại");
    }

    /**
     * Return error message
     *
     * @param msg Return content
     * @return Warning message
     */
    public static ApiResponse error(String msg)
    {
        return ApiResponse.error(msg, null);
    }

    /**
     * Return error message
     *
     * @param msg Return content
     * @param data Data object
     * @return Warning message
     */
    public static ApiResponse error(String msg, Object data)
    {
        return new ApiResponse(Type.ERROR, msg, data);
    }
}
