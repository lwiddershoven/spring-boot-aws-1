package nl.leonw.demo.springbootaws1.startup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SSMParameterReformatterTest {
    private SSMParameterReformatter reformatter = new SSMParameterReformatter();

    @ParameterizedTest
    @CsvSource({
            "/x/y/abc,    abc  ",
            "/x/y/a.b.c,  a.b.c",
            "/x/y/a/b.c,  a.b.c",
            "/x/y/a/b/c,  a.b.c",
            "/x/y/a_b_c,  a_b_c"
    })
    public void removes_service_path(String provided, String expected) {
        assertEquals(expected, reformatter.reformat(provided));
    }

    @Test
    public void no_postfix_returns_empty() {
        assertEquals("", reformatter.reformat("/x/y/"));
    }

    @Test
    public void too_short_throws_exception() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> reformatter.reformat("/x/"));
    }

    @Test
    public void no_trailing_slash_throws_exception() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> reformatter.reformat("/x/y"));
    }

}