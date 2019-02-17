package com.rarnu.bdownload

import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.plaf.metal.MetalLookAndFeel

fun main() {
    UIManager.setLookAndFeel(MetalLookAndFeel())
    JFrame.setDefaultLookAndFeelDecorated(true)
    JDialog.setDefaultLookAndFeelDecorated(true)
    MainForm()
}