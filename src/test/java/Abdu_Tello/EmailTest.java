package Abdu_Tello;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Map;
import java.util.Date;

// This is a test comment to trigger the CI workflow.
public class EmailTest {
    private TestEmail email;
    private class TestEmail extends Email {
        public String getMimeMessageType() { return "text/plain"; }
        @Override
        public Email setMsg(String msg) throws EmailException {
            setContent(msg, getMimeMessageType());
            return this;
        }
    }
    @Before
    public void setUp() { email = new TestEmail(); email.setHostName("localhost"); }
    @After
    public void tearDown() { email = null; }
    @Test
    public void testAddBcc() throws EmailException {
        email.addBcc("test1@example.com", "test2@example.com");
        List<InternetAddress> bcc = email.getBccAddresses();
        assertEquals(2, bcc.size());
        assertEquals("test1@example.com", bcc.get(0).getAddress());
        assertEquals("test2@example.com", bcc.get(1).getAddress());
    }
    @Test
    public void testAddCc() throws EmailException {
        email.addCc("cc@example.com");
        List<InternetAddress> cc = email.getCcAddresses();
        assertEquals(1, cc.size());
        assertEquals("cc@example.com", cc.get(0).getAddress());
    }
    @Test
    public void testAddHeader() {
        email.addHeader("X-Test", "Value");
        Map<String, String> headers = email.getHeaders();
        assertEquals("Value", headers.get("X-Test"));
    }
    @Test
    public void testAddReplyTo() throws EmailException {
        email.addReplyTo("reply@example.com", "Reply");
        List<InternetAddress> reply = email.getReplyToAddresses();
        assertEquals(1, reply.size());
        InternetAddress addr = reply.get(0);
        assertEquals("reply@example.com", addr.getAddress());
        assertEquals("Reply", addr.getPersonal());
    }
    @Test
    public void testBuildMimeMessage() throws EmailException {
        email.setFrom("from@example.com");
        email.setMsg("Test");
        email.buildMimeMessage();
        assertNotNull(email.getMimeMessage());
    }
    @Test
    public void testGetHostName() {
        email.setHostName("smtp.example.com");
        assertEquals("smtp.example.com", email.getHostName());
    }
    @Test
    public void testGetMailSession() throws EmailException {
        email.setHostName("smtp.example.com");
        Session session = email.getMailSession();
        assertNotNull(session);
    }
    @Test
    public void testGetSentDate() throws EmailException {
        assertNull(email.getSentDate());
        email.setFrom("from@example.com");
        email.setMsg("Test");
        email.buildMimeMessage();
        assertNotNull(email.getSentDate());
    }
    @Test
    public void testGetSocketConnectionTimeout() {
        assertEquals(0, email.getSocketConnectionTimeout());
    }
    @Test
    public void testSetFrom() throws EmailException {
        email.setFrom("from@example.com");
        assertEquals("from@example.com", email.getFromAddress().getAddress());
    }
}
