package ru.dexit.admindev.models.Identity;

import lombok.Data;

@Data
public class IdentityResponseModel {
    public String access_token;
    public int expires_in;
    public String token_type;
    public String refresh_token;
    public String scope;
}
