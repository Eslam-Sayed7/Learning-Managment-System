package com.Java.LMS.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Service
public class GradeService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public boolean calculateGrade(int submissionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("submissionId", submissionId);

        Boolean result = jdbcTemplate.execute(
                "SELECT calculate_grade(:submissionId)",
                params,
                (PreparedStatementCallback<Boolean>) ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return rs.getBoolean(1);
                    }
                    return false;
                }
        );

        return result;
    }
}