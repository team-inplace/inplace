package my.inplace.security.admin.dto;

import my.inplace.security.application.dto.AdminCommand.Register;

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
