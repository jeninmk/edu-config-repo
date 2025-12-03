package org.com.education.course.jms;

import org.com.education.course.model.CourseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * JMS Message Producer for sending course events
 */
@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.course.topic:course-events}")
    private String courseTopic;

    /**
     * Send a course event to the JMS topic
     * @param eventType Type of event (CREATED, UPDATED, DELETED)
     * @param courseId ID of the course
     * @param courseName Name of the course
     * @param description Course description
     * @param instructor Course instructor
     */
    public void sendCourseEvent(String eventType, Long courseId, String courseName, 
                              String description, String instructor) {
        try {
            CourseEvent event = new CourseEvent(eventType, courseId, courseName, description, instructor);
            logger.info("Sending course event: {} to topic: {}", event, courseTopic);
            
            jmsTemplate.convertAndSend(courseTopic, event);
            
            logger.info("Course event successfully sent with eventId: {}", event.getEventId());
        } catch (Exception e) {
            logger.error("Failed to send course event: {}", e.getMessage(), e);
        }
    }

    /**
     * Send a course event object directly
     * @param event CourseEvent object
     */
    public void sendCourseEvent(CourseEvent event) {
        try {
            logger.info("Sending course event: {} to topic: {}", event, courseTopic);
            
            jmsTemplate.convertAndSend(courseTopic, event);
            
            logger.info("Course event successfully sent with eventId: {}", event.getEventId());
        } catch (Exception e) {
            logger.error("Failed to send course event: {}", e.getMessage(), e);
        }
    }

    /**
     * Send a course creation event
     * @param courseId ID of the created course
     * @param courseName Name of the created course
     * @param description Course description
     * @param instructor Course instructor
     */
    public void sendCourseCreatedEvent(Long courseId, String courseName, String description, String instructor) {
        sendCourseEvent("CREATED", courseId, courseName, description, instructor);
    }

    /**
     * Send a course update event
     * @param courseId ID of the updated course
     * @param courseName Updated name of the course
     * @param description Updated course description
     * @param instructor Updated course instructor
     */
    public void sendCourseUpdatedEvent(Long courseId, String courseName, String description, String instructor) {
        sendCourseEvent("UPDATED", courseId, courseName, description, instructor);
    }

    /**
     * Send a course deletion event
     * @param courseId ID of the deleted course
     * @param courseName Name of the deleted course
     * @param description Course description
     * @param instructor Course instructor
     */
    public void sendCourseDeletedEvent(Long courseId, String courseName, String description, String instructor) {
        sendCourseEvent("DELETED", courseId, courseName, description, instructor);
    }
}
