package order

import org.junit.Test
import kotlin.test.assertEquals

class AddressTest {
    @Test
    fun `echo out of constructor`() {
        val a = Address("first", "second", "street", "city")
        assertEquals("first", a.firstName)
        assertEquals("second", a.lastName)
        assertEquals("street", a.street)
        assertEquals("city", a.city)
    }
}