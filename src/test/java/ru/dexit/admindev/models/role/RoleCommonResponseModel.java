package ru.dexit.admindev.models.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCommonResponseModel {
    public String id;
    public String name;
    public List<String> policies;
    public String createdUtc;
    public String deletedUtc;
}
