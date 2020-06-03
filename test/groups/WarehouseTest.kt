package groups

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WarehouseTest {
    var warehouse: Warehouse = Warehouse()

    @Before
    fun init() {
        warehouse = Warehouse().apply {
            fillWarehouse("1", 1.0, "desc1")
            fillWarehouse("2", 2.0, "desc2")
            fillWarehouse("3", 3.0, "desc3")
        }
    }

    @Test
    fun `iterator relay`() {
        warehouse.iterator()
        // no exceptions
    }

    @Test
    fun `listOfProducts is accurate`() {
        assertEquals("1 1.50 Euro. desc1\n2 3.00 Euro. desc2\n3 4.50 Euro. desc3\n", warehouse.listOfProducts)

    }

    @Test
    fun `productDescriptions formed correctly`() {
        assertEquals("desc1\n" +
                "desc2\n" +
                "desc3\n", warehouse.productDescriptions)
    }

    @Test
    fun `getProductByName finds existing Product`() {
        val product = warehouse.getProductByName("1")
        assertNotNull(product)
        assertEquals("1", product?.productName)
        assertEquals("desc1", product?.description)
    }

    @Test
    fun `getProductByName returns null on missing Product`() {
        assertNull(warehouse.getProductByName("4"))
    }

    @Test
    fun `hasProduct detects existing Product`() {
        assertTrue(warehouse.hasProduct("1"))
    }

    @Test
    fun `hasProduct detects missing Product`() {
        assertFalse(warehouse.hasProduct("4"))
    }
}