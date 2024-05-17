package kr.co.lotte.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(TemplateInputException.class)
    public String handleTemplateInputException(TemplateInputException e) {
        return "/error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleTemplateInputExceptions(Exception e) {
        return "/error/error";
    }

}
