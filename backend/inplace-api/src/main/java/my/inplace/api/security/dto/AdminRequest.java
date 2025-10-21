package my.inplace.api.security.dto;

import my.inplace.application.security.admin.dto.AdminCommand.Register;

public class AdminRequest {
    public record Login(
        String username,
        String password
    ) {

        public static Register toRegisterCommand(Login login) {
            return new Register(login.username, login.password);
        }
    }
}
