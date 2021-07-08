package org.mentalizr.backend.rest.entities.event;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExerciseEvent {

    private long timestap;
    private boolean isNew;
    private String userId;
    private String contentId;

    public ExerciseEvent() {
    }

    public ExerciseEvent(long timestap, boolean isNew, String userId, String contentId) {
        this.timestap = timestap;
        this.isNew = isNew;
        this.userId = userId;
        this.contentId = contentId;
    }

    public long getTimestap() {
        return timestap;
    }

    public void setTimestap(long timestap) {
        this.timestap = timestap;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

}
