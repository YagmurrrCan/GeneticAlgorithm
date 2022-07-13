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
