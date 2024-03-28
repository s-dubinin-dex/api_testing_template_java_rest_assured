package ru.dex_it.k3s.admin_dev.models.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployeeRequestModel {
    public String id;
    public String name;
    public String roleId;
}
