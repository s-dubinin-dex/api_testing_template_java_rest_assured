package ru.dexit.admindev.data;

public enum UserRole {
    FULL_RIGHTS(new Role("Полные права", "8b1ded60-2979-4728-a0f6-eb69f43eb44a")),
    FULL_WRITE(new Role("Test FullWrite Role", "b2a142fe-9035-46a5-bd9f-06baf40be2b0")),
    FULL_READ(new Role("Test FullRead Role", "d2ee530d-8384-439a-8486-3e960118084b")),
    NO_RIGHTS(new Role("Нет прав", "4e964e99-95b9-424a-bd20-7e9da61d2aef"));

    public final Role role;

    UserRole(Role role) {
        this.role = role;
    }


    public static UserRole findUserRoleById(String id){

        for (UserRole role: UserRole.values()){
            if (role.role.uid.equals(id)){
                return role;
            }
        }
        return null;
    }
}
