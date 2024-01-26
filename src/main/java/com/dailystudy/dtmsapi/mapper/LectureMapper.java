package com.dailystudy.dtmsapi.mapper;

import com.dailystudy.dtmsapi.base.DbList;
import com.dailystudy.dtmsapi.base.DbMap;
import com.dailystudy.dtmsapi.model.Lecture;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface LectureMapper {

    DbList selectLectureList();

    Lecture selectLecture(Integer isnumber);

}
