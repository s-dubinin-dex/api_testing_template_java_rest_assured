package ru.dexit.admindev.helpers;

public class ErrorDescription {
    public static final String duplicateKeyEmployeeEmail = "duplicate key value violates unique constraint \"IX_Employee_Email\"";
    public static final String duplicateKeyRoleName = "duplicate key value violates unique constraint \"IX_Role_Name\"";
    public static final String exceptionOfTypeEntityInUseExceptionRoleInfo = "Exception of type 'Shared.Domain.Exceptions.EntityInUseException`1[Admin.Application.Abstraction.Models.Queries.Role.RoleInfo]' was thrown.";
    public static final String exceptionOfTypeEntityNotFoundExceptionEmployeeInfo = "Exception of type 'Shared.Domain.Exceptions.EntityNotFoundException`1[Admin.Application.Abstraction.Models.Queries.Employee.EmployeeInfo]' was thrown.";
    public static final String exceptionOfTypeEntityNotFoundExceptionRoleInfo = "Exception of type 'Shared.Domain.Exceptions.EntityNotFoundException`1[Admin.Application.Abstraction.Models.Queries.Role.RoleInfo]' was thrown.";
    public static final String idIsEmpty = "id is empty (Parameter 'id')";
    public static final String noAccessRoleIsReadOnly = "NoAccess role is read-only";
    public static final String validationsEditRoleFieldIsRequired = "validations.The editRole field is required.";
    public static final String validationsEmployeeIdFieldIsRequired = "validations.The employeeId field is required.";
    public static final String validationsInvalidName = "validations.InvalidName";
    public static final String validationsInvalidEmail = "validations.InvalidEmail";
    public static final String validationsMaxLengthExceeded = "validations.MaxLengthExceeded";
    public static final String validationsNameFieldIsRequired = "validations.The Name field is required.";
    public static final String validationsNewEmployeeFieldIsRequired = "validations.The newEmployee field is required.";
    public static final String validationsRequired = "validations.Required";
    public static final String validationsTryingToApplyNonExistentPolicy = "validations.TryingToApplyNonExistentPolicy";
    public static final String validationsUpdateEmployeeFieldIsRequired = "validations.The updateEmployee field is required.";
    public static final String requestValidationError = "Request Validation Error";
    public static final String userWithSuperAdminRoleIsReadOnly = "User with SuperAdmin role is read-only";
}
