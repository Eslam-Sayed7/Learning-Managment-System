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
    @GetMapping("/{studentId}/chart")
    public String getProgressChart(@PathVariable Long studentId) {

        ProgressTrackingDTO pg = new ProgressTrackingDTO();
        pg = progressTrackingService.getProgressByStudentId(studentId);

        long donePercentage = Math.round((pg.getAttendancePercentage()));
        long notYetPercentage = 100 - donePercentage;

        String baseUrl = "https://image-charts.com/chart";
        String params = String.format("cht=p&chd=t:%d,%d&chl=Done|Not Yet&chs=700x400&chtt=Progress Tracking",
                donePercentage, notYetPercentage);

        return baseUrl + "?" + params;
    }

}
