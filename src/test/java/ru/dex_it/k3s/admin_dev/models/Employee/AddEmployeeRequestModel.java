package ru.dex_it.k3s.admin_dev.models.Employee;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeRequestModel {
    public String name;
    public String roleId;
    public String email;
}
