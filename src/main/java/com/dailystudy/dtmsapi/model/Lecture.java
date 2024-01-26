package com.dailystudy.dtmsapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    private Integer isnumber;
    private String isname;
    private String teacher;
    private String level;
}
