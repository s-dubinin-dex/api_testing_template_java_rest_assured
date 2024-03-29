package ru.dexit.admindev.helpers;



public class Authorization {

    public static String getToken(){
         return CoreApiMethodsIdentity.connectToken().jsonPath().get("access_token");
    }

}
