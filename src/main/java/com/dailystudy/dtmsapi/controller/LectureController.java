package com.dailystudy.dtmsapi.controller;

import com.dailystudy.dtmsapi.dto.LectureDto;
import com.dailystudy.dtmsapi.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("all")
    public List<LectureDto> selectLectureList() throws Exception {
        log.info("123123333333333333333333333333333");
        return lectureService.selectLectureList();
    }

    @GetMapping("attendance/{isnumber}")
    public LectureDto selectLecture(@PathVariable("isnumber") Integer isnumber) {
        return lectureService.selectLecture(isnumber);
    }

}
