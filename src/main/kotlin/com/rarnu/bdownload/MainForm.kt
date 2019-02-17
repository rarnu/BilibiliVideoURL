package com.rarnu.bdownload

import java.awt.*
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder

class MainForm: JFrame("Get Bilibili Download URL"), ActionListener {

    private val edtAv: JTextField
    private val edtUrl: JTextField
    private val edtBack: JTextField
    private val btnGetUrl: JButton
    private val btnCopyUrl: JButton
    private val btnCopyBack: JButton

    init {
        contentPane.background = Color.white
        setSize(500, 150)
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        layout = BorderLayout()

        fun buildPane(b: Border): JPanel {
            val p = JPanel(BorderLayout())
            p.background = Color.white
            p.border = b
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

        val avPane = buildPane(EmptyBorder(8, 8, 0, 8))
        val urlPane = buildPane(EmptyBorder(8, 8, 8, 8))
        val urlBackupPane = buildPane(EmptyBorder(0, 8, 8, 8))
        buildLabel("av", avPane)
        buildLabel("URL", urlPane)
        buildLabel("Backup", urlBackupPane)
        edtAv = buildTextField(avPane)
        edtUrl = buildTextField(urlPane, false)
        edtBack = buildTextField(urlBackupPane, false)
        btnGetUrl = buildButton("Get", avPane)
        btnCopyUrl = buildButton("Copy", urlPane)
        btnCopyBack = buildButton("Copy", urlBackupPane)

        add(avPane, BorderLayout.NORTH)
        add(urlPane, BorderLayout.CENTER)
        add(urlBackupPane, BorderLayout.SOUTH)

        btnGetUrl.addActionListener(this)
        btnCopyUrl.addActionListener(this)
        btnCopyBack.addActionListener(this)

        setAvEditAction()

        isVisible = true
    }

    override fun actionPerformed(e: ActionEvent) {
        when(e.source) {
            btnGetUrl -> {
                UrlRequest.getBilibiliDownloadUrl(edtAv.text) { u, u2 ->
                    SwingUtilities.invokeLater {
                        edtUrl.text = u
                        edtBack.text = u2
                    }
                }
            }
            btnCopyUrl -> Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(edtUrl.text), null)
            btnCopyBack -> Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(edtBack.text), null)
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

}