package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.ProgressTracking;
import com.Java.LMS.platform.service.dto.ProgressTrackingDTO;
import com.Java.LMS.platform.service.impl.ProgressTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress-tracking")
public class ProgressTrackingController {

    @Autowired
    private ProgressTrackingService progressTrackingService;

//    @GetMapping("/student/{studentId}")
//    public List<ProgressTracking> getProgressByStudentId(@PathVariable Long studentId) {
//        return progressTrackingService.getProgressByStudentId(studentId);
//    }


    @GetMapping("/{studentId}")
    public ProgressTrackingDTO getProgress(@PathVariable Long studentId){
        return progressTrackingService.getProgressByStudentId(studentId);
    }

}
