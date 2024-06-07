package ru.dexit.admindev.models.role;

import lombok.Data;

import java.util.List;

@Data
public class ODataRoleResponseModel {
    String id;
    String name;
    List<String> policies;
    String createdUtc;
    String deletedUtc;
}
