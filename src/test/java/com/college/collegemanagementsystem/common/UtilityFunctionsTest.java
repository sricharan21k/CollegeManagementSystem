package com.college.collegemanagementsystem.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UtilityFunctionsTest {

    @Autowired private UtilityFunctions utilityFunctions;


    @Test
    public void testGenerateAcronym(){
        assertEquals("CSE", utilityFunctions.generateAcronym("Computer Science Engineering"));
        assertEquals("EEE", utilityFunctions.generateAcronym("Electrical and Electronics Engineering"));
        assertEquals("XYZ", utilityFunctions.generateAcronym(" x y z "));
        assertEquals("ABC", utilityFunctions.generateAcronym("A b and c"));
//        assertEquals("AMB", utilityFunctions.generateAcronym("- a minus + b"));
    }

    @Test
    public void testGenerateProfessorId(){

    }
}
