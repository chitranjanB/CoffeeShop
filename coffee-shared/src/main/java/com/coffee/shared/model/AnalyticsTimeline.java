package com.coffee.shared.model;

import java.util.List;

public class AnalyticsTimeline {
    private List<TimelineData> timelineDataList;
    private Step name;

    public List<TimelineData> getTimelineDataList() {
        return timelineDataList;
    }

    public void setTimelineDataList(List<TimelineData> timelineDataList) {
        this.timelineDataList = timelineDataList;
    }

    public Step getName() {
        return name;
    }

    public void setName(Step name) {
        this.name = name;
    }
}