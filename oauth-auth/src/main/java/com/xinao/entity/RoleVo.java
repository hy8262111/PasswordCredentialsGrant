package com.xinao.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 2179037393108205286L;
    private Integer id;

    private String name;

    private String value;

    private Timestamp createTime;


}
