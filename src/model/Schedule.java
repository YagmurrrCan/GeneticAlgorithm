/*
Bu sınıf, bir "Schedule" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde çizelge numarası (scheduleId) ve toplantı zamanı (meetingTime) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış.
 */

package model;

public class Schedule {

    private String scheduleId;
    private String meetingTime;

    public Schedule(String scheduleId, String meetingTime)
    {
        super();
        this.scheduleId = scheduleId;
        this.meetingTime = meetingTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

}
