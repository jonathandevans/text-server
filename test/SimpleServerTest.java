import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.*;

/**
 * JUnit tests for {@code SimpleServer}.
 * 
 * @author 210016688
 */
public class SimpleServerTest {

    private PrintStream stdout = System.out;
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setupStreams() {
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void endStreams() {
        System.setOut(stdout);
    }


    @Test
    public void checkValidPort() {
        String[] args = new String[]{"3333"};
        assertEquals(true, SimpleServer.setPort(args));
    }

    @Test
    public void checkInvalidHostAndPort() {
        String[] args = new String[]{"127.0.0.1", "3333"};
        assertEquals(false, SimpleServer.setPort(args));
        assertEquals("Incorrect number of arguments provided.", output.toString().trim());
    }

    @Test
    public void checkInvalidHostAndPort1() {
        String[] args = new String[]{};
        assertEquals(false, SimpleServer.setPort(args));
        assertEquals("Incorrect number of arguments provided.", output.toString().trim());
    }

    @Test
    public void checkInvalidHostAndPort2() {
        String[] args = new String[]{"port"};
        assertEquals(false, SimpleServer.setPort(args));
        assertEquals("Argument provided is not an integer.", output.toString().trim());
    }
}
