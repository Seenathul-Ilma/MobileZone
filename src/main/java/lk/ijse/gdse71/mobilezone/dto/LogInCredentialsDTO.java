package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogInCredentialsDTO {
    private String userId;
    private String role;
    private String userName;
    private String password;
}
