package com.ecolution.ecofood.profile.manageNotification;

public class NotificationSettingModel {
    private String title;
    private boolean pushNotificationEnable;
    private boolean inAppNotificationEnable;

    public NotificationSettingModel(String title, boolean pushNotificationEnable, boolean inAppNotificationEnable) {
        this.title = title;
        this.pushNotificationEnable = pushNotificationEnable;
        this.inAppNotificationEnable = inAppNotificationEnable;
    }

    public String getTitle() { return title; }
    public boolean getPushNotificationEnable() { return pushNotificationEnable; }
    public boolean getInAppNotificationEnable() { return inAppNotificationEnable; }
}
