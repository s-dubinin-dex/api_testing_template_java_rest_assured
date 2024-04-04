package ru.dexit.admindev.helpers;


import ru.dexit.admindev.apiMethods.identity.CoreApiMethodsIdentity;

public class Authorization {

    public static String getToken(){
         return CoreApiMethodsIdentity.connectToken().jsonPath().get("access_token");
    }

}
