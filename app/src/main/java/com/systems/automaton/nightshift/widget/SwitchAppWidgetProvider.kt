/*
 * Copyright (c) 2016  Marien Raat <marienraat@riseup.net>
 * Copyright (c) 2017  Stephen Michel <s@smichel.me>
 * SPDX-License-Identifier: GPL-3.0-or-later
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 *     Copyright (c) 2016 Zoraver <https://github.com/Zoraver>
 *
 *     Permission to use, copy, modify, and/or distribute this software
 *     for any purpose with or without fee is hereby granted, provided
 *     that the above copyright notice and this permission notice appear
 *     in all copies.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 *     WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 *     WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE
 *     AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 *     CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 *     OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *     NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 *     CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.systems.automaton.nightshift.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.systems.automaton.nightshift.Command
import com.systems.automaton.nightshift.filterIsOn
import com.systems.automaton.nightshift.helper.Logger
import com.systems.automaton.nightshift.R

class SwitchAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.i("onUpdate()")

        for (i in appWidgetIds.indices) {
            val appWidgetId = appWidgetIds[i]

            val toggleIntent = Intent(context, SwitchAppWidgetProvider::class.java)
            toggleIntent.action = ACTION_TOGGLE
            val togglePendingIntent = PendingIntent.getBroadcast(context, 0, toggleIntent, 0)

            val views = RemoteViews(context.packageName, R.layout.appwidget_switch)
            views.setOnClickPendingIntent(R.id.widget_pause_play_button, togglePendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
            updateImage(context, filterIsOn)
        }
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action == ACTION_TOGGLE) {
            Command.toggle(!filterIsOn)
        } else if (intent.action == ACTION_UPDATE) {
            updateImage(ctx, intent.getBooleanExtra(EXTRA_POWER, false))
        } else {
            super.onReceive(ctx, intent)
        }
    }

    internal fun updateImage(context: Context, filterIsOn: Boolean) {
        Log.i("Updating image! filterIsOn: $filterIsOn")
        val views = RemoteViews(context.packageName, R.layout.appwidget_switch)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetComponent = ComponentName(context, SwitchAppWidgetProvider::class.java.name)
        val drawable = if (filterIsOn) R.drawable.ic_stop else R.drawable.ic_play

        views.setInt(R.id.widget_pause_play_button, "setImageResource", drawable)
        appWidgetManager.updateAppWidget(appWidgetComponent, views)
    }

    companion object : Logger() {
        const val ACTION_TOGGLE = "com.systems.automaton.nightshift.action.APPWIDGET_TOGGLE"
        const val ACTION_UPDATE = "com.systems.automaton.nightshift.action.APPWIDGET_UPDATE"
        const val EXTRA_POWER = "com.systems.automaton.nightshift.action.APPWIDGET_EXTRA_POWER"
    }
}
