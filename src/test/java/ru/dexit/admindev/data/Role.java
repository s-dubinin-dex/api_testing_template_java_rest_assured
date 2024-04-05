package ru.dexit.admindev.data;

public enum Role {
    FULL_RIGHTS("Полные права", "8b1ded60-2979-4728-a0f6-eb69f43eb44a"),
    FULL_WRITE("Test FullWrite Role", "b2a142fe-9035-46a5-bd9f-06baf40be2b0"),
    FULL_READ("Test FullRead Role", "d2ee530d-8384-439a-8486-3e960118084b"),
    NO_RIGHTS("Нет прав", "4e964e99-95b9-424a-bd20-7e9da61d2aef");

    public final String roleName;
    public final String roleUUID;

    Role(String roleName, String roleUUID) {
        this.roleName = roleName;
        this.roleUUID = roleUUID;
    }


    public static Role findRoleById(String id){

        for (Role role: Role.values()){
            if (role.roleUUID.equals(id)){
                return role;
            }
        }
        return null;
    }

}
