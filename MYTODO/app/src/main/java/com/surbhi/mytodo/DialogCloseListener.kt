package com.surbhi.mytodo

import android.content.DialogInterface


interface DialogCloseListener {
    fun handleDialogClose(dialog: DialogInterface?)
}