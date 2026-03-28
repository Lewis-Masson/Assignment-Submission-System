package com.university.ass;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc,
                                          HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/student/")) {
            return "redirect:/student/dashboard?filesizerror";
        } else {
            return "redirect:/adviser/dashboard?filesizerror";
        }
    }
}