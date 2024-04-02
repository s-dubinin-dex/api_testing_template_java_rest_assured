package ru.dexit.admindev.models.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ODataEmployeeResponseModel {
    public String id;
    public String name;
    public String createdUtc;
    public String deletedUtc;
    public String roleId;
    public String role;
    public String email;
    public String activationDate;
}
