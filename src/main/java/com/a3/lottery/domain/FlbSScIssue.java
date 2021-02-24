package com.a3.lottery.domain;

import java.util.List;
import java.util.Map;

public class FlbSScIssue {

    private String date;
    private Map<String, List<FlbSScIssueDetail>> data;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, List<FlbSScIssueDetail>> getData() {
        return data;
    }

    public void setData(Map<String, List<FlbSScIssueDetail>> data) {
        this.data = data;
    }

}
