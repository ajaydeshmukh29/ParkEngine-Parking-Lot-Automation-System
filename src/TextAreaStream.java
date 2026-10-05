import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/*
    TextAreaStream
    --------------
    Your classes print with System.out.println(...).
    We redirect System.out into a JTextArea, so every ticket, bill and
    display-board message shows up inside the GUI automatically.
*/
class TextAreaStream extends OutputStream
{
    private final JTextArea area;
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    TextAreaStream(JTextArea area)
    {
        this.area = area;
    }

    @Override
    public void write(int b)
    {
        buffer.write(b);
    }

    @Override
    public void flush()
    {
        final String text = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        buffer.reset();

        if(text.isEmpty())
        {
            return;
        }

        // Swing components must be updated on the Event Dispatch Thread
        SwingUtilities.invokeLater(() ->
        {
            area.append(text);
            area.setCaretPosition(area.getDocument().getLength());
        });
    }
}
