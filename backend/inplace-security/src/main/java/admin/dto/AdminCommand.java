package admin.dto;

public class AdminCommand {

    public record Register(
        String username,
        String password
    ) {

    }
}
