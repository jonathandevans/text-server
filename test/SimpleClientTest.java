import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.*;

/**
 * JUnit tests for {@code SimpleClient}.
 * 
 * @author 210016688
 */
public class SimpleClientTest {
    
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
    public void checkValidHostAndPort() {
        String[] args = new String[]{"127.0.0.1", "3333"};
        assertEquals(true, SimpleClient.setHostAndPort(args));
    }

    @Test
    public void checkInvalidHostAndPort() {
        String[] args = new String[]{"127.0.0.1"};
        assertEquals(false, SimpleClient.setHostAndPort(args));
        assertEquals("Incorrect number of arguments provided.", output.toString().trim());
    }

    @Test
    public void checkInvalidHostAndPort1() {
        String[] args = new String[]{"127.0.0.1", "3333", "3333"};
        assertEquals(false, SimpleClient.setHostAndPort(args));
        assertEquals("Incorrect number of arguments provided.", output.toString().trim());
    }

    @Test
    public void checkInvalidHostAndPort2() {
        String[] args = new String[]{"127.0.0.1", "port"};
        assertEquals(false, SimpleClient.setHostAndPort(args));
        assertEquals("Second argument provided is not an integer.", output.toString().trim());
    }
}
