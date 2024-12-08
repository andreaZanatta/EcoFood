package com.ecolution.ecofood.profile;

import java.time.LocalDateTime;

public class NotificationInfo {
    String mainText, descriptionText;
    LocalDateTime creationDate;

    public NotificationInfo(String mainText, String descriptionText, LocalDateTime creationDate) {
        this.mainText = mainText;
        this.descriptionText = descriptionText;
        this.creationDate = creationDate;
    }

    public String getMainText() {
        return mainText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}