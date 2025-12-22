package my.inplace.security.application.dto;

public class AdminCommand {

    public record Register(
        String username,
        String password
    ) {

    }
}
