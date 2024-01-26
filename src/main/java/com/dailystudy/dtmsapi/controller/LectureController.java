package com.dailystudy.dtmsapi.controller;

import com.dailystudy.dtmsapi.base.ResultMap;
import com.dailystudy.dtmsapi.model.Lecture;
import com.dailystudy.dtmsapi.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/lecture")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("all")
//    public ResultMap selectLectureList() {
//        return lectureService.selectLectureList();
//    }
    public ResultMap selectLectureList(HttpServletRequest request) throws Exception {
        return lectureService.selectLectureList();
    }

    @GetMapping("attendance/{isnumber}")
    public Lecture selectLecture(@PathVariable("isnumber") Integer isnumber) {
        return lectureService.selectLecture(isnumber);
    }

}
