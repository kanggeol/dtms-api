package com.dailystudy.dtmsapi.service;

import com.dailystudy.dtmsapi.base.DbList;
import com.dailystudy.dtmsapi.base.LogManager;
import com.dailystudy.dtmsapi.base.RequestMap;
import com.dailystudy.dtmsapi.base.ResultMap;
import com.dailystudy.dtmsapi.mapper.LectureMapper;
import com.dailystudy.dtmsapi.model.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureMapper lectureMapper;

    public ResultMap selectLectureList() {
        ResultMap resultMap = ResultMap.create();
        resultMap.put("resultData", lectureMapper.selectLectureList());
        return resultMap;
    }

//    public ResultMap selectLectureList(HttpServletRequest request) throws Exception {
//        RequestMap requestMap = RequestMap.create(request);
//        LogManager.requestLog(this.getClass(), "selectLectureList", requestMap);
//
//        ResultMap resultMap = ResultMap.create();
//        resultMap.setResultData(lectureMapper.selectLectureList());
//        LogManager.responseLog(this.getClass(), "selectLectureList", resultMap);
//
//        return resultMap;
//    }

    public Lecture selectLecture(Integer isnumber) {
        return lectureMapper.selectLecture(isnumber);
    }
}
