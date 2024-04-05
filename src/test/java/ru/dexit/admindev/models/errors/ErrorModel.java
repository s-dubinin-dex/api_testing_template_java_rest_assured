package ru.dexit.admindev.models.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class ErrorModel {
    public int status;
    public Data data;
    public Errors errors;
    public String detail;
    public String message;
    public String stackTrace;
    public String title;
    public String type;
    @lombok.Data
    public static class Data {
        public String key;
        public String type;
    }
    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Errors{
        public List<Error> editRole;
        public List<Error> email;
        public List<Error> employeeId;
        public List<Error> id;
        public List<Error> name;
        public List<Error> newEmployee;
        public List<Error> policies;
        public List<Error> updateEmployee;
    }
    @lombok.Data
    public static class Error{
        public String errorCode;
        public List<String> params;
    }
}

