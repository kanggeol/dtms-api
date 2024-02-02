package com.dailystudy.dtmsapi.service;

import com.dailystudy.dtmsapi.mapper.LectureMapper;
import com.dailystudy.dtmsapi.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureMapper lectureMapper;

    public LectureDto selectLecture(Integer isnumber) {
        return lectureMapper.selectLecture(isnumber);
    }

    public List<LectureDto> selectLectureList() {
        return lectureMapper.selectLectureList();
    }
}
