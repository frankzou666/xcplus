package com.example.xcpluscontentmodel.content.model.dto;

/*


 */

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryCourseParamsDto {

    private String auditStatus ;
    private String courseName ;
    private String publishStatus ;

    public QueryCourseParamsDto() {
    }

    public QueryCourseParamsDto(String auditStatus, String courseName, String publishStatus) {
        this.auditStatus = auditStatus;
        this.courseName = courseName;
        this.publishStatus = publishStatus;
    }


}
