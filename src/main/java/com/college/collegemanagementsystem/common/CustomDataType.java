package com.college.collegemanagementsystem.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomDataType {
    private List<String> stringList;
    private Object object;
}
