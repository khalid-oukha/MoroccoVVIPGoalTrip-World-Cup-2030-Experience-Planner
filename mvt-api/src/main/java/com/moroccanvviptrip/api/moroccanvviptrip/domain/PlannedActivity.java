package com.moroccanvviptrip.api.moroccanvviptrip.domain;

import com.moroccanvviptrip.api.moroccanvviptrip.domain.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "planned_activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlannedActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Priority priority;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
