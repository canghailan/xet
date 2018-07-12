package cc.whohow.xet.engine.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编辑器
 */
public class XetEditor implements Runnable {
    private JFrame frame;
    private JSplitPane vSplitPane;
    private JSplitPane hSplitPane;
    private JToolBar toolBar;
    private JButton previewButton;
    private JButton saveButton;
    private JEditorPane editorPane;
    private JTextArea ioTextArea;
    private ImageView<BufferedImage> imageView;
    private ImageXetEngine engine = new ImageXetEngine();

    public XetEditor() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame = new JFrame("XET编辑器");
        frame.setSize((int) (screenSize.getWidth() / 3 * 2), (int) (screenSize.getHeight() / 3 * 2));
        frame.setLocation((int) (screenSize.getWidth() / 6), (int) (screenSize.getHeight() / 6));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                XetEditor.this.componentResized(e);
            }
        });

        toolBar = new JToolBar();

        previewButton = new JButton("预览");
        previewButton.addActionListener(this::onPreviewButtonClicked);
        toolBar.add(previewButton);

        saveButton = new JButton("保存");
        saveButton.addActionListener(this::onSaveButtonClicked);
        toolBar.add(saveButton);

        frame.getContentPane().add(toolBar, BorderLayout.NORTH);

        vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        editorPane = new JEditorPane();
        editorPane.setFont(Font.getFont(Font.MONOSPACED));
        hSplitPane.setLeftComponent(new JScrollPane(editorPane));

        imageView = new ImageView<>();
        hSplitPane.setRightComponent(new JScrollPane(imageView));
        vSplitPane.setTopComponent(hSplitPane);

        ioTextArea = new JTextArea();
        vSplitPane.setBottomComponent(new JScrollPane(ioTextArea));

        frame.getContentPane().add(vSplitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(getPreferredLookAndFeel());
        SwingUtilities.invokeLater(() -> new XetEditor().run());
    }

    private static String getPreferredLookAndFeel() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                return info.getClassName();
            }
        }
        return UIManager.getCrossPlatformLookAndFeelClassName();
    }

    @Override
    public void run() {
        frame.setVisible(true);
        componentResized(null);
    }

    public void componentResized(ComponentEvent e) {
        SwingUtilities.invokeLater(() -> {
            vSplitPane.setDividerLocation(0.9);
            hSplitPane.setDividerLocation(0.5);
        });
    }

    private void onPreviewButtonClicked(ActionEvent e) {
        CompletableFuture
                .supplyAsync(() -> engine.process(editorPane.getText()))
                .whenComplete((image, ex) -> {
                    if (ex == null) {
                        SwingUtilities.invokeLater(() -> {
                            imageView.setImage(image);
                            ioTextArea.setText("");
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> ioTextArea.setText(getStackTrace(ex)));
                    }
                });
    }

    private void onSaveButtonClicked(ActionEvent e) {
        CompletableFuture
                .runAsync(() -> {
                    BufferedImage image = imageView.getImage();
                    if (image == null) {
                        throw new IllegalArgumentException("图片不存在");
                    }
                    String text = ioTextArea.getText();
                    if (text == null) {
                        throw new IllegalArgumentException("请输入文件路径");
                    }
                    Matcher matcher = Pattern.compile("\\.(?<e>[a-z0-9]{1,8})$", Pattern.CASE_INSENSITIVE).matcher(text);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException("无法识别文件格式");
                    }
                    String format = matcher.group("e");
                    try {
                        ImageIO.write(imageView.getImage(), format, new File(text));
                    } catch (IOException ex) {
                        throw new UncheckedIOException(ex);
                    }
                })
                .whenComplete((r, ex) -> {
                    if (ex == null) {
                        SwingUtilities.invokeLater(() -> ioTextArea.setText("保存成功"));
                    } else {
                        SwingUtilities.invokeLater(() -> ioTextArea.setText(getStackTrace(ex)));
                    }
                });
    }

    private String getStackTrace(Throwable e) {
        StringWriter buffer = new StringWriter();
        e.printStackTrace(new PrintWriter(buffer));
        return buffer.toString();
    }

    private static final class ImageView<T extends Image> extends JComponent {
        private T image;

        public T getImage() {
            return image;
        }

        public void setImage(T image) {
            this.image = image;
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) {
                return;
            }
            g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
        }
    }
}
