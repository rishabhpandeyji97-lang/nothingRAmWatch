package com.example.ramwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private LinearLayout list;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(24, 28, 24, 24); root.setBackgroundColor(0xFF202124);
        scroll.addView(root);
        TextView title = text("RAM WATCH", 24, 0xFFE8FF6B); title.setTypeface(null, 1); root.addView(title);
        TextView note = text("Live system memory snapshot", 14, 0xFFD4D4D4); root.addView(note);
        Button refresh = new Button(this); refresh.setText("Refresh now"); root.addView(refresh);
        TextView summary = text("", 18, 0xFFFFFFFF); root.addView(summary);
        TextView limitation = text("Per-app memory is best-effort. Android may hide other apps' processes; a missing app does not mean it uses no RAM. Usage Access does not unlock per-app RAM readings.", 14, 0xFFFFD180); root.addView(limitation);
        TextView section = text("Processes Android makes visible", 17, 0xFFE8FF6B); section.setPadding(0, 18, 0, 8); root.addView(section);
        list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); root.addView(list);
        refresh.setOnClickListener(v -> refresh(summary));
        setContentView(scroll);
        refresh(summary);
    }
    private void refresh(TextView summary) {
        RamStats.Snapshot s = RamStats.read(this);
        summary.setText("\nTotal RAM: " + RamStats.size(this, s.total) + "\nUsed (total − available): " + RamStats.size(this, s.used) + "\nAvailable: " + RamStats.size(this, s.available) + "\n");
        list.removeAllViews();
        if (s.processes.isEmpty()) list.addView(text("Android did not expose a process list on this device/version.", 14, 0xFFFFFFFF));
        else for (String p : s.processes) list.addView(text("• " + p, 13, 0xFFFFFFFF));
        RamWidgetProvider.updateAll(this);
    }
    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); t.setPadding(0, 8, 0, 8); return t;
    }
}
