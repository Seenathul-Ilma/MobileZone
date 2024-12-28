package lk.ijse.gdse71.mobilezone.dto.tm;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerTM {
    private String customerId;
    private String name;
    private String nic;
    private String email;
    private String phone;
}
