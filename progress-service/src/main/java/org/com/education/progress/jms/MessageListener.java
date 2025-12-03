package org.com.education.progress.jms;

import org.com.education.progress.model.CourseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * JMS Message Listener for processing course events
 */
@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    /**
     * Listen to course events from the JMS topic
     * @param event The received CourseEvent
     */
    @JmsListener(destination = "${jms.course.topic:course-events}", containerFactory = "jmsListenerContainerFactory")
    public void receiveCourseEvent(CourseEvent event) {
        try {
            logger.info("Received course event: {}", event);
            
            // Process the event based on its type
            switch (event.getEventType().toUpperCase()) {
                case "CREATED":
                    handleCourseCreated(event);
                    break;
                case "UPDATED":
                    handleCourseUpdated(event);
                    break;
                case "DELETED":
                    handleCourseDeleted(event);
                    break;
                default:
                    logger.warn("Unknown event type: {}", event.getEventType());
            }
            
            logger.info("Successfully processed course event with eventId: {}", event.getEventId());
        } catch (Exception e) {
            logger.error("Failed to process course event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle course creation event
     * @param event CourseEvent containing creation information
     */
    private void handleCourseCreated(CourseEvent event) {
        logger.info("Processing course creation - Course ID: {}, Name: {}, Instructor: {}", 
                   event.getCourseId(), event.getCourseName(), event.getInstructor());
        
        // Here you would typically:
        // 1. Create initial progress tracking for the new course
        // 2. Set up course completion metrics
        // 3. Initialize enrollment tracking
        // 4. Update progress databases
        
        // For demonstration, we'll just log the processing
        simulateProgressUpdate(event, "Course created - initializing progress tracking");
    }

    /**
     * Handle course update event
     * @param event CourseEvent containing update information
     */
    private void handleCourseUpdated(CourseEvent event) {
        logger.info("Processing course update - Course ID: {}, Name: {}, Instructor: {}", 
                   event.getCourseId(), event.getCourseName(), event.getInstructor());
        
        // Here you would typically:
        // 1. Update progress tracking for course changes
        // 2. Adjust completion criteria if needed
        // 3. Notify enrolled students of course changes
        // 4. Update progress databases
        
        // For demonstration, we'll just log the processing
        simulateProgressUpdate(event, "Course updated - adjusting progress tracking");
    }

    /**
     * Handle course deletion event
     * @param event CourseEvent containing deletion information
     */
    private void handleCourseDeleted(CourseEvent event) {
        logger.info("Processing course deletion - Course ID: {}, Name: {}, Instructor: {}", 
                   event.getCourseId(), event.getCourseName(), event.getInstructor());
        
        // Here you would typically:
        // 1. Archive progress data for the deleted course
        // 2. Remove active progress tracking
        // 3. Update student records
        // 4. Clean up progress databases
        
        // For demonstration, we'll just log the processing
        simulateProgressUpdate(event, "Course deleted - archiving progress data");
    }

    /**
     * Simulate progress service updates (for demonstration purposes)
     * @param event CourseEvent
     * @param action Action being performed
     */
    private void simulateProgressUpdate(CourseEvent event, String action) {
        logger.info("Progress Service Action: {} for Course '{}' (ID: {})", 
                   action, event.getCourseName(), event.getCourseId());
        
        // Simulate some processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted during progress update", e);
        }
        
        // Log the completion of the action
        logger.info("Progress Service Action completed: {} for Course '{}' (ID: {})", 
                   action, event.getCourseName(), event.getCourseId());
    }
}
