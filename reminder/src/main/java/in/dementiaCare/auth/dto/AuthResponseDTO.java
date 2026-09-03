package in.dementiaCare.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;       // the JWT — app stores this and sends it on future requests
    private UUID userId;        // same as patientId — links to Reminder.patientId
    private String patientName;
    private String trustedPersonName;
}
