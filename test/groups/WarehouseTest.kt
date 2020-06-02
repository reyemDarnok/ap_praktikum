package groups

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WarehouseTest {
    var warehouse: Warehouse = Warehouse()

    @Before
    fun init() {
        warehouse = Warehouse()
        warehouse.fillWarehouse("1", 1.0, "desc1")
        warehouse.fillWarehouse("2", 2.0, "desc2")
        warehouse.fillWarehouse("3", 3.0, "desc3")
    }

    @Test
    fun `listOfProducts is accurate`() {
        assertTrue(warehouse.listOfProducts.contains("1	1.50€ 	x	"))
        assertTrue(warehouse.listOfProducts.contains("2	3.00€ 	x	"))
        assertTrue(warehouse.listOfProducts.contains("3	4.50€ 	x	"))
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