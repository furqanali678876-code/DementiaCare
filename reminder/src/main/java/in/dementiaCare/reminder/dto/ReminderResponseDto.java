package in.dementiaCare.reminder.dto;

import in.dementiaCare.reminder.entity.ReminderStatus;
import in.dementiaCare.reminder.entity.ReminderType;
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
public class ReminderResponseDto {

    private UUID id;
    private UUID patientId;
    private ReminderType type;
    private String title;
    private String description;
    private LocalDateTime scheduledTime;
    private Boolean isRecurring;
    private String recurrenceRule;
    private ReminderStatus status;
    private LocalDateTime completedAt;
}