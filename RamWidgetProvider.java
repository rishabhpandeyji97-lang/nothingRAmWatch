package com.example.ramwatch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.text.DateFormat;
import java.util.Date;

public class RamWidgetProvider extends AppWidgetProvider {
    public static void updateAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, RamWidgetProvider.class));
        for (int id : ids) updateOne(context, manager, id);
    }
    private static void updateOne(Context context, AppWidgetManager manager, int id) {
        RamStats.Snapshot s = RamStats.read(context);
        RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.ram_widget);
        v.setTextViewText(R.id.widget_used, "Used: " + RamStats.size(context, s.used) + " / " + RamStats.size(context, s.total));
        v.setTextViewText(R.id.widget_free, "Available: " + RamStats.size(context, s.available));
        v.setTextViewText(R.id.widget_updated, "Updated " + DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()) + " · tap to open");
        Intent refresh = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, refresh, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        v.setOnClickPendingIntent(R.id.widget_title, pi);
        v.setOnClickPendingIntent(R.id.widget_used, pi);
        v.setOnClickPendingIntent(R.id.widget_free, pi);
        v.setOnClickPendingIntent(R.id.widget_updated, pi);
        manager.updateAppWidget(id, v);
    }
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) updateOne(context, manager, id);
    }
    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction()) || "com.example.ramwatch.REFRESH".equals(intent.getAction())) updateAll(context);
    }
}
