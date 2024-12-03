package com.datn.hotelmanagement.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Creator */
    private String createBy;

    /** Create time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /** updater */
    private String updateBy;

    /** Update Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** Remarks */
    private String remark;

    /** Request parameters */
    private Map<String, Object> params;
}
