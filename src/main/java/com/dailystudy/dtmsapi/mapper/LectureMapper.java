package com.dailystudy.dtmsapi.mapper;

import com.dailystudy.dtmsapi.dto.LectureDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LectureMapper {

    List<LectureDto> selectLectureList();

    LectureDto selectLecture(Integer isnumber);

}
