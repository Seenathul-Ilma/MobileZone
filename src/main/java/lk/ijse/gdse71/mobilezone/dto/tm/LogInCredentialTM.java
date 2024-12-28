package lk.ijse.gdse71.mobilezone.dto.tm;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogInCredentialTM {
    private String userId;
    private String role;
    private String userName;
    private String password;
}
