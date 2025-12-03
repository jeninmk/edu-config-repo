package org.com.education.progress.model;

import java.time.LocalDateTime;

/**
 * Course Event model for JMS messaging (shared model for progress service)
 */
public class CourseEvent {
    private String eventType;
    private Long courseId;
    private String courseName;
    private String description;
    private String instructor;
    private LocalDateTime timestamp;
    private String eventId;

    public CourseEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public CourseEvent(String eventType, Long courseId, String courseName, String description, String instructor) {
        this.eventType = eventType;
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.timestamp = LocalDateTime.now();
        this.eventId = java.util.UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "CourseEvent{" +
                "eventType='" + eventType + '\'' +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", instructor='" + instructor + '\'' +
                ", timestamp=" + timestamp +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
