package com.college.collegemanagementsystem.common;


import com.college.collegemanagementsystem.dto.response.error.ErrorResponse;
import com.college.collegemanagementsystem.exception.entityalreadyexists.DepartmentAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entityalreadyexists.ProfessorAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entityalreadyexists.StudentAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entityalreadyexists.SubjectAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entitynotfound.DepartmentNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.ProfessorNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.StudentNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.SubjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DepartmentNotFoundException.class,
            ProfessorNotFoundException.class,
            StudentNotFoundException.class,
            SubjectNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleEntityNotFoundExceptions(RuntimeException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        var response = ErrorResponse.create(status, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({
            DepartmentAlreadyExistsException.class,
            ProfessorAlreadyExistsException.class,
            SubjectAlreadyExistsException.class,
            StudentAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(RuntimeException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        var response = ErrorResponse.create(status, ex.getMessage());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> responseMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> responseMap.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ));

        return ResponseEntity.badRequest().body(responseMap);
    }

//    private ErrorResponse buildErrorResponse(HttpStatus status, String errorDescription){
//        return ErrorResponse.builder()
//                .status(status.value())
//                .error(status.getReasonPhrase())
//                .message(errorDescription)
//                .timestamp(LocalDateTime.now()).build();
//    }
}
