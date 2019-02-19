package com.rarnu.bdownload

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel


class ViewImageForm(owner: JFrame, imgPath: String) : JDialog(owner, "Image", true) {

    init {
        defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
        layout = BorderLayout()
        val img = object: JPanel() {
            override fun paint(g: Graphics) {
                val image = ImageIO.read(File(imgPath))
                g.drawImage(image, 0, 0, 240, 180, null)
            }
        }
        img.preferredSize = Dimension(240, 180)
        add(img, BorderLayout.CENTER)
        isResizable = false
        pack()
        setLocationRelativeTo(owner)
        isVisible = true
    }

}