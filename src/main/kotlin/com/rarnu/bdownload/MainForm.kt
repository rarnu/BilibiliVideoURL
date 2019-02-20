package com.rarnu.bdownload

import com.rarnu.kt.common.DownloadState
import com.rarnu.kt.common.downloadAsync
import com.rarnu.kt.common.swingMainThread
import java.awt.*
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.io.File
import java.net.URI
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder

class MainForm : JFrame("Get Bilibili Download URL"), ActionListener {

    private val edtAv: JTextField
    private val edtUrl: JTextField
    private val edtBack: JTextField
    private val edtImg: JTextField
    private val edtBullet: JTextField
    private val btnGetUrl: JButton
    private val btnCopyUrl: JButton
    private val btnCopyBack: JButton
    private val btnDownloadImage: JButton
    private val btnViewBullet: JButton

    init {
        contentPane.background = Color.white
        (contentPane as JPanel).border = EmptyBorder(8, 8, 8, 8)
        setSize(500, 200)
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        layout = GridLayout(5, 1, 4, 4)


        fun buildPane(): JPanel {
            val p = JPanel(BorderLayout())
            p.background = Color.white
            (p.layout as BorderLayout).hgap = 4
            return p
        }

        fun buildLabel(text: String, parent: Container) {
            val lbl = JLabel(text)
            lbl.preferredSize = Dimension(52, 28)
            parent.add(lbl, BorderLayout.WEST)
        }

        fun buildTextField(parent: Container, isEditable: Boolean = true): JTextField {
            val tf = JTextField()
            tf.background = Color.white
            tf.isEditable = isEditable
            parent.add(tf, BorderLayout.CENTER)
            return tf
        }

        fun buildButton(text: String, parent: Container): JButton {
            val btn = JButton(text)
            btn.preferredSize = Dimension(70, 28)
            parent.add(btn, BorderLayout.EAST)
            btn.addActionListener(this)
            return btn
        }

        val avPane = buildPane()
        val urlPane = buildPane()
        val urlBackupPane = buildPane()
        val imgPane = buildPane()
        val bulletPane = buildPane()
        buildLabel("av", avPane)
        buildLabel("URL", urlPane)
        buildLabel("Backup", urlBackupPane)
        buildLabel("Image", imgPane)
        buildLabel("Bullet", bulletPane)
        edtAv = buildTextField(avPane)
        edtUrl = buildTextField(urlPane, false)
        edtBack = buildTextField(urlBackupPane, false)
        edtImg = buildTextField(imgPane, false)
        edtBullet = buildTextField(bulletPane, false)
        btnGetUrl = buildButton("Get", avPane)
        btnCopyUrl = buildButton("Copy", urlPane)
        btnCopyBack = buildButton("Copy", urlBackupPane)
        btnDownloadImage = buildButton("View", imgPane)
        btnViewBullet = buildButton("View", bulletPane)
        add(avPane)
        add(urlPane)
        add(urlBackupPane)
        add(imgPane)
        add(bulletPane)
        setAvEditAction()
        isVisible = true
    }

    override fun actionPerformed(e: ActionEvent) {
        when (e.source) {
            btnGetUrl -> {
                UrlRequest.getBilibiliDownloadUrl(edtAv.text) { u, u2, u3, u4 ->
                    swingMainThread {
                        edtUrl.text = u
                        edtBack.text = u2
                        edtImg.text = u3
                        edtBullet.text = u4
                    }
                }
            }
            btnCopyUrl -> Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(edtUrl.text), null)
            btnCopyBack -> Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(edtBack.text), null)
            btnDownloadImage -> openImage(edtImg.text)
            btnViewBullet -> openBullet(edtBullet.text)
        }
    }

    private fun setAvEditAction() {
        edtAv.actionMap.put("Paste", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                edtAv.paste()
            }
        })
        edtAv.actionMap.put("SelectAll", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                edtAv.selectAll()
            }
        })
        edtAv.inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.META_MASK), "Paste")
        edtAv.inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.META_MASK), "SelectAll")
    }

    private fun openImage(u: String) {
        val path = System.getProperty("user.dir")
        downloadAsync {
            url = u
            localFile = File(path, "tmp.jpg").absolutePath
            progress { state, _, _, error ->
                if (state == DownloadState.WHAT_DOWNLOAD_FINISH && error == null) {
                    swingMainThread { ViewImageForm(this@MainForm, localFile) }
                }
            }
        }
    }

    private fun openBullet(u: String) {
        val uri = URI.create(u)
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri)
        }
    }

}