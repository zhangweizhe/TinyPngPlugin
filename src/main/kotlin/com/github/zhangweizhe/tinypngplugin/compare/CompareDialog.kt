package com.github.zhangweizhe.tinypngplugin.compare

import com.github.zhangweizhe.tinypngplugin.FileSizeFormatter
import com.intellij.ui.components.Label
import com.intellij.ui.components.panels.HorizontalLayout
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.dsl.builder.RowLayout
import java.awt.*
import java.io.File
import javax.swing.BorderFactory
import javax.swing.DefaultListCellRenderer
import javax.swing.DefaultListModel
import javax.swing.ImageIcon
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane

class CompareDialog: JDialog() {

    companion object {
        private const val IMAGE_CELL_MAX_WIDTH = 380
        private const val IMAGE_CELL_MAX_HEIGHT = 380


        fun loadImage(imgPath: String): ImageIcon {
            val orgImageIcon = ImageIcon(imgPath)
            val orgImage = orgImageIcon.image
            val orgIconWidth = orgImageIcon.iconWidth
            val orgIconHeight = orgImageIcon.iconHeight
            if (orgIconHeight <= IMAGE_CELL_MAX_HEIGHT && orgIconWidth <= IMAGE_CELL_MAX_WIDTH) {
                return orgImageIcon
            }

            val widthRatio = IMAGE_CELL_MAX_WIDTH / orgIconWidth.toFloat()
            val heightRatio = IMAGE_CELL_MAX_HEIGHT / orgIconHeight.toFloat()
            val scaleRatio = minOf(widthRatio, heightRatio)

            val scaleWidth = (orgIconWidth * scaleRatio).toInt()
            val scaleHeight = (orgIconHeight * scaleRatio).toInt()

            val scaledImg = orgImage.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH)
            return ImageIcon(scaledImg)
        }
    }

    init {
        title = "Compress result"
        setSize(800, 600)
        setLocationRelativeTo(null)

        val listModel = DefaultListModel<Pair<String, String>>()
        val listView = JList<Pair<String, String>>(listModel)
        listView.cellRenderer = CompareRenderer()

        val compareList = CompareManager.instance.compareList
        for (item in compareList) {
            listModel.addElement(item)
        }
//        layout = BorderLayout()
//        add(Label("Before compress"), BorderLayout.WEST)
//        add(Label("After compress"), BorderLayout.EAST)
        add(JScrollPane(listView))

    }

    override fun setVisible(b: Boolean) {
        super.setVisible(b)
        if (!b) {
            CompareManager.instance.clearCompareList()
        }
    }


    class CompareRenderer: DefaultListCellRenderer() {
        override fun getListCellRendererComponent(
            list: JList<*>?,
            value: Any?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            if (value is Pair<*, *>) {
                val panel = JPanel(VerticalLayout(5))
                val fileNameLabel = Label(value.second as String).also {
                    it.border = BorderFactory.createEmptyBorder(10,5,0,5)
                }
                panel.add(fileNameLabel)
                val imgPanel = JPanel()
                imgPanel.add(createImageCell(value.first as String), )
                imgPanel.add(createImageCell(value.second as String))
                panel.add(imgPanel)

                return panel
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        }

        private fun createImageCell(imgPath: String): JPanel {
            val panel = JPanel(VerticalLayout(5))
            val file = File(imgPath)
            val fileLength = file.length()
            val fileSize = FileSizeFormatter.formatBytes(fileLength)
            panel.add(JLabel(fileSize))
            panel.add(JLabel(loadImage(imgPath)))
            return panel
        }
    }
}