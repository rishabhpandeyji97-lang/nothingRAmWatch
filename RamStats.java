package com.example.ramwatch;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.format.Formatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class RamStats {
    private RamStats() {}
    public static class Snapshot {
        public long total, available, used;
        public List<String> processes = new ArrayList<>();
    }
    public static Snapshot read(Context context) {
        Snapshot s = new Snapshot();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        s.total = info.totalMem;
        s.available = info.availMem;
        s.used = Math.max(0, s.total - s.available);
        try {
            List<ActivityManager.RunningAppProcessInfo> running = am.getRunningAppProcesses();
            if (running != null) {
                for (ActivityManager.RunningAppProcessInfo p : running) {
                    if (p == null || p.pid <= 0) continue;
                    try {
                        Debug.MemoryInfo[] arr = am.getProcessMemoryInfo(new int[]{p.pid});
                        if (arr != null && arr.length > 0) {
                            long kb = arr[0].getTotalPss();
                            s.processes.add(String.format(Locale.getDefault(), "%s — %s", p.processName,
                                    Formatter.formatFileSize(context, kb * 1024L)));
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}
        Collections.sort(s.processes, new Comparator<String>() {
            @Override public int compare(String a, String b) { return b.compareTo(a); }
        });
        return s;
    }
    public static String size(Context c, long bytes) { return Formatter.formatFileSize(c, bytes); }
}
