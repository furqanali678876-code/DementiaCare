package in.dementiaCare.helper.dto;

import in.dementiaCare.helper.entity.ReminderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequestDto {

    private UUID id; // client generates this offline, so we accept it here
    private UUID patientId;
    private ReminderType type;
    private String title;
    private String description;
    private LocalDateTime scheduledTime;
    private Boolean isRecurring;
    private String recurrenceRule;
}