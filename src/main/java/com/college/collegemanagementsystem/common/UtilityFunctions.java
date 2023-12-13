package com.college.collegemanagementsystem.common;

import org.apache.tomcat.util.file.Matcher;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class UtilityFunctions {
    private static final int FIXED_LENGTH = 7;
    private static final String PROFESSOR_PREFIX = "PR@";
    private static final String STUDENT_PREFIX = "ST@";

    public String generateAcronym(String s) {
        return Arrays
                .stream(s.trim().split("\\s+"))
                .filter(word -> !word.equalsIgnoreCase("and"))
                .map(word -> word.substring(0, 1).toUpperCase())
                .collect(Collectors.joining());
    }

    private String generateId(String firstname, String lastname, String departmentCode, int count, String prefix) {
        String acronym = String.format("%C%C", firstname.charAt(0), lastname.charAt(0));
        String infix = acronym+departmentCode.substring(0,2);
        String suffix = String.valueOf(++count);

        int numberOfZeros = FIXED_LENGTH - (infix.length() + suffix.length());
        String format = prefix + "%S" + "0".repeat(Math.max(0, numberOfZeros)) + "%S";
        return String.format(format, infix, suffix);
    }

    public String generateProfessorId(String firstname, String lastname, String departmentCode, int professorCount) {
        return generateId(firstname, lastname, departmentCode, professorCount, PROFESSOR_PREFIX);
    }

    public String generateStudentRollNumber(String firstname, String lastname, String departmentCode, int studentCount) {
        return generateId(firstname, lastname, departmentCode, studentCount, STUDENT_PREFIX);
    }

}
