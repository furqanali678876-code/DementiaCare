package in.dementiaCare.helper.dto;

import in.dementiaCare.helper.entity.ReminderStatus;
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
public class ReminderStatusUpdateDto {

    private UUID id;
    private ReminderStatus status;
    private LocalDateTime completedAt;
}