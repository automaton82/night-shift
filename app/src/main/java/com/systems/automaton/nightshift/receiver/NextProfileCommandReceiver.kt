/*
 * Copyright (c) 2016 Marien Raat <marienraat@riseup.net>
 * Copyright (c) 2017  Stephen Michel <s@smichel.me>
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.systems.automaton.nightshift.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.systems.automaton.nightshift.activeProfile

import com.systems.automaton.nightshift.ProfilesModel
import com.systems.automaton.nightshift.helper.Logger

class NextProfileCommandReceiver : BroadcastReceiver() {

    companion object : Logger()

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Next profile requested")
        activeProfile = ProfilesModel.profileAfter(activeProfile)
    }
}
