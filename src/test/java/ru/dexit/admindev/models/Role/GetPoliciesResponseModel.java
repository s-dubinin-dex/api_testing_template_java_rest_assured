package ru.dexit.admindev.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPoliciesResponseModel {
    public String category;
    public List<GetPoliciesResponsePolicyModel> policies;

}
