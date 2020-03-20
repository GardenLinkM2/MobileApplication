package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Report;
import com.gardenlink_mobile.serialization.ReportSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class POST_REPORT extends Operation {

    private String url = API_URL + "Gardens";
    private String REPORT = "report";
    private static ReportSerializer serializer = new ReportSerializer();
    private Object argument;

    public POST_REPORT(Report report) {
        if (!report.getOfGarden().isEmpty()) {
            url += "/" + report.getOfGarden() + "/" + REPORT;
            argument = report;
        } else {
            Log.e("OPERATION_POST_REPORT", "POST_REPORT: id is empty");
        }
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.post(sender, url, serializer, argument, this, Session.getInstance().getSessionToken());
    }
}
